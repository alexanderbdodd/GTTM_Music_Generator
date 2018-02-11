/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manipulators;

import Elements.KeyLetterEnum;
import Elements.Scale;
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
public class ScaleConstructorTest {
    
    public ScaleConstructorTest() {
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
     * Test of getInstance method, of class ScaleConstructor.
     */
    @Test
    public void testGetInstance() {
        ScaleConstructor s = ScaleConstructor.getInstance();
         assertNotNull(s);
         
         assertSame(s, ScaleConstructor.getInstance());
    }

    /**
     * Test of getRelativeMinor method, of class ScaleConstructor.
     */
    @Test
    public void testGetRelativeMinor() {
        assertEquals(KeyLetterEnum.A, ScaleConstructor.getInstance().getRelativeMinor(KeyLetterEnum.C).getTonic());
    }

    /**
     * Test of getRelativeMajor method, of class ScaleConstructor.
     */
    @Test
    public void testGetRelativeMajor() {
  assertEquals(KeyLetterEnum.C, ScaleConstructor.getInstance().getRelativeMajor(KeyLetterEnum.A).getTonic());
    }

    /**
     * Test of constructScale method, of class ScaleConstructor.
     */
    @Test
    public void testConstructScale() {
      Scale s = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR);
      
      assertEquals(s.getTonic(), KeyLetterEnum.C);
      assertEquals(s.getSupertonic(), KeyLetterEnum.D);
      assertEquals(s.getSubdominant(), KeyLetterEnum.F);
      assertEquals(s.getSubmediant(), KeyLetterEnum.A);
      assertEquals(s.getDominant(), KeyLetterEnum.G);
      assertEquals(s.getMediant(), KeyLetterEnum.E);
      assertEquals(s.getSubtonic(), KeyLetterEnum.B);
    }


    
}
