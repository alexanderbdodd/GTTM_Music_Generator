/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GTTM_Analyser;

import Elements.*;
import Grammar_Elements.ExceptionClasses.BranchingWellFormednessException;
import Manipulators.ScaleConstructor;
import Manipulators.ScaleModeEnum;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander
 */
public class TimeSpanReductionAnalyserTest {

    public TimeSpanReductionAnalyserTest() {
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
     * Test of getInstance method, of class TimeSpanReductionAnalyser.
     */
    @Test
    public void testGetInstance() {
        TimeSpanReductionAnalyser instance = TimeSpanReductionAnalyser.getInstance();

        assertNotNull(instance);
        assertSame(instance, TimeSpanReductionAnalyser.getInstance());

    }

    /**
     * Test of assessTSPR2 method, of class TimeSpanReductionAnalyser.
     */
    @Test
    public void testAssessTSPR2() {
        assertTrue("Test 1", testTSPR21());
        assertTrue("Test 2", testTSPR22());
        assertTrue("Test 3", testTSPR23());
        
    }

    //strongest TSPR1
    private boolean testTSPR21() {
      
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
       

        Scale localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR);

        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessTSPR2(a3,a7, localScale);

        //the two TSPR2 rules seem to conflict, so 0.7 is the middle ground
        if (rating > 0.7) {
            return true;
        } else {
            return false;
        }
    }

