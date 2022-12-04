package org.sinhala.summarization;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * PDF formatting according to the useful manner
 */
public class PDFFormatter {

    /**
     * Get the PDF file and send the summary to the client
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static HashMap<String, Object> getDetails(String file) throws IOException {

        String[] words = file.split("/");
        String fileName = words[words.length - 1];
        String gazette = "./SamplePDFs/" + fileName;
        System.out.println("Requested PDF file: " + gazette);
        HashMap<String, Object> response = formatPDF(gazette);
        return response;
    }

    /**
     * Format the PDF by removing the unwanted lines
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    private static HashMap<String, Object> formatPDF(String fileName) throws IOException {

        List<String> lines = new ArrayList<String>();
        PDDocument document = PDDocument.load(new File(fileName));
        if (!document.isEncrypted()) {
            PDFTextStripper stripper = new PDFTextStripper("ISO-15924");
            String text = stripper.getText(document);
            text = text.trim();
            if (text.contains("ɼ")) {
                text = ConvertToSinhala.correctUnicode(text);
            }
            text = clear(text);
            if (!text.contains("ශී ලංකා")) {
                text = ConvertToSinhala.convertText(text);
                lines = remove(text);
            } else {
                lines = removeUnwantedSinhalaLines(text);
            }

            lines = format(lines);
        }

        document.close();
        HashMap<String, Object> response = new HashMap<>();
        response = GenerateSummary.createSummary(fileName, lines);
        return response;

    }

    /**
     * Remove sinhala unwanted lines
     *
     * @param text
     * @return
     */
    private static List<String> removeUnwantedSinhalaLines(String text) {

        List<String> lines = new ArrayList<>();
        for (String line : text.split("\n")) {
            if (!KeyWords.unWantedSinhalaLines.contains(line.trim())) {
                if (line.contains("නිෙව්දන")) {
                    line = line.replaceAll("නිෙව්දන", "නිවේදන");
                }
                lines.add(line);
            }

        }
        return lines;
    }

    /**
     * Replace unwanted uni-codes
     *
     * @param text
     * @return
     */
    private static String clear(String text) {

        String[] words = text.split(" ");
        String newWord = "";
        for (String word : words) {
            if (word.equals("a") || word.equals("s") || word.equals("=") || word.equals("sa")) {
                word = "";
            }
            newWord = newWord + " " + word;
        }
        text = newWord.trim();
        text = text.replaceAll("  ", " ");
        return text;
    }

    /**
     * Validate and remove other unwanted texts
     * ex: English words, numbers and other characters
     *
     * @param text
     * @return
     */
    public static boolean isUnwanted(String text) {

        boolean hasNumbers = false;
        boolean hasEnglish = false;
        boolean hasUnwantedChar = false;

        String[] unWantedList = KeyWords.unwantedText();
        Pattern lastDate = Pattern.compile("^[0-9]{4}+රැ+[0-9]{2}");

        if (StringUtils.indexOfAny(text, unWantedList) == -1) {
            if (text.matches("^.*[0-9]රැ.*$") || KeyWords.englishWords.contains(text)) {
                hasUnwantedChar = true;
            }

            String[] words = text.split("\\p{Punct}");
            for (String letter : words) {
                if (letter.trim().matches("[0-9]+")) {
                    hasNumbers = true;
                }
                if (KeyWords.alphabet.contains(letter)) {
                    hasEnglish = true;
                }
            }

            if ((hasEnglish && hasNumbers) || hasUnwantedChar) {
                return true;
            }

        } else {
            return true;
        }
        return false;
    }

    /**
     * Remove unwanted words
     *
     * @param text
     * @return
     */
    private static List<String> remove(String text) {

        String[] allWords = text.split("\n");
        List<String> lines = new ArrayList<String>();
        for (int i = 0; i < allWords.length; i++) {
            if (!isUnwanted(allWords[i])) {
                lines.add(allWords[i].trim());
            } else {
                lines.add(replaceUnwanted(allWords[i]).trim());
            }
        }

        lines.removeAll(Arrays.asList("", null));
        return lines;
    }

    /**
     * Replace identified unwanted words/ chars
     *
     * @param text
     * @return
     */
    public static String replaceUnwanted(String text) {

        boolean hasNumbers = false;
        boolean hasEnglish = false;
        boolean hasUnwantedChar = false;

        String[] unWantedList = KeyWords.unwantedText();
        Pattern lastDate = Pattern.compile("^[0-9]{4}+රැ+[0-9]{2}");

        for (String unWantedWord : unWantedList) {
            if (StringUtils.indexOf(text, unWantedWord) != -1) {
                text = text.replaceAll(unWantedWord, "");
            } else {
                if (text.matches("^.*[0-9]රැ.*$") || KeyWords.englishWords.contains(text)) {
                    hasUnwantedChar = true;
                }

                String[] words = text.split("\\p{Punct}");
                for (String letter : words) {
                    if (letter.trim().matches("[0-9]+")) {
                        hasNumbers = true;
                    }
                    for (String englishChar : KeyWords.alphabet) {
                        if (letter.contains(englishChar)) {
                            hasEnglish = true;
                        }
                    }
                }
                if ((hasEnglish && hasNumbers) || hasUnwantedChar || hasEnglish) {
                    text = "";
                }
            }
        }
        return text;
    }

    /**
     * Format Strings against the punctuation
     *
     * @param text
     * @return
     */
    private static List<String> format(List<String> text) {

        List<String> lines = new ArrayList<String>();

        for (int i = 0; i < text.size(); i++) {
            if (text.get(i).contains("අංක - ")) {
                text.set(i, text.get(i).replace("අංක - ", "අංක "));
            }
            if (!text.get(i).contains("වැනි කොටස")) {
                String[] tokenized = text.get(i).trim().split("-| ජ් ");
                for (int j = 0; j < tokenized.length; j++) {
                    lines.add(tokenized[j].trim());
                }
            } else {
                String[] tokenized = text.get(i).trim().split(" - ");
                for (int j = 0; j < tokenized.length; j++) {
                    lines.add(tokenized[j].trim());
                }
            }

        }
        return lines;
    }
}
