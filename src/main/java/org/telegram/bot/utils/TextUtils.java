package org.telegram.bot.utils;

import org.telegram.bot.domain.entities.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    /**
     * Gets a potential command from text.
     *
     * @param text - text to be processed.
     * @return potential command without rest text.
     */
    public static String getPotentialCommandInText(String text) {
        if (text.charAt(0) == '/') {
            text = text.substring(1);
        }
        Pattern pattern = Pattern.compile("^[a-zA-Zа-яА-Я0-9Ёё]+", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String buf = matcher.group(0).trim();
            pattern = Pattern.compile("\\W$", Pattern.UNICODE_CHARACTER_CLASS);
            matcher = pattern.matcher(buf);
            if (matcher.find()) {
                return buf.substring(0, buf.length() - 1).toLowerCase();
            }
            return buf.toLowerCase();
        }

        return null;
    }

    public static String cutMarkdownSymbolsInText(String text) {
        return text.replaceAll("[*_`\\[\\]()]", "").replaceAll("<.*?>","");
    }

    public static String reduceSpaces(String text) {
        while (text.contains("  ")) {
            text = text.replaceAll(" +", " ");
        }
        while (text.contains("\n\n")) {
            text = text.replaceAll("\n\n", "\n");
        }

        return text.trim();
    }

    public static String cutHtmlTags(String text) {
        return text.replaceAll("<.*?>","");
    }

    public static String withCapital(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static String removeCapital(String text) {
        return text.substring(0, 1).toLowerCase() + text.substring(1);
    }

    public static Boolean startsWithElementInList(String text, List<String> symbolsList) {
        return symbolsList.stream().anyMatch(text::startsWith);
    }

    public static Boolean equalsWithElementInList(String text, List<String> symbolsList) {
        return symbolsList.stream().anyMatch(text::equals);
    }

    public static Boolean isTextLengthIncludedInLimit(String text) {
        return text.length() < 4096;
    }

    public static String deleteWordsInText(String wordStartsWith, String text) {
        int i = text.indexOf(wordStartsWith);
        while (i >= 0) {
            String word = text.substring(i);
            int endOfWord = word.indexOf(" ");
            if (endOfWord < 0) {
                endOfWord = word.length();
            }
            word = word.substring(0, endOfWord);
            text = text.replace(word, "");
            i = text.indexOf(wordStartsWith);
        }

        return text;
    }

    public static String getLinkToUser(User user, Boolean htmlMode) {
        if (htmlMode) {
            return "<a href=\"tg://user?id=" + user.getUserId() + "\">" + user.getUsername() + "</a>";
        }
        return "[" + user.getUsername() + "](tg://user?id=" + user.getUserId() + ")";
    }
}
