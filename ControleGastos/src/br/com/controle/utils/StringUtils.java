/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controle.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author igor.santos
 */
public class StringUtils {

    public static String[] REPLACES = {"a", "e", "i", "o", "u", "c"};
    public static Pattern[] PATTERNS = null;

    public static void compilePatterns() {
        PATTERNS = new Pattern[REPLACES.length];
        PATTERNS[0] = Pattern.compile("[âãáàä]", Pattern.CASE_INSENSITIVE);
        PATTERNS[1] = Pattern.compile("[éèêë]", Pattern.CASE_INSENSITIVE);
        PATTERNS[2] = Pattern.compile("[íìîï]", Pattern.CASE_INSENSITIVE);
        PATTERNS[3] = Pattern.compile("[óòôõö]", Pattern.CASE_INSENSITIVE);
        PATTERNS[4] = Pattern.compile("[úúûü]", Pattern.CASE_INSENSITIVE);
        PATTERNS[5] = Pattern.compile("[ç]", Pattern.CASE_INSENSITIVE);
    }

    public static String replaceAcentos(String text) {
        if (PATTERNS == null) {
            compilePatterns();
        }

        String result = text;
        for (int i = 0; i < PATTERNS.length; i++) {
            Matcher matcher = PATTERNS[i].matcher(result);
            result = matcher.replaceAll(REPLACES[i]);
        }
        return result;
    }

    public static String capitalize(String text) {
        if (!text.isEmpty()) {
            String[] aux = text.toLowerCase().split(" ");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < aux.length; i++) {
                builder.append(Character.toTitleCase(aux[i].charAt(0))).append(aux[i].substring(1)).append(" ");
            }
            return builder.toString().trim();
        } else {
            return "";
        }
    }

    public static String formatDate(int dia, int mes, int ano) {
        return dia + "/" + pad(mes) + "/" + ano;
    }
    
    private static String pad(int number) {
        if (number >= 10) {
            return String.valueOf(number);
        } else {
            return "0" + String.valueOf(number);
        }
    }
}
