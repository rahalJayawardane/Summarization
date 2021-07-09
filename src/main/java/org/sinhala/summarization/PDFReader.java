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

public class PDFReader {


    public static void main(String[] args) throws IOException {

        KeyWords.addKeywords();

        long start = System.currentTimeMillis();

        List<String> files = new ArrayList<>();
        List<Path> result = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("./SamplePDFs"))) {
            result = paths.filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        result.forEach(x -> files.add(""+ x));


//        files.add("./SamplePDFs/2194-06_S.pdf");
//        files.add("./SamplePDFs/2209-75_S.pdf");
//        files.add("./SamplePDFs/2205-04_S.pdf");
//        files.add("./SamplePDFs/2183-46_S.pdf");
//        files.add("./SamplePDFs/2181-24_S.pdf");
//        files.add("./SamplePDFs/2230-11_S.pdf");


        int i =1;
        for (String file:files) {
            if (!file.endsWith(".pdf")) {
                continue;
            }
            System.out.println(file);
            System.out.println("------------------ File "+ i +"------------------------");
            method(file);
            System.out.println();
            System.out.println();
            //System.out.println("------------------------------------------");
            i++;
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("Time elapsed: "+ time/1000);

    }

    public static HashMap<String, Object> downloadFile(String file) throws IOException {
        DownloadPDF.download(file);
        return getDetails(file);
    }

    public static HashMap<String, Object> getDetails(String file) throws IOException {
        KeyWords.addKeywords();
        String[] words = file.split("/");
        String fileName = words[words.length-1];
        HashMap<String, Object> response = method("./SamplePDFs/"+fileName);
        return response;
    }

    private static HashMap<String, Object> method(String filename) throws IOException {

        List<String> lines = new ArrayList<String>();
        PDDocument document = PDDocument.load(new File(filename));
        if (!document.isEncrypted()) {
            PDFTextStripper stripper = new PDFTextStripper("ISO-15924");
            String text = stripper.getText(document);
            text = text.trim();
            if(text.contains("ɼ")) {
                text = ConvertToSinhala.correctUnicode(text);
            }
            text = clear(text);
            if (!text.contains("ශී ලංකා")) {
                text = ConvertToSinhala.convertText(text);
                lines = remove(text);
            } else {
                lines = removeSinhala(text);
            }

            lines = format(lines);
        }

        document.close();

        AbstractSummary.AssignValues(lines);

        int total = Utils.countWords(lines);

        lines = ExtractSummary.filerNotice(lines);
        String firstSummary = Utils.joinLines(lines);

        lines = ExtractSummary.removeRepeats(lines);
        String secondSummary = Utils.joinLines(lines);

        lines = ExtractSummary.findEndSentence(lines);
        String thirdSummary = Utils.joinLines(lines);

        String finalSummary = ExtractSummary.finalSummary(Utils.joinLines(lines));
        AbstractSummary.fillDetails();


        String no = AbstractSummary.no;
        String date_desc = AbstractSummary.date_desc;
        String date = AbstractSummary.date;
        String part = ConvertToSinhala.formatValues(AbstractSummary.part);
        String about = AbstractSummary.about;
        String act = AbstractSummary.act;
        String who = AbstractSummary.who;
        String where = AbstractSummary.where;
        String title = AbstractSummary.title;
        int summary = Utils.countWords(finalSummary);
        double ratio = (double) summary/ (double) total;

        System.out.println("No: "+ no);
        System.out.println("Date_in_details: "+ date_desc);
        System.out.println("Date: "+ date);
        System.out.println("About: "+ about);
        System.out.println("Sections: "+ part);
        System.out.println("Acts: "+ act);
        System.out.println("Who: "+ who);
        System.out.println("Where: "+ where);
        System.out.println("Title: "+ title);
        System.out.println(KeyWords.gazetteKeywords);
        System.out.println(finalSummary);
        System.out.println("Total - " + total);
        System.out.println("Summary - " + summary);
        System.out.println("Ratio - " + ratio);
        System.out.println();

        String line = filename+";" + no +";" + date_desc+";" +date +";" +about +";" +
                part +";" +act +";" +who +";" +where +";"+title +";" +total +";"+summary +";"+ratio +";" +KeyWords.gazetteKeywords+";" +lines;
        WritetoFile(line);


        HashMap<String, Object> response = new HashMap<>();
        response.put("No", no);
        response.put("Date_in_details", date_desc);
        response.put("Date", date);
        response.put("About", about);
        response.put("Sections", part );
        response.put("Acts", act);
        response.put("Who", who);
        response.put("Where", where);
        response.put("Title", title);
        response.put("NoticeWordCount", total);
        response.put("SummaryCount", total);
        response.put("Ratio", String.format("%.2f",ratio));
        response.put("FinalOutput", finalSummary);
        response.put("others", lines);

        return response;

    }

    private static List<String> removeSinhala(String text) {
        List<String> lines = new ArrayList<>();
        for (String line: text.split("\n")) {
            if (!KeyWords.unWantedSinhalaLines.contains(line.trim())) {
                if (line.contains("නිෙව්දන")) {
                    line = line.replaceAll("නිෙව්දන","නිවේදන");
                }
                lines.add(line);
            }

        }
        return lines;
    }

    private static String clear(String text) {
        String[] words = text.split(" ");
        String newWord = "";
        for (String word: words) {
            if (word.equals("a") || word.equals("s") || word.equals("=") || word.equals("sa")) {
                word = "";
            }
            newWord = newWord + " " + word;
        }
        text = newWord.trim();
        text = text.replaceAll("  ", " ");
        return text;
    }

    private static void WritetoFile(String line) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        try {
            fw = new FileWriter("test.txt", true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            pw.println(line);
            System.out.println("Data Successfully appended into file");
            pw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                pw.close();
                bw.close();
                fw.close();
            } catch (IOException io) {
                // can't do anything }
            }

        }
    }

    public static boolean isUnwanted(String text) {
        boolean hasNumbers = false;
        boolean hasEnglish = false;
        boolean hasUnwantedChar = false;

        String[] unWantedList = KeyWords.unwantedText();
        Pattern lastDate = Pattern.compile("^[0-9]{4}+රැ+[0-9]{2}");

        if (StringUtils.indexOfAny(text, unWantedList) == -1) {
            if (text.matches("^.*[0-9]රැ.*$") || KeyWords.englishWords.contains(text) ) {
                hasUnwantedChar = true;
            }

            String[] words = text.split("\\p{Punct}");
            for (String letter: words) {
                if(letter.trim().matches("[0-9]+")) {
                    hasNumbers = true;
                }
                if(KeyWords.alphabet.contains(letter)) {
                    hasEnglish = true;
                }
            }

            if((hasEnglish && hasNumbers) || hasUnwantedChar) {
                return true;
            }

        } else {
            return true;
        }
        return false;
    }

    public static String replaceUnwanted(String text) {
        boolean hasNumbers = false;
        boolean hasEnglish = false;
        boolean hasUnwantedChar = false;

        String[] unWantedList = KeyWords.unwantedText();
        Pattern lastDate = Pattern.compile("^[0-9]{4}+රැ+[0-9]{2}");

        for (String unWantedWord: unWantedList) {
            if(StringUtils.indexOf(text,unWantedWord) != -1) {
                text = text.replaceAll(unWantedWord, "");
            } else {
                if (text.matches("^.*[0-9]රැ.*$") || KeyWords.englishWords.contains(text) ) {
                    hasUnwantedChar = true;
                }

                String[] words = text.split("\\p{Punct}");
                for (String letter: words) {
                    if(letter.trim().matches("[0-9]+")) {
                        hasNumbers = true;
                    }
                    for (String englishChar: KeyWords.alphabet) {
                        if (letter.contains(englishChar)) {
                            hasEnglish = true;
                        }
                    }
                }
                if((hasEnglish && hasNumbers) || hasUnwantedChar || hasEnglish) {
                    text = "";
                }
            }
        }


        return text;
    }

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

    private static List<String> format(List<String> text) {

        List<String> lines = new ArrayList<String>();

        for (int i = 0; i < text.size(); i++) {
            if (text.get(i).contains("අංක - ")) {
                text.set(i, text.get(i).replace("අංක - ", "අංක "));
            }
            if(!text.get(i).contains("වැනි කොටස")){
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
