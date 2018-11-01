package edu.bsu.cs222.finalproject.database;

public class phoneNumberMutators

{
    private phoneNumberMutators()
    {

    }

    public static Boolean isPhoneNumberValid(String phoneNumber) {
        char[] phoneCheck = phoneNumber.toCharArray();

        for (char iterator : phoneCheck){
            if (("" + iterator).matches("\\D")) {
                return false;
            }
    }
    return true;
    }

    public static String makePhoneNumberAllNumbers(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("\\D", "");
        return phoneNumber;
    }

    public static String makePhoneNumberEasyToRead(String phoneNumber)
    {
        if (isPhoneNumberValid(phoneNumber)) {
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
