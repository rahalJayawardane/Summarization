package org.sinhala.summarization;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.sound.midi.Soundbank;

/**
 * Download the PDF and do the validation
 */
public class DownloadPDF {

    static String errorMsg = "";

    /**
     * For unit testing: validation process
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        String baseURL = "http://documents.gov.lk/files/egz/";
        String year = "2021";
        String month = "06";
        String URL = baseURL + "/" + year + "/" + month + "/";
        downloadAllGazettes(URL);
    }

    /**
     * Download the file and save it local
     *
     * @param file
     * @return
     */
    public static boolean download(String file) {

        String[] words = file.split("/");
        String fileName = "./SamplePDFs/" + words[words.length - 1];
        System.out.println(fileName);
        try (BufferedInputStream in = new BufferedInputStream(new URL(file).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("wrote");
            return singlePageValidation(fileName);
        } catch (IOException e) {
            errorMsg = "URL is not accessible";
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Validate the downloaded PDF has one page
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    private static boolean singlePageValidation(String fileName) throws IOException {

        File pdf = new File(fileName);
        PDDocument doc = PDDocument.load(pdf);
        int count = doc.getNumberOfPages();
        if (count != 1) {
            pdf.delete();
            errorMsg = "File is invalid. Has more than one page";
            return false;
        }
        return true;
    }

    /**
     * Validate the downloaded PDF is a Sinhala Gazette
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static boolean isSinhalaGazette(String fileName) throws IOException {

        List<String> lines = new ArrayList<String>();
        PDDocument document = PDDocument.load(new File(fileName));
        if (!document.isEncrypted()) {
            PDFTextStripper stripper = new PDFTextStripper("ISO-15924");
            String text = stripper.getText(document);
            text = text.trim();

            if (text.startsWith("ɼ") || text.contains("ශී ලංකා")) {
                return true;
            }
            errorMsg = "Cannot found a valid Sinhala document";
            System.out.println("Cannot found a valid Sinhala document : " + fileName);
        } else {
            errorMsg = "Document is encrypted";
            System.out.println("Document is encrypted: " + fileName);
        }
        return false;
    }

    /**
     * Download single file
     *
     * @param file
     * @return
     */
    public static HashMap<String, Object> getFile(String file) {

        boolean pass = download(file);
        HashMap<String, Object> response = new HashMap<>();
        if (pass) {
            response.put("status", "ok");
        } else {
            response.put("status", "failed");
            response.put("reason", errorMsg);
        }
        return response;
    }

    /**
     * Download all the gazettes for given month and year
     *
     * @param URL
     * @throws IOException
     */
    private static void downloadAllGazettes(String URL) throws IOException {

        for (int i = 0; i <= 8; i++) {
            Document doc = Jsoup.connect(URL).get();
            for (Element file : doc.select("td a")) {
                String fileName = file.attr("href");
                if (fileName.endsWith("_S.pdf")) {
                    System.out.println("Downloading the file: " + fileName);
                    download(URL + fileName);
                }
            }
        }

    }
}
