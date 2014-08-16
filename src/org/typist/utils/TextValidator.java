package org.typist.utils;

import java.util.*;

import org.typist.beans.*;

public class TextValidator {
    
    private static SourceTextBean stb;
    private static String sourceText;
    private static String[] splittedSourceText;

    public void setSourceTextBean(SourceTextBean stb) {
        // This is set just after SourceTextFetcher fetches text (at load
        // time).
        this.stb = stb;
        this.sourceText = stb.getText();
        splitSourceText();
    }

    private void splitSourceText() {
        // This builds a String array from stb. Strings can be retreived
        // from this array by index.
        splittedSourceText = sourceText.split(" ");
    }

    public static boolean isValidText(String text, int pos) {
        // To validate a word, we match the word with the one retreived from
        // the String array for the same index / offset.
        return text.equals(splittedSourceText[pos]);
    }

    public static boolean userIsDone(String docText) {
        // This checks for the word count ONLY in source and dest text.
        String[] splittedDestText = docText.trim().split(" ");
        if (splittedDestText.length == splittedSourceText.length) {
            // set StatsBean
            GeneralUtils.setStats(splittedSourceText, splittedDestText);
            return true;
        }
        return false;
    }
}
