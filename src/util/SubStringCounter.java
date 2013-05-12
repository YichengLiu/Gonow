package util;

public class SubStringCounter {
    public static int count(String str, String subStr) {
        int lastIndex = 0;
        int count = 0;
        while ((lastIndex = str.indexOf(subStr, lastIndex)) != -1) {
            count++;
            lastIndex += subStr.length() - 1;
        }
        return count;
    }
}
