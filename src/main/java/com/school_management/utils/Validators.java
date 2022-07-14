package com.school_management.utils;

import java.util.regex.Pattern;

public class Validators {
    public static Boolean isValidName(String value) {
        return Pattern.matches("[A-Z][a-z]+", value);
    }

    public static Boolean isValidEmail(String value) {
        return Pattern.matches("\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,6}", value);
    }

    public static Boolean isValidPassword(String value) {
        return Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,15}$", value);
    }

    public static Boolean isValidNumber(String num) {
        return num.matches("^[0-1][7-9][0-1][0-9]{8}$");
    }

}