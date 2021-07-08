package org.sinhala.summarization;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ExtractSummary {

    private static Map<String, Integer> map = new HashMap<String, Integer>();
    public static int noticeWordCount = 0;
    public static int summarizedWordCount = 0;


    public static List<String> filerNotice(List<String> lines) {
        noticeWordCount = Utils.countWords(lines);
        List<String> newLines = new ArrayList<>();
        outerloop:
        for (String line: lines) {
            boolean adding = true;
            String[] words = line.split(" ");
            if (words.length < 7 ) {
                for (String word: words) {
                    // if lines have only integers remove them
                    if(words.length == 1 && (word.matches("[0-9]+") || !word.contains("[a-zA-Z]+"))) {
                        adding = false;
                    }
                    if (line.replaceAll("\\p{Punct}","").endsWith("වැනි දින")) {
                        adding = false;
                    }
                }
            }
            if (adding) {
                newLines.add(replaceLast(line,"\\p{Punct}",""));
            }
        }
        lines = newLines.stream().distinct().collect(Collectors.toList());
        lines = selectRemainingWords(lines);
        lines = cleanNotice(lines);
        lines = removeLines(lines);
        summarizedWordCount = Utils.countWords(lines);
        return lines;
    }

    private static List<String> removeLines(List<String> lines) {
        List<String> newLines = new ArrayList<>();
        for (String line: lines) {
            for (String removeWord: KeyWords.removingKeywords) {
                line = line.replaceAll(removeWord, "");
            }
            String newLine = "";
            for (String words : line.split(",")) {
                if (words.split(" ").length > 3) {
                    newLine = newLine + words;
                }
            }

            if (newLine.split(" ").length > 4) {
                newLines.add(newLine);
            }
        }
        return newLines;
    }

    private static List<String> identifyTitle(List<String> lines) {
        AbstractSummary.title = "";
        for (String line: lines) {
            if (line.replaceAll("\\p{Punct}","").endsWith("පිරවීම")) {
                AbstractSummary.title = line;
            }
        }
        if (AbstractSummary.title != "") {
            lines.remove(lines.indexOf(AbstractSummary.title));
        }
        return lines;
    }

    private static List<String> cleanNotice(List<String> lines) {
        for (String line: lines) {
            int index = lines.indexOf(line);
            line = clearStrings(line);
            for (String removeWord: KeyWords.removingKeywords) {
                line = line.replaceAll(removeWord, "");
            }
            if (AbstractSummary.who.length() > 0) {
                line = checkAndReplace(AbstractSummary.who, line);
            }
            if (AbstractSummary.act.length() > 0) {
                line = checkAndReplace(AbstractSummary.act, line);
            }
            if (AbstractSummary.about.length() > 0) {
                line = checkAndReplace(AbstractSummary.about, line);
            }
            line = line.replaceAll("  ", " ");
            lines.set(index, line);
        }
        return lines;
    }

    private static String clearStrings(String line) {
        for (String word: line.split(" ")) {
            if(word.length() <= 2) {
                 switch (word) {
                     case "ේ":
                         line = line.replace("ේ","");
                         break;
                     case "ඃ":
                         line = line.replace("ඃ","");
                         break;
                 }
            }
        }
        return line.trim();
    }

    private static String checkAndReplace(String words, String line) {
        String[] selectedWords = words.split("-");
        for (String selectedWord: selectedWords) {
            if(selectedWord.trim().length() < 2) {
                continue;
            }
            line = line.replaceAll(selectedWord, " ");
            line = line.replaceAll(selectedWord.replaceAll("\\p{Punct}","").trim(), " ");

            List<String> selectedWordPhase = new ArrayList<>(Arrays.asList(selectedWord.trim().toLowerCase().split("\\s+")));
            List<String> lineWordPhase = new ArrayList<>(Arrays.asList(line.toLowerCase().split("\\s+")));
            lineWordPhase.retainAll(selectedWordPhase);

            if (lineWordPhase.size() > 1) {
                for (String word: lineWordPhase) {
                    line = line.replaceAll(Pattern.quote(word), "");
                }
            }
        }

        return line;
    }

    public static String replaceLast(String text, String regex, String replacement) {
        if (text.endsWith(regex)) {
            return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
        }
        return text;
    }

    private static List<String> selectRemainingWords(List<String> lines) {
        List<String> needToRemove = new ArrayList<>();
        for (String line: lines) {
            int scoreSection = 0;
            int scoreParts = 0;
            int scoreTitle = 0;
            String[] words = line.split(" ");
            for (String word: words) {
                if (KeyWords.sectionKeywords.contains(word)) {
                    scoreSection++;
                }
                if (KeyWords.titleKeywords.contains(word)) {
                    scoreTitle++;
                }
                if (KeyWords.partKeywords.contains(word)) {
                    scoreParts++;
                }
            }

            if((Double.parseDouble(String.valueOf(scoreSection)) / words.length) >= 0.5 ) {
                AbstractSummary.part = AbstractSummary.part + " - " + line;
                needToRemove.add(line);
            }
            if((Double.parseDouble(String.valueOf(scoreTitle)) / words.length) >= 0.5 ) {
                if (AbstractSummary.about.split("-").length < 2 ) {
                    AbstractSummary.about = AbstractSummary.about + " - " + line;
                }
                needToRemove.add(line);
            }
            if((Double.parseDouble(String.valueOf(scoreParts)) / words.length) >= 0.5 ) {
                AbstractSummary.part = AbstractSummary.part + " - " + line;
                needToRemove.add(line);
            }
        }

        for (String removingLine: needToRemove) {
            lines.remove(removingLine);
        }

        return lines;
    }

    public static List<String> removeRepeats(List<String> lines) {
        int count;
        HashMap<Integer, String> repeatingWords = new HashMap<>();
        List<String> repeatedPhases = new ArrayList<>();
        String words[] = Utils.joinLines(lines).split(" ");

        for(int i = 0; i < words.length; i++) {
            count = 1;
            for(int j = i+1; j < words.length; j++) {
                if(Utils.removePunt(words[i]).equals(Utils.removePunt(words[j]))) {
                    count++;
                    words[j] = "0";
                }
            }

            if(count > 1 && words[i] != "0") {
                String word = Utils.removePunt(words[i]);

                repeatingWords.put(i, word);
            }
        }

        for (int i = 0; i < words.length; i++) {
            if (repeatingWords.containsKey(i)) {
                String word = repeatingWords.get(i);
                for (int j = i+1; j< words.length; j++) {
                    if (!repeatingWords.containsKey(j)) {
                        if (word.split(" ").length >= 2) {
                            repeatedPhases.add(Utils.removePunt(word));
                        }
                        i = j;
                        break;
                    } else {
                        word = word + " " + repeatingWords.get(j);
                    }
                }
            }

        }

        KeyWords.gazetteKeywords = repeatedPhases;
        lines = identifyTitle(lines);
        lines = removeRepeatsFromNotice(lines);
        lines = removeBrackets(lines);
        lines = removeLines(lines);
        return lines;
    }

    private static List<String> removeBrackets(List<String> lines) {
        int startIndex = 0;
        boolean foundOpenBracket = false;
        int endIndex = 0;
        boolean foundCloseBracket = false;

        for (String line: lines) {
            int index = lines.indexOf(line);
            if (line.contains("(") && line.contains(")")) {
                line = line.replaceAll("\\(.*.\\)", "");
            } else {
                line = line.replaceAll("\\(.*", "");
                line = line.replaceAll(".*\\)", "");
            }
            lines.set(index, line);
        }

        return lines;
    }

    private static List<String> removeRepeatsFromNotice(List<String> lines) {

        for (String line: lines) {
            int index = lines.indexOf(line);
            for (String keyword: KeyWords.gazetteKeywords) {
                line = line.replaceAll(Utils.removePunt(keyword), "");
            }
            lines.set(index, line.trim());
        }

        List<String> tempList = new ArrayList<>();

        for (String line: lines) {
            int index = lines.indexOf(line);
            if (line.split(" ").length >= 2) {
                tempList.add(line.trim());
            }
        }

        return tempList;
    }

    public static List<String> findEndSentence(List<String> lines) {
        int index = 0;
        List<String> needToRemove = new ArrayList<>();
        for (String line: lines) {
            String[] words = line.split(" ");
            String lastword = words[words.length-1];
            if (KeyWords.closingKeywords.contains(lastword)) {
                index = lines.indexOf(line);
            }
        }

        //remove rest of lines
        if (index == 0 || index == lines.size()-1) {
            return lines;
        }
        for (int i = index+1; i < lines.size(); i++) {
            needToRemove.add(lines.get(i));
        }

        for (int i = 0; i < needToRemove.size(); i++) {
            lines.remove(lines.indexOf(needToRemove.get(i)));
        }

        return lines;
    }

}
