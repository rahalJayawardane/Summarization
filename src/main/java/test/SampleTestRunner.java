package test;

import org.sinhala.summarization.KeyWords;
import org.sinhala.summarization.PDFFormatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SampleTestRunner {

    private static List<String> datesKeywords = new ArrayList<String>();
    private static List<String> partKeywords = new ArrayList<String>();
    private static List<String> sectionKeywords = new ArrayList<String>();
    private static List<String> actKeywords = new ArrayList<String>();
    private static List<String> publicationKeywords = new ArrayList<String>();
    private static List<String> whoKeywords = new ArrayList<String>();
    private static List<String> typeKeywords = new ArrayList<String>();
    private static List<String> whereKeywords = new ArrayList<String>();
    private static int index = 0;

    /**
     * Unit test.test case runner: Validate the output of one or many gazettes manually
     * @param args
     * @throws IOException
     */
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

//        files.add("./SamplePDFs/2233-13_S.pdf");
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
            PDFFormatter.getDetails(file);
            System.out.println();
            System.out.println();
            System.out.println("------------------------------------------");
            i++;
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("Time elapsed: "+ time/1000);

    }
}

