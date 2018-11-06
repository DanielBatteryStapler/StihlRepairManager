package edu.bsu.cs222.finalproject.backend;

public class PhoneNumber {
    private PhoneNumber() {

    }

    public static Boolean isValid(String phoneNumber) {
        char[] phoneCheck = phoneNumber.toCharArray();

        for (char iterator : phoneCheck){
            if (("" + iterator).matches("\\D")) {
                return false;
            }
        }
        return true;
    }

    public static String toNormalized(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("\\D", "");
        return phoneNumber;
    }

    public static String toFormatted(String phoneNumber) {
        if (isValid(phoneNumber)) {
            StringBuilder mutableNumber = new StringBuilder(phoneNumber);
            mutableNumber.insert(3,'-');
            mutableNumber.insert(7,'-');
            return (mutableNumber.toString());
        }
       else {
           return phoneNumber;
        }
    }
}
