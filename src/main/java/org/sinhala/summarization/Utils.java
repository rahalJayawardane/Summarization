package org.sinhala.summarization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static int countWords(String text) {
        return text.split(" ").length;
    }

    public static String joinLines(List<String> lines) {
        String sentence = "";
        for (String line: lines) {
            if (line.startsWith(" ")) {
                line = line.substring(1, line.length());
            }
            line = line.replaceAll("  ", " ");
            if(line.startsWith(" ")) {
                line = line.substring(1, line.length()-1);
            }
            if (sentence.length() == 0) {
                sentence = line;
            } else {
                sentence = sentence + " " + line;
            }
        }
        return sentence.trim();
    }

    public static String removePunt(String word) {
        if (word.endsWith("\\p{Punct}")) {
            word = word.substring(0,word.length()-2);
        }
        //COMMENT DUE TO 2181-19_S.pdf
        //word = word.replaceAll("\\p{P}","");
        return word;
    }

    public static String formatSection(String values) {

        String noDuplicates = "";
        Iterator<String> itObjectForList =Arrays.asList(values.split("-")).iterator();
        while (itObjectForList.hasNext()) {
            if(noDuplicates.length() == 0) {
                noDuplicates =  itObjectForList.next();
            } else {
                noDuplicates = noDuplicates + "," + itObjectForList.next();
            }
        }
        return noDuplicates;
    }

    public static String formatValues(String text) {
        if (text.startsWith(" ")) {
            text = text.substring(1, text.length());
        }
        return text.replaceAll("-", "");
    }
}
