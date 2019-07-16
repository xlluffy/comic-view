package com.luffy.comic;

import com.luffy.comic.tools.Tools;
import org.junit.Test;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ComicApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void test01() {
        /*String root = "D:\\BaiduNetdiskDownload\\Hayate\\cropped";
        for (String dir : new File(root).list()) {
            System.out.println(dir);
        }*/
    }

    @Test
    public void test02() {
        System.out.println(Tools.transToPath("wdw", "fawf", "grwe"));
    }
}
