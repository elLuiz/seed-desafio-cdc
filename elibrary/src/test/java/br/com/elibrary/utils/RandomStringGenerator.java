package br.com.elibrary.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomStringGenerator {
    public static String fill(int range, String baseText) {
        StringBuilder stringBuilder = new StringBuilder(baseText);
        stringBuilder.append(baseText.repeat(Math.max(0, range)));
        return stringBuilder.toString();
    }
}
