package com.luffy.comic.tools;

import cn.hutool.json.JSONUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tools {

    @SafeVarargs
    public static <T> String transToPath(T... args) {
        if (args.length > 0) {
            String path = (String) args[0];
            for (int i = 1; i < args.length; i++) {
                path += "\\" + args[i];
            }
            return path;
        }
        return "";
    }

    public static String splitSuffix(String filename) {
        return filename.substring(filename.indexOf('.'));
    }

    public static <T> List<T> removeAll(List<T> A, List<T> B) {
        List<T> result = new ArrayList<>(A.size() - B.size());
        for (T e : A) {
            if (!B.contains(e)) {
                result.add(e);
            }
        }
        return result;
    }

    // 从"text/plain"的post请求中获取bean
    public static <T> T fetchPostByTextPlain(HttpServletRequest request, Class<T> clazz) {
        try {
            BufferedReader reader = request.getReader();
            char[] buf = new char[512];
            int len = 0;
            StringBuilder contentBuffer = new StringBuilder();
            while ((len = reader.read(buf)) != -1) {
                contentBuffer.append(buf, 0, len);
            }
            return JSONUtil.parseObj(contentBuffer.toString()).toBean(clazz);

        } catch (IOException e) {
            e.printStackTrace();
//            log.error("[获取request中用POST方式“Content-type”是“text/plain”发送的json数据]异常:{}", e.getCause());
        }
        return null;
    }
}
