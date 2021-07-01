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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PDFReader {

    private static List<String> datesKeywords = new ArrayList<String>();
    private static List<String> partKeywords = new ArrayList<String>();
    private static List<String> sectionKeywords = new ArrayList<String>();
    private static List<String> actKeywords = new ArrayList<String>();
    private static List<String> publicationKeywords = new ArrayList<String>();
    private static List<String> whoKeywords = new ArrayList<String>();
    private static List<String> typeKeywords = new ArrayList<String>();
    private static List<String> whereKeywords = new ArrayList<String>();
    private static int index = 0;

    public static void main(String[] args) throws IOException {

        addKeywords();

        List<String> files = new ArrayList<>();
        List<Path> result = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("./SamplePDFs"))) {
            result = paths.filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        result.forEach(x -> files.add(""+ x));


//        files.add("./SamplePDFs/2184-52_S.pdf");
//        files.add("./SamplePDFs/2.pdf");
//        files.add("./SamplePDFs/3.pdf");
//        files.add("./SamplePDFs/4.pdf");
//        files.add("./SamplePDFs/5.pdf");
//        files.add("./SamplePDFs/6.pdf");
//        files.add("./SamplePDFs/7.pdf");
//        files.add("./SamplePDFs/8.pdf");
//        files.add("./SamplePDFs/9.pdf");
//        files.add("./SamplePDFs/10.pdf");
//        files.add("./SamplePDFs/11.pdf");
//        files.add("./SamplePDFs/2178-04_S_2.pdf");
//        files.add("./SamplePDFs/2181-19_S.pdf");
//        files.add("./SamplePDFs/2045-01_S.pdf");
//        files.add("./SamplePDFs/2184-52_S.pdf");
//        files.add("./SamplePDFs/2179-08_S.pdf");


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

    }

    public static HashMap<String, Object> downloadFile(String file) throws IOException {
        DownloadPDF.download(file);
        return getDetails(file);
    }

    public static HashMap<String, Object> getDetails(String file) throws IOException {
        addKeywords();
        String[] words = file.split("/");
        String fileName = words[words.length-1];
        HashMap<String, Object> response = method("./SamplePDFs/"+fileName);
        return response;
    }

    private static void addDateKeywords() {
        List<String> months = new ArrayList<String>(Arrays.asList("මස", "ජනවාරි","පෙබරවාරි","මාර්තු","අප්\u200Dරියෙල්","මැයි","ජූනි",
                "ජූලි","අගෝස්තු","සැප්තැම්බර්","ඔක්තෝබර්","නොවැම්බර්","දෙසැම්බර්"));
        List<String> days = new ArrayList<String>(Arrays.asList("සඳුදා","අඟහරුවාදා","බදාදා","බ්\u200Dරහස්පතින්දා","සිකුරාදා",
                "සෙනසුරාදා","ඉරිදා"));

        datesKeywords.addAll(months);
        datesKeywords.addAll(days);
    }

    private static void addKeywords() {

        partKeywords = new ArrayList<String>(Arrays.asList("වැනි","කොටස", "කොටසථ"));
        sectionKeywords = new ArrayList<String>(Arrays.asList("වැනි","ඡෙදය", "පළාත්", "පාලනය"));
        typeKeywords = new ArrayList<String>(Arrays.asList("සාමාන්\u200Dය"));
        actKeywords = new ArrayList<String>(Arrays.asList("ආඥාපනත", "පනත", "වගන්තිය", "සංග්\u200Dරහය","පරිච්ෙඡ්දය","ව්\u200Dයවස්ථා"));
        publicationKeywords = new ArrayList<String>(Arrays.asList("රජයේ","නිවේදන", "යටතේ", "දැන්වීම්", "ආදිය"));
        whoKeywords = new ArrayList<String>(Arrays.asList("ලේකම්","අමාත්\u200Dය", "නිලධාරි", "ආණ්ඩුකාරවර", "කොමසාරිස්",
                "අමාත්\u200Dය","ජනරාල්","ජනාධිපති","සභාපති","නිලධාරී","දිසාපති", "අමාත\u200Dය","නිලධාරීල"));
        whereKeywords = new ArrayList<String>(Arrays.asList("රාජගිරිය","කොළඹ","බත්තරමුල්ල","ගාල්ල","මාතලේ"));
        addDateKeywords();
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
            text = ConvertToSinhala.convertText(text);
            lines = remove(text);
            lines = format(lines);
        }

        document.close();

        String no = formatNo(getCorrectLine(lines, new ArrayList<String>(Arrays.asList("අංක"))));
        String date_desc = getCorrectLine(lines, datesKeywords);
        String date = getDate(lines);
        String part = ConvertToSinhala.formatValues(getCorrectLine(lines, sectionKeywords));
        String about = getCorrectLine(lines, publicationKeywords);
        String act = formatAct(lines, actKeywords);
        String who = selectedLineReverse(lines, whoKeywords);
        String where = getWhere(lines, whereKeywords);

        System.out.println("No: "+ no);
        System.out.println("Date_in_details: "+ date_desc);
        System.out.println("Date: "+ date);
        System.out.println("About: "+ about);
        System.out.println("Sections: "+ part);
        System.out.println("Acts: "+ act);
        System.out.println("Who: "+ who);
        System.out.println("Where: "+ where);
        System.out.println(lines);
        System.out.println();

        String line = filename+"^" + no +"^" + date_desc+"^" +date +"^" +about +"^" +
                part +"^" +act +"^" +who +"^" +where +"^" +lines;
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
        response.put("others", lines);

        return response;

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
                return getValue(lines, line);
            }
        }
        return line2;
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

    private static String getCorrectLine(List<String> lines, List<String> keywords) {
        String lineWord = "";
        for (String line:lines) {
            String[] words = line.split(" ");
            for(String word: words) {
                if(keywords.contains(word)) {
                    return getValue(lines, line);
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
            } if (line.endsWith("දින,")) {
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
                    if(whereKeywords.contains(word.replaceAll("\\p{Punct}","")) ) {
                        endIndex = i;
                        break;
                    }
                }
                if (endIndex > 0) {
                    break;
                }
            }
        }

        for (int i = startIndex+1; i <= endIndex; i++) {
            where = where + " - " + tempLines.get(i);
            getValue(lines, tempLines.get(i));
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
                    tempWord = getValue(lines, line);
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
            act = selectedLine(lines, actKeywords, "යටතේ");
            String[] actWords = act.split(" ");
            for (int i = actWords.length-1 ; i > 0; i--) {
                if (actKeywords.contains(actWords[i]) ||
                        (actWords[i].replaceAll("\\p{Punct}", "").matches("[0-9]+")) &&
                                actKeywords.contains(actWords[i-1])) {
                    act = Stream.of(actWords).limit((i+1)).collect(Collectors.joining(" "));
                    break;
                }
            }
        } else {
            String[] words = act.split(",- ");
            String newAct = "";
            for (String word: words) {
                if (newAct.equals("")) {
                    newAct = getValue(lines, word);
                } else {
                    newAct = newAct + ", " +  getValue(lines, word);
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
                    String position = getValue(lines, lines.get(i));
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
                if(isUnwanted(word)) {
                    isName = false;
                }
            }
            if (isName) {
                if (lines.get(i).split(" ").length > 6) {
                    return "";
                }
                return getValue(lines, i);
            }
        }
        return "";
    }

    private static String getValue(List<String> lines) {
        String value = lines.get(index);
        lines.remove(index);
        return value;

    }

    private static String getValue(List<String> lines, int index) {
        String value = lines.get(index);
        lines.remove(index);
        return value;

    }

    private static String getValue(List<String> lines, String value) {
        int index = lines.indexOf(value);
        lines.remove(index);
        return value;

    }

    private static String checkLine(List<String> lines, List<String> sectionKeywords) {
        int score = 0;
        String[] selecteLineWords = lines.get(index).split(" ");
        for (String word: selecteLineWords) {
            if(sectionKeywords.contains(word)) {
                score++;
            }
        }
        if (score > 0) {
            return getValue(lines);
        }
        return "";
    }

    private static List<String> remove(String text) {
        String[] allWords = text.split("\n");
        List<String> lines = new ArrayList<String>();
        for (int i = 0; i < allWords.length; i++) {
            if (!isUnwanted(allWords[i])) {
                lines.add(allWords[i].trim());
            }
        }

        lines.removeAll(Arrays.asList("", null));
        return lines;
    }

    private static boolean isUnwanted(String text) {
        boolean hasNumbers = false;
        boolean hasEnglish = false;
        boolean hasUnwantedChar = false;
        String[] unWantedList = new String[20];
        List<String> englishWords = new ArrayList<String>(Arrays.asList("ඇන්ඩ්","ඔෆ්", "ද"));
        List<String> alphabet = new ArrayList<String>(Arrays.asList("ඒ","බී","සී", "ඩී","එල්"));
        unWantedList[0] = "ශ්\u200Dරී ලංකා ප්\u200Dරජාතාන්ත්\u200Dරික සමාජවාදී ජනරජයේ ගැසට් පත්\u200Dරය";
        unWantedList[1] = "මෙම අති විශෙෂ ගැසට් පත්\u200Dරය අඅඅගාදජමපැබඑිගටදඩගකන වෙබ් අඩවියෙන් බාගත කළ හැක.";
        unWantedList[2] = "ශ්\u200Dරී ලංකා රජයේ මුද්\u200Dරණ දෙපාර්තමේන්තුවේ මුද්\u200Dරණය කරන ලදී.";
        unWantedList[3] = "රජයේ බලයපිට ප්\u200Dරසිද්ධ කරන ලදී";
        unWantedList[4] = "අති විශෙෂ";
        unWantedList[5] = "ශ්\u200Dරී ලංකා";
        unWantedList[6] = "යොමු අංකය";
        unWantedList[7] = "ජනා. කා.";

        Pattern lastDate = Pattern.compile("^[0-9]{4}+රැ+[0-9]{2}");

        if (StringUtils.indexOfAny(text, unWantedList) == -1) {
            if (text.matches("^.*[0-9]රැ.*$") || englishWords.contains(text) ) {
                hasUnwantedChar = true;
            }

            String[] words = text.split("\\p{Punct}");
            for (String letter: words) {
                if(letter.trim().matches("[0-9]+")) {
                    hasNumbers = true;
                }
                if(alphabet.contains(letter)) {
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


    private static List<String> format(List<String> text) {

        List<String> lines = new ArrayList<String>();
        for (int i = 0; i < text.size(); i++) {
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

