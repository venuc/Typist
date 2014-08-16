package org.typist.beans;

public class StatsBean {
    private static String[] splittedSourceText;
    private static String[] splittedDestText;
    static long startTime = 0;
    static float timeTaken = 0.0F;
    static StringBuilder wrongWords = new StringBuilder();
    static Float wpm = 0.0F;
    static Float accuracy = 0.0F;

    public static void setStartTime() {
        startTime = System.currentTimeMillis();
    }

    public static void setStats(String[] sst, String[] sdt) {
        // Build time taken
        timeTaken = (System.currentTimeMillis() - startTime) / 1000.0F;
        splittedSourceText = sst;
        splittedDestText = sdt;
        Float wrongWordsCount = 0.0F;

        // Build wrongWords
        for (int i = 0; i < splittedSourceText.length; i++) {
            if (! splittedSourceText[i].equals(splittedDestText[i])) {
                wrongWords.append(" " + splittedSourceText[i]);
                wrongWordsCount++;
            }
        }

        // Build wpm
        wpm = (splittedDestText.length / timeTaken) * 60;

        // Build accuracy
        accuracy = ((splittedSourceText.length - wrongWordsCount)
                     / splittedSourceText.length) * 100;
    }

    public static String getStats() {
        return "\n\n================================================"
             + "\n-- -- Results: -- --"
             + "\nTotal time taken: " + timeTaken + " secs."
             + "\nWords per minute: " + wpm
             + "\nAccuracy: " + accuracy +"%"
             + "\nThe red words should have been: " + wrongWords.toString();
    }
}

