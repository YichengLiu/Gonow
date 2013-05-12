package timing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.recognition.NatureRecognition;
import org.apache.commons.collections.bag.TreeBag;
import org.apache.commons.io.FileUtils;

public class OpinionMiner {
    TreeBag wordBag = new TreeBag();
    ArrayList<String> wordList = new ArrayList<String>();
    Pattern personPattern = Pattern.compile("(.*人(很|好|太|相当|特别|超级|比较)*多.*)|(.*人山人海.*)");
    Pattern personFewPattern = Pattern.compile("(.*人(不|不算)(很|好|太|相当|特别|超级|比较)*多.*)|(.*人(很|好|太|相当|特别|超级|比较)*少.*)");

    public void countWords() {
        File weiboList= new File("/home/gods/Downloads/玉渊潭公园.txt");
        try {
            List<String> lines = FileUtils.readLines(weiboList);
            for (String line : lines) {
                if (personPattern.matcher(line).matches()) {
                    System.out.println(line);
                }
                List<Term> terms = ToAnalysis.paser(line);
                new NatureRecognition(terms).recognition() ;
                for (Term term : terms) {
                    //System.out.print(term.getName()+"/"+term.getNatrue().natureStr + "  ");
                    if(wordBag.add(term.getName())) {
                        wordList.add(term.getName());
                    }
                }
                //System.out.println();
            }
            Collections.sort(wordList, new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    return Integer.compare(wordBag.getCount(o1), wordBag.getCount(o2));
                }

            });
            for (Object term : wordList) {
                //System.out.println((String)term + " " + wordBag.getCount(term));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        (new OpinionMiner()).countWords();
    }
}
