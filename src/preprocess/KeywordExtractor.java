package preprocess;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.recognition.NatureRecognition;

public class KeywordExtractor {
    public static void main(String[] args) {
        String content = "玉渊潭公园，人不算特别多，但节日气氛相当浓！静观儿子快乐大滑梯，也祝各位朋友，中秋节快乐！长假快乐";
        List<Term> terms = ToAnalysis.paser(content);
        new NatureRecognition(terms).recognition() ;
        for (Term term : terms) {
            System.out.println(term.getName()+"\t"+term.getNatrue().natureStr);
        }
    }
}
