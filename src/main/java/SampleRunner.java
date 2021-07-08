import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.sinhala.summarization.ConvertToSinhala;
import org.sinhala.summarization.PDFReader;

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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SampleRunner {

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

        List<String> files = new ArrayList<>();
        List<Path> result = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("./SamplePDFs"))) {
            result = paths.filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        result.forEach(x -> files.add(""+ x));


//        files.add("./SamplePDFs/2180-12_S.pdf");
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

    private static void method(String filename) throws IOException {

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
            lines = SampleRunner.remove(text);
            checkMethod(lines);
        }

        document.close();


    }

    private static List<String> remove(String text) {
        String[] allWords = text.split("\n");
        List<String> lines = new ArrayList<String>();
        for (int i = 0; i < allWords.length; i++) {
            if (!PDFReader.isUnwanted(allWords[i])) {
                lines.add(allWords[i].trim());
            } else {
                lines.add(PDFReader.replaceUnwanted(allWords[i]).trim());
            }
        }

        lines.removeAll(Arrays.asList("", null));
        return lines;
    }

    private static void checkMethod (List<String> lines) {
        for (String line: lines) {
            if (line.endsWith(".") && line.split(" ").length > 1) {
                WritetoFile(line);
            }
        }
    }

    private static void WritetoFile(String line) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        try {
            fw = new FileWriter("test123.txt", true);
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


}

