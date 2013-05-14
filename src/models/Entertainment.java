package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Entertainment implements Serializable {
    private static final long serialVersionUID = 7939362248585478411L;

    public String id;
    public String name;
    public int price;
    public int rate;
    public String address;
    public ArrayList<Keyword> keyWords;

    class Keyword implements Comparable<Keyword> {
        String word;
        int score;

        Keyword(String word, int score) {
            this.word = word;
            this.score = score;
        }

        @Override
        public int compareTo(Keyword o) {
            return o.score - this.score;
        }
    }

    public void setKeyword(String keywordStr) {
        keyWords = new ArrayList<Keyword>();
        for (String keywordPair : keywordStr.split("::;")) {
            String[] sp = keywordPair.split("::=");
            keyWords.add(new Keyword(sp[0], Integer.valueOf(sp[1])));
        }
        Collections.sort(keyWords);
    }

    public String getKeywordList(int limit) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < limit; i++) {
            sb.append(keyWords.get(i).word + "&nbsp;&nbsp;");
        }
        return sb.toString();
    }

    public static Comparator<Entertainment> getComparator(String sort) {
        Comparator<Entertainment> c = null;

        try {
            int mode = Integer.valueOf(sort);

            if (mode == 1) {
                //sort by price, bottom up
                c = new Comparator<Entertainment>() {
                    @Override
                    public int compare(Entertainment o1, Entertainment o2) {
                        int p = o1.price - o2.price;
                        if (p != 0) {
                            return p;
                        } else {
                            return o2.rate - o1.rate;
                        }
                    }
                };
            } else if (mode == 2) {
                //sort by price, top down
                c = new Comparator<Entertainment>() {
                    @Override
                    public int compare(Entertainment o1, Entertainment o2) {
                        int p = o2.price - o1.price;
                        if (p != 0) {
                            return p;
                        } else {
                            return o2.rate - o1.rate;
                        }
                    }
                };
            } else if (mode == 3) {
                //sort by rate, top down
                c = new Comparator<Entertainment>() {
                    @Override
                    public int compare(Entertainment o1, Entertainment o2) {
                        int r = o2.rate - o1.rate;
                        if (r != 0) {
                            return r;
                        } else {
                            return o1.price - o2.price;
                        }
                    }
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }
}
