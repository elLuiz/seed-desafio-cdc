package br.com.elibrary.util.string;

public class StringUtils {
    private StringUtils() {}

    public static boolean isNotEmpty(String input) {
        return !isEmpty(input);
    }

    public static boolean isEmpty(String input) {
        return input == null || input.isBlank();
    }

    public static boolean isGreaterThan(String input, int size) {
        return input != null && input.length() > size;
    }
}