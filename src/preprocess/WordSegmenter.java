package preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import config.Config;

import org.thunlp.language.chinese.LangUtils;
import org.thunlp.tagsuggest.contentbase.SMTwithTfidfWeibo;

public class WordSegmenter {
    public static String segment(String text) throws Exception {
        //clean
        text = SMTwithTfidfWeibo.dealWithWeiboText(text);
        text = LangUtils.removePunctuationMarks(text);
        text = LangUtils.removeLineEnds(text);
        text = LangUtils.removeExtraSpaces(text);
        text = text.toLowerCase();

        String cleanfile = Config.TEXTPATH + "clean.tmp";
        String segmentfile = Config.TEXTPATH + "seg.tmp";

        BufferedWriter cleanwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cleanfile), "UTF-8"));
        cleanwriter.write(text);
        cleanwriter.newLine();
        cleanwriter.flush();
        cleanwriter.close();

        //segment
        Process wsProcess = null;
        if (Config.inLinux) {
            // In linux
            String[] cmds = {"/bin/sh","-c",Config.WSPATH+"postagging.sh pku <"+cleanfile+" >"+segmentfile};//wsPath + "sl_decoder pku_model.bin pku_dat.bin pku_label_info.txt";
            wsProcess = Runtime.getRuntime().exec(cmds, null,new File(Config.WSPATH));
            System.out.println("exec "+cmds[2]);
        }
        else {
            // In windows
            String[] cmds = {"/bin/sh","-c",Config.WSPATH + "sl_decoder pku_model.bin pku_dat.bin pku_label_info.txt <\"" + cleanfile + "\" >\"" + segmentfile + "\""};//wsPath + "sl_decoder pku_model.bin pku_dat.bin pku_label_info.txt";
            String cmdPath = Config.WSPATH + "seg.bat";
            BufferedWriter outCmd = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cmdPath)));
            outCmd.write(cmds[2]);
            outCmd.newLine();
            outCmd.flush();
            outCmd.close();
            wsProcess = Runtime.getRuntime().exec("\""+cmdPath+"\"", null,new File(Config.WSPATH));
            //System.out.println("exec "+cmdPath);
            wsProcess.waitFor();
        }

        BufferedReader segreader = new BufferedReader(new InputStreamReader(new FileInputStream(segmentfile),"UTF-8"));
        String result = segreader.readLine();
        segreader.close();

        return result;
    }

    public static void main(String args[]) throws Exception {
        File dir = new File("F:/IR2");
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println(i);
            File f = files[i];
            if (f.getName().endsWith("txt")) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsolutePath()), "utf8"));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("F:/IR2/new/" + f.getName()), "UTF-8"));
                String line;
                int count = 0;
                while ((line = br.readLine()) != null) {
                    count++;
                    if (count % 10 == 0) {
                        System.out.println(count);
                    }
                    String time = line;
                    String id = br.readLine();
                    String content = br.readLine();
                    String seg = WordSegmenter.segment(content);

                    if (seg != null) {
                        bw.write(time);
                        bw.newLine();
                        bw.write(id);
                        bw.newLine();
                        bw.write(content);
                        bw.newLine();
                        bw.write(WordSegmenter.segment(content));
                        bw.newLine();
                        bw.flush();
                    }
                }
                br.close();
                bw.close();
            }
        }
    }
}