package org.sinhala.summarization;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListDownFiles {

    /**
     * Retrieve all data set
     *
     * @return
     */
    public static List<String> files() {

        List<String> files = new ArrayList<>();
        List<Path> result = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("./SamplePDFs"))) {
            result = paths.filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(result);

        for (Path path : result) {
            if (path.toString().endsWith(".pdf")) {
                files.add(path.toString().split("/")[2].split(".pdf")[0]);
            }
        }
        return files;
    }
}
