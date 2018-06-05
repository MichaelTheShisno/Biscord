package com.mmunoz.util;

public class PortNumber {
    public static boolean isValid(String str) {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }// end: isValid

}// end: class PortNumber
