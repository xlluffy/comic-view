package com.luffy.comic.controller;

import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.model.Record;
import com.luffy.comic.service.RecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(tags = "RecordController", description = "阅读记录管理")
@Controller
@RequestMapping("/record")
@PreAuthorize("hasRole('USER')")
public class RecordController {

    private static final Log logger = LogFactory.getLog(RecordController.class);
    private RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @ApiOperation("插入或者更新记录")
    @PostMapping("/update")
    @ResponseBody
    public CommonResult insertOrUpdateRecord(@RequestBody Record record) {
        if (!recordService.updateByChapterId(record)) {
            recordService.insert(record);
            logger.info("Insert record: userId = " + record.getUser().getId() +
                    ", title = " + record.getChapter().getTitle() +
                    ", comic = " + record.getComic().getTitle() +
                    ", page = " + record.getPage() +
                    ", suffix = " + record.getSuffix());
        } else {
            logger.info("Update record: userId = " + record.getUser().getId() +
                    ", comic = " + record.getComic().getTitle() +
                    ", title = " + record.getChapter().getTitle() +
                    ", page = " + record.getPage());
        }
        return CommonResult.success("阅读记录已同步");
    }

    @ApiOperation("插入或者更新记录")
    @GetMapping("/delete/{recordId}")
    public String deleteRecord(@PathVariable int recordId) {
        // TODO: 改为异步删除
        recordService.deleteById(recordId);
        logger.info("recordId = " + recordId);
        return "redirect:/user/record";
    }

    @ApiOperation("获取最新记录")
    @GetMapping("/last")
    @ResponseBody
    public Record getLastRecord() {
        Record record = recordService.findLastOne(2);
        return record == null ? new Record() : record;
    }
}
