package com.luffy.comic.controller;

import com.luffy.comic.model.Comic;
import com.luffy.comic.model.Record;
import com.luffy.comic.service.ComicService;
import com.luffy.comic.service.RecordService;
import com.luffy.comic.service.UmsAdminService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ComicControllerTest {
    @Autowired
    private UmsAdminService umsAdminService;
    @Autowired
    private ComicService comicService;
    @Autowired
    private RecordService recordService;

    @Before
    public void setUp() throws Exception {
        umsAdminService.login("test", "123456");
    }

    @Test
    public void login() {
        List<Comic> comics = comicService.findByPage(1, 20).getList();
        Record lastRecord = recordService.findLastOne();
        int count = comicService.count();
        Comic comic = comicService.findByChapterId(lastRecord.getChapter().getId());
        Map<Integer, Record> record = recordService.findAllByComics(comics);
    }
}