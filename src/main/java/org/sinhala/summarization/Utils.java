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

    public static String joinLines(List<String> lines) {
        String sentence = "";
        for (String line: lines) {
            sentence = sentence + " " + line;
        }
        return sentence.trim();
    }

    public static String removePunt(String word) {
        if (word.endsWith("\\p{Punct}")) {
            word = word.substring(0,word.length()-2);
        }
        word = word.replaceAll("\\p{P}","");
        return word;
    }
}
