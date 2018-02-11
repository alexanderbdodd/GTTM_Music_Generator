/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GTTM_Analyser;

import Elements.*;
import GTTM_Analyser.Exceptions.ProlongationalReductionAnalysisException;
import Grammar_Elements.ExceptionClasses.BranchingWellFormednessException;
import Grammar_Elements.MetricalStructure.Beat;
import Grammar_Elements.ReductionBranches.*;
import Manipulators.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander
 */
public class ProlongationalReductionAnalyserTest {

    public ProlongationalReductionAnalyserTest() {
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
     * Test of assessPRPR3 method, of class ProlongationalReductionAnalyser.
     */
    @Test
    public void testAssessPRPR3() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        assertTrue("Strong Example", strongPRPR3CandidateTest());
        assertTrue("Weak Example", weakPRPR3CandidateTest());
    }

    private boolean strongPRPR3CandidateTest() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        Beat b1 = new Beat(2);
        Beat b2 = new Beat(1);
        Beat b3 = new Beat(3);
        Beat b4 = new Beat(3);

        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        
        b1.setPosition(0);
        b2.setPosition(1);
        b3.setPosition(2);
        b4.setPosition(3);

        ProlongationalBranch topBranch = new ProlongationalBranch(b1, 0);
        ProlongationalBranch subBranch1 = new ProlongationalBranch(b2, 1);
        ProlongationalBranch subBranch2 = new ProlongationalBranch(b3, 2);
        ProlongationalBranch subBranch3 = new ProlongationalBranch(b4, 3);

        topBranch.addChildBranch(subBranch1);
        topBranch.addChildBranch(subBranch2);
        topBranch.addChildBranch(subBranch3);
        
