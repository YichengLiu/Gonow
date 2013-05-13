package timing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import timing.ComputeKeyWord.WeightedWord;

import db.DBInterface;

public class RealtimeInfoManager {
    private static DBInterface db = new DBInterface();
    private static ComputeKeyWord ckw = new ComputeKeyWord();

    public static void update(String target) {
        try {
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery("select text, segment from weibo where target='" + target + "';");
            ArrayList<String> texts = new  ArrayList<String>();
            ArrayList<String> segs = new ArrayList<String>();
            while(rs.next()) {
                String text = rs.getString(1);
                String seg = rs.getString(2);
                texts.add(text);
                segs.add(seg);
            }
            List<WeightedWord> keyWords = ckw.compute(texts, segs);
            for (WeightedWord ww : keyWords) {
                System.out.println(ww.word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        update("三里屯酒吧街");
    }
}
