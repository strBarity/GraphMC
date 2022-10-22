package main.calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionCalculator {
    public static String calculateFunction(String source) {
        List<String> f = new ArrayList<>(Arrays.asList("sin\\(", "sin\\(-", "cos\\(", "cos\\(-", "tan\\(", "tan\\(-", "abs\\(", "abs\\(-", "\\|", "\\|-", "√\\(", "√\\(-"));
        StringBuilder[] r = new StringBuilder[f.size()];
        for (int u = 0; u < r.length; u++) r[u] = new StringBuilder();
        int i = -1;
        for (String s : f) {
            i++; Pattern p; Matcher m; String d;
            if (s.contains("|")) p = Pattern.compile("(" + s + "\\b(.*?)\\b\\|)");
            else p = Pattern.compile("(" + s + "\\b(.*?)\\b\\))");
            if (i <= 0) m = p.matcher(source);
            else m = p.matcher(r[i-1].toString());
            while (m.find()) {
                if (DoubleParser.isNotDouble(m.group(2))) {
                    StringCalculator calculator;
                    calculator = new StringCalculator();
                    d = calculateFunction(m.group(2));
                    d = d.replace("(", " ( ");
                    d = d.replace(")", " ) ");
                    d = d.replace("+", " + ");
                    d = d.replace("/", " / ");
                    d = d.replace("*", " * ");
                    d = d.replace("^", " ^ ");
                    d = d.replace("  ", " ");
                    d = Double.toString(calculator.makeResult(d));
                } else d = m.group(2);
                if (s.equals("sin\\(")) m.appendReplacement(r[i], String.valueOf(Math.sin(Double.parseDouble(d))));
                if (s.equals("sin\\(-")) m.appendReplacement(r[i], String.valueOf(Math.sin(Double.parseDouble(d)*(-1))));
                if (s.equals("cos\\(")) m.appendReplacement(r[i], String.valueOf(Math.cos(Double.parseDouble(d))));
                if (s.equals("cos\\(-")) m.appendReplacement(r[i], String.valueOf(Math.cos(Double.parseDouble(d)*(-1))));
                if (s.equals("tan\\(")) m.appendReplacement(r[i], String.valueOf(Math.tan(Double.parseDouble(d))));
                if (s.equals("tan\\(-")) m.appendReplacement(r[i], String.valueOf(Math.tan(Double.parseDouble(d)*(-1))));
                if (s.equals("abs\\(") || s.equals("\\|")) m.appendReplacement(r[i], String.valueOf(Math.abs(Double.parseDouble(d))));
                if (s.equals("abs\\(-") || s.equals("\\|-")) m.appendReplacement(r[i], String.valueOf(Math.abs(Double.parseDouble(d)*(-1))));
                if (s.equals("√\\(")) m.appendReplacement(r[i], String.valueOf(Math.sqrt(Double.parseDouble(d))));
                if (s.equals("√\\(-")) m.appendReplacement(r[i], String.valueOf(-2147483648));
            } m.appendTail(r[i]);
        } return r[i].toString();
    }
}
