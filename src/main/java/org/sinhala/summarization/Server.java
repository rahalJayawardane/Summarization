package org.sinhala.summarization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {

    /**
     * Server Start up
     * @param args
     */
    public static void main(String[] args) {

        AddKeyWords();
        SpringApplication.run(Server.class, args);
    }

    /**
     * Load all keywords to the system
     */
    private static void AddKeyWords() {

        KeyWords.addKeywords();
    }

}
