package models;

import java.io.Serializable;

public class Weibo implements Serializable {
    private static final long serialVersionUID = 8279146170460292521L;

    public String id;
    public String time;
    public String target;
    public String text;
    public int emotion;
}
