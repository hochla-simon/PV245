
package cz.muni.fi.pv245.vocabularyrecommender.data;

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
        int limit = 1;
        String filename = "inputExample.json";
        String expResult = "[{\"rok\":\"sk\"},{\"big\":\"en\"},{\"better\":\"en\"},{\"career\":\"en\"},{\"desk\":\"en\"},{\"cynic\":\"en\"}]";
        String result = TfIdf.getTfidf(limit, filename);
        //System.out.print(result);
        assertEquals(expResult, result);
    }    
}
