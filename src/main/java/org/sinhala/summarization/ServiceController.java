package org.sinhala.summarization;

import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
public class ServiceController {

    private static String productRepo = new String("daonne");

    private static JSONObject results;

    public static JSONObject getResults() {

        return results;
    }

    public static void setResults(JSONObject results) {

        ServiceController.results = results;
    }

    /**
     * Get file via fileID
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/file")
    public ResponseEntity<Object> getFile(@RequestParam("fileId") String file) {

        try {
            return ResponseEntity.ok(PDFFormatter.getDetails("./SamplePDFs/" + file + ".pdf"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("File Not found");
        }

    }

    /**
     * Get file via URL
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/file/v2")
    public ResponseEntity<Object> downloadFileAndGetContent(@RequestHeader("url") String file) {

        try {
            return ResponseEntity.ok(DownloadPDF.getFile(file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("File Not found");
        }

    }

    /**
     * Get all files
     *
     * @return
     */
    @RequestMapping(value = "/getallfiles")
    public ResponseEntity<Object> getAllFile() {

        try {
            return ResponseEntity.ok(ListDownFiles.files());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }

    }

    /**
     * Get and Download the PDF
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    @RequestMapping(value = "/getpdf")
    public ResponseEntity<InputStreamResource> getPDF(@RequestParam("file") String file) throws FileNotFoundException {

        String filePath = "./SamplePDFs/";
        String fileName = file + ".pdf";
        String filePDF = filePath + fileName;
        System.out.println(filePDF);
        File pdf = new File(filePDF);
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "inline;filename=" + fileName);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(pdf));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdf.length())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);
    }
}
