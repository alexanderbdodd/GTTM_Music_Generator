/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grammar_Elements;

import Elements.DurationEnum;
import Grammar_Elements.ExceptionClasses.GroupingWellFormednessException;
import Grammar_Elements.GroupingStructure.BaseGroup;
import Grammar_Elements.GroupingStructure.Group;
import Grammar_Elements.GroupingStructure.HighLevelGroup;
import Grammar_Elements.MetricalStructure.Beat;
import Grammar_Elements.MetricalStructure.MetricalContainer;
import Grammar_Elements.ReductionBranches.Branch;
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
public class WellFormednessAnalyserTest {
    
    public WellFormednessAnalyserTest() {
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
     * Test of getInstance method, of class WellFormednessAnalyser.
     */
    @Test
    public void testGetInstance() {
        WellFormednessAnalyser a = WellFormednessAnalyser.getInstance();
        
        assertNotNull(a);
        
        assertSame(a, WellFormednessAnalyser.getInstance());
    }

    /**
     * Test of verifyGWFR2 method, of class WellFormednessAnalyser.
     */
    @Test
    public void testVerifyGWFR2() throws Exception {
        
        Beat b1 = new Beat(1);
        Beat b2 = new Beat(2);
        Beat b3 = new Beat(1);
        Beat b4 = new Beat(3);
        Beat b5 = new Beat(1);
        Beat b6 = new Beat(2);
        
       MetricalContainer m = new MetricalContainer();
        
       m.addBeat(b1);
       m.addBeat(b2);
       m.addBeat(b3);
       m.addBeat(b4);
       m.addBeat(b5);
       m.addBeat(b6);
       
       List<Beat> span1 = new ArrayList<>();
       List<Beat> span2 = new ArrayList<>();
       
       span1.add(b1);
       span1.add(b2);
       span1.add(b3);
       
       span2.add(b4);
       span2.add(b5);
       span2.add(b6);
       
       Group bg = new BaseGroup(span1);
       Group bg2 = new BaseGroup(span2);
       
       List<Group> groups = new ArrayList<>();
       
       groups.add(bg);
       groups.add(bg2);
            
       Group highLevel = new HighLevelGroup(groups);
       
      try{ WellFormednessAnalyser.getInstance().verifyGWFR2(m, highLevel);
      assertTrue(true);
      }
      catch(GroupingWellFormednessException e)
      {
          fail();
      }
    }

    
    /**
     * Test of verifyGWFR1 method, of class WellFormednessAnalyser.
     */
    @Test
    public void testVerifyGWFR1() throws Exception {
         Beat b1 = new Beat(1);
        Beat b2 = new Beat(2);
        Beat b3 = new Beat(1);
        Beat b4 = new Beat(3);
        Beat b5 = new Beat(1);
        Beat b6 = new Beat(2);
        
       MetricalContainer m = new MetricalContainer();
        
       m.addBeat(b1);
       m.addBeat(b2);
       m.addBeat(b3);
       m.addBeat(b4);
       m.addBeat(b5);
       m.addBeat(b6);
       
       List<Beat> span1 = new ArrayList<>();
       List<Beat> span2 = new ArrayList<>();
       
       span1.add(b1);
       span1.add(b2);
       span1.add(b3);
       
       span2.add(b4);
       span2.add(b5);
       span2.add(b6);
       
       Group bg = new BaseGroup(span1);
       Group bg2 = new BaseGroup(span2);
       
       List<Group> groups = new ArrayList<>();
       
       groups.add(bg);
       groups.add(bg2);
            
       Group highLevel = new HighLevelGroup(groups);
       
      try{ WellFormednessAnalyser.getInstance().verifyGWFR1(m.getMetricalBeatsList());
      assertTrue(true);
      }
      catch(GroupingWellFormednessException e)
      {
          fail();
      }
      
      List<Beat> bs = new ArrayList<>();
      
      bs.add(b6);
      bs.add(b2);
      bs.add(b1);
      
       try{ WellFormednessAnalyser.getInstance().verifyGWFR1(bs);
        fail();
      }
      catch(GroupingWellFormednessException e)
      {
          assertTrue(true);
      }
      
    }

    
}
