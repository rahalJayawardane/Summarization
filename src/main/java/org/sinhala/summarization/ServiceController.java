package org.sinhala.summarization;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

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

    @RequestMapping(value = "/file")
    public ResponseEntity<Object> getFile(@RequestParam("fileId") int fileId)  {
        try {
            return ResponseEntity.ok(PDFReader.getDetails(fileId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("File Not found");
        }

    }
}
