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
        String expResult = "[{\"rieseni\":\"sk\"},{\"bird\":\"en\"},{\"hand\":\"en\"},{\"career\":\"en\"},{\"desk\":\"en\"},{\"smell\":\"en\"}]";
        
        String result = Keywords.getKeywords(limit, filename);
        //System.out.println(result);
        assertEquals(expResult, result);
    }

}
