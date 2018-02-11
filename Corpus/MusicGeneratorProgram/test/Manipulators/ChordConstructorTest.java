/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manipulators;

import Elements.Chord;
import Elements.Key;
import Elements.KeyLetterEnum;
import Elements.KeyPositionEnum;
import java.util.ArrayList;
import java.util.List;
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
public class ChordConstructorTest {
    
    public ChordConstructorTest() {
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
     * Test of constructChord method, of class ChordConstructor.
     */
    @Test
    public void testConstructChord() {
        Chord chrd =
                ChordConstructor.getInstance().constructChord(new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST), 
                        ChordEnum.MAJOR_TRIAD);
        
      assertEquals(chrd.getKeys().get(0).getKeyLetterEnum().getKeyPosition(),
              KeyLetterEnum.C.getKeyPosition());
      assertEquals(chrd.getKeys().get(1).getKeyLetterEnum().getKeyPosition(),
              KeyLetterEnum.E.getKeyPosition());
        assertEquals(chrd.getKeys().get(2).getKeyLetterEnum().getKeyPosition(),
              KeyLetterEnum.G.getKeyPosition());
        
        assertEquals(chrd.getKeys().get(0).getPosition(),
              KeyPositionEnum.FIRST);
        assertEquals(chrd.getKeys().get(1).getPosition(),
              KeyPositionEnum.FIRST);
        assertEquals(chrd.getKeys().get(2).getPosition(),
              KeyPositionEnum.FIRST);
        
        chrd =
                ChordConstructor.getInstance().constructChord(new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST), 
                        ChordEnum.MINOR_TRIAD);
        
      assertEquals(chrd.getKeys().get(0).getKeyLetterEnum().getKeyPosition(),
              KeyLetterEnum.C.getKeyPosition());
      assertEquals(chrd.getKeys().get(1).getKeyLetterEnum().getKeyPosition(),
              KeyLetterEnum.Ef.getKeyPosition());
        assertEquals(chrd.getKeys().get(2).getKeyLetterEnum().getKeyPosition(),
              KeyLetterEnum.G.getKeyPosition());
        
        assertEquals(chrd.getKeys().get(0).getPosition(),
              KeyPositionEnum.FIRST);
        assertEquals(chrd.getKeys().get(1).getPosition(),
              KeyPositionEnum.FIRST);
        assertEquals(chrd.getKeys().get(2).getPosition(),
              KeyPositionEnum.FIRST);
        
        
        chrd =
                ChordConstructor.getInstance().constructChord(new Key(KeyLetterEnum.A, KeyPositionEnum.FIRST), 
                        ChordEnum.MAJOR_TRIAD);
        
      assertEquals(chrd.getKeys().get(0).getKeyLetterEnum().getKeyPosition(),
              KeyLetterEnum.A.getKeyPosition());
      assertEquals(chrd.getKeys().get(1).getKeyLetterEnum().getKeyPosition(),
              KeyLetterEnum.CS.getKeyPosition());
        assertEquals(chrd.getKeys().get(2).getKeyLetterEnum().getKeyPosition(),
              KeyLetterEnum.E.getKeyPosition());
        
        assertEquals(chrd.getKeys().get(0).getPosition(),
              KeyPositionEnum.FIRST);
        assertEquals(chrd.getKeys().get(1).getPosition(),
              KeyPositionEnum.SECOND);
        assertEquals(chrd.getKeys().get(2).getPosition(),
              KeyPositionEnum.SECOND);
        
          chrd =
                ChordConstructor.getInstance().constructChord(new Key(KeyLetterEnum.A, KeyPositionEnum.FIRST), 
                        ChordEnum.MINOR_TRIAD);
        
      assertEquals(chrd.getKeys().get(0).getKeyLetterEnum().getKeyPosition(),
              KeyLetterEnum.A.getKeyPosition());
      assertEquals(chrd.getKeys().get(1).getKeyLetterEnum().getKeyPosition(),
              KeyLetterEnum.C.getKeyPosition());
        assertEquals(chrd.getKeys().get(2).getKeyLetterEnum().getKeyPosition(),
              KeyLetterEnum.E.getKeyPosition());
        
        assertEquals(chrd.getKeys().get(0).getPosition(),
              KeyPositionEnum.FIRST);
        assertEquals(chrd.getKeys().get(1).getPosition(),
              KeyPositionEnum.SECOND);
        assertEquals(chrd.getKeys().get(2).getPosition(),
              KeyPositionEnum.SECOND);
    }
    
}
