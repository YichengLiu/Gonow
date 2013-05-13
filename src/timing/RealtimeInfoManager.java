package timing;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import timing.ComputeKeyWord.WeightedWord;
import util.Config;

import db.DBInterface;

public class RealtimeInfoManager {
    private static DBInterface db = new DBInterface();
    private static ComputeKeyWord ckw = new ComputeKeyWord();

    private static HashMap<String, Integer> keywordMap = new HashMap<String, Integer>();

    public static void updateGlobalKeyword() {
        try {
            Statement st = db.getConnection().createStatement();
            StringBuilder globalKeywordBuilder = new StringBuilder();
            File outCSV = new File(Config.getProperties().getProperty("DATA_DIR") + "/global_keywords.csv");

            try {
                FileUtils.write(outCSV, "text, size\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String keyword : keywordMap.keySet()) {
                if (keywordMap.get(keyword) < 200) {
                    continue;
                }
                globalKeywordBuilder.append(keyword + "::=" + keywordMap.get(keyword) + "::;");
                try {
                    FileUtils.write(outCSV, keyword + "," + (int)(keywordMap.get(keyword) / 1000 + 20 ) + "\n", true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String sql = "INSERT INTO keyword (target, keyword) VALUES ('global', '" + globalKeywordBuilder.toString() + "') ON DUPLICATE KEY UPDATE keyword = '" + globalKeywordBuilder.toString() + "'";
            st.execute(sql);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTargetKeyword(String target) {
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
            st.close();

            String insql="INSERT INTO keyword (target, keyword) VALUES (?, ?) ON DUPLICATE KEY UPDATE keyword = ?";
            PreparedStatement ps = null;
            try {
                ps = db.getConnection().prepareStatement(insql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            List<WeightedWord> keyWords = ckw.compute(texts, segs);
            StringBuilder keywordBuilder = new StringBuilder();
            for (WeightedWord ww : keyWords) {
                keywordBuilder.append(ww.word + "::=" + ww.score + "::;");
                if (!keywordMap.containsKey(ww.word)) {
                    keywordMap.put(ww.word, 0);
                }
                keywordMap.put(ww.word, keywordMap.get(ww.word) + ww.score);
            }
            ps.setString(1, target);
            ps.setString(2, keywordBuilder.toString());
            ps.setString(3, keywordBuilder.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update() {
        keywordMap.clear();

        try {
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery("select distinct(target) from weibo");
            while(rs.next()) {
                System.out.println("UPDATING real-time information for " + rs.getString(1));
                updateTargetKeyword(rs.getString(1));
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateGlobalKeyword();
    }

    public static void main(String[] args) {
        update();
    }
}
