package cz.muni.fi.pv245.vocabularyrecommender.data;

import java.io.StringWriter;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

/**
 * Class generete rank keywords from given file with texts in json format
 *
 * @author Tomas Durcak
 * @param limit limit of words per document
 * @param filename documents to proces
 * @return result and save it to keyword_output.json file
 */
public class Keywords {

    public static String getKeywords(int limit, String filename) {
        String line = " ";

        try {
            ScriptingContainer container = new ScriptingContainer();
            String[] argv = new String[2];
            argv[0] = "" + limit;
            argv[1] = filename;
            container.setArgv(argv);
            
            StringWriter stringWriter = new StringWriter();
            container.setWriter(stringWriter);
            
            container.runScriptlet(PathType.RELATIVE, "keywords.rb");
            line = stringWriter.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }
}
