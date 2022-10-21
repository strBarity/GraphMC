package main.calculator;

public class StringCalculator {

    public boolean isBlank(String input) {
            return input.equals(" ");
        }

        public double makeResult(String input) {
            if (isBlank(input))
                throw new RuntimeException("Input was blank");
            return calculateSplitedString(splitBlank(input));
        }

        public String[] splitBlank(String str) {
            return str.split(" ");
        }

        public double calculateSplitedString(String[] str) {
            double result = Double.parseDouble(str[0]);
            for (double i = 0; i < str.length - 2; i += 2) {
                result = calculate(result, str[(int) (i + 1)].charAt(0), Double.parseDouble(str[(int) (i + 2)]));
            }
            return result;
        }

        public double calculate(double firstValue, char operator, double secondValue) {
            if (operator == '+')
                return add(firstValue, secondValue);
            if (operator == '-')
                return subtract(firstValue, secondValue);
            if (operator == '*')
                return multiply(firstValue, secondValue);
            if (operator == '/')
                return divide(firstValue, secondValue);
            if (operator == '^')
                return Math.pow(firstValue, secondValue);
            throw new RuntimeException("Unexpected Operator");
        }

        public double add(double i, double j) {
            return i + j;
        }

        public double subtract(double i, double j) {
            return i - j;
        }

        public double multiply(double i, double j) {
            return i * j;
        }

        public double divide(double i, double j) {
            if (j == 0) {
                throw new RuntimeException("Cannot divide by zero");
            }
            return i / j;
    }
}
