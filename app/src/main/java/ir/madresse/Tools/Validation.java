package ir.madresse.Tools;

import java.util.regex.Pattern;

public class Validation {

    public static boolean ValidPhoneNumber(String PhoneNumber) {
//        String PN = PhoneNumber;
//        switch (PN.charAt(0)) {
//            case '+':
//                PN = PN.substring(1);
//                break;
//            case '0':
//                PN = PN.substring(1);
//                PN = "98" + PN;
//                break;
//            default:
//                if (!PN.matches("^98.*")) PN = "98" + PN;
//                break;
//        }
//
//        if (PN.length() > 7 && PN.length() < 15) return true;
//        return false;

        return Pattern.compile("(0|\\+98)?([ ]|-|[()]){0,2}9[1|2|3|4]([ ]|-|[()]){0,2}(?:[0-9]([ ]|-|[()]){0,2}){8}").matcher(PhoneNumber).matches();


    }




    }
