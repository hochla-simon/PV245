package cz.muni.fi.pv245.vocabularyrecommender.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author tomas
 */
public class TfIdf {

    /**
     * Class generete keywords by TF-IDF from given file with texts in json format
     * @author Tomas Durcak
     * @param limit limit of words per document
     * @param filename documents to proces
     * @return result and save it to tfidf_output.json file
     */
    public static String getTfidf(int limit, String filename) {
        String line = "";

        try {
            String[] argv = new String[4];
            argv[0] = "ruby";
            argv[1] = "tfidf.rb";
            argv[2] = ""+limit;
            argv[3] = filename;

            Process process = Runtime.getRuntime().exec(argv);
            process.waitFor();

            BufferedReader processIn = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            
            line = processIn.readLine();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }
}
