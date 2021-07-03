package org.sinhala.summarization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyWords {

    public static List<String> datesKeywords = new ArrayList<String>();
    public static List<String> partKeywords = new ArrayList<String>();
    public static List<String> sectionKeywords = new ArrayList<String>();
    public static List<String> actKeywords = new ArrayList<String>();
    public static List<String> secondActKeywords = new ArrayList<String>();
    public static List<String> publicationKeywords = new ArrayList<String>();
    public static List<String> whoKeywords = new ArrayList<String>();
    public static List<String> typeKeywords = new ArrayList<String>();
    public static List<String> whereKeywords = new ArrayList<String>();
    public static List<String> months = new ArrayList<String>(Arrays.asList(
            "මස", "ජනවාරි","පෙබරවාරි","මාර්තු","අප්\u200Dරියෙල්","මැයි","ජූනි",
            "ජූලි","අගෝස්තු","සැප්තැම්බර්","ඔක්තෝබර්","නොවැම්බර්","දෙසැම්බර්"));

    public static List<String> days = new ArrayList<String>(Arrays.asList(
            "සඳුදා","අඟහරුවාදා","බදාදා","බ්\u200Dරහස්පතින්දා","සිකුරාදා","සෙනසුරාදා","ඉරිදා"));

    public static List<String> englishWords = new ArrayList<String>(Arrays.asList("ඇන්ඩ්","ඔෆ්", "ද"));
    public static List<String> alphabet = new ArrayList<String>(Arrays.asList("ඒ","බී","සී", "ඩී","එල්"));
    public static String[] unWantedList = new String[20];



    private static void addDateKeywords() {

        datesKeywords.addAll(months);
        datesKeywords.addAll(days);
    }

    public static void addKeywords() {

        partKeywords = new ArrayList<String>(Arrays.asList("වැනි","කොටස", "කොටසථ"));
        sectionKeywords = new ArrayList<String>(Arrays.asList("වැනි","ඡෙදය", "පළාත්", "පාලනය","සාමාන්\u200Dය"));
        typeKeywords = new ArrayList<String>(Arrays.asList("සාමාන්\u200Dය"));
        actKeywords = new ArrayList<String>(Arrays.asList("ආඥාපනත", "පනත", "වගන්තිය", "සංග්\u200Dරහය","පරිච්ෙඡ්දය","ව්\u200Dයවස්ථා"));
        publicationKeywords = new ArrayList<String>(Arrays.asList("රජයේ","නිවේදන", "යටතේ", "දැන්වීම්", "ආදිය"));
        whoKeywords = new ArrayList<String>(Arrays.asList("ලේකම්","අමාත්\u200Dය", "නිලධාරි", "ආණ්ඩුකාරවර", "කොමසාරිස්",
                "අමාත්\u200Dය","ජනරාල්","ජනාධිපති","සභාපති","නිලධාරී","දිසාපති", "අමාත\u200Dය","නිලධාරීල"));
        whereKeywords = new ArrayList<String>(Arrays.asList("රාජගිරිය","කොළඹ","බත්තරමුල්ල","ගාල්ල","මාතලේ","කුරුණෑගල","ගම්පහ",
                "මහනුවර","මුලතිව්","මඩකලපුව","අම්පාර","හම්බන්තොට","යාපනය","පුත්තලම","ත්\u200Dරිකුණාමලය","වවුනියාව", "මොණරාගල",
                "අනුරාධපුර","බදූල්ල",
                "දිස්ති්\u200Dරක්කය","දිස්ත්\u200Dරික්කය"));

        secondActKeywords= new ArrayList<String>(Arrays.asList("නියමය", "වැනි", "යටතේ","ප්\u200Dරකාශය"));
        sectionKeywords.addAll(actKeywords);

        addDateKeywords();
    }

    public static String[] unwantedText() {
        unWantedList[0] = "ශ්\u200Dරී ලංකා";
        unWantedList[1] = "මෙම අති විශෙෂ ගැසට් පත්\u200Dරය අඅඅගාදජමපැබඑිගටදඩගකන වෙබ් අඩවියෙන් බාගත කළ හැක.";
        unWantedList[2] = "රජයේ මුද්\u200Dරණ දෙපාර්තමේන්තුවේ මුද්\u200Dරණය කරන ලදී.";
        unWantedList[3] = "රජයේ බලයපිට ප්\u200Dරසිද්ධ කරන ලදී";
        unWantedList[4] = "අති විශෙෂ";
        unWantedList[5] = "යොමු අංකය";
        unWantedList[6] = "ජනා. කා.";
        unWantedList[7] = "ජනරජයේ";
        unWantedList[8] = "ගැසට් පත්\u200Dරයේ";
        unWantedList[9] = "ප්\u200Dරජාතාන්ත්\u200Dරික";
        unWantedList[10] = "සමාජවාදී";
        unWantedList[11] = "ගැසට් පත්\u200Dරය";


        return unWantedList;
    }

}
