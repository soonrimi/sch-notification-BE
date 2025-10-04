package com.schnofiticationbe.Utils;

import java.util.List;
import java.util.Locale;


public class KeywordMatcher {

    public static boolean containsAnyKeyword(String text, List<String> keywords) {
        if (text == null || keywords == null || keywords.isEmpty()) return false;
        String lower = text.toLowerCase(Locale.ROOT);
        for (String k : keywords) {
            if (k == null || k.isBlank()) continue;
            if (lower.contains(k.toLowerCase(Locale.ROOT))) return true;
        }
        return false;
    }

    public static boolean containsNoneKeyword(String text, List<String> keywords) {
        if (text == null || keywords == null || keywords.isEmpty()) return true;
        String lower = text.toLowerCase(Locale.ROOT);
        for (String k : keywords) {
            if (k == null || k.isBlank()) continue;
            if (lower.contains(k.toLowerCase(Locale.ROOT))) return false;
        }
        return true;
    }
}
