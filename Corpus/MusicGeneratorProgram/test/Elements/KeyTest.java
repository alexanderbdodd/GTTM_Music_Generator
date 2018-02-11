 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Elements;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander
 */
public class KeyTest {
    
    public KeyTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }


    /**
     * Test of compare method, of class Key.
     */
    @Test
    public void testCompare() {
     Key key1 = new Key(KeyLetterEnum.A, KeyPositionEnum.FOURTH);
     Key key2 = new Key(KeyLetterEnum.A, KeyPositionEnum.FOURTH);
     
     assertEquals(0, key1.compare(key1, key2));
     
     key2 = new Key(KeyLetterEnum.A, KeyPositionEnum.FIFTH);
     
     assertEquals(-1, key1.compare(key1,key2));
     
     key2 = new Key(KeyLetterEnum.A, KeyPositionEnum.THIRD);
     
     assertEquals(1, key1.compare(key1,key2));
     
     key2 = new Key(KeyLetterEnum.B, KeyPositionEnum.FOURTH);
     
     assertEquals(-1, key1.compare(key1, key2));
     
     key2 = new Key(KeyLetterEnum.E, KeyPositionEnum.FOURTH);
     
     assertEquals(1, key1.compare(key1, key2));
     
     
     
    }
    
}
