package models;

import java.util.HashMap;

public class Entertainment {
    public String id;
    public String name;
    public int price;
    public int rate;
    public String address;
    public HashMap<String, Double> keyWords;

    public String getKeywordList() {
        StringBuilder sb = new StringBuilder();
        for (String keyWord : keyWords.keySet()) {
            sb.append(keyWord + "&nbsp;&nbsp;");
        }
        return sb.toString();
    }
}
