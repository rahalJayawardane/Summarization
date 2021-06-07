import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class test {

    public static void main(String[] args) throws IOException {
//        String t = "ඨ 33510 —  385  ඃ2021රැ01";
//        t = " ඡඨ 5262  —  600  ඃ2021රැ01";
//        //if (t.matches("[0-9]රැ")) {
//        if (t.matches("^.*[0-9]රැ.*$")) {
//            System.out.println("test");
//        }
//        else {
//            System.out.println("no");
//        }

//        String t2 = "කොළඹ 07.";
//
//        List<String> whereKeywords = new ArrayList<String>(Arrays.asList("රාජගිරිය","කොළඹ","බත්තරමුල්ල","ගාල්ල","මාතලේ"));
//        for (String word:t2.split(" ")) {
//            System.out.println(whereKeywords.contains(word));
//        }

//        System.out.println(t2.replace());

//        String t3 = "මුදල් අමාත්\u200Dයාංශයේ දී ය2.";
//        System.out.println(t3.endsWith("දී ය."));


//        try (Stream<Path> paths = Files.walk(Paths.get("./SamplePDFs"))) {
//            paths
//                    .filter(Files::isRegularFile)
//                    .forEach(System.out::println);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        List<String> alphabet = new ArrayList<String>(Arrays.asList("ඒ","බී","සී", "ඩී","එල්"));
//        String t4 = "එල්.ඩී.බී. 12/2014 ( ෂ)";
//        String[] charaters = t4.split("\\p{Punct}");
//        List<String> letters = new ArrayList<>(Arrays.asList(charaters));
//        Pattern number = Pattern.compile("[0-9]+");
//
//        boolean hasNumbers = false;
//        boolean hasEnglish = false;
//
//        for (String letter: charaters) {
//            if(letter.trim().matches("[0-9]+")) {
//                hasNumbers = true;
//            }
//
//            if(alphabet.contains(letter)) {
//                hasEnglish = true;
//            }
//        }
//
//        if(hasEnglish && hasNumbers) {
//            System.out.println("removed");
//        }


        String filename = "./SamplePDFs/2184-50_S.pdf";
//        filename ="./SamplePDFs/2045-01_S.pdf";
        PDDocument document = PDDocument.load(new File(filename));
        if (!document.isEncrypted()) {
            PDFTextStripper stripper = new PDFTextStripper("ISO-15924");
            String text = stripper.getText(document);
            text = text.replaceAll("  ", " ");
            if(text.contains("ɼ")) {
                text = correctUnicode(text);
            }
            text = text.trim();
            text = PDFReader.convertText(text);
            System.out.println(text);
        }


        document.close();


    }

    public static String correctUnicode(String text) {
        text = text.replaceAll("ɼ","Y%S");
        text = text.replaceAll("\\/","\\$");
        text = text.replaceAll("\\,","\"");
        text = text.replaceAll("ȝජාතාǦƵක","m%cd;dka;%sl");
        text = text.replaceAll("Ǐ","§");
        text = text.replaceAll("රජෙɏ","rcfha");
        text = text.replaceAll("Ÿ","Ü");
        text = text.replaceAll("පƴය","m;%h");
        text = text.replaceAll("Ư",";s");
        text = text.replaceAll("ɪෙශෂ","úfYI");
        text = text.replaceAll("őǧ","cqks");
        text = text.replaceAll("වැǧ","jeks");
        text = text.replaceAll("ǧ","ks");
        text = text.replaceAll("\\.","\'");
        text = text.replaceAll("බලයȘට","n,hmsg");
        text = text.replaceAll("ȝʆǊධ","m%isoaO");
        text = text.replaceAll("ෙකʣටස","fldgi");
        text = text.replaceAll("ඉඩȼ","bvï");
        text = text.replaceAll("ගැǨම",".ekSu");
        text = text.replaceAll("Șʘබඳ","ms<sn|");
        text = text.replaceAll("දැǦɫȼ","oekaùï");
        text = text.replaceAll("ෙමම","fuu");
        text = text.replaceAll("www'documents'gov'lk","අඅඅගාදජමපැබඑිගටදඩගකන");
        text = text.replaceAll("අඩɪෙයǦ","wvúfhka");
        text = text.replaceAll("ෙවȩ","fjí");
        text = text.replaceAll("ෙමම","oekaùï");
        text = text.replaceAll("Ʈ","ත්");
        text = text.replaceAll("ගැǨෙȼ",".ekSfï");
        text = text.replaceAll("Ǧ","න්");
        text = text.replaceAll("ɫ","ù");
        text = text.replaceAll("පɜļෙņදය","mßÉfþoh");
        text = text.replaceAll("Ę","ග්");
        text = text.replaceAll("ෙයʣȿ","fhduq");
        text = text.replaceAll("ȿǖණ","uqøK");
        text = text.replaceAll("ෙදපාəතෙȼන්Ʊෙɩ","fomd¾;fïka;=fõ");
        text = text.replaceAll("ɪ","වි");
        text = text.replaceAll("ඡි","කු");
        text = text.replaceAll("ə","ර්");
        text = text.replaceAll("ȝාෙǊɵය","ප්\u200Dරාදේශීය");
        text = text.replaceAll("ȼ","ම්");
        text = text.replaceAll("ෙɢ","ලේ");
        text = text.replaceAll("ɐ","යි");
        text = text.replaceAll("ǎ","දි");
        text = text.replaceAll("ɏ","ය්");

        return text;
    }
}
