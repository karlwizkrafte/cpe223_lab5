package cpe223.karlvince.lab5;

import java.text.DecimalFormat;

import javafx.scene.control.TextField;

public class CalculatorCore {
    
    public static String parseOperands(TextField textField) {

        String current = textField.getText().toString();
        String resultString;
        Double result = 0.0;

        if (!current.matches(".*[+−×].*")) {
        System.out.println("[Calculator Core] Expected exception has occured: Incomplete Operrands");
        return current; 
        }

        try {    if (current.contains("+")) {
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
            System.out.println("[Calculator Core] Expected exception has occured: " + e);
            return current;
        } 

        DecimalFormat adjustDecimalFormat = new DecimalFormat("0.##############");
        resultString = adjustDecimalFormat.format(result);

        return resultString;
    }
}
