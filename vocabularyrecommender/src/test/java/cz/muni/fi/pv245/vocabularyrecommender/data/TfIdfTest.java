package cz.muni.fi.pv245.vocabularyrecommender.data;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import junit.framework.TestCase;

/**
 *
 * @author tomas
 */
public class TfIdfTest extends TestCase {
    
    public TfIdfTest(String testName) {
        super(testName);  
    }

    /**
     * Test of tfidf method, of class TfIdf.
     */
    public void testTfidf() {
        System.out.println("tfidfTest");
        
        int limit = 2;
        String filename = "inputExample.json";
        String expResult = "[{\"rok\":\"sk\",\"riešenie\":\"sk\"},{\"big\":\"en\",\"mistake\":\"en\"},{\"better\":\"en\",\"overhead\":\"en\"},{\"career\":\"en\",\"job\":\"en\"},{\"desk\":\"en\",\"clean\":\"en\"},{\"cynic\":\"en\",\"smells\":\"en\"}]\n";
        String result = TfIdf.getTfidf(limit, filename);
        //System.out.print(result);

        //assertEquals(expResult, result);
    }
}
