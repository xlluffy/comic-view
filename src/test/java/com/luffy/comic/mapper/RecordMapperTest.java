package com.luffy.comic.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecordMapperTest {

    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private ChapterMapper chapterMapper;

    @Test
    public void update() throws Exception{
        /*List<Record> records = recordMapper.findAll();
        for (Record record : records) {
            record.setPage(String.format("%03d", Integer.parseInt(record.getPage()) - 2));
            recordMapper.updateByChapterId(record);
            Thread.sleep(1000);
        }*/
    }

    @Test
    public void fix() throws Exception {
        /*List<Record> records = recordMapper.findAll();

        for (Record record : records) {
//            if (record.getComic() == null) {
                Comic comic = new Comic();
                comic.setId(chapterMapper.findById(record.getChapter().getId()).getComic().getId());
                record.setComic(comic);
                recordMapper.updateByChapterId(record);
                Thread.sleep(1000);
//            }
        }*/
    }
}