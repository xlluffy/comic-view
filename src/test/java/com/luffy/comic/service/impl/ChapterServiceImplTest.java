package com.luffy.comic.service.impl;

import com.luffy.comic.service.ChapterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChapterServiceImplTest {
    @Autowired
    private ChapterService chapterService;

    @Test
    public void findById() {
        System.out.println("chapterService.findById(1) = " + chapterService.findById(1));
    }

    @Test
    public void findByComicIdByPage() {
    }
}