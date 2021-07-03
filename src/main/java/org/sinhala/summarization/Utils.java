package org.sinhala.summarization;

import java.util.List;

public class Utils {

    public static String getValue(List<String> lines, int index) {
        String value = lines.get(index);
        lines.remove(index);
        return value;

    }

    public static String getValue(List<String> lines, String value) {
        int index = lines.indexOf(value);
        lines.remove(index);
        return value;

    }

    public static int countWords(List<String> lines) {
        int count = 0;
        for (String line: lines) {
            count = count + line.split(" ").length;
        }
        return count;
    }

}
