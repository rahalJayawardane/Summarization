package org.sinhala.summarization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class ExtractSummary {

    private static Map<String, Integer> map = new HashMap<String, Integer>();
    public static int noticeWordCount = 0;
    public static int summarizedWordCount = 0;


    public static List<String> filerNotice(List<String> lines) {
        List<String> newLines = new ArrayList<>();

        outerloop:
        for (String line: lines) {
            String[] words = line.split(" ");
            if (words.length < 7 ) {
                for (String word: words) {
                    // if lines have only integers remove them
                    if(words.length == 1 && word.matches("[0-9]+")) {
                        break outerloop;
                    }
                    if (line.replaceAll("\\p{Punct}","").endsWith("වැනි දින")) {
                        break outerloop;
                    }
                }
            }
            newLines.add(line.replaceAll("\\p{Punct}",""));
        }
        lines = newLines.stream().distinct().collect(Collectors.toList());
        lines = selectRemainingWords(lines);
        noticeWordCount = Utils.countWords(lines);
        lines = cleanNotice(lines);
        summarizedWordCount = Utils.countWords(lines);
        return lines;
    }

    private static List<String> cleanNotice(List<String> lines) {
        for (String line: lines) {
            int index = lines.indexOf(line);
            line = line.replaceAll("ප්\u200Dරජාතාන්ත්\u200Dරික සමාජවාදී ජනරජයේ", "");
            line = line.replaceAll("වන මම", "");
            line = line.replaceAll("වන ම", "");
            if (AbstractSummary.act.length() > 0) {
                line = checkAndReplace(AbstractSummary.who, line);
            }
            if (AbstractSummary.act.length() > 0) {
                line = checkAndReplace(AbstractSummary.act, line);
            }
            line = line.replaceAll("  ", " ");
            lines.set(index, line);
        }
        return lines;
    }

    private static String checkAndReplace(String words, String line) {
        String[] selectedWords = words.split("-");
        for (String selectedWord: selectedWords) {
            line = line.replaceAll(selectedWord.replaceAll("\\p{Punct}","").trim(), " ");
        }
        return line;
    }

    private static List<String> selectRemainingWords(List<String> lines) {
        List<String> needToRemove = new ArrayList<>();
        for (String line: lines) {
            int scoreSection = 0;
            int scoreActs = 0;
            String[] words = line.split(" ");
            for (String word: words) {
                if (KeyWords.sectionKeywords.contains(word)) {
                    scoreSection++;
                }
                if (KeyWords.secondActKeywords.contains(word)) {
                    scoreActs++;
                }
            }

            if((Double.parseDouble(String.valueOf(scoreSection)) / words.length) >= 0.5 ) {
                AbstractSummary.part = AbstractSummary.part + " - " + line;
                needToRemove.add(line);
            }
            if((Double.parseDouble(String.valueOf(scoreActs)) / words.length) >= 0.5 ) {
                AbstractSummary.act = AbstractSummary.act + " - " + line;
                needToRemove.add(line);
            }
        }

        for (String removingLine: needToRemove) {
            lines.remove(removingLine);
        }

        return lines;
    }
}
