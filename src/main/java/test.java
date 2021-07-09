import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.sinhala.summarization.KeyWords;
import org.sinhala.summarization.PDFReader;

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


//        String filename = "./SamplePDFs/2180-22_S.pdf";
////        filename ="./SamplePDFs/2045-01_S.pdf";
//        PDDocument document = PDDocument.load(new File(filename));
//        if (!document.isEncrypted()) {
//            PDFTextStripper stripper = new PDFTextStripper("ISO-15924");
//            String text = stripper.getText(document);
//            text = text.replaceAll("  ", " ");
//            if(text.contains("ɼ")) {
//                text = PDFReader.correctUnicode(text);
//            }
//            text = text.trim();
////            text = PDFReader.convertText(text);
//            System.out.println(text);
//        }
//
//
//        document.close();

//        String lastword = "ඉඩම් හා ඉඩම් සංවර්ධන අමාත්\u200Dය.";
//        if(lastword.matches(".*\\p{Punct}")) {
////            lastword = lastword.replace(".*\\p{Punct}", "");
//            lastword = lastword.replace(".", "");
//            lastword = lastword.replace(",", "");
//            System.out.println(lastword);
//        }


//        String t5 = "2020 අංක 63/1";
//        String[] words = t5.split(" ");
//        String lastWord = words[words.length-1].replaceAll("\\p{Punct}", "");
//        if ((lastWord.matches("[0-9]+") && "අංක".equals(words[words.length-2]))) {
//            System.out.println("done");
//        } else {
//            System.out.println("no");
//        }


        String g = "ආර්ඩබ්පී ඇන්ඩ් බාර් වීඑස්වී යූඑස්පී පීඑච්ඩී,";
        System.out.println(KeyWords.alphabet.contains(g));

        System.out.println(g.contains("යූ"));

        String dd = "bvï w;alr .ekSfï mkf;a 38 jeks j.ka;sh ^w& w;=re úOdkh\n" +
                "hgf;a mkjk ,oaodjQ o\" wxl 2084$06 yd 2018'08'14 jeks Èk ork\n" +
                "w;s úfYI .eiÜ m;%fha m<lrk ,oaodjQ o\" 2018 wxl 193 yd 2018'06'20\n" +
                "jeks Èk ork wd{dfõ iúia;rj oelafjk bvï fldgi\n" +
                "Y%S ,xld m%cd;dka;%sl iudcjd§ ckrch fj; mejÍ ;sfí' tkuq;a\n" +
                "tu bvï fldgfia N=la;sh Y%S ,xld m%cd;dka;%sl iudcjd§ ckrch\n" +
                "fjkqfjka ;ju Ndrf.k ke;' tneúka\" bvï yd bvï ixj¾Ok\n" +
                "wud;H\" iurfldaka uqÈhkafia,df.a pkaøfiak jk uu\" bvï w;alr\n" +
                ".ekSfï mkf;a 39^1& jeks j.ka;sh hgf;a ud yg mejÍ we;s n,;,\n" +
                "wkqj wxl 2084$06 yd 2018'08'14 jeks Èk ork w;s úfYI .eiÜ\n" +
                "m;%fha m<lrk ,o by; i|yka 2018 wxl 193 yd 2018'06'20 jeks Èk\n" +
                "ork wd{dj fuu kshuh u.ska m%;HdÈIaG lrñ";

        System.out.println(dd.split(" ").length);

    }


}
