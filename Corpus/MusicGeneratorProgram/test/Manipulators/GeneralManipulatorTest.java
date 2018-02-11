/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manipulators;

import Elements.Key;
import Elements.KeyLetterEnum;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander
 */
public class GeneralManipulatorTest {
    
    public GeneralManipulatorTest() {
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
     * Test of rearrangeNotes method, of class GeneralManipulator.
     */
    @Test
    public void testRearrangeNotes() {
     KeyLetterEnum[] array = new KeyLetterEnum[5];
     array[0] = KeyLetterEnum.B;
     array[1] = KeyLetterEnum.C;
     array[2] = KeyLetterEnum.D;
     array[3] = KeyLetterEnum.E;
     array[4] = KeyLetterEnum.F;
     
     List<KeyLetterEnum> array2 = GeneralManipulator.rearrangeNotes(KeyLetterEnum.C, array);
     
     assertEquals(array2.get(0), array[1]);
     assertEquals(array2.get(1), array[2]);
     assertEquals(array2.get(2), array[3]);
     assertEquals(array2.get(3), array[4]);
     assertEquals(array2.get(4), array[0]);
     
    }
    
}
