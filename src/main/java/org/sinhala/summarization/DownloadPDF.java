package org.sinhala.summarization;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DownloadPDF {

    public static void main(String[] args) throws IOException {
        String URL = "";
        URL = "http://documents.gov.lk/files/egz/2020/6/2178-04_S.pdf";
//
        test();
    }

    public static boolean download(String file) {

        String[] words = file.split("/");
        String fileName = "./SamplePDFs/" + words[words.length-1];
        System.out.println(fileName);
        try (BufferedInputStream in = new BufferedInputStream(new URL(file).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("wrote");
            return check(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean check(String fileName) throws IOException {
        File pdf = new File(fileName);
        PDDocument doc = PDDocument.load(pdf);
        int count = doc.getNumberOfPages();
        if (count != 1) {
            System.out.println("deleted: "+fileName);
            pdf.delete();
            return false;
        }
        return true;
    }

    public static HashMap<String, Object> getFile(String file) {
        boolean pass = download(file);
        HashMap<String, Object> response = new HashMap<>();
        if(pass) {
            response.put("status","ok");
        } else {
            response.put("status","failed");
            response.put("reason","PDF file has more than one page");
        }
        return response;
    }


    private static void test() throws IOException {
        String URL = "http://documents.gov.lk/files/egz/2020/7/";
        Document doc = Jsoup.connect(URL).get();
        for (Element file : doc.select("td a")) {
            String fileName = file.attr("href");
            if(fileName.endsWith("_S.pdf")) {
                System.out.println(fileName);
                download(URL+fileName);
                //check("./SamplePDFs/fileName");
            }
        }
    }
}
