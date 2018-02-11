/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grammar_Elements.GroupingStructure;

import Elements.DurationEnum;
import Grammar_Elements.ExceptionClasses.GroupingWellFormednessException;
import Grammar_Elements.MetricalStructure.Beat;
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
public class BaseGroupTest {
    
    public BaseGroupTest() {
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
     * Test of constructor method, of class BaseGroup.
     */
    @Test
    public void testConstructor()  {
     
       assertTrue("T1", getConstructorTest1());
       assertTrue("T2", getConstructorTest2());
       assertTrue("T3", getConstructorTest3());
    }
    
    @Test
    public void testGetMetricalSpan()
    {
        assertNotNull(getMetricalSpanTest1());
        assertNotNull(getMetricalSpanTest2());
    }
    
    private List<Beat> getMetricalSpanTest1()
    {
          List<Beat> beats = new ArrayList<>();
       try{
           BaseGroup group = new BaseGroup(beats);
           return group.getMetricalBeatSpan();
       }
       catch(GroupingWellFormednessException e)
       {
          
       }
       
       return null;
       
    }
    
    private List<Beat> getMetricalSpanTest2()
    {
           List<Beat> beats = new ArrayList<>();
           
        
         Beat beat1 = new Beat(3);
         Beat beat2 = new Beat(1);
         Beat beat3 = new Beat(2);
         Beat beat4 = new Beat(1);
         Beat beat5 = new Beat(3);
         
         beats.add(beat1);
         beats.add(beat2);
         beats.add(beat3);
         beats.add(beat4);
         beats.add(beat5);
           
       try{
           BaseGroup group = new BaseGroup(beats);
           return group.getMetricalBeatSpan();
       }
       catch(GroupingWellFormednessException e)
       {
          
       }
       
       return null;
    }
    
    private boolean getConstructorTest1()
    {
       List<Beat> beats = new ArrayList<>();
       try{
           BaseGroup group = new BaseGroup(beats);
           return true;
       }
       catch(GroupingWellFormednessException e)
       {
          return false; 
       }
    }
    
    private boolean getConstructorTest2()
    {
         List<Beat> beats = new ArrayList<>();
         Beat beat1 = new Beat(3);
         Beat beat2 = new Beat(1);
         Beat beat3 = new Beat(2);
         Beat beat4 = new Beat(1);
         Beat beat5 = new Beat(3);
         
         beats.add(beat1);
         beats.add(beat2);
         beats.add(beat3);
         beats.add(beat4);
         beats.add(beat5);
         
         
       try{
           BaseGroup group = new BaseGroup(beats);
           return true;
       }
       catch(GroupingWellFormednessException e)
       {
          return false; 
       }
    }
    
      private boolean getConstructorTest3()
    {
        //testing overlap failure
        
         List<Beat> beats = new ArrayList<>();
         Beat beat1 = new Beat(3);
         Beat beat2 = new Beat(1);
         Beat beat3 = new Beat(2);
         Beat beat4 = new Beat(1);
         Beat beat5 = new Beat(3);
         
         beats.add(beat1);
         beats.add(beat2);
         beats.add(beat3);
         beats.add(beat4);
         beats.add(beat5);
         
         List<Beat> beats2 = new ArrayList<>();
         
         Beat beat6 = new Beat(1);
         Beat beat7 = new Beat(2);
         
         beats.add(beat1);
         beats.add(beat6);
         beats.add(beat7);
         
         
         
       try{
           BaseGroup group = new BaseGroup(beats);
           BaseGroup group2 = new BaseGroup(beats2);
           return false;
       }
       catch(GroupingWellFormednessException e)
       {
          return true; 
       }
    }
    
    
}
