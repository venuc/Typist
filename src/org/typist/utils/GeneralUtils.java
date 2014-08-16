package org.typist.utils;

import org.typist.beans.*;

public class GeneralUtils {
    public static String getLastWord(String s) {
        String trimmedString = s.trim();
        int lastSpaceAt = trimmedString.lastIndexOf(" ");
        if (lastSpaceAt == -1) {
            return trimmedString;
        } else {
            return trimmedString.substring(lastSpaceAt);
        }
    }

    public static int getLastWordOffset(String s) {
        String trimmedString = s.trim();
        int lastSpaceAt = trimmedString.lastIndexOf(" ");
        if (lastSpaceAt == -1) {
            return 0;
        } else {
            return lastSpaceAt;
        }
    }

    public static int getLastWordPos(String s) {
        return s.trim().split(" ").length - 1;
    }

    public static void setStats(String[] sst, String[] sdt) {
        StatsBean.setStats(sst, sdt);
    }

    public static String getStats() {
        return StatsBean.getStats();
    }
}

