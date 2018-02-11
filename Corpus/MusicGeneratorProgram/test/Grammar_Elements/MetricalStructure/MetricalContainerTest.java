/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grammar_Elements.MetricalStructure;

import Elements.DurationEnum;
import Grammar_Elements.ExceptionClasses.MetricalWellFormednessException;
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
public class MetricalContainerTest {

    public MetricalContainerTest() {
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
     * Test of addBeat method, of class MetricalContainer.
     */
    @Test
    public void testAddBeat() throws Exception {

        assertTrue("Test 1", addBeatTest1());
        assertTrue("Test 2", addBeatTest2());
        assertTrue("Test 3", addBeatTest3());
        assertTrue("Test 4", addBeatTest4());
        assertTrue("Test 5", addBeatTest5());

    }

    private boolean addBeatTest1() {
        MetricalContainer container = new MetricalContainer();

        try {
            container.addBeat(new Beat(1));
            return true;
        } catch (MetricalWellFormednessException e) {
            return false;
        }
    }

    private boolean addBeatTest2() {
        MetricalContainer container = new MetricalContainer();

        Beat beat1 = new Beat(1);

        try {
            container.addBeat(beat1);
            
        } catch (MetricalWellFormednessException e) {
            return false;
        }

        Beat beat2 = new Beat(1);

        try {
            container.addBeat(beat2);
            return false;

        } //checking MWFR3
        catch (MetricalWellFormednessException e) {
            return true;
        }
    }
    
    private boolean addBeatTest3()
    {
        MetricalContainer container = new MetricalContainer();
        
        //testing structure with strong beats being separated by 2 beats
        
        Beat beat1 = new Beat(3);
        Beat beat2 = new Beat(1);
        Beat beat3 = new Beat(2);
        Beat beat4 = new Beat(1);
        Beat beat5 = new Beat(3);
        Beat beat6 = new Beat(1);
        Beat beat7 = new Beat(2);
        
        try{
            container.addBeat(beat1);
            container.addBeat(beat2);
            container.addBeat(beat3);
            container.addBeat(beat4);
            container.addBeat(beat5);
            container.addBeat(beat6);
            container.addBeat(beat7);
            
            return true;
        }
        
        catch(MetricalWellFormednessException e)
        {
            return false;
        }
        
    }
    
    private boolean addBeatTest4()
    {
        MetricalContainer container = new MetricalContainer();
        
        //testing structure with strong beats being separated by a combination of 2 or 3 beats
        
        Beat beat1 = new Beat(3);
        Beat beat2 = new Beat(1);
        Beat beat3 = new Beat(2);
        Beat beat4 = new Beat(1);
        Beat beat5 = new Beat(2);
        Beat beat6 = new Beat(1);
        Beat beat7 = new Beat(3);
        Beat beat8 = new Beat(1);
        Beat beat9 = new Beat(2);
        Beat beat10 = new Beat(1);
        Beat beat11 = new Beat(3);
        
        try{
            container.addBeat(beat1);
            container.addBeat(beat2);
            container.addBeat(beat3);
            container.addBeat(beat4);
            container.addBeat(beat5);
            container.addBeat(beat6);
            container.addBeat(beat7);
            container.addBeat(beat8);
            container.addBeat(beat9);
            container.addBeat(beat10);
            container.addBeat(beat11);
                     
            return true;
        }
        
        catch(MetricalWellFormednessException e)
        {
            return false;
        }
        
    }
    
      private boolean addBeatTest5()
    {
        MetricalContainer container = new MetricalContainer();
        
        //testing structure with strong beats being separated by a combination of 2 or 3 beats
        
        Beat beat1 = new Beat(3);
        Beat beat2 = new Beat(1);
        Beat beat3 = new Beat(2);
        Beat beat4 = new Beat(1);
        Beat beat5 = new Beat(2);
        Beat beat6 = new Beat(1);
        Beat beat7 = new Beat(2);
        Beat beat8 = new Beat(1);
        Beat beat9 = new Beat(3);
        
        
        try{
            container.addBeat(beat1);
            container.addBeat(beat2);
            container.addBeat(beat3);
            container.addBeat(beat4);
            container.addBeat(beat5);
            container.addBeat(beat6);
            container.addBeat(beat7);
            container.addBeat(beat8);
            container.addBeat(beat9);
          
                     
            return false;
        }
        
        catch(MetricalWellFormednessException e)
        {
            return true;
        }
        
    }

    /**
     * Test of getMetricalBeatsList method, of class MetricalContainer.
     */
    @Test
    public void testGetMetricalBeatsList() {
        MetricalContainer container = new MetricalContainer();
        
        assertNotNull(container.getMetricalBeatsList());
    }

    /**
     * Test of getBeatAtPosition method, of class MetricalContainer.
     */
    @Test
    public void testGetBeatAtPosition() {
        
        MetricalContainer container = new MetricalContainer();
        
        Beat b1 = new Beat(1);
        Beat b2 = new Beat(5);
        Beat b3 = new Beat(2);
        Beat b4 = new Beat(3);
        
      try{  container.addBeat(b1);
        container.addBeat(b2);
        container.addBeat(b3);
        container.addBeat(b4);
      }
      catch(MetricalWellFormednessException e)
      {
          fail("Add beat failure");
      }
        
        assertEquals("T1",null, container.getBeatAtPosition(-1));
        assertEquals("T2",null, container.getBeatAtPosition(4));
        assertEquals("T3", b1, container.getBeatAtPosition(0));
        assertEquals("T4", b4, container.getBeatAtPosition(3));
        assertEquals("T5", b2, container.getBeatAtPosition(1));
        assertEquals("T6", b3, container.getBeatAtPosition(2));
        
   
    }

}
