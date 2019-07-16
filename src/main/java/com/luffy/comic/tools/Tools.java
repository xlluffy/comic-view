package com.luffy.comic.tools;

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
}
