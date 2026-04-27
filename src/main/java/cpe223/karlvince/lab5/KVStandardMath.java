package cpe223.karlvince.lab5;

import javafx.scene.control.TextField;

// [Version 0.0.1]
public class KVStandardMath {

    public static double add(TextField... inputs) {
        double sum = 0;

        try {
            for (TextField in : inputs) {
                sum += Double.parseDouble(in.getText());
            }
            return sum;

        } catch (NumberFormatException err) {
            return Double.NaN;
        }
    }

    public static double minus(TextField... inputs) {
        try {
            double result = Double.parseDouble(inputs[0].getText());
            for (int i = 1; i < inputs.length; i++) {
                result -= Double.parseDouble(inputs[i].getText());
            }
            return result;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException err) {
            return Double.NaN;
        }
    }

    public static double multiply(TextField... inputs) {
        try {
            double result = 1;
            for (TextField in : inputs) {
                result *= Double.parseDouble(in.getText());
            }
            return result;
        } catch (NumberFormatException err) {
            return Double.NaN;
        }
    }

    public static double divide(TextField... inputs) {
        try {
            double result = Double.parseDouble(inputs[0].getText());
            for (int i = 1; i < inputs.length; i++) {
                double divisor = Double.parseDouble(inputs[i].getText());
                if (divisor == 0)
                    return Double.NaN;
                result /= divisor;
            }
            return result;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException err) {
            return Double.NaN;
        }
    }

}