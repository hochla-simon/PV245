package cz.muni.fi.pv245.vocabularyrecommender.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class generete rank keywords from given file with texts in json format
 *
 * @author Tomas Durcak
 * @param limit limit of words per document
 * @param filename documents to proces
 * @return result and save it to keyword_output.json file
 */
public class Keywords {

    public static String getKeywords(int limit, String filename, String scripname) {
        String line = "";

        try {
            String[] argv = new String[4];
            argv[0] = "ruby";
            argv[1] = scripname;
            argv[2] = "" + limit;
            argv[3] = filename;

            File f = new File(argv[1]);
            if (!f.exists()) {
                throw new RuntimeException("File keywords.rb could not be find!!!!!");
            }

            f = new File(argv[3]);
            if (!f.exists()) {
                throw new RuntimeException("File " + argv[3] + " could not be find!!!!!");
            }

            Process process = Runtime.getRuntime().exec(argv);
            process.waitFor();

            BufferedReader processIn = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            line = processIn.readLine();
            System.out.println("Keywords output: " + line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }
}
