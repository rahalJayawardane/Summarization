package org.sinhala.summarization;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class GenerateSummary {

    static String gazette, no, date_desc, date, part, about, act, who, where, title, finalSummary = null;
    static int total, summary = 0;
    static double ratio = 0;

    /**
     * Get the Abstract and Extract summary and set the response
     *
     * @param lines
     * @return
     */
    public static HashMap<String, Object> createSummary(String fileName, List<String> lines) {

        gazette = fileName;

        //Abstract summary generates here
        AbstractSummary.generateSummary(lines);

        //Extractive summary generates here
        finalSummary = generateExtractSummary(lines);

        AbstractSummary.fillDetails();
        AssignAbstractiveValues();

        calculateRatio(lines);
        WriteToFile(lines);

        HashMap<String, Object> response = new HashMap<>();
        response.put("No", no);
        response.put("Date_in_details", date_desc);
        response.put("Date", date);
        response.put("About", about);
        response.put("Sections", part);
        response.put("Acts", act);
        response.put("Who", who);
        response.put("Where", where);
        response.put("Title", title);
        response.put("NoticeWordCount", total);
        response.put("SummaryCount", summary);
        response.put("Ratio", String.format("%.2f", ratio));
        response.put("FinalOutput", finalSummary);
        response.put("others", lines);

        return response;
    }

    /**
     * Calculate ratio
     *
     * @param lines
     */
    private static void calculateRatio(List<String> lines) {

        total = Utils.countWords(lines);
        summary = Utils.countWords(finalSummary);
        ratio = (double) summary / (double) total;
    }

    /**
     * Assign Abstract values to variables
     */
    private static void AssignAbstractiveValues() {

        no = AbstractSummary.no;
        date_desc = AbstractSummary.date_desc;
        date = AbstractSummary.date;
        part = Utils.formatSection(ConvertToSinhala.formatValues(AbstractSummary.part));
        about = AbstractSummary.about;
        act = AbstractSummary.act;
        who = Utils.formatValues(AbstractSummary.who);
        where = Utils.formatValues(AbstractSummary.where);
        title = AbstractSummary.title;
    }

    /**
     * Generate extractive summary
     *
     * @param lines
     * @return
     */
    private static String generateExtractSummary(List<String> lines) {

        lines = ExtractSummary.filerNotice(lines);
        String firstSummary = Utils.joinLines(lines);

        lines = ExtractSummary.removeRepeats(lines);
        String secondSummary = Utils.joinLines(lines);

        lines = ExtractSummary.findEndSentence(lines);
        String thirdSummary = Utils.joinLines(lines);

        return ExtractSummary.finalSummary(Utils.joinLines(lines));
    }

    /**
     * Write the output to
     *
     * @param lines
     */
    private static void WriteToFile(List<String> lines) {

        String line = gazette + ";" + no + ";" + date_desc + ";" + date + ";" + about + ";" +
                part + ";" + act + ";" + who + ";" + where + ";" + title + ";" + total + ";" + summary + ";"
                + ratio + ";" + KeyWords.gazetteKeywords + ";" + lines;

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        PrintWriter printWriter = null;

        try {
            fileWriter = new FileWriter("results.txt", true);
            bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);

            printWriter.println(line);
            System.out.println("Data Successfully appended into file");
            printWriter.flush();

        } catch (IOException e) {
            System.out.println("File write operation failed");
            e.printStackTrace();
        } finally {
            try {
                printWriter.close();
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException io) {
                System.out.println("Closing throws an exception");
            }

        }
    }

}
