package edu.bsu.cs222.finalproject.backend;

public class PhoneNumber {
    private PhoneNumber() {

    }

    public static Boolean isValid(String phoneNumber) {
        char[] phoneCheck = phoneNumber.toCharArray();

        int numOfNums = 0;

        for (char iterator : phoneCheck){
            if (("" + iterator).matches("\\D")) {
                if(iterator != '-' && iterator != '(' && iterator != ')') {//if it is not one of the special allowed characters
                    return false;
                }
            }
            else{
                numOfNums++;
            }
        }
        return numOfNums == 10;
    }

    public static String toNormalized(String phoneNumber) throws IllegalArgumentException{
        if(isValid(phoneNumber)){
            phoneNumber = phoneNumber.replaceAll("\\D", "");
            return phoneNumber;
        }
        else{
            throw new IllegalArgumentException("Attempted to normalize an invalid phone number");
        }
    }

    public static String toFormatted(String phoneNumber) throws IllegalArgumentException{
        if (isValid(phoneNumber)) {
            StringBuilder mutableNumber = new StringBuilder(phoneNumber);
            mutableNumber.insert(3,'-');
            mutableNumber.insert(7,'-');
            return (mutableNumber.toString());
        }
       else {
            throw new IllegalArgumentException("Attempted to format an invalid phone number");
        }
    }
}
