package com.luffy.comic.controller;

import com.luffy.comic.model.Record;
import com.luffy.comic.service.RecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(tags = "RecordController", description = "阅读记录管理")
@Controller
@RequestMapping("/record")
public class RecordController {

    private static final Log logger = LogFactory.getLog(RecordController.class);
    @Autowired
    private RecordService recordService;

    @ApiOperation("插入或者更新记录")
    @PostMapping("/update")
    @ResponseBody
    public String insertOrUpdateRecord(@RequestBody Record record) {
        if (!recordService.updateByChapterId(record)) {
            recordService.insert(record);
            logger.info("Insert record: title = " + record.getChapter().getTitle() +
                    ", page = " + record.getPage() +
                    ", suffix = " + record.getSuffix());
        } else {
            logger.info("Update record: title = " + record.getChapter().getTitle() +
                    ", page = " + record.getPage());
        }
        return "";
    }

    @ApiOperation("获取最新记录")
    @GetMapping("/last")
    @ResponseBody
    public Record getLastRecord() {
        Record record = recordService.findLastOne();
        return record == null ? new Record() : record;
    }
}
