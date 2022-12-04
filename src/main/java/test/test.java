package test;

import org.sinhala.summarization.ConvertToSinhala;
import org.sinhala.summarization.KeyWords;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class test {

    public static void main(String[] args) throws Exception{

//        Map<String, String> words = new HashMap<String,String>();
//        String file = "text.txt";
//        String output = "";
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = br.readLine()) != null) {
////               System.out.println(line);
//                output = output + line + "\n";
////                String[] lines = line.split("\t");
////                words.put(lines[0], lines[1]);
//            }
//        }
//
//        System.out.println(output);

//        Map<String, String> words = KeyWords.stemming();
//        System.out.println(words.get("ෆෝඞ්ගේ"));

//        ArrayList<String> x = KeyWords.suffixes();
//        System.out.println(x);


        String x = "k.rdêm;s'";
        System.out.println(ConvertToSinhala.convertText(x));
    }

}
