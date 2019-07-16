package com.luffy.comic.Controller;

import com.luffy.comic.model.Record;
import com.luffy.comic.service.RecordService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/record")
public class RecordController {

    private static final Log logger = LogFactory.getLog(RecordController.class);
    @Autowired
    private RecordService recordService;

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

    @GetMapping("/last")
    @ResponseBody
    public Record getLastRecord() {
        Record record = recordService.findLastOne();
        return record == null ? new Record() : record;
    }
}
