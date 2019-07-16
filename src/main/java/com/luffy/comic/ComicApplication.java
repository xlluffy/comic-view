package com.luffy.comic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.luffy.comic.mapper")
public class ComicApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComicApplication.class, args);
    }
}
