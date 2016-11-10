
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
        String expResult = "[{\"rok\":1.56},{\"big\":0.78},{\"better\":0.78},{\"career\":0.78},{\"desk\":1.56},{\"cynic\":0.78}]";
        String result = TfIdf.getTfidf(limit, filename);
        //System.out.print(result);
        assertEquals(expResult, result);
    }    
}
