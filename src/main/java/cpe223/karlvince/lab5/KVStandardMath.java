package cpe223.karlvince.lab5;

// [Version 0.0.1]
public class KVStandardMath {

    public static double add(double... values) {
        double sum = 0;
        for (double val : values) {
            sum += val;
        }
        return sum;
    }

    public static double minus(double... values) {
        if (values.length == 0)
            return Double.NaN;
        double result = values[0];
        for (int i = 1; i < values.length; i++) {
            result -= values[i];
        }
        return result;
    }

    public static double multiply(double... values) {
        double result = 1;
        for (double val : values) {
            result *= val;
        }
        return result;
    }

    public static double divide(double... values) {
        if (values.length == 0)
            return Double.NaN;
        double result = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] == 0)
                return Double.NaN;
            result /= values[i];
        }
        return result;
    }

    public static double negate(double value) {
        return -value;
    }

    public static double percentage(double value) {
        return value / 100.0;
    }

}
