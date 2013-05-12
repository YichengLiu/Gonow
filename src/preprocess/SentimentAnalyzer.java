package preprocess;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ansj.domain.Term;
import org.apache.commons.io.FileUtils;

import util.Config;
import util.SubStringCounter;

public class SentimentAnalyzer {
    private List<String> wordPos;
    private List<String> wordNeg;
    private List<String> emoticonPos;
    private List<String> emoticonNeg;

    public SentimentAnalyzer() {
        String dataDir = Config.getProperties().getProperty("DATA_DIR");
        try {
            wordPos = FileUtils.readLines(new File(dataDir + "/word.positive"));
            wordNeg = FileUtils.readLines(new File(dataDir + "/word.negative"));
            emoticonPos = FileUtils.readLines(new File(dataDir + "/emoticon.positive"));
            emoticonNeg = FileUtils.readLines(new File(dataDir + "/emoticon.negative"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int analyze(String text, Object terms) {
        int result = 0;

        //count for emoticons
        for (String em : emoticonPos) {
            result += SubStringCounter.count(text, "[" + em + "]");
        }
        for (String em : emoticonNeg) {
            result -= SubStringCounter.count(text, "[" + em + "]");
        }
        //count for sentiment words

        return result;
    }

    public static void main(String[] args) {
        new SentimentAnalyzer();
    }
}
