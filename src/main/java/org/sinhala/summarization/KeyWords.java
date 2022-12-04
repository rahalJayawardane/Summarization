package org.sinhala.summarization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyWords {

    public static List<String> datesKeywords = new ArrayList<String>();
    public static List<String> partKeywords = new ArrayList<String>();
    public static List<String> sectionKeywords = new ArrayList<String>();
    public static List<String> titleKeywords = new ArrayList<String>();
    public static List<String> actKeywords = new ArrayList<String>();
    public static List<String> secondActKeywords = new ArrayList<String>();
    public static List<String> publicationKeywords = new ArrayList<String>();
    public static List<String> whoKeywords = new ArrayList<String>();
    public static List<String> typeKeywords = new ArrayList<String>();
    public static List<String> whereKeywords = new ArrayList<String>();
    public static List<String> months = new ArrayList<String>(Arrays.asList(
            "මස", "ජනවාරි", "පෙබරවාරි", "මාර්තු", "අප්\u200Dරියෙල්", "මැයි", "ජූනි",
            "ජූලි", "අගෝස්තු", "සැප්තැම්බර්", "ඔක්තෝබර්", "නොවැම්බර්", "දෙසැම්බර්"));

    public static List<String> days = new ArrayList<String>(Arrays.asList(
            "සඳුදා", "අඟහරුවාදා", "බදාදා", "බ්\u200Dරහස්පතින්දා", "සිකුරාදා", "සෙනසුරාදා", "ඉරිදා"));

    public static List<String> englishWords = new ArrayList<String>(Arrays.asList("ඇන්ඩ්", "ඔෆ්", "ද"));
    public static List<String> alphabet = new ArrayList<String>(Arrays.asList("ඒ", "බී", "සී", "ඩී", "එෆ්", "එල්", "අයි", "එම්", "යූ",
            "ආර්", "එස්", "පී"));
    public static String[] unWantedList = new String[20];
    public static List<String> removingKeywords = new ArrayList<String>(Arrays.asList(
            " ප්\u200Dරජාතාන්ත්\u200Dරික සමාජවාදී ජනරජයේ ", " වන මම ", " මම ", " වන මා ", " මා විසින් ", " උපලේඛනයේ ", " උපලේඛනය ",
            " මගින් ", " එනමුත් ", " මා හට ", " මා වෙත ", " සියලූ දෙනාගේම ", " දැන ගැනීම සඳහා ",
            " එබැවින් ", " එම ", " වැනි යටතේ ", " දින ", "දරන ", " වැනි ", " ගැනීමේ ", " හා ", " ේ ", " සිට දින දක්වා ",
            " දින දක්වා ", "ඉහත කී ආඥාපනතේ"));

    public static List<String> gazetteKeywords = new ArrayList<>();

    public static List<String> closingKeywords = new ArrayList<String>(Arrays.asList(
            "කරමි.", "යුතුය.", "ලැබේ.", "ය.", "හැක.", "දන්වමි.", "ලබයි.", "ඇත.", "යුතුයි.", "සිටිමි.", "දෙමි.", "තිබේ.", "පවරමි.",
            "වේ.", "ඃ"));

    public static List<String> unWantedRepeats = new ArrayList<String>(Arrays.asList("මස"));
    public static List<String> unWantedSinhalaLines = new ArrayList<String>(Arrays.asList(
            "ශී ලංකා පජාතාන්තික සමාජවාදී ජනරජෙය් ගැසට් පතය",
            "අති විෙශෂ",
            "(රජෙය් බලයපිට පසිද්ධ කරන ලදී)",
            "ෙමම අති විෙශෂ ගැසට් පතය www.documents.gov.lk ෙවබ් අඩවිෙයන් බාගත කළ හැක.",
            "ශී ලංකා රජෙය් මුදණ ෙදපාර්තෙම්න්තුෙව් මුදණය කරන ලදී."
    ));

    /**
     * add Date keywords
     *
     * @return
     */
    private static void addDateKeywords() {

        datesKeywords.addAll(months);
        datesKeywords.addAll(days);
    }

    /**
     * Add keywords to system
     */
    public static void addKeywords() {

        partKeywords = new ArrayList<String>(Arrays.asList("වැනි", "කොටස", "කොටසථ"));
        sectionKeywords = new ArrayList<String>(Arrays.asList("වැනි", "ඡෙදය", "පළාත්", "පාලනය", "සාමාන්\u200Dය"));
        typeKeywords = new ArrayList<String>(Arrays.asList("සාමාන්\u200Dය"));
        actKeywords = new ArrayList<String>(Arrays.asList("ආඥාපනත", "පනත", "වගන්තිය", "සංග්\u200Dරහය",
                "පරිච්ෙඡ්දය", "ව්\u200Dයවස්ථා", "බද්ද", "පදක්කම"));
        publicationKeywords = new ArrayList<String>(Arrays.asList("රජයේ", "නිවේදන", "දැන්වීම්", "ආදිය", "පැවරීම", "දනුම්"));
        whoKeywords = new ArrayList<String>(Arrays.asList("ලේකම්", "අමාත්\u200Dය", "නිලධාරි", "ආණ්ඩුකාරවර", "කොමසාරිස්",
                "අමාත්\u200Dය", "ජනරාල්", "ජනාධිපති", "සභාපති", "නිලධාරී", "දිසාපති", "අමාත\u200Dය", "ආණ්ඩුකාර", "අධ්\u200Dයක්ෂ",
                "අධිපති", "නගරාධිපති"));
        whereKeywords = new ArrayList<String>(Arrays.asList("රාජගිරිය", "කොළඹ", "බත්තරමුල්ල", "ගාල්ල", "මාතලේ", "කුරුණෑගල",
                "ගම්පහ", "මහනුවර", "මුලතිව්", "මඩකලපුව", "අම්පාර", "හම්බන්තොට", "යාපනය", "පුත්තලම", "ත්\u200Dරිකුණාමලය", "වවුනියාව",
                "මොණරාගල", "අනුරාධපුර", "බදූල්ල", "වත්තල", "රත්නපුර", "කළුතර",
                "අමාත්\u200Dයාංශය", "දිස්ති්\u200Dරක්කය", "දිස්ත්\u200Dරික්කය"));

        titleKeywords = new ArrayList<String>(Arrays.asList("පිරවීම", "දර්ශකය්", "දර්ශකය", "දැන්වීමයි", "දැන්වීම", "දීමනා",
                "නිවේදනය", "නියෝගය", "නියමය", "නියමයයි"));
        sectionKeywords.addAll(actKeywords);

        addDateKeywords();
    }

    /**
     * unwanted lines which can remove directly.
     *
     * @return
     */
    public static String[] unwantedText() {

        unWantedList[0] = "ශ්\u200Dරී ලංකා";
        unWantedList[1] = "මෙම අති විශෙෂ ගැසට් පත්\u200Dරය අඅඅගාදජමපැබඑිගටදඩගකන වෙබ් අඩවියෙන් බාගත කළ හැක.";
        unWantedList[2] = "මෙම අති විශේෂ ගැසට් පත්\u200Dරය අඅඅගාදජමපැබඑිගටදඩගකන වෙබ් අඩවියෙන් බාගත කළ හැක.";
        unWantedList[3] = "රජයේ මුද්\u200Dරණ දෙපාර්තමේන්තුවේ මුද්\u200Dරණය කරන ලදී.";
        unWantedList[4] = "රජයේ බලයපිට ප්\u200Dරසිද්ධ කරන ලදී";
        unWantedList[5] = "අති විශෙෂ";
        unWantedList[6] = "යොමු අංකය";
        unWantedList[7] = "ජනා. කා.";
        unWantedList[8] = "ජනරජයේ";
        unWantedList[9] = "ගැසට් පත්\u200Dරයේ";
        unWantedList[10] = "ප්\u200Dරජාතාන්ත්\u200Dරික";
        unWantedList[11] = "සමාජවාදී";
        unWantedList[12] = "ගැසට් පත්\u200Dරය";
        unWantedList[13] = "මගේ අංකයථ";
        unWantedList[14] = "රජයේ මුද්\u200Dරණදෙපාර්තමේන්තුවේ මුද්\u200Dරණය කරන ලදී.";

        return unWantedList;
    }

    /**
     * Get all stemming words
     *
     * @return
     */
    public static Map<String, String> stemming() {

        String fileName = "stemWords.txt";
        String output = Utils.readFile(fileName);

        Map<String, String> words = new HashMap<String, String>();
        String lines[] = output.split("\\n");
        for (String line : lines) {
            String[] word = line.split("\t");
            words.put(word[0], word[1]);
        }

        return words;
    }

    /**
     * Get all suffixes
     *
     * @return
     */
    public static ArrayList<String> suffixes() {

        String fileName = "suffixes.txt";
        String output = Utils.readFile(fileName);

        ArrayList<String> words = new ArrayList<String>();
        String lines[] = output.split("\\n");
        for (String line : lines) {
            words.add(line);
        }
        return words;
    }

}
