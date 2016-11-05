
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
        String expResult = "[{\"hammer\":0.7781512503836436},{\"mistake\":0.7781512503836436},{\"better\":0.7781512503836436},{\"career\":0.7781512503836436},{\"desk\":1.5563025007672873},{\"cynic\":0.7781512503836436}]";
        String result = TfIdf.getTfidf(limit, filename);
        System.out.print(result);
        assertEquals(expResult, result);
    }
    
}