        Scale localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR);

        double rating = ProlongationalReductionAnalyser.getInstance().assessPRPR3(a1, a4, true, true, localScale);


        boolean rightBranching = ProlongationalReductionAnalyser.getInstance().isRightBranching(subBranch3.getAssociatedBeat(), subBranch3.getParent().getAssociatedBeat());
        double rating2 = ProlongationalReductionAnalyser.getInstance().assessPRPR3(a1, a4, rightBranching, true, ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR));

        if (rating != rating2) {
            return false;
        }

        if (rating > 0.75) {
            return true;
        } else {
            return false;
        }
    }

    private boolean weakPRPR3CandidateTest() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        Beat b1 = new Beat(2);
        Beat b2 = new Beat(1);
        Beat b3 = new Beat(3);
        Beat b4 = new Beat(3);

        AttackEvent a1 = new AttackEvent(KeyLetterEnum.CS, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.Df, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.ES, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        
        b1.setPosition(0);
        b2.setPosition(1);
        b3.setPosition(2);
        b4.setPosition(3);

        ProlongationalBranch topBranch = new ProlongationalBranch(b1, 0);
        ProlongationalBranch subBranch1 = new ProlongationalBranch(b2, 1);
        ProlongationalBranch subBranch2 = new ProlongationalBranch(b3, 2);
        ProlongationalBranch subBranch3 = new ProlongationalBranch(b4, 3);

        topBranch.addChildBranch(subBranch1);
        topBranch.addChildBranch(subBranch2);
        topBranch.addChildBranch(subBranch3);
        
        Scale localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.CS, ScaleModeEnum.MAJOR);


        double rating = ProlongationalReductionAnalyser.getInstance().assessPRPR3(a1, a4, true, false, localScale);


        if (rating <= 0.4) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Test of assessBranchingStability method, of class
     * ProlongationalReductionAnalyser.
     */
    @Test
    public void testAssessBranchingStability() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        assertTrue("Test 1", testBranching1());
        assertTrue("Test 2", testBranching2());
        assertTrue("Test 3", testBranching3());
        assertTrue("Test 4", testBranching4());
        assertTrue("Test 6", testBranching6());
        assertTrue("Test 7", testBranching7());
        assertTrue("Test 8", testBranching8());
    }

    //strong prolongation right branch 
    private boolean testBranching1()
            throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        Beat b = new Beat(1);
        AttackEvent a1 =  new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        ProlongationalBranch topBranch = new ProlongationalBranch(b, 0, ProlongationalTypeEnum.PROGRESSION);
        b.setPosition(0);
        Beat b2 = new Beat(2);
        AttackEvent a2 =  new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        ProlongationalBranch subBranch = new ProlongationalBranch(b2, 1, ProlongationalTypeEnum.STRONG_PROLONGATION);
        b2.setPosition(1);
        topBranch.addChildBranch(subBranch);

        double rating = ProlongationalReductionAnalyser.getInstance().assessBranchingStability(a2, a1, true);

        if (rating == 1) {
            return true;
        } else {
            return false;
        }

    }

    //strong prolongation left branching
    private boolean testBranching2() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        Beat b = new Beat(1);
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        ProlongationalBranch subBranch = new ProlongationalBranch(b, 1, ProlongationalTypeEnum.PROGRESSION);
        b.setPosition(0);
        Beat b2 = new Beat(2);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        ProlongationalBranch topBranch = new ProlongationalBranch(b2, 0, ProlongationalTypeEnum.STRONG_PROLONGATION);
        b2.setPosition(1);
        topBranch.addChildBranch(subBranch);

        double rating = ProlongationalReductionAnalyser.getInstance().assessBranchingStability(a1, a2, false);

        if (rating == 0.2) {
            return true;
        } else {
            return false;
        }

    }

    //test left branching progression
    private boolean testBranching3() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        Beat b = new Beat(1);
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        ProlongationalBranch subBranch = new ProlongationalBranch(b, 1, ProlongationalTypeEnum.PROGRESSION);
        b.setPosition(0);
        Beat b2 = new Beat(2);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        ProlongationalBranch topBranch = new ProlongationalBranch(b2, 0, ProlongationalTypeEnum.PROGRESSION);
        b2.setPosition(1);
        topBranch.addChildBranch(subBranch);

        double rating = ProlongationalReductionAnalyser.getInstance().assessBranchingStability(a1, a2, false);

        if (rating == 1.0) {
            return true;
        } else {
            return false;
        }

    }

    //test right branching progression stability
    private boolean testBranching4() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        Beat b = new Beat(1);
        AttackEvent a1 =  new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        ProlongationalBranch subBranch = new ProlongationalBranch(b, 1, ProlongationalTypeEnum.PROGRESSION);
        b.setPosition(1);
        Beat b2 = new Beat(2);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        ProlongationalBranch topBranch = new ProlongationalBranch(b2, 0, ProlongationalTypeEnum.PROGRESSION);
        b2.setPosition(0);
        topBranch.addChildBranch(subBranch);
        
        double rating = ProlongationalReductionAnalyser.getInstance().assessBranchingStability(a1, a2, true);

        if (rating == 0.2) {
            return true;
        } else {
            return false;
        }

    }

   

    //test weak prolongation
    private boolean testBranching6()
            throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {

        AttackEvent root = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent interval2 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent interval3 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);

        List<Key> array = new ArrayList<>();

        array.add(root);
        array.add(interval2);

        Chord chrd1 = new Chord(array);

        List<Key> array2 = new ArrayList<>();

        array2.add(root);
        array2.add(interval3);

        Chord chrd2 = new Chord(array2);

        AttackEventChord attackChord = new AttackEventChord(chrd1, DurationEnum.TWO_BEATS);
        AttackEventChord attackChord2 = new AttackEventChord(chrd2, DurationEnum.TWO_BEATS);

        Beat b1 = new Beat(1);
        Beat b2 = new Beat(2);

        b1.setPosition(0);
        b2.setPosition(1);


        ProlongationalBranch top = new ProlongationalBranch(b1, 0);
        ProlongationalBranch sub = new ProlongationalBranch(b2, 1);

        top.addChildBranch(sub);

        boolean result = false;

        double rating = ProlongationalReductionAnalyser.getInstance().assessBranchingStability(attackChord2, attackChord, true);

        if (rating == 0.5) {
            result = true;
        }

        return result;

    }

    //test strong right prolongation interval
    private boolean testBranching7() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {

        AttackEvent root = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent interval2 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent interval3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);

        List<Key> array = new ArrayList<>();

        array.add(root);
        array.add(interval2);

        Chord chrd1 = new Chord(array);

        List<Key> array2 = new ArrayList<>();

        array2.add(root);
        array2.add(interval3);

        Chord chrd2 = new Chord(array2);

        AttackEventChord attackChord = new AttackEventChord(chrd1, DurationEnum.TWO_BEATS);
        AttackEventChord attackChord2 = new AttackEventChord(chrd2, DurationEnum.TWO_BEATS);

        Beat b1 = new Beat(1);
        Beat b2 = new Beat(2);

        b1.setPosition(0);
        b2.setPosition(1);

        ProlongationalBranch top = new ProlongationalBranch(b1, 0);
        ProlongationalBranch sub = new ProlongationalBranch(b2, 1);

        top.addChildBranch(sub);

        boolean result = false;

        double rating = ProlongationalReductionAnalyser.getInstance().assessBranchingStability(attackChord2, attackChord, true);

        if (rating == 1.0) {
            result = true;
        }

        return result;

    }

    //test strong left prolongation interval
    private boolean testBranching8()
            throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {

        AttackEvent root = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent interval2 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent interval3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);

        List<Key> array = new ArrayList<>();

        array.add(root);
        array.add(interval2);

        Chord chrd1 = new Chord(array);

        List<Key> array2 = new ArrayList<>();

        array2.add(root);
        array2.add(interval3);

        Chord chrd2 = new Chord(array2);

        AttackEventChord attackChord = new AttackEventChord(chrd1, DurationEnum.TWO_BEATS);
        AttackEventChord attackChord2 = new AttackEventChord(chrd2, DurationEnum.TWO_BEATS);

        Beat b1 = new Beat(1);
        Beat b2 = new Beat(2);

        b1.setPosition(1);
        
        ProlongationalBranch top = new ProlongationalBranch(b1, 0);
        ProlongationalBranch sub = new ProlongationalBranch(b2, 1);

        top.addChildBranch(sub);

        boolean result = false;

        double rating = ProlongationalReductionAnalyser.getInstance().assessBranchingStability(attackChord2, attackChord, false);

        if (rating == 0.2) {
            result = true;
        }

        return result;

    }

    /**
     * Test of getInstance method, of class ProlongationalReductionAnalyser.
     */
    @Test
    public void testGetInstance() {
        ProlongationalReductionAnalyser instance = ProlongationalReductionAnalyser.getInstance();
        assertNotNull(instance);
        assertSame(instance, ProlongationalReductionAnalyser.getInstance());
    }

    /**
     * Test of assessMelodicStabilityA method, of class
     * ProlongationalReductionAnalyser.
     */
    @Test
    public void testAssessMelodicStabilityA() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        assertTrue("Test 1", mTest1());
        assertTrue("Test 2", mTest2());
    }

    //test small distance
    private boolean mTest1() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        ProlongationalReductionAnalyser instance
                = ProlongationalReductionAnalyser.getInstance();
        
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.B, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);


        double rating = instance.assessMelodicStabilityA(a1, a2);

        if (rating == 0.90) {
            return true;
        } else {
            return false;
        }

    }

    //test great distance
    private boolean mTest2()
            throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {

        ProlongationalReductionAnalyser instance
                = ProlongationalReductionAnalyser.getInstance();
       
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);


        double rating = instance.assessMelodicStabilityA(a1, a2);

        if (rating == 0.0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Test of assessPitchStability method, of class
     * ProlongationalReductionAnalyser.
     */
    @Test
    public void testAssessPitchStability() throws Exception {
        assertTrue(pTest1());
        assertTrue(pTest2());
    }

    private boolean pTest1() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        Beat b1 = new Beat(2);
        Beat b2 = new Beat(1);
        Beat b3 = new Beat(3);
        Beat b4 = new Beat(3);

        b1.setPosition(0);
        b2.setPosition(1);
        b3.setPosition(2);
        b4.setPosition(3);
        
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 =  new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
                
                
        ProlongationalBranch topBranch = new ProlongationalBranch(b1, 0);
        ProlongationalBranch subBranch1 = new ProlongationalBranch(b2, 1);
        ProlongationalBranch subBranch2 = new ProlongationalBranch(b3, 2);
        ProlongationalBranch subBranch3 = new ProlongationalBranch(b4, 3);

        topBranch.addChildBranch(subBranch1);
        topBranch.addChildBranch(subBranch2);
        topBranch.addChildBranch(subBranch3);
        
        Scale localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR);

        double rating = ProlongationalReductionAnalyser.getInstance().assessPitchStability(a4, localScale);

        if (rating == 1.0) {
            return true;
        } else {
            return false;
        }

    }

    private boolean pTest2() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        Beat b1 = new Beat(2);
        Beat b2 = new Beat(1);
        Beat b3 = new Beat(3);
        Beat b4 = new Beat(3);

        AttackEvent a1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a3 = new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent a4 =  new AttackEvent(KeyLetterEnum.FS, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
                        
        b1.setPosition(0);
        b2.setPosition(1);
        b3.setPosition(2);
        b4.setPosition(3);

        ProlongationalBranch topBranch = new ProlongationalBranch(b1, 0);
        ProlongationalBranch subBranch1 = new ProlongationalBranch(b2, 1);
        ProlongationalBranch subBranch2 = new ProlongationalBranch(b3, 2);
        ProlongationalBranch subBranch3 = new ProlongationalBranch(b4, 3);

        topBranch.addChildBranch(subBranch1);
        topBranch.addChildBranch(subBranch2);
        topBranch.addChildBranch(subBranch3);
        
        Scale localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR);

        double rating = ProlongationalReductionAnalyser.getInstance().assessPitchStability(a4, localScale);

        if (rating == 0.0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Test of assessMelodicStabilityB method, of class
     * ProlongationalReductionAnalyser.
     */
    @Test
    public void testAssessMelodicStabilityB()
            throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {

        //strong right ascension
        assertTrue("Test 1", mBTest1());
        //no ascension or descension
        assertTrue("Test 2", mBTest2());
        //strong right descension
        assertTrue("Test 3", mBTest3());
        //strong left descension
        assertTrue("Test 4", mBTest4());
        //strong left ascension
        assertTrue("Test 5", mBTest5());

    }

    private boolean mBTest1() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        ProlongationalReductionAnalyser instance
                = ProlongationalReductionAnalyser.getInstance();
              
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.B, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);


        double rating = instance.assessMelodicStabilityB(a1, a2, true);

        if (rating == 1.0) {
            return true;
        } else {
            return false;
        }

    }

    private boolean mBTest2() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        ProlongationalReductionAnalyser instance
                = ProlongationalReductionAnalyser.getInstance();
        
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);

        
        double rating = instance.assessMelodicStabilityB(a1, a2, true);

        if (rating == 0.5) {
            return true;
        } else {
            return false;
        }

    }

    private boolean mBTest3() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        ProlongationalReductionAnalyser instance
                = ProlongationalReductionAnalyser.getInstance();
              
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);

        double rating = instance.assessMelodicStabilityB(a1, a2, true);

        if (rating == 0.0) {
            return true;
        } else {
            return false;
        }

    }

    private boolean mBTest4() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        ProlongationalReductionAnalyser instance
                = ProlongationalReductionAnalyser.getInstance();
        
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);

        

        double rating = instance.assessMelodicStabilityB(a1, a2, false);

        if (rating == 1.0) {
            return true;
        } else {
            return false;
        }

    }

    private boolean mBTest5() throws BranchingWellFormednessException, ProlongationalReductionAnalysisException {
        ProlongationalReductionAnalyser instance
                = ProlongationalReductionAnalyser.getInstance();
                
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.B, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);


        double rating = instance.assessMelodicStabilityB(a1,a2, false);

        if (rating == 0.0) {
            return true;
        } else {
            return false;
        }

    }


    /**
     * Test of assessProlongationalType method, of class
     * ProlongationalReductionAnalyser.
     */
    @Test
    public void testAssessProlongationalType()
            throws BranchingWellFormednessException {

        ProlongationalReductionAnalyser instance
                = ProlongationalReductionAnalyser.getInstance();

        assertEquals(ProlongationalTypeEnum.STRONG_PROLONGATION,
                getStrongProlongation());
        assertEquals(ProlongationalTypeEnum.STRONG_PROLONGATION,
                getStrongProlongationInterval());
        assertEquals(ProlongationalTypeEnum.WEAK_PROLONGATION,
                getWeakProlongationInterval());
        assertEquals(ProlongationalTypeEnum.PROGRESSION,
                getProgression());
        assertEquals(ProlongationalTypeEnum.PROGRESSION,
                getProgressionInterval());

    }

    private ProlongationalTypeEnum getStrongProlongation() throws BranchingWellFormednessException {
        Beat b1 = new Beat(1);
        Beat b2 = new Beat(1);

        b1.setPosition(0);
        b2.setPosition(1);
        
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);

        ProlongationalBranch top = new ProlongationalBranch(b1, 0);
        ProlongationalBranch sub = new ProlongationalBranch(b2, 1);

        top.addChildBranch(sub);

        return ProlongationalReductionAnalyser.getInstance()
                .assessProlongationalType(a1, a2);
    }

    private ProlongationalTypeEnum getStrongProlongationInterval() throws BranchingWellFormednessException {
        List<Key> keys = new ArrayList<>();

        Key key1 = new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST);
        Key key2 = new Key(KeyLetterEnum.A, KeyPositionEnum.FIRST);

        keys.add(key1);
        keys.add(key2);

        Chord chrd1 = new Chord(keys);

        
        AttackEvent a1 = new AttackEventChord(chrd1, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEventChord(chrd1, DurationEnum.TWO_BEATS);

        return ProlongationalReductionAnalyser.getInstance()
                .assessProlongationalType(a1,
                        a2);
    }

    private ProlongationalTypeEnum getWeakProlongationInterval()
            throws BranchingWellFormednessException {
        List<Key> keys = new ArrayList<>();
        List<Key> keys2 = new ArrayList<>();

        Key key1 = new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST);
        Key key2 = new Key(KeyLetterEnum.A, KeyPositionEnum.FIRST);
        Key key3 = new Key(KeyLetterEnum.B, KeyPositionEnum.FIRST);

        keys.add(key1);
        keys.add(key2);

        keys2.add(key1);
        keys2.add(key3);

        Chord chrd1 = new Chord(keys);
        Chord chrd2 = new Chord(keys2);

        Beat b1 = new Beat(1);
        Beat b2 = new Beat(1);
        
         AttackEvent a1 = new AttackEventChord(chrd1, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEventChord(chrd2, DurationEnum.TWO_BEATS);

        b1.setPosition(0);
        b2.setPosition(1);

        ProlongationalBranch top = new ProlongationalBranch(b1, 0);
        ProlongationalBranch sub = new ProlongationalBranch(b2, 1);

        top.addChildBranch(sub);

        return ProlongationalReductionAnalyser.getInstance()
                .assessProlongationalType(a1, a2);
    }

    private ProlongationalTypeEnum getProgression() throws BranchingWellFormednessException {
        Beat b1 = new Beat(1);
        Beat b2 = new Beat(1);

        b1.setPosition(0);
        b2.setPosition(1);
        
        AttackEvent a1 = new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEvent(KeyLetterEnum.B, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);

        ProlongationalBranch top = new ProlongationalBranch(b1, 0);
        ProlongationalBranch sub = new ProlongationalBranch(b2, 1);

        top.addChildBranch(sub);

        return ProlongationalReductionAnalyser.getInstance()
                .assessProlongationalType(a1, a2);
    }

    private ProlongationalTypeEnum getProgressionInterval()
            throws BranchingWellFormednessException {
        List<Key> keys = new ArrayList<>();
        List<Key> keys2 = new ArrayList<>();

        Key key1 = new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST);
        Key key2 = new Key(KeyLetterEnum.A, KeyPositionEnum.FIRST);
        Key key3 = new Key(KeyLetterEnum.B, KeyPositionEnum.FIRST);
        Key key4 = new Key(KeyLetterEnum.C, KeyPositionEnum.FIFTH);

        keys.add(key1);
        keys.add(key2);

        keys2.add(key4);
        keys2.add(key3);

        Chord chrd1 = new Chord(keys);
        Chord chrd2 = new Chord(keys2);

        Beat b1 = new Beat(1);
        Beat b2 = new Beat(1);
        
        AttackEvent a1 = new AttackEventChord(chrd1, DurationEnum.TWO_BEATS);
        AttackEvent a2 = new AttackEventChord(chrd2, DurationEnum.TWO_BEATS);

        b1.setPosition(0);
        b2.setPosition(1);

        ProlongationalBranch top = new ProlongationalBranch(b1, 0);
        ProlongationalBranch sub = new ProlongationalBranch(b2, 1);

        top.addChildBranch(sub);

        return ProlongationalReductionAnalyser.getInstance()
                .assessProlongationalType(a1, a2);
    }



    private boolean testStrongHarmonicA1() {
        double rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationA(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS),
                        new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS));

        if (rating != 1) {
            return false;
        }

        rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationA(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS),
                        new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS));

        if (rating == 1) {
            return true;
        } else {
            return false;
        }

    }

    private boolean testStrongHarmonicA2() {
        double rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationA(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS),
                        new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS));

        if (rating != 0.85) {
            return false;
        }

        rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationA(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS),
                        new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS));

        if (rating == 0.85) {
            return true;
        } else {
            return false;
        }

    }

    private boolean testStrongHarmonicA3() {
        
         double rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationA(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS),
                        new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS));

        if (rating != 0.85) {
            return false;
        }

        rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationA(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS),
                        new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS));

        if (rating == 0.85) {
            return true;
        } else {
            return false;
        }
    }
    
      private boolean testMiddleHarmonicA() {
        
         double rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationA(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS),
                        new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS));

        if (rating < 0.5 || rating > 0.8) {
            return false;
        }

        rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationA(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS),
                        new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS));

        if (rating > 0.4 && rating < 0.7) {
            return true;
        } else {
            return false;
        }
    }
      
      private boolean testWeakHarmonicA()
      {
            double rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationA(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS),
                        new AttackEvent(KeyLetterEnum.Gf, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS));

        if (rating > 0.1) {
            return false;
        }

        rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationA(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS),
                        new AttackEvent(KeyLetterEnum.Gf, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS));

        if (rating <= 0.1) {
            return true;
        } else {
            return false;
        }
      }

    /**
     * Test of assessHarmonicRelationA method, of class
     * ProlongationalReductionAnalyser.
     */
    @Test
    public void testAssessHarmonicRelationA() {
        //strong cases
        assertTrue("Strong Test 1", testStrongHarmonicA1());
        assertTrue("Strong Test 2", testStrongHarmonicA2());
        assertTrue("Strong Test 3", testStrongHarmonicA3());        
        //middling case
        assertTrue("Middling Test", testMiddleHarmonicA());
        //weak case
        assertTrue("Weak Test", testWeakHarmonicA());
    }
    
    private boolean testStrongHarmonicB1()
    {
        AttackEvent parent = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        AttackEvent child = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        
        double rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationB(parent, child, true);
        
        if(rating == 1.0)
        {
            return true;
        }
        else{
            return false;
        }
    }
    
    private boolean testStrongHarmonicB2()
    {
        AttackEvent parent = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        AttackEvent child = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        
        double rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationB(parent, child, true);
        
        if(rating == 0.95)
        {
            return true;
        }
        else{
            return false;
        }
    }
    
    private boolean testStrongHarmonicB3()
    {
        AttackEvent parent = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        AttackEvent child = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        
        double rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationB(parent, child, false);
        
        if(rating == 1.0)
        {
            return true;
        }
        else{
            return false;
        }
    }
    
    private boolean testStrongHarmonicB4()
    {
        AttackEvent parent = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        AttackEvent child = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.SECOND, DurationEnum.TWO_BEATS);
        
        double rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationB(parent, child, false);
        
        if(rating == 0.95)
        {
            return true;
        }
        else{
            return false;
        }
    }
    
    private boolean testMiddleHarmonicB()
    {
        AttackEvent parent = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        AttackEvent child = new AttackEvent(KeyLetterEnum.Df, KeyPositionEnum.SEVENTH, DurationEnum.TWO_BEATS);
        
        double rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationB(parent, child, true);
        
        if(rating > 0.4 && rating < 0.7)
        {
            return true;
        }
        else{
            return false;
        }
    }
    
    
    private boolean testWeakHarmonicB1()
    {
        AttackEvent parent = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        AttackEvent child = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        
        double rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationB(parent, child, false);
        
        if(rating == 0)
        {
            return true;
        }
        else{
            return false;
        }
    }
    
    
    private boolean testWeakHarmonicB2()
    {
        AttackEvent parent = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        AttackEvent child = new AttackEvent(KeyLetterEnum.F, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        
        double rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationB(parent, child, true);
        
        if(rating == 0.0)
        {
            return true;
        }
        else{
            return false;
        }
    }
    
    
    private boolean testWeakHarmonicB3()
    {
        AttackEvent parent = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        AttackEvent child = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        
        double rating = ProlongationalReductionAnalyser.getInstance()
                .assessHarmonicRelationB(parent, child, true);
        
        if(rating == 0.0)
        {
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Test of assessHarmonicRelationB method, of class
     * ProlongationalReductionAnalyser.
     */
    @Test
    public void testAssessHarmonicRelationB() {
        assertTrue("Strong Test 1", testStrongHarmonicB1());
        assertTrue("Strong Test 2", testStrongHarmonicB2());
        assertTrue("Strong Test 3", testStrongHarmonicB3());
        assertTrue("Weak Test 1", testWeakHarmonicB1());
        assertTrue("Weak Test 2",testWeakHarmonicB2());
        assertTrue("Weak Test 3", testWeakHarmonicB3());
        assertTrue("Middle example", testMiddleHarmonicB());
    }
}
