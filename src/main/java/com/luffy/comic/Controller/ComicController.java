package com.luffy.comic.Controller;

import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.Comic;
import com.luffy.comic.model.Record;
import com.luffy.comic.service.ChapterService;
import com.luffy.comic.service.ComicService;
import com.luffy.comic.service.RecordService;
import com.luffy.comic.tools.Tools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.luffy.comic.tools.Tools.transToPath;

@Controller
@RequestMapping("/comic")
public class ComicController {

    private static final Log logger = LogFactory.getLog(ComicController.class);
    private static final String root = "D:\\Comic";
    @Autowired
    private ComicService comicService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private RecordService recordService;

    @GetMapping("/index")
    public String index(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                        Model model) {
        model.addAttribute("comics", comicService.findByPage(pageNum, pageSize).getList());
        model.addAttribute("last_record", recordService.findLastOne());
        return "index";
    }

    private void addChapter1(String title, Comic comic) {
        String[] pages = new File(transToPath(root, comic.getTitle(), title)).list();
        if (pages != null && pages.length > 0) {
            Chapter chapter = new Chapter(comic, title, pages.length, Tools.splitSuffix(pages[0]));
            chapterService.insertOrUpdate(chapter);
        }
    }

    private void addComic1(String title) {
        Comic comic = new Comic(0, title, title, "");
        comicService.insertOrUpdate(comic);

        String[] chapters = new File(transToPath(root, title)).list();
        if (chapters != null) {
            List<Chapter> chapterList = new ArrayList<>();
            for (String chapterTitle : chapters) {
                String[] pages = new File(transToPath(root, title, chapterTitle)).list();
                if (pages != null && pages.length > 0) {
                    chapterList.add(new Chapter(comic, chapterTitle, pages.length, Tools.splitSuffix(pages[0])));
                }
            }
            chapterService.insertOrUpdateBatch(chapterList);
        }
    }

    @GetMapping("/add")
    @ResponseBody
    public String addComic(@RequestParam String title) {
        addComic1(title);
        return "Add comic " + title + " succeed";
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public String deleteComic(@RequestParam String title, @RequestParam(name = "id", defaultValue = "0") int id) {
        if (id > 0) {
            comicService.deleteById(id);
        } else if (title != null) {
            comicService.deleteByTitle(title);
        }
        return "";
    }

    @GetMapping("/update")
    @ResponseBody
    public String updateComics() {
        String[] comics = new File(root).list();
        if (comics != null) {
            for (String title : comics) {
                addComic1(title);
            }
        }
        return "Update succeed.";
    }

    @GetMapping("/{id}")
    public String getComic(@PathVariable int id,
                           @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                           @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                           Model model) {
        Comic comic = comicService.findById(id);
        if (comic == null)
            return "error/404";
        model.addAttribute("comic", comic);

        List<Chapter> chapters = chapterService.findByComicIdByPage(id, pageNum, pageSize).getList();
        model.addAttribute("chapters", chapters);

        Map<Integer, String> all_records = new HashMap<>();
        if (chapters != null && chapters.size() > 0) {
            for (Record record : recordService.findByChapters(chapters)) {
                all_records.put(record.getChapter().getId(), record.getPage());
            }
        }
        model.addAttribute("all_records", all_records);
        model.addAttribute("record", recordService.findLastOneByComicId(id));
        return "comic";
    }

    @GetMapping("/{id}/addChapter")
    @ResponseBody
    public String addChapter(@PathVariable int id, @RequestParam String title) {
        addChapter1(title, comicService.findById(id));
        return "Add chapter " + title + "succeed.";
    }

    @DeleteMapping("/{id}/deleteChapter")
    @ResponseBody
    public String deleteChapter(@PathVariable int id, @RequestParam String title) {
        if (title != null) {
            chapterService.deleteByComicIdAndTitle(id, title);
        }
        return "Delete chapter " + title + "succeed.";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        Comic comic = new Comic(0, "Hayate2", "fesf", null);
//        comicService.insertOrUpdate(comic);
//        logger.info("insert:" + comic);
        comic.setFullTitle("Hayate2");
//        comic.setId(0);
        comicService.insertOrUpdate(comic);
        logger.info("update" + comic);
        return "";
    }
}
