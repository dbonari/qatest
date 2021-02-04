package com.dbonari.qatest.rest.utils;

import com.github.dbonari.qatest.PropertiesReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.dbonari.qatest.PropertiesReader.Property.REGEX_EMAIL;

public class ValidationUtils {

    private static PropertiesReader propertiesReader = new PropertiesReader();

    public static boolean isValidEmail(String email) {
        Pattern emailRegex = Pattern.compile(propertiesReader.getProperty(REGEX_EMAIL));
        Matcher matcher = emailRegex.matcher(email);
        return matcher.matches();
    }
}
