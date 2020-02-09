package com.luffy.comic.tools;

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
}
