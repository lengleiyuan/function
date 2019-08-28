package com.laniakea.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wb-lgc489196
 * @version RegexKit.java, v 0.1 2019年08月28日 17:57 wb-lgc489196 Exp
 */
public class RegexKit {

    static Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");

    public static boolean isNumeric(String str) {
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean iscontainsFunc(String funcStr) {
        if (!funcStr.contains("fun")) {
            return false;
        } else {
            return true;
        }
    }
}
