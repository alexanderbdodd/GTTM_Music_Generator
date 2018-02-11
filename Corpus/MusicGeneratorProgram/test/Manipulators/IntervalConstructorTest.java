/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manipulators;

import Elements.Key;
import Elements.KeyLetterEnum;
import Elements.KeyPositionEnum;
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
public class IntervalConstructorTest {
    
    public IntervalConstructorTest() {
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
     * Test of getKeyAtIntervalDistance method, of class IntervalConstructor.
     */
    @Test
    public void testGetKeyAtIntervalDistance_3args_1() {
        Key key1 = new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST);
        Key key2 = IntervalConstructor.getInstance().getKeyAtIntervalDistance(key1, IntervalEnum.MINOR2ND, true);
        assert(key2.getKeyLetterEnum() == KeyLetterEnum.CS);
        assert(key2.getPosition() == KeyPositionEnum.FIRST);
        
        key1 = new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST);
        key2 = IntervalConstructor.getInstance().getKeyAtIntervalDistance(key1, IntervalEnum.PERFECT5TH, true);
        assert(key2.getKeyLetterEnum() == KeyLetterEnum.G);
        assert(key2.getPosition() == KeyPositionEnum.FIRST);
        
        key1 = new Key(KeyLetterEnum.B, KeyPositionEnum.FIRST);
        key2 = IntervalConstructor.getInstance().getKeyAtIntervalDistance(key1, IntervalEnum.PERFECT5TH, true);
        assert(key2.getKeyLetterEnum() == KeyLetterEnum.FS);
        assert(key2.getPosition() == KeyPositionEnum.SECOND);
        
        key1 = new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND);
        key2 = IntervalConstructor.getInstance().getKeyAtIntervalDistance(key1, IntervalEnum.PERFECT5TH, false);
        assert(key2.getKeyLetterEnum().getKeyPosition() == KeyLetterEnum.F.getKeyPosition());
        assert(key2.getPosition() == KeyPositionEnum.FIRST);
        
        
        key1 = new Key(KeyLetterEnum.G, KeyPositionEnum.SECOND);
        key2 = IntervalConstructor.getInstance().getKeyAtIntervalDistance(key1, IntervalEnum.PERFECT5TH, false);
        assert(key2.getKeyLetterEnum().getKeyPosition() == KeyLetterEnum.C.getKeyPosition());
        assert(key2.getPosition() == KeyPositionEnum.SECOND);
             
    }

    /**
     * Test of getKeyAtIntervalDistance method, of class IntervalConstructor.
     */
    @Test
    public void testGetKeyAtIntervalDistance_3args_2() {
        KeyLetterEnum key1 = KeyLetterEnum.C;
        KeyLetterEnum key2 = IntervalConstructor.getInstance().getKeyAtIntervalDistance(key1, IntervalEnum.MINOR2ND, true);
        assert(key2 == KeyLetterEnum.CS);
        
      
        key2 = IntervalConstructor.getInstance().getKeyAtIntervalDistance(key1, IntervalEnum.PERFECT5TH, true);
        assert(key2 == KeyLetterEnum.G);
        
        
        key2 = IntervalConstructor.getInstance().getKeyAtIntervalDistance(key1, IntervalEnum.PERFECT5TH, false);
        assert(key2.getKeyPosition() == KeyLetterEnum.F.getKeyPosition());
        
        
        key1 = KeyLetterEnum.G;
        key2 = IntervalConstructor.getInstance().getKeyAtIntervalDistance(key1, IntervalEnum.PERFECT5TH, false);
        assert(key2.getKeyPosition() == KeyLetterEnum.C.getKeyPosition());
    }

    /**
     * Test of calculateDistance method, of class IntervalConstructor.
     */
    @Test
    public void testCalculateDistance() {
        
        Key key1 = new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST);
        Key key2 = new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST);
        assertEquals(0, IntervalConstructor.getInstance()
                .calculateDistance(key1, key2));
        
        key1 = new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST);
        key2 = new Key(KeyLetterEnum.D, KeyPositionEnum.FIRST);
        assertEquals(2, IntervalConstructor.getInstance()
                .calculateDistance(key1, key2));
        
        key1 = new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST);
        key2 = new Key(KeyLetterEnum.D, KeyPositionEnum.SECOND);
        assertEquals(14, IntervalConstructor.getInstance()
                .calculateDistance(key1, key2));
        
        key1 = new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST);
        key2 = new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST);
        assertEquals(0, IntervalConstructor.getInstance().calculateDistance(key1, key2));
        
        key1 = new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND);
        key2 = new Key(KeyLetterEnum.D, KeyPositionEnum.FIRST);
        assertEquals(-10, IntervalConstructor.getInstance().calculateDistance(key1, key2));
        
        key1 = new Key(KeyLetterEnum.C, KeyPositionEnum.THIRD);
        key2 = new Key(KeyLetterEnum.D, KeyPositionEnum.FIRST);
        assertEquals(-22, IntervalConstructor.getInstance()
                .calculateDistance(key1, key2));
        
       
    }
    
}
