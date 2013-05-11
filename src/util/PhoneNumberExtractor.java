package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberExtractor {
    private static Pattern phonePattern = Pattern.compile(".*\\s+(\\d{8,11})$");

    public static String extract(String rawAddress) {
        Matcher m = phonePattern.matcher(rawAddress);
        if (m.matches()) {
            return m.group(1);
        }
        return null;
    }
}
