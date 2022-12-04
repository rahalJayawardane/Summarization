package org.sinhala.summarization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    /**
     * Get value and remove line by index
     *
     * @param lines
     * @param index
     * @return
     */
    public static String getValue(List<String> lines, int index) {

        String value = lines.get(index);
        lines.remove(index);
        return value;

    }

    /**
     * Get value and remove line by string value
     *
     * @param lines
     * @param value
     * @return
     */
    public static String getValue(List<String> lines, String value) {

        int index = lines.indexOf(value);
        lines.remove(index);
        return value;

    }

    /**
     * count no of words in complete document
     *
     * @param lines
     * @return
     */
    public static int countWords(List<String> lines) {

        int count = 0;
        for (String line : lines) {
            count = count + line.split(" ").length;
        }
        return count;
    }

    /**
     * count no of words in a given line
     *
     * @param text
     * @return
     */
    public static int countWords(String text) {

        return text.split(" ").length;
    }

    /**
     * Join sentences for extraction summary
     *
     * @param lines
     * @return
     */
    public static String joinLines(List<String> lines) {

        String sentence = "";
        for (String line : lines) {
            if (line.startsWith(" ")) {
                line = line.substring(1, line.length());
            }
            line = line.replaceAll("  ", " ");
            if (line.startsWith(" ")) {
                line = line.substring(1, line.length() - 1);
            }
            if (sentence.length() == 0) {
                sentence = line;
            } else {
                sentence = sentence + " " + line;
            }
        }
        return sentence.trim();
    }

    /**
     * Remove Punctuations
     *
     * @param word
     * @return
     */
    public static String removePunt(String word) {

        if (word.endsWith("\\p{Punct}")) {
            word = word.substring(0, word.length() - 2);
        }
        //COMMENT DUE TO 2181-19_S.pdf
        //word = word.replaceAll("\\p{P}","");
        return word;
    }

    /**
     * Remove duplicates and format sections
     *
     * @param values
     * @return
     */
    public static String formatSection(String values) {

        String noDuplicates = "";
        Iterator<String> itObjectForList = Arrays.asList(values.split("-")).iterator();
        while (itObjectForList.hasNext()) {
            if (noDuplicates.length() == 0) {
                noDuplicates = itObjectForList.next();
            } else {
                noDuplicates = noDuplicates + "," + itObjectForList.next();
            }
        }
        return noDuplicates;
    }

    /**
     * Format selected sections
     *
     * @param text
     * @return
     */
    public static String formatValues(String text) {

        if (text.startsWith(" ")) {
            text = text.substring(1, text.length());
        }
        return text.replaceAll("-", "");
    }

    /**
     * Read File from given location
     *
     * @param fileName
     * @return
     */
    public static String readFile(String fileName) {

        String output = "";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                output = output + line + "\n";
            }
        } catch (IOException e) {
            System.out.println("Error when reading the file");
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Score and select the most suitable sentence
     *
     * @param line
     * @param lines
     * @param keyWords
     * @return
     */
    public static String scoreSentences(String line, List<String> lines, ArrayList<String> keyWords) {

        int selectedLineScore = 0;
        HashMap<Integer, Integer> lineScores = new HashMap<>();

        //score the selected line
        String[] selectedLineWords = line.split(" ");
        for (String word : selectedLineWords) {
            if (keyWords.contains(word)) {
                selectedLineScore++;
            }
        }

        //score other lines
        for (int i = 0; i < lines.size(); i++) {
            int score = 0;
            String[] lineWords = lines.get(i).split(" ");
            for (String word : lineWords) {
                if (keyWords.contains(word)) {
                    score++;
                }
            }
            lineScores.put(i, score);
        }

        //get max scored line
        int index = Collections.max(lineScores.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();

        if (lines.get(index).equals(lines.get(lines.indexOf(line)))) {
            return line;
        } else {
            return lines.get(index);
        }
    }

}
