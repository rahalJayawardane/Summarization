import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {

    public static void main(String[] args) {
        String t = "ඨ 33510 —  385  ඃ2021රැ01";
        t = " ඡඨ 5262  —  600  ඃ2021රැ01";
        //if (t.matches("[0-9]රැ")) {
        if (t.matches("^.*[0-9]රැ.*$")) {
            System.out.println("test");
        }
        else {
            System.out.println("no");
        }

        String t2 = "කොළඹ 07.";

        List<String> whereKeywords = new ArrayList<String>(Arrays.asList("රාජගිරිය","කොළඹ","බත්තරමුල්ල","ගාල්ල","මාතලේ"));
        for (String word:t2.split(" ")) {
            System.out.println(whereKeywords.contains(word));
        }

//        System.out.println(t2.replace());

        String t3 = "මුදල් අමාත්\u200Dයාංශයේ දී ය2.";
        System.out.println(t3.endsWith("දී ය."));



    }
}
