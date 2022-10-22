package main.calculator;

public class NumberParser {
    public static boolean isNotDouble(String s) {
        try {
            Double.parseDouble(s);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    } public static boolean isNotFloat(String s) {
        try {
            Float.parseFloat(s);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
