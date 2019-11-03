package com.teamwd.chipin.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static boolean validEmail(String email){
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
