package com.luffy.comic.service.impl;

import com.luffy.comic.model.Comic;
import com.luffy.comic.service.ComicService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComicServiceImplTest {

    @Autowired
    private ComicService comicService;

    @Test
    public void insertOrUpdateBatch() {
        List<Comic> comics = new ArrayList<>();
        comics.add(new Comic(0, "one", "oune", ""));
        comics.add(new Comic(0, "two", "utwo", ""));
        comicService.insertOrUpdateBatch(comics);

        comics.forEach(System.out::println);
    }

    @Test
    public void findById() {
        Comic comic = comicService.findById(1);
        System.out.println("comic = " + comic);

        System.out.println("comicService.findByTitle(\"Hayate\") = " + comicService.findByTitle("Hayate"));
        comicService.findAll().forEach(System.out::println);
    }
}