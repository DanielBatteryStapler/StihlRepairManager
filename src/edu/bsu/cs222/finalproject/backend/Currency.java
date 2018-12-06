package edu.bsu.cs222.finalproject.backend;

import java.text.NumberFormat;

public class Currency {
    public static boolean isValid(String dollars){
        dollars = dollars.replaceAll("\\$", "").replaceAll(" ", "");

        if(dollars.isEmpty()){
            return false;
        }

        char[] dollarCheck = dollars.toCharArray();

        boolean countingAfter = false;
        int afterDecimal = 0;

        for (char iterator : dollarCheck){
            if (("" + iterator).matches("\\D")) {
                if(iterator == '.') {
                    countingAfter = true;
                }
                else{
                    return false;
                }
            }
            else if(countingAfter) {
                afterDecimal++;
            }
        }

        return afterDecimal <= 2;
    }

    public static int toNormalized(String dollars) {
        if(!isValid(dollars)){
            throw new RuntimeException("Attempted to normalize an invalid dollar amount");
        }
        dollars = dollars.replaceAll("\\$", "").replaceAll(" ", "");
        return (int)(Double.parseDouble(dollars) * 100);//isValid makes sure it only goes to cents, so int casting is fine
    }

    public static String toFormatted(int cents){
        return NumberFormat.getCurrencyInstance().format(cents / 100.0);
    }
}
