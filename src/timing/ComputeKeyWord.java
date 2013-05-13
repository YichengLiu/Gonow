package timing;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.thunlp.language.chinese.LangUtils;
import org.thunlp.misc.Counter;
import org.thunlp.misc.WeightString;
import org.thunlp.tagsuggest.common.Post;
import org.thunlp.tagsuggest.contentbase.SMTwithTfidfWeibo;

public class ComputeKeyWord {
    public class WeightedWord {
        String word;
        int score;

        public WeightedWord(String word, int score) {
            this.word = word;
            this.score = score;
        }
    }

    public static SMTwithTfidfWeibo smt;

    static {
        try {
            smt = new SMTwithTfidfWeibo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<WeightedWord> compute(List<String> ori_content, List<String> seg_content) {
        // for tfdf
        HashMap<String, HashMap<String, Integer>> taggingMap = new HashMap<String, HashMap<String, Integer>>();
        HashMap<String, Integer> tfMap = new HashMap<String, Integer>();
        HashMap<String, Integer> dfMap = new HashMap<String, Integer>();
        HashSet<String> wordSet = new HashSet<String>();

        // for smt
        HashMap<Integer, Double> smtTfidf = new HashMap<Integer, Double>();
        Counter<String> smtTf = new Counter<String>();
        HashMap<String, Integer> smtDf = new HashMap<String, Integer>();
        HashSet<String> smtWordSet = new HashSet<String>();
        HashSet<String> smtSenWordSet = new HashSet<String>();

        int smtLength = 0;
        Post p = new Post();
        for (int i = 0; i < ori_content.size(); i++) {
            String line = ori_content.get(i);
            p.setContent(line);
            String[] words = smt.extractor.extract(p);
            smtLength += words.length;

            for (String word : words) {
                if (smt.idMap.containsKey(word)) {
                    smtWordSet.add(word);
                    smtTf.inc(word, 1);
                    if(!smtSenWordSet.contains(word)) {
                        if(!smtDf.containsKey(word)) {
                            smtDf.put(word, 0);
                        }
                        smtDf.put(word, smtDf.get(word) + 1);
                        smtSenWordSet.add(word);
                    }
                }
            }
            smtSenWordSet.clear();
        }


        Iterator<Entry<String, Long>> iter = smtTf.iterator();
        //System.out.println(smtTf.size());
        HashMap<Integer, Double> proMap = new HashMap<Integer, Double>();
        double para = Double.parseDouble(smt.config.getProperty("harmonic_para","1.0"));
        while (iter.hasNext()) {
            Entry<String, Long> e = iter.next();
            // System.out.println(e.getKey() + ":" + e.getValue());
            String word = e.getKey();
            double tf = (double) e.getValue() / smtLength;
            // double idf = (double)D/(double)df.get(word);
            double idf = 0.0;
            if (smt.wordLex.getWord(word) != null) {
                idf = Math.log((double) smt.wordLex.getNumDocs() / (double) smt.wordLex.getWord(word).getDocumentFrequency());
            } else {
                idf = Math.log((double) smt.wordLex.getNumDocs() / 800.0);
            }
            double tfidf = tf * idf;
            int id = smt.idMap.get(word);
            if (smt.proTable.containsKey(id)) {
                smtTfidf.put(id, tfidf);

                // to suggest the tags
                for (Entry<Integer, Double> ee : smt.proTable.get(id).entrySet()) {
                    int tagId = ee.getKey();

                    if (smt.inverseTable.containsKey(id) && smt.inverseTable.get(id).containsKey(tagId)) {
                        double pro = 1.0 / (para / ee.getValue() + (1.0 - para)
                                / smt.inverseTable.get(id).get(tagId));
                        if (proMap.containsKey(tagId)) {
                            double tmp = proMap.get(tagId);
                            tmp += tfidf * pro;
                            proMap.remove(tagId);
                            proMap.put(tagId, tmp);
                        } else {
                            proMap.put(tagId, tfidf * pro);
                        }
                    }
                }
            }
        }

        //System.out.println(proMap.size());

        double totalScore = 0.0;
        for (Entry<Integer, Double> e : proMap.entrySet()) {
            totalScore += e.getValue();
        }
        //System.out.println(totalScore);
        double totalTfidf = 0.0;
        for (Entry<Integer, Double> e : smtTfidf.entrySet()) {
            totalTfidf += e.getValue();
        }
        //System.out.println(totalTfidf);

        double methodPara = Double.parseDouble(smt.config.getProperty("methodPara", "0.5"));
        // ranking
        List<WeightString> tags = new ArrayList<WeightString>();
        for (Entry<Integer, Double> e : proMap.entrySet()) {
            double score = methodPara * e.getValue() / totalScore;
            String tag = smt.bookTagMap.get(e.getKey());
            if (!smtWordSet.contains(tag)) {
                continue;
            }
            if (smt.idMap.containsKey(tag) && smtTfidf.containsKey(smt.idMap.get(tag))) {
                score += (1.0 - methodPara) * smtTfidf.get(smt.idMap.get(tag))
                    / totalTfidf;
            }
            tags.add(new WeightString(tag, score));
        }
        //System.out.println(tags.size());

        Collections.sort(tags, new Comparator<WeightString>() {
            @Override
            public int compare(WeightString o1, WeightString o2) {
                return Double.compare(o2.weight, o1.weight);
            }

        });

        HashMap<String, Double> smtScores = new HashMap<String, Double>();
        double maxSmtScore = 0.0;
        if(tags.size() != 0) {
            maxSmtScore = tags.get(0).weight;
        }

        for (WeightString s : tags) {
            if(s.text.matches("[0-9]+$")){
                continue;
            }
            smtScores.put(s.text, s.weight * 0.4 / maxSmtScore);
        }


        for (int k = 0; k < seg_content.size(); k++) {
            String line = seg_content.get(k);
            //System.out.println(line);
            String[] datas = line.split(" ");
            for (int i = 0; i < datas.length; i++) {
                int index = datas[i].lastIndexOf("/");
                if (index == -1) {
                    continue;
                }
                String word = datas[i].substring(0, index);
                String label = datas[i].substring(index + 1);
                if (word.length() < 2) {
                    continue;
                }
                if (smt.stopWords.contains(word)) {
                    continue;
                }
                if(word.matches("[0-9０-９]+$")) {
                    continue;
                }

                if(word.matches("[a-zA-ZＡ-Ｚａ-ｚ]+$")) {
                    word = LangUtils.mapFullWidthLetterToHalfWidth(word);
                    if(!smtScores.containsKey(word)){
                        continue;
                    }
                }

                if (!wordSet.contains(word)) {
                    if (!dfMap.containsKey(word)) {
                        dfMap.put(word, 0);
                    }
                    dfMap.put(word, dfMap.get(word) + 1);
                    wordSet.add(word);
                }
                if (!taggingMap.containsKey(word)) {
                    taggingMap.put(word, new HashMap<String, Integer>());
                }
                if (!taggingMap.get(word).containsKey(label)) {
                    taggingMap.get(word).put(label, 0);
                }
                taggingMap.get(word).put(label, taggingMap.get(word).get(label) + 1);

                if (!tfMap.containsKey(word)) {
                    tfMap.put(word, 0);
                }
                tfMap.put(word, tfMap.get(word) + 1);
            }
            wordSet.clear();
        }

        HashMap<String, Double> tfidfScores = new HashMap<String, Double>();
        //double totalTfidf = 0.0;
        double maxTfidf = 0.0;
        for(Entry<String, HashMap<String, Integer>> e : taggingMap.entrySet()){
            String word = e.getKey();

            if (smtScores.containsKey(word)) {
                double tf = tfMap.get(word);
                double df = dfMap.get(word);
                double score = tf * Math.log(df + 1.0) * ((double)word.length()) / Math.log(2.0);
                tfidfScores.put(word, score);
                if(maxTfidf < score) {
                    maxTfidf = score;
                }
                continue;
            }

            boolean isNoun = false;
            boolean isNpsz = false;
            boolean isI = false;
            boolean isKeep = false;
            int nounNum = 0;
            int allNum = 0;
            for(Entry<String, Integer> ee : e.getValue().entrySet()) {
                allNum += ee.getValue();
                if (smt.possibleSet.contains(ee.getKey())) {
                    isNpsz = true;
                    isKeep = true;
                } else if (ee.getKey().equals("n")) {
                    nounNum += ee.getValue();
                    isKeep = true;
                    isNoun = true;
                } else if (ee.getKey().equals("i")) {
                    isI = true;
                    isKeep = true;
                }
            }
            if (isKeep) {
                if (isNoun) {
                    double per = (double)nounNum / (double)allNum;
                    if (per < 0.499) {
                        continue;
                    }
                    if (word.length() > 2) {
                        if (smt.wikiWords.contains(word)) {
                            if (!tfidfScores.containsKey(word)) {
                                double tf = tfMap.get(word);
                                double df = dfMap.get(word);
                                double score = tf * Math.log(df + 1.0) * ((double)word.length()) / Math.log(2.0);
                                tfidfScores.put(word, score);
                                //totalTfidf += score;
                                if(maxTfidf < score) {
                                    maxTfidf = score;
                                }
                            } else {
                                System.out.println("Word appear more than once");
                            }
                        }
                    } else if ((word.length() == 2) && (dfMap.get(word) >= 2)) {
                        if (smt.wikiWords.contains(word)) {
                            if(!tfidfScores.containsKey(word)) {
                                double tf = tfMap.get(word);
                                double df = dfMap.get(word);
                                double score = 0.9 *  tf * Math.log(df + 1.0) * ((double)word.length()) / Math.log(2.0);
                                tfidfScores.put(word, score);
                                //totalTfidf += score;
                                if(maxTfidf < score) {
                                    maxTfidf = score;
                                }
                            } else {
                                System.out.println("Word appear more than once");
                            }
                        }
                    }
                }
                else if(isNpsz) {
                    if (smt.wikiWords.contains(word) || (tfMap.containsKey(word) && (tfMap.get(word) >= 2))) {
                        if(!tfidfScores.containsKey(word)){
                            double tf = tfMap.get(word);
                            double df = dfMap.get(word);
                            //double score = tf * Math.log(df + 1.0) / Math.log(2.0);
                            //double score = tf * Math.log(df + 1.0) * ((double)word.length()) / (Math.log(2.0) * 3.0);
                            double score = tf * Math.log(df + 1.0) * 3.0 / Math.log(2.0);
                            tfidfScores.put(word, score);
                            //totalTfidf += score;
                            if (maxTfidf < score) {
                                maxTfidf = score;
                            }
                        } else {
                            System.out.println("Word appear more than once");
                        }
                    }
                } else if (isI) {
                    if (smt.wikiWords.contains(word)) {
                        if(!tfidfScores.containsKey(word)) {
                            double tf = tfMap.get(word);
                            double df = dfMap.get(word);
                            //double score = tf * Math.log(df + 1.0) / Math.log(2.0);
                            //double score = tf * Math.log(df + 1.0) * ((double)word.length()) / (Math.log(2.0) * 3.0);
                            double score = tf * Math.log(df + 1.0) * 4.0 / Math.log(2.0);
                            tfidfScores.put(word, score);
                            //totalTfidf += score;
                            if (maxTfidf < score) {
                                maxTfidf = score;
                            }
                        } else {
                            System.out.println("Word appear more than once");
                        }
                    }
                }
            }
        }

        DecimalFormat dFormat=new DecimalFormat("0.000");

        for (Entry<String, Double> e:smtScores.entrySet()) {
            String word = e.getKey();
            if(tfidfScores.containsKey(word)) {
                continue;
            } else {
                double tf = smtTf.get(word);
                double df = smtDf.get(word);
                double tfidfScore = tf * Math.log(df + 1.0) * ((double)word.length()) / Math.log(2.0);
                tfidfScores.put(word, tfidfScore);
                if(maxTfidf < tfidfScore) {
                    maxTfidf = tfidfScore;
                }
            }
        }

        HashMap<String, Double> comScores = new HashMap<String, Double>();
        for (Entry<String, Double> e : tfidfScores.entrySet()) {
            String word = e.getKey();
            if(smtScores.containsKey(word)) {
                double tfidfScore = e.getValue() / maxTfidf;
                double smtScore = smtScores.get(word);
                /*
                   double max = 0.0;
                   double min = 0.0;
                   if(tfidfScore > smtScore){
                   max = tfidfScore;
                   min = smtScore;
                   }else{
                   max = smtScore;
                   min = tfidfScore;
                   }
                   double score = max + 0.5 * min;
                   */
                double score = tfidfScore + smtScore;
                comScores.put(word, score);
            } else {
                comScores.put(word, e.getValue() / maxTfidf);
            }
        }

        Object[] ans2 = comScores.entrySet().toArray();
        Arrays.sort(ans2, smt.c);

        //String outCombineFile = Config.TEXTPATH + "OUT"+input + "Combine.txt";
        //BufferedWriter outCom = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outCombineFile),"UTF-8"));

        ArrayList<WeightedWord> result = new ArrayList<WeightedWord>();
        int counter = 0;
        int limit = 150;
        for(Object s : ans2){
            String word = ((Entry<String, Double>) s).getKey();
            double score = ((Entry<String, Double>) s).getValue();

            double tfidfScore = 0.0;
            if (tfidfScores.containsKey(word)) {
                tfidfScore = tfidfScores.get(word) / maxTfidf;
            }
            double smtScore = 0.0;
            if (smtScores.containsKey(word)) {
                smtScore = smtScores.get(word);
            }
            //		    outCom.write(word+"	score:"+dFormat.format(score)+"	tfdfScore:"+dFormat.format(tfidfScore)+"	smtScore:"+dFormat.format(smtScore)+"	TF:"+(tfMap.get(word)==null?smtTf.get(word):tfMap.get(word))+"	DF:"+(dfMap.get(word)==null?smtDf.get(word):dfMap.get(word)));
            //		    outCom.newLine();
            //		    outCom.flush();


            int scoreInt = (int) (score * 1000.0);
            if (scoreInt == 0) {
                break;
            }
            WeightedWord w = new WeightedWord(word, scoreInt);
            result.add(w);

            //			outTag.write(word + " " + scoreInt);
            //			outTag.newLine();
            //			outTag.flush();

            counter++;
            if (counter > limit) {
                break;
            }
        }
        //		outCom.close();
        //		outTag.close();

        tfMap.clear();
        dfMap.clear();
        wordSet.clear();
        taggingMap.clear();
        tfidfScores.clear();
        proMap.clear();
        tags.clear();
        smtTfidf.clear();
        smtTf.clear();
        smtDf.clear();
        smtWordSet.clear();
        smtSenWordSet.clear();
        smtScores.clear();
        comScores.clear();

        return result;
    }
}
