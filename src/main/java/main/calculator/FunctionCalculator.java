package main.calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionCalculator {
    public static String calculateFunction(String y) {
        List<String> f = new ArrayList<>(Arrays.asList("sinh\\(", "sinh\\(-", "cosh\\(", "cosh\\(-", "tanh\\(", "tanh\\(-", "asin\\(", "asin\\(-", "acos\\(", "acos\\(-", "atan\\(", "atan\\(-", "sin\\(", "sin\\(-", "cos\\(", "cos\\(-", "tan\\(", "tan\\(-", "rais\\(", "rais\\(-", "log\\(", "log\\(-", "abs\\(", "abs\\(-", "\\|", "\\|-", "√\\(", "√\\(-"));
        StringBuilder[] r = new StringBuilder[f.size()];
        for (int u = 0; u < r.length; u++) r[u] = new StringBuilder();
        int i = -1;
        for (String s : f) {
            i++; Pattern p; Matcher m; String d;
            if (s.contains("|")) p = Pattern.compile("(" + s + "\\b(.*?)\\b\\|)");
            else p = Pattern.compile("(" + s + "\\b(.*?)\\b\\))");
            if (i <= 0) m = p.matcher(y);
            else m = p.matcher(r[i-1].toString());
            while (m.find()) {
                if (NumberParser.isNotDouble(m.group(2))) {
                    StringCalculator c;
                    c = new StringCalculator();
                    d = ExpressionParser.splitOperator(calculateFunction(m.group(2)));
                    d = Double.toString(c.makeResult(d));
                } else d = m.group(2);
                final double l = Double.parseDouble(d); final double w = l * (-1);
                switch (s) {
                    case "√\\(-" -> m.appendReplacement(r[i], d + "i");
                    case "√\\(" -> m.appendReplacement(r[i], String.valueOf(Math.sqrt(l)));
                    case "log\\(-" -> m.appendReplacement(r[i], d + "iπ");
                    case "log\\(" -> m.appendReplacement(r[i], String.valueOf(Math.log(l)));
                    case "sinh\\(" -> m.appendReplacement(r[i], String.valueOf(Math.sinh(l)));
                    case "sinh\\(-" -> m.appendReplacement(r[i], String.valueOf(Math.sinh(w)));
                    case "cosh\\(" -> m.appendReplacement(r[i], String.valueOf(Math.cosh(l)));
                    case "cosh\\(-" -> m.appendReplacement(r[i], String.valueOf(Math.cosh(w)));
                    case "tanh\\(" -> m.appendReplacement(r[i], String.valueOf(Math.tanh(l)));
                    case "tanh\\(-" -> m.appendReplacement(r[i], String.valueOf(Math.tanh(w)));
                    case "asin\\(" -> {
                        if (l <= 1 && l >= -1) m.appendReplacement(r[i], String.valueOf(Math.asin(l)));
                        else m.appendReplacement(r[i], "NaN");
                    } case "asin\\(-" -> {
                        if (w <= 1 && w >= -1) m.appendReplacement(r[i], String.valueOf(Math.asin(w)));
                        else m.appendReplacement(r[i], "NaN");
                    } case "acos\\(" -> {
                        if (l <= 1 && l >= -1) m.appendReplacement(r[i], String.valueOf(Math.acos(l)));
                        else m.appendReplacement(r[i], "NaN");
                    } case "acos\\(-" -> {
                        if (w <= 1 && w >= -1) m.appendReplacement(r[i], String.valueOf(Math.acos(w)));
                        else m.appendReplacement(r[i], "NaN");
                    } case "atan\\(" -> m.appendReplacement(r[i], String.valueOf(Math.atan(l)));
                    case "atan\\(-" -> m.appendReplacement(r[i], String.valueOf(Math.atan(w)));
                    case "sin\\(" -> m.appendReplacement(r[i], String.valueOf(Math.sin(l)));
                    case "sin\\(-" -> m.appendReplacement(r[i], String.valueOf(Math.sin(w)));
                    case "cos\\(" -> m.appendReplacement(r[i], String.valueOf(Math.cos(l)));
                    case "cos\\(-" -> m.appendReplacement(r[i], String.valueOf(Math.cos(w)));
                    case "tan\\(" -> m.appendReplacement(r[i], String.valueOf(Math.tan(l)));
                    case "tan\\(-" -> m.appendReplacement(r[i], String.valueOf(Math.tan(w)));
                    case "rais\\(" -> m.appendReplacement(r[i], String.valueOf(Math.exp(l)));
                    case "rais\\(-" -> m.appendReplacement(r[i], String.valueOf(Math.exp(w)));
                    case "abs\\(", "\\|" -> m.appendReplacement(r[i], String.valueOf(Math.abs(l)));
                    case "abs\\(-", "\\|-" -> m.appendReplacement(r[i], String.valueOf(Math.abs(w)));
                }
            } m.appendTail(r[i]);
        } return r[i].toString();
    }
}