    //weakest TSPR2
    private boolean testTSPR22() throws BranchingWellFormednessException {
        
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.DS, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        
        List<KeyLetterEnum> span = new ArrayList<>();
        span.add(a1.getKeyLetterEnum());
        span.add(a2.getKeyLetterEnum());
        span.add(a3.getKeyLetterEnum());
        span.add(a4.getKeyLetterEnum());
        span.add(a5.getKeyLetterEnum());
        span.add(a6.getKeyLetterEnum());
        span.add(a7.getKeyLetterEnum());

        List<Scale> scl = ScaleConstructor.getInstance().assessDiatonicScale(span);

        Scale localScale;

        if (scl.get(0).getTonic() == a1.getKeyLetterEnum()) {
            localScale = scl.get(0);
        } else {
            localScale = scl.get(1);
        }

        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessTSPR2(a3, a7, localScale);

        if (rating < 0.2) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean testTSPR23() throws BranchingWellFormednessException {
       
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
      
        List<KeyLetterEnum> span = new ArrayList<>();
        span.add(a1.getKeyLetterEnum());
        span.add(a2.getKeyLetterEnum());
        span.add(a3.getKeyLetterEnum());
        span.add(a4.getKeyLetterEnum());
        span.add(a5.getKeyLetterEnum());
        span.add(a6.getKeyLetterEnum());
        span.add(a7.getKeyLetterEnum());
        
        List<Scale> scl = ScaleConstructor.getInstance().assessDiatonicScale(span);

        Scale localScale;

        if (scl.get(0).getTonic() == a1.getKeyLetterEnum()) {
            localScale = scl.get(0);
        } else {
            localScale = scl.get(1);
        }

        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessTSPR2(a3,a7, localScale);

        if (rating < 0.8 && rating > 0.2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Test of assessTSPR3 method, of class TimeSpanReductionAnalyser.
     */
    @Test
    public void testAssessTSPR3a() throws BranchingWellFormednessException {
        assertTrue("Test 1", TSPR3Test1());
        assertTrue("Test 2", TSPR3Test2());
        assertTrue("Test 3", TSPR3Test3());
    }

    private boolean TSPR3Test1() throws BranchingWellFormednessException {
    
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 =  new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
       
        
        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessTSPR3a(a3, a7);
        
        
        if(rating == 1.0)
        {
            return true;
        }
        else{
            return false;
        }
    }

    
     private boolean TSPR3Test2() throws BranchingWellFormednessException {
                
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 =  new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
       
        
        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessTSPR3a(a3, a7);
        
        
        if(rating == 0.0)
        {
            return true;
        }
        else{
            return false;
        }
    }
     
     private boolean TSPR3Test3() throws BranchingWellFormednessException {
        
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.SECOND, DurationEnum.TWO_BEATS);
        
        
        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessTSPR3a(a7, a3);
        
        
        if(rating > 0.3 && rating <= 0.8)
        {
            return true;
        }
        else{
            return false;
        }
    }
     
    /**
     * Test of assessConsonance method, of class TimeSpanReductionAnalyser.
     */
    @Test
    public void testAssessConsonance() throws BranchingWellFormednessException {
        assertTrue(testConsonance1());
        assertTrue(testConsonance2());
        assertTrue(testConsonance3());
    }

    //strong consonance
    private boolean testConsonance1() throws BranchingWellFormednessException {
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 =  new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
       
        List<KeyLetterEnum> span = new ArrayList<>();
        span.add(a1.getKeyLetterEnum());
        span.add(a2.getKeyLetterEnum());
        span.add(a3.getKeyLetterEnum());
        span.add(a4.getKeyLetterEnum());
        span.add(a5.getKeyLetterEnum());
        span.add(a6.getKeyLetterEnum());
        span.add(a7.getKeyLetterEnum());
        
        List<Scale> scl = ScaleConstructor.getInstance().assessDiatonicScale(span);

        Scale localScale;

        if (scl.get(0).getTonic() == a1.getKeyLetterEnum()) {
            localScale = scl.get(0);
        } else {
            localScale = scl.get(1);
        }

        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessConsonance(a3, a7);

        if (rating == 1.0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean testConsonance2() throws BranchingWellFormednessException {
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 =  new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
       
        List<KeyLetterEnum> span = new ArrayList<>();
        span.add(a1.getKeyLetterEnum());
        span.add(a2.getKeyLetterEnum());
        span.add(a3.getKeyLetterEnum());
        span.add(a4.getKeyLetterEnum());
        span.add(a5.getKeyLetterEnum());
        span.add(a6.getKeyLetterEnum());
        span.add(a7.getKeyLetterEnum());
    
        List<Scale> scl = ScaleConstructor.getInstance().assessDiatonicScale(span);

        Scale localScale;

        if (scl.get(0).getTonic() == a1.getKeyLetterEnum()) {
            localScale = scl.get(0);
        } else {
            localScale = scl.get(1);
        }

        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessConsonance(a3,a7);

        if (rating == 0.1) {
            return true;
        } else {
            return false;
        }
    }
    
    //middling preference
     private boolean testConsonance3() throws BranchingWellFormednessException {
        
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 =  new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
       
        
        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessConsonance(a3, a7);

        if (rating == 0.4) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Test of assessTonicRelation method, of class TimeSpanReductionAnalyser.
     */
    @Test
    public void testAssessTonicRelation() 
            throws BranchingWellFormednessException {
        assertTrue(pitchRelationTest1());
        assertTrue(pitchRelationTest2());
        assertTrue(pitchRelationTest3());
        assertTrue(pitchRelationTest4());
    }

    //strong relation
    private boolean pitchRelationTest1() throws BranchingWellFormednessException {
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 =  new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
       
        List<KeyLetterEnum> span = new ArrayList<>();
        span.add(a1.getKeyLetterEnum());
        span.add(a2.getKeyLetterEnum());
        span.add(a3.getKeyLetterEnum());
        span.add(a4.getKeyLetterEnum());
        span.add(a5.getKeyLetterEnum());
        span.add(a6.getKeyLetterEnum());
        span.add(a7.getKeyLetterEnum());
      
        List<Scale> scl = ScaleConstructor.getInstance().assessDiatonicScale(span);

        Scale localScale;

        if (scl.get(0).getTonic() == a1.getKeyLetterEnum()) {
            localScale = scl.get(0);
        } else {
            localScale = scl.get(1);
        }

        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessTonicRelation(a7, localScale);

        if (rating == 1.0) {
            return true;
        } else {
            return false;
        }
    }
    
        //no relation
    private boolean pitchRelationTest2() throws BranchingWellFormednessException {
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 =  new AttackEvent(KeyLetterEnum.CS, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
       
        List<KeyLetterEnum> span = new ArrayList<>();
        span.add(a1.getKeyLetterEnum());
        span.add(a2.getKeyLetterEnum());
        span.add(a3.getKeyLetterEnum());
        span.add(a4.getKeyLetterEnum());
        span.add(a5.getKeyLetterEnum());
        span.add(a6.getKeyLetterEnum());
        span.add(a7.getKeyLetterEnum());
        

        List<Scale> scl = ScaleConstructor.getInstance().assessDiatonicScale(span);

        Scale localScale;

        if (scl.get(0).getTonic() == a1.getKeyLetterEnum()) {
            localScale = scl.get(0);
        } else {
            localScale = scl.get(1);
        }

        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessTonicRelation(a7, localScale);

        if (rating == 0.0) {
            return true;
        } else {
            return false;
        }
    }
    
        //weak relation
    private boolean pitchRelationTest3() throws BranchingWellFormednessException {
        
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 =  new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
       
        List<KeyLetterEnum> span = new ArrayList<>();
        span.add(a1.getKeyLetterEnum());
        span.add(a2.getKeyLetterEnum());
        span.add(a3.getKeyLetterEnum());
        span.add(a4.getKeyLetterEnum());
        span.add(a5.getKeyLetterEnum());
        span.add(a6.getKeyLetterEnum());
        span.add(a7.getKeyLetterEnum());
        
        List<Scale> scl = ScaleConstructor.getInstance().assessDiatonicScale(span);

        Scale localScale;

        if (scl.get(0).getTonic() == a1.getKeyLetterEnum()) {
            localScale = scl.get(0);
        } else {
            localScale = scl.get(1);
        }

        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessTonicRelation(a7, localScale);

        if (rating == 0.4) {
            return true;
        } else {
            return false;
        }
    }
        //mild relation
    private boolean pitchRelationTest4() throws BranchingWellFormednessException {
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 =  new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
       
        List<KeyLetterEnum> span = new ArrayList<>();
        span.add(a1.getKeyLetterEnum());
        span.add(a2.getKeyLetterEnum());
        span.add(a3.getKeyLetterEnum());
        span.add(a4.getKeyLetterEnum());
        span.add(a5.getKeyLetterEnum());
        span.add(a6.getKeyLetterEnum());
        span.add(a7.getKeyLetterEnum());
     

        List<Scale> scl = ScaleConstructor.getInstance().assessDiatonicScale(span);

        Scale localScale;

        if (scl.get(0).getTonic() == a1.getKeyLetterEnum()) {
            localScale = scl.get(0);
        } else {
            localScale = scl.get(1);
        }

        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessTonicRelation(a7, localScale);

        if (rating == 0.7) {
            return true;
        } else {
            return false;
        }
    }

    
   
    /**
     * Test of assessTonicRelationBetweenEvents method, of class TimeSpanReductionAnalyser.
     */
    @Test
    public void testAssessTonicRelationBetweenEvents() throws BranchingWellFormednessException {
        assertTrue("Test 1", tonicRelationTest1());
        assertTrue("Test 2", tonicRelationTest2());
        assertTrue("Test 3", tonicRelationTest3());
    }

    
    //equal relation
    private boolean tonicRelationTest1() throws BranchingWellFormednessException {
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 =  new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
       
        List<KeyLetterEnum> span = new ArrayList<>();
        span.add(a1.getKeyLetterEnum());
        span.add(a2.getKeyLetterEnum());
        span.add(a3.getKeyLetterEnum());
        span.add(a4.getKeyLetterEnum());
        span.add(a5.getKeyLetterEnum());
        span.add(a6.getKeyLetterEnum());
        span.add(a7.getKeyLetterEnum());
        

        List<Scale> scl = ScaleConstructor.getInstance().assessDiatonicScale(span);

        Scale localScale;

        if (scl.get(0).getTonic() == a1.getKeyLetterEnum()) {
            localScale = scl.get(0);
        } else {
            localScale = scl.get(1);
        }

        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessTonicRelationBetweenEvents(a1,a7, localScale);

        if (rating == 0.5) {
            return true;
        } else {
            return false;
        }
    }
    
    //parent closer relation
    private boolean tonicRelationTest2() throws BranchingWellFormednessException {
     
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 =  new AttackEvent(KeyLetterEnum.DS, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
       
        List<KeyLetterEnum> span = new ArrayList<>();
        span.add(a1.getKeyLetterEnum());
        span.add(a2.getKeyLetterEnum());
        span.add(a3.getKeyLetterEnum());
        span.add(a4.getKeyLetterEnum());
        span.add(a5.getKeyLetterEnum());
        span.add(a6.getKeyLetterEnum());
        span.add(a7.getKeyLetterEnum());
        
        List<Scale> scl = ScaleConstructor.getInstance().assessDiatonicScale(span);

        Scale localScale;

        if (scl.get(0).getTonic() == a1.getKeyLetterEnum()) {
            localScale = scl.get(0);
        } else {
            localScale = scl.get(1);
        }

        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessTonicRelationBetweenEvents(a1,a7, localScale);

        if (rating == 1.0) {
            return true;
        } else {
            return false;
        }
    }
    
    //child closer relation
    private boolean tonicRelationTest3() throws BranchingWellFormednessException {
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.DS, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a5 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a6 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a7 =  new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
       
        List<KeyLetterEnum> span = new ArrayList<>();
        span.add(a1.getKeyLetterEnum());
        span.add(a2.getKeyLetterEnum());
        span.add(a3.getKeyLetterEnum());
        span.add(a4.getKeyLetterEnum());
        span.add(a5.getKeyLetterEnum());
        span.add(a6.getKeyLetterEnum());
        span.add(a7.getKeyLetterEnum());
        

        Scale localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR);

        

        double rating = TimeSpanReductionAnalyser.getInstance()
                .assessTonicRelationBetweenEvents(a1,a7, localScale);

        if (rating == 0.0) {
            return true;
        } else {
            return false;
        }
    }

}
