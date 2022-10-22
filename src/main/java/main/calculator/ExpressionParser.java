package main.calculator;

public class ExpressionParser {
    public static String splitOperator(String s) {
        return s.replace("(", " ( ")
        .replace(")", " ) ")
        .replace("+", " + ")
        .replace("/", " / ")
        .replace("*", " * ")
        .replace("^", " ^ ")
        .replace("  ", " ");
    } public static String highlightFunction(String s) {
        return s.replace("x", "§dx§b")
        .replace("e", "§5§oe§b")
        .replace("pi", "§5§oπ§b")
        .replace("π", "§5§oπ§b")
        .replace("sinh", "§2si§2nh§b")
        .replace("cosh", "§2co§2sh§b")
        .replace("tanh", "§2ta§2nh§b")
        .replace("atan", "§aata§an§b")
        .replace("sin", "§esin§b")
        .replace("cos", "§ecos§b")
        .replace("tan", "§etan§b")
        .replace("§5§oe§b§dx§bp", "§3exp§b")
        .replace("log", "§6log§b")
        .replace("abs", "§4abs§b")
        .replace("|", "§4|§b")
        .replace("√", "§c§o√§b")
        .replace("root", "§c§o√§b")
        .replace("sqrt", "§c§o√§b");
    }
}
