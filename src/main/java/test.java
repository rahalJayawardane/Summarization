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

    public static void main(String[] args) {
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

        List<String> alphabet = new ArrayList<String>(Arrays.asList("ඒ","බී","සී", "ඩී","එල්"));
        String t4 = "එල්.ඩී.බී. 12/2014 ( ෂ)";
        String[] charaters = t4.split("\\p{Punct}");
        List<String> letters = new ArrayList<>(Arrays.asList(charaters));
        Pattern number = Pattern.compile("[0-9]+");

        boolean hasNumbers = false;
        boolean hasEnglish = false;

        for (String letter: charaters) {
            if(letter.trim().matches("[0-9]+")) {
                hasNumbers = true;
            }

            if(alphabet.contains(letter)) {
                hasEnglish = true;
            }
        }

        if(hasEnglish && hasNumbers) {
            System.out.println("removed");
        }




    }
}
