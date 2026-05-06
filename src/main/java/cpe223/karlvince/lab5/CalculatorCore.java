package cpe223.karlvince.lab5;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextField;

public class CalculatorCore {

    public static String formatNumber(double value) {
        DecimalFormat adjustDecimalFormat = new DecimalFormat("0.##############");
        return adjustDecimalFormat.format(value);
    }

    public static String handleNegate(String current) {
        Pattern pattern = Pattern.compile("^(.*?)(-?(?:\\d+\\.\\d*|\\.\\d+|\\d+))([+−×÷]?)$");
        Matcher matcher = pattern.matcher(current);

        if (matcher.matches()) {
            String prefix = matcher.group(1);
            String numberStr = matcher.group(2);
            String suffix = matcher.group(3);

            double number = Double.parseDouble(numberStr);
            double negated = KVStandardMath.negate(number);

            return prefix + formatNumber(negated) + suffix;
        }
        return current;
    }

    public static String handlePercentage(String current) {
        Pattern pattern = Pattern.compile("^(.*?)(-?(?:\\d+\\.\\d*|\\.\\d+|\\d+))([+−×÷]?)$");
        Matcher matcher = pattern.matcher(current);

        if (matcher.matches()) {
            String prefix = matcher.group(1);
            String numberStr = matcher.group(2);
            String suffix = matcher.group(3);

            double number = Double.parseDouble(numberStr);
            double percent = KVStandardMath.percentage(number);

            return prefix + formatNumber(percent) + suffix;
        }
        return current;
    }

    public static String parseOperands(TextField textField) {

        String current = textField.getText().toString();
        String resultString;
        Double result = 0.0;

        if (!current.matches(".*[+÷−×].*")) {
            System.out.println("[Calculator Core] Expected exception has occured: Incomplete Operands");
            return current;
        }

        try {
            if (current.contains("+")) {
                String[] oprrnd = current.split("\\+");
                result = KVStandardMath.add(Double.parseDouble(oprrnd[0]), Double.parseDouble(oprrnd[1]));
            } else if (current.contains("−")) {
                String[] oprrnd = current.split("\\−");
                result = KVStandardMath.minus(Double.parseDouble(oprrnd[0]), Double.parseDouble(oprrnd[1]));
            } else if (current.contains("×")) {
                String[] oprrnd = current.split("\\×");
                result = KVStandardMath.multiply(Double.parseDouble(oprrnd[0]), Double.parseDouble(oprrnd[1]));
            } else {
                String[] oprrnd = current.split("\\÷");
                result = KVStandardMath.divide(Double.parseDouble(oprrnd[0]), Double.parseDouble(oprrnd[1]));
            }
        } catch (Exception e) {
            System.out.println("[Calculator Core] Unexpected exception has occured: " + e);
            return current;
        }

        resultString = formatNumber(result);

        return resultString;
    }
}
