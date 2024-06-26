package com.example.tripster.util;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_PATTERN =
            "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
    private static final Pattern email_pattern = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern phone_pattern = Pattern.compile(PHONE_PATTERN);


    public static boolean isValidEmail(String email) {
        Matcher matcher = email_pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidPhone(String phone) {
        Matcher matcher = phone_pattern.matcher(phone);
        return matcher.matches();
    }
}
