package com.luffy.comic.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.common.utils.JwtTokenUtil;
import com.luffy.comic.model.Record;
import com.luffy.comic.service.RecordService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@Api(tags = "RecordController", description = "阅读记录管理")
@RestController
@RequestMapping("/record")
public class RecordController {

    private static final Log logger = LogFactory.getLog(RecordController.class);
    private RecordService recordService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    /**
     * 由于前端sendBeacon的headers不能携带验证token，所以将token放入请求体里额外处理
     * @param recordString record json string
     * @param authHeader auth token
     */
    @ApiOperation("插入或者更新记录")
    @PostMapping("/update")
    public CommonResult insertOrUpdateRecord(@RequestParam("record") String recordString,
                                             @RequestParam("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            String authToken = authHeader.substring(this.tokenHead.length());
            try {
                jwtTokenUtil.getClaimsFromToken(authToken);
            } catch (ExpiredJwtException e) {
                jwtTokenUtil.parseJwtPayload(authToken);
                if (!jwtTokenUtil.canRefresh()) {
                    return CommonResult.validateFailed();
                }
            } catch (SignatureException e) {
                return CommonResult.validateFailed();
            }

            String username = jwtTokenUtil.getUsername();
            if (StrUtil.isNotEmpty(username)) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    Record record = JSONUtil.parseObj(recordString).toBean(Record.class);
                    if (record != null && username.equals(record.getUser().getUsername())) {
                        if (!recordService.updateByChapterId(record)) {
                            recordService.insert(record);
                            logger.info("Insert record: username = " + record.getUser().getUsername() +
                                    ", title = " + record.getChapter().getTitle() +
                                    ", comic = " + record.getComic().getTitle() +
                                    ", page = " + record.getPage() +
                                    ", suffix = " + record.getSuffix());
                        } else {
                            logger.info("Update record: username = " + record.getUser().getUsername() +
                                    ", comic = " + record.getComic().getTitle() +
                                    ", title = " + record.getChapter().getTitle() +
                                    ", page = " + record.getPage());
                        }
                        return CommonResult.success("阅读记录已同步");
                    }
                }
            }

        }
        return CommonResult.validateFailed();
    }

    @ApiOperation("删除记录")
    @GetMapping("/delete/{recordId}")
    @PreAuthorize("hasRole('USER')")
    public CommonResult deleteRecord(@PathVariable int recordId) {
        // TODO: 改为异步删除
        recordService.deleteById(recordId);
        logger.info("recordId = " + recordId);
        return CommonResult.success("记录删除成功");
    }

    @ApiOperation("获取最新记录")
    @GetMapping("/last")
    @PreAuthorize("hasRole('USER')")
    public Record getLastRecord() {
        Record record = recordService.findLastOne(2);
        return record == null ? new Record() : record;
    }
}
