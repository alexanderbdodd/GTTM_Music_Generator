/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grammar_Elements;

import Elements.DurationEnum;
import Grammar_Elements.ExceptionClasses.BranchingWellFormednessException;
import Grammar_Elements.ExceptionClasses.GroupingWellFormednessException;
import Grammar_Elements.ExceptionClasses.MetricalWellFormednessException;
import Grammar_Elements.GroupingStructure.BaseGroup;
import Grammar_Elements.GroupingStructure.Group;
import Grammar_Elements.GroupingStructure.HighLevelGroup;
import Grammar_Elements.GroupingStructure.ProlongationalBaseGroup;
import Grammar_Elements.GroupingStructure.ProlongationalGroup;
import Grammar_Elements.MetricalStructure.Beat;
import Grammar_Elements.MetricalStructure.MetricalContainer;
import Grammar_Elements.ReductionBranches.ProlongationalBranch;
import Grammar_Elements.ReductionBranches.ProlongationalTypeEnum;
import Grammar_Elements.ReductionBranches.TimeSpanReductionBranch;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class GrammarContainerTest {

    public GrammarContainerTest() {
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

    @Test
    public void testConstructor() {
        
        assertTrue(ctest1());
        assertTrue(ctest2());
        
    }
    
    //tests setup with wellformed grammar
    private boolean ctest1()
    {
        Beat beat1 = new Beat(1);
        Beat beat2 = new Beat(2);
        Beat beat3 = new Beat(1);
        Beat beat4 = new Beat(3);
        Beat beat5 = new Beat(1);
        Beat beat6 = new Beat(2);
        Beat beat7 = new Beat(1);
        Beat beat8 = new Beat(3);
        Beat beat9 = new Beat(1);
        Beat beat10 = new Beat(2);
        Beat beat11 = new Beat(1);
        Beat beat12 = new Beat(3);

        MetricalContainer mContainer = new MetricalContainer();

        try {
            mContainer.addBeat(beat1);
            mContainer.addBeat(beat2);
            mContainer.addBeat(beat3);
            mContainer.addBeat(beat4);
            mContainer.addBeat(beat5);
            mContainer.addBeat(beat6);
            mContainer.addBeat(beat7);
            mContainer.addBeat(beat8);
            mContainer.addBeat(beat9);
            mContainer.addBeat(beat10);
            mContainer.addBeat(beat11);
            mContainer.addBeat(beat12);
        } catch (MetricalWellFormednessException e) {
            fail("Metrical Container construction failed");
        }

        List<Beat> span1 = new ArrayList<>();
        span1.add(beat1);
        span1.add(beat2);
        span1.add(beat3);
        span1.add(beat4);

        List<Beat> span2 = new ArrayList<>();
        span2.add(beat5);
        span2.add(beat6);
        span2.add(beat7);
        span2.add(beat8);

        List<Beat> span3 = new ArrayList<>();
        span3.add(beat9);
        span3.add(beat10);
        span3.add(beat11);
        span3.add(beat12);

        Group b1 = null;
        Group b2 = null;
        Group b3 = null;

        try {
            b1 = new BaseGroup(span1);
            b2 = new BaseGroup(span2);
            b3 = new BaseGroup(span3);
        } catch (GroupingWellFormednessException e) {
            fail("Failure constructing base group spans");
        }

        Group h1;
        HighLevelGroup h2 = null;

        List<Group> sub = new ArrayList<>();
        sub.add(b1);
        sub.add(b2);

        try {
            h1 = new HighLevelGroup(sub);
            List<Group> sub2 = new ArrayList<>();
            sub2.add(h1);
            sub2.add(b3);
            h2 = new HighLevelGroup(sub2);

        } catch (GroupingWellFormednessException e) {
            fail("Failure constructing high level group");
        }

        ProlongationalBranch pbranch = null;
        ProlongationalBranch pbranch2;
        ProlongationalBranch pbranch3;
        ProlongationalBranch pbranch4;

        try {
            pbranch = new ProlongationalBranch(beat12, 0, ProlongationalTypeEnum.PROGRESSION);
            pbranch2 = new ProlongationalBranch(beat1, 1, ProlongationalTypeEnum.PROGRESSION);
            pbranch3 = new ProlongationalBranch(beat4, 2, ProlongationalTypeEnum.PROGRESSION);
            pbranch4 = new ProlongationalBranch(beat7, 2, ProlongationalTypeEnum.PROGRESSION);

            pbranch.addChildBranch(pbranch2);
            pbranch.addChildBranch(pbranch4);

            pbranch2.addChildBranch(pbranch3);

        } catch (BranchingWellFormednessException ex) {
            fail("Failure constructing prolongational branch");
        }

        TimeSpanReductionBranch tbranch = null;
        TimeSpanReductionBranch tbranch2;
        TimeSpanReductionBranch tbranch3;
        TimeSpanReductionBranch tbranch4;

        try {
            tbranch = new TimeSpanReductionBranch(beat12, 0);
            tbranch2 = new TimeSpanReductionBranch(beat1, 1);
            tbranch3 = new TimeSpanReductionBranch(beat4, 2);
            tbranch4 = new TimeSpanReductionBranch(beat7, 2);

            tbranch.addChildBranch(tbranch2);
            tbranch.addChildBranch(tbranch4);

            tbranch2.addChildBranch(tbranch3);

        } catch (BranchingWellFormednessException ex) {
            fail("Failure constructing time span branch");
        }

        List<Beat> beats = new ArrayList<>();

        beats.add(beat1);
        beats.add(beat2);
        beats.add(beat3);
        beats.add(beat4);
        beats.add(beat5);
        beats.add(beat6);
        beats.add(beat7);
        beats.add(beat8);
        beats.add(beat9);
        beats.add(beat10);
        beats.add(beat11);
        beats.add(beat12);

        Group pbase = null;

        try {
            pbase = new ProlongationalBaseGroup(beats);
        } catch (Exception e) {
            fail("BaseGroup construction error 2");
        }
        List<Group> pgroups = new ArrayList<>();
        pgroups.add(pbase);

        ProlongationalGroup pgroup = new ProlongationalGroup(pgroups);

        try {
            GrammarContainer container = new GrammarContainer(pbranch, tbranch, mContainer, h2, pgroup);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //test failure for crossing branches
    private boolean ctest2()
    {
        Beat beat1 = new Beat(1);
        Beat beat2 = new Beat(2);
        Beat beat3 = new Beat(1);
        Beat beat4 = new Beat(3);
        Beat beat5 = new Beat(1);
        Beat beat6 = new Beat(2);
        Beat beat7 = new Beat(1);
        Beat beat8 = new Beat(3);
        Beat beat9 = new Beat(1);
        Beat beat10 = new Beat(2);
        Beat beat11 = new Beat(1);
        Beat beat12 = new Beat(3);

        MetricalContainer mContainer = new MetricalContainer();

        try {
            mContainer.addBeat(beat1);
            mContainer.addBeat(beat2);
            mContainer.addBeat(beat3);
            mContainer.addBeat(beat4);
            mContainer.addBeat(beat5);
            mContainer.addBeat(beat6);
            mContainer.addBeat(beat7);
            mContainer.addBeat(beat8);
            mContainer.addBeat(beat9);
            mContainer.addBeat(beat10);
            mContainer.addBeat(beat11);
            mContainer.addBeat(beat12);
        } catch (MetricalWellFormednessException e) {
            fail("Metrical Container construction failed");
        }

        List<Beat> span1 = new ArrayList<>();
        span1.add(beat1);
        span1.add(beat2);
        span1.add(beat3);
        span1.add(beat4);

        List<Beat> span2 = new ArrayList<>();
        span2.add(beat5);
        span2.add(beat6);
        span2.add(beat7);
        span2.add(beat8);

        List<Beat> span3 = new ArrayList<>();
        span3.add(beat9);
        span3.add(beat10);
        span3.add(beat11);
        span3.add(beat12);

        Group b1 = null;
        Group b2 = null;
        Group b3 = null;

        try {
            b1 = new BaseGroup(span1);
            b2 = new BaseGroup(span2);
            b3 = new BaseGroup(span3);
        } catch (GroupingWellFormednessException e) {
            fail("Failure constructing base group spans");
        }

        Group h1;
        HighLevelGroup h2 = null;

        List<Group> sub = new ArrayList<>();
        sub.add(b1);
        sub.add(b2);

        try {
            h1 = new HighLevelGroup(sub);
            List<Group> sub2 = new ArrayList<>();
            sub2.add(h1);
            sub2.add(b3);
            h2 = new HighLevelGroup(sub2);

        } catch (GroupingWellFormednessException e) {
            fail("Failure constructing high level group");
        }

        ProlongationalBranch pbranch = null;
        ProlongationalBranch pbranch2;
        ProlongationalBranch pbranch3;
        ProlongationalBranch pbranch4;

        try {
            pbranch = new ProlongationalBranch(beat12, 0, ProlongationalTypeEnum.PROGRESSION);
            pbranch2 = new ProlongationalBranch(beat1, 1, ProlongationalTypeEnum.PROGRESSION);
            pbranch3 = new ProlongationalBranch(beat4, 2, ProlongationalTypeEnum.PROGRESSION);
            pbranch4 = new ProlongationalBranch(beat7, 2, ProlongationalTypeEnum.PROGRESSION);

            pbranch.addChildBranch(pbranch2);
            pbranch.addChildBranch(pbranch4);

            pbranch2.addChildBranch(pbranch3);

        } catch (BranchingWellFormednessException ex) {
            fail("Failure constructing prolongational branch");
        }

        TimeSpanReductionBranch tbranch = null;
        TimeSpanReductionBranch tbranch2;
        TimeSpanReductionBranch tbranch3;
        TimeSpanReductionBranch tbranch4;

        try {
            tbranch = new TimeSpanReductionBranch(beat12, 0);
            tbranch2 = new TimeSpanReductionBranch(beat1, 1);
            tbranch3 = new TimeSpanReductionBranch(beat7, 2);
            tbranch4 = new TimeSpanReductionBranch(beat4, 2);

            tbranch.addChildBranch(tbranch2);
            tbranch.addChildBranch(tbranch4);

            tbranch2.addChildBranch(tbranch3);

        } catch (BranchingWellFormednessException ex) {
            fail("Failure constructing time span branch");
        }

        List<Beat> beats = new ArrayList<>();

        beats.add(beat1);
        beats.add(beat2);
        beats.add(beat3);
        beats.add(beat4);
        beats.add(beat5);
        beats.add(beat6);
        beats.add(beat7);
        beats.add(beat8);
        beats.add(beat9);
        beats.add(beat10);
        beats.add(beat11);
        beats.add(beat12);

        Group pbase = null;

        try {
            pbase = new ProlongationalBaseGroup(beats);
        } catch (Exception e) {
            fail("BaseGroup construction error 2");
        }
        List<Group> pgroups = new ArrayList<>();
        pgroups.add(pbase);

        ProlongationalGroup pgroup = new ProlongationalGroup(pgroups);

        try {
            GrammarContainer container = new GrammarContainer(pbranch, tbranch, mContainer, h2, pgroup);
            return false;
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * Test of getTopProlongationalReductionBranch method, of class
     * GrammarContainer.
     */
    @Test
    public void testGetTopProlongationalReductionBranch() {
       
    }

    /**
     * Test of getTopTimeSpanReductionBranch method, of class GrammarContainer.
     */
    @Test
    public void testGetTopTimeSpanReductionBranch() {
   
    }

    /**
     * Test of getMetricalStructure method, of class GrammarContainer.
     */
    @Test
    public void testGetMetricalStructure() {
       
    }

    /**
     * Test of getTopLevelGroup method, of class GrammarContainer.
     */
    @Test
    public void testGetTopLevelGroup() {
     
    }

    /**
     * Test of getTopProlongationalGroup method, of class GrammarContainer.
     */
    @Test
    public void testGetTopProlongationalGroup() {
    
    }

}
