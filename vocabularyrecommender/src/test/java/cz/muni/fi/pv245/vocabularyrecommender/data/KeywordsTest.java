/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv245.vocabularyrecommender.data;

import junit.framework.TestCase;

/**
 *
 * @author tomas
 */
public class KeywordsTest extends TestCase {
    
    public KeywordsTest(String testName) {
        super(testName);
    }
    /**
     * Test of getKeywords method, of class Keywords.
     */
    public void testKeywords() {
        System.out.println("keywordsTest");
        int limit = 1;
        String filename = "inputExample.json";
        String expResult = "[{\"rieseni\":25.43},{\"bird\":14.0},{\"bird\":14.0},{\"career\":13.0},{\"desk\":28.0},{\"cynic\":14.2}]";
        String result = Keywords.getKeywords(limit, filename);
        //System.out.print(result);

        assertEquals(expResult, result);
    }
    
}
