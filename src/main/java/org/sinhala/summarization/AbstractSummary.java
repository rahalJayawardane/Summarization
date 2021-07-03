package org.sinhala.summarization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbstractSummary {

    private static int index = 0;
    public static String no;
    public static String date_desc;
    public static String date;
    public static String part;
    public static String about;
    public static String act;
    public static String who;
    public static String where;


    public static void AssignValues(List<String> lines) {
        no = formatNo(getCorrectLine(lines, new ArrayList<String>(Arrays.asList("අංක"))));
        date_desc = getCorrectLine(lines, KeyWords.datesKeywords);
        date = getDate(lines);
        part = ConvertToSinhala.formatValues(getCorrectLine(lines, KeyWords.sectionKeywords));
        about = getCorrectLine(lines, KeyWords.publicationKeywords);
        act = formatAct(lines, KeyWords.actKeywords);
        who = selectedLineReverse(lines, KeyWords.whoKeywords);
        where = getWhere(lines, KeyWords.whereKeywords);
    }

    private static String formatNo(String text) {
        if(text.split(" ").length > 2) {
            return "අංක" + " " + text.split("අංක")[1];
        }
        return text;
    }

    private static String getDate(List<String> lines) {
        String line2 = "";
        for (String line:lines) {
            if (line.matches("([12]\\d{3}.(0[1-9]|1[0-2]).(0[1-9]|[12]\\d|3[01]))")) {
                return Utils.getValue(lines, line);
            }
        }
        return line2;
    }

    private static String getCorrectLine(List<String> lines, List<String> keywords) {
        String lineWord = "";
        for (String line:lines) {
            String[] words = line.split(" ");
            for(String word: words) {
                if(keywords.contains(word)) {
                    return Utils.getValue(lines, line);
                }
            }
        }
        return lineWord;
    }

    private static String getWhere(List<String> lines, List<String> whereKeywords) {
        String where = "";
        List<String> tempLines = new ArrayList<String>(lines);
        int startIndex = 0;
        int endIndex = 0;
        for (int i = 0 ; i < tempLines.size() ; i++) {
            String line = tempLines.get(i);
            if(line.endsWith("දී ය.")) {
                endIndex = tempLines.indexOf(line);
            } if (line.endsWith("දින,") || line.endsWith("දින.")) {
                startIndex = tempLines.indexOf(line);
            }
            if (endIndex > 0 && startIndex >0) {
                break;
            }
        }

        if (endIndex == 0) {
            for (int i = (tempLines.size()-1) ; i > 0 ; i--) {
                String[] words = tempLines.get(i).split(" ");
                for (String word: words) {
                    if(whereKeywords.contains(word.replaceAll("\\p{Punct}","")) &&
                            (word.endsWith(".") || word.endsWith(",") || (word.equals("කොළඹ")) && words.length <= 2) ) {
                        endIndex = i;
                        break;
                    }
                }
                if (endIndex > 0) {
                    break;
                }
            }
        }

        if (endIndex-startIndex > 5) {
            startIndex = endIndex-1;
        }

        for (int i = startIndex+1; i <= endIndex; i++) {
            where = where + " - " + tempLines.get(i);
            Utils.getValue(lines, tempLines.get(i));
        }

        return where;
    }

    private static String formatAct(List<String> lines, List<String> keywords) {
        String lineWord = "";
        String tempWord = "";
        outerloop:
        for (String line:lines) {
            String[] words = line.split(" ");
            String lastWord = words[words.length-1].replaceAll("\\p{Punct}", "");
            for(String word: words) {
                if(keywords.contains(word) || ("අංක".equals(word))) {
                    tempWord = Utils.getValue(lines, line);
                    break outerloop;
                }
            }
        }

        String[] words = tempWord.split(" ");
        String lastWord = words[words.length-1].replaceAll("\\p{Punct}", "");
        if(keywords.contains(lastWord) || (lastWord.matches("[0-9]+") && "අංක".equals(words[words.length-2]))) {
            return tempWord;
        } else {
            int index = words.length-1;
            for (int i= index; i > 0; i--) {
                if (keywords.contains(words[i]) || (words[i].matches("[0-9]+") && "අංක".equals(words[i-1]))) {
                    index = i;
                    break;
                }

            }
            for (int i = 0; i < index + 1; i++) {
                lineWord = lineWord + " " + words[i];
            }
        }
        return lineWord;
    }



    private static String formatAct_old(List<String> lines, List<String> keywords) {
        String act = "";
        List<String> tempLines = lines;
        for (int i = 0 ; i < tempLines.size() ; i++) {
            int score = 0;
            String[] selectedLineWords = tempLines.get(i).split(" ");
            String lastword = selectedLineWords[selectedLineWords.length-1].replaceAll("\\p{Punct}", "");
            if(keywords.contains(lastword)) {
                if (act.length() == 0) {
                    act = tempLines.get(i);
                } else {
                    act = act + ",- " +  tempLines.get(i);
                }
            }
        }
        if (act.equals("")) {
            act = selectedLine(lines, KeyWords.actKeywords, "යටතේ");
            String[] actWords = act.split(" ");
            for (int i = actWords.length-1 ; i > 0; i--) {
                if (KeyWords.actKeywords.contains(actWords[i]) ||
                        (actWords[i].replaceAll("\\p{Punct}", "").matches("[0-9]+")) &&
                                KeyWords.actKeywords.contains(actWords[i-1])) {
                    act = Stream.of(actWords).limit((i+1)).collect(Collectors.joining(" "));
                    break;
                }
            }
        } else {
            String[] words = act.split(",- ");
            String newAct = "";
            for (String word: words) {
                if (newAct.equals("")) {
                    newAct = Utils.getValue(lines, word);
                } else {
                    newAct = newAct + ", " +  Utils.getValue(lines, word);
                }
            }
            act = newAct;
        }
        return act;
    }

    private static String selectedLineReverse(List<String> lines, List<String> keywords) {
        int lineIndex = 0;
        for (int i = (lines.size()-1) ; i > 0 ; i--) {
            int score = 0;
            String line = lines.get(i);
            String[] selecteLineWords = line.split("[ /]");
            String lastword = selecteLineWords[selecteLineWords.length-1];
            if(lastword.matches(".*\\p{Punct}") || lastword.endsWith("ල")) {
                lastword = lastword.replace(".", "");
                lastword = lastword.replace(",", "");
                if(keywords.contains(lastword)) {
                    String position = Utils.getValue(lines, lines.get(i));
                    return getNameOfIssuer(lines, i) + " - " + position;
                }
            }
        }
        return "";
    }

    private static String getNameOfIssuer(List<String> lines, int index) {
        for (int i = index-1 ; i > 0 ; i--) {
            boolean isName = true;
            String prevLine = lines.get(i);
            String[] selectedLineWords = prevLine.split(" ");
            for (String word: selectedLineWords) {
                if(PDFReader.isUnwanted(word)) {
                    isName = false;
                }
            }
            if (isName) {
                if (lines.get(i).split(" ").length > 6) {
                    return "";
                }
                return Utils.getValue(lines, i);
            }
        }
        return "";
    }

    private static String selectedLine(List<String> lines, List<String> keywords, String specialBraker) {
        HashMap<Integer, Integer> scoredLine = new HashMap<Integer, Integer>();
        for (int i = 0; i < lines.size(); i++) {
            int score = 0;
            String[] selecteLineWords = lines.get(i).split(" ");
            for (String word: selecteLineWords) {
                if(keywords.contains(word)) {
                    score++;
                }
                if (word.contains(specialBraker)) {
                    break;
                }
            }
            if (score > 0) {
                scoredLine.put(i, score);
            }
        }
        if (!scoredLine.isEmpty()) {
            Integer key = Collections.max(scoredLine.entrySet(), Map.Entry.comparingByValue()).getKey();
            return lines.get(key);
        }
        return "";
    }
}
