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

    @Test
    public void findByPage() {
        System.out.println("First page: ");
        comicService.findByPage(1, 20).getList().forEach(System.out::println);
        System.out.println("Second page: ");
        comicService.findByPage(2, 20).getList().forEach(System.out::println);
        System.out.println("Third page: ");
        comicService.findByPage(3, 20).getList().forEach(System.out::println);
        System.out.println(comicService.count());
    }

    @Test
    public void createBatchComic() {
        for (int i = 1; i < 60; i++) {
            comicService.insert(new Comic(0, String.format("test%02d", i), "", "No one"));
        }
    }
}