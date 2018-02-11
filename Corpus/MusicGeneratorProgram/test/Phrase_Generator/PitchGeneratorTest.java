/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Phrase_Generator;

import Elements.KeyLetterEnum;
import Elements.AttackEvent;
import Elements.Key;
import Elements.Scale;
import Elements.KeyPositionEnum;
import Elements.DurationEnum;
import Phrase_Generator.PitchGenerator;
import Grammar_Elements.ExceptionClasses.GroupingWellFormednessException;
import Grammar_Elements.GrammarContainer;
import Grammar_Elements.GroupingStructure.*;
import Grammar_Elements.MetricalStructure.Beat;
import Grammar_Elements.MetricalStructure.MetricalContainer;
import Grammar_Elements.ReductionBranches.*;
import Manipulators.ScaleConstructor;
import Manipulators.ScaleModeEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander
 */
public class PitchGeneratorTest {

    public PitchGeneratorTest() {
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
     * Test of filterByGPR3a method, of class PitchGenerator.
     */
    @Test
    public void filterByGPR3atest() {
        assertTrue("Strong Test", strongGPR3aTest1());
        assertTrue("Weak Test", weakGPR3aTest());
        assertTrue("Strong Test 2", strongGPR3aTest2());
    }

    private boolean weakGPR3aTest() {

        Group g1 = createGroup();
        Group g2 = createGroup();
        Group g3 = createGroup();

        Beat firstBeat = g1.getMetricalBeatSpan().get(2);

        Map<Beat, Key> chain = new HashMap<>();

        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));

        firstBeat = firstBeat.getNextBeat();
        //firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));

        firstBeat = firstBeat.getNextBeat();
        //firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));

        List<Group> groups = new ArrayList<>();
        groups.add(g1);
        groups.add(g2);
        groups.add(g3);

        PitchGenerator.TestProbe probe = PitchGenerator.getInstance().new TestProbe();

        return !probe.filterByGPR3a(chain, groups);

    }

    private boolean strongGPR3aTest1() {

        Group g1 = createGroup();
        Group g2 = createGroup();
        Group g3 = createGroup();

        Beat firstBeat = g1.getMetricalBeatSpan().get(2);

        Map<Beat, Key> chain = new HashMap<>();

        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));

        firstBeat = firstBeat.getNextBeat();
        //firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));

        firstBeat = firstBeat.getNextBeat();
        //firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND));

        List<Group> groups = new ArrayList<>();
        groups.add(g1);
        groups.add(g2);
        groups.add(g3);

        PitchGenerator.TestProbe probe = PitchGenerator.getInstance().new TestProbe();

        return probe.filterByGPR3a(chain, groups);
    }

    private boolean strongGPR3aTest2() {

        Group g1 = createGroup();
        Group g2 = createGroup();
        Group g3 = createGroup();

        Beat firstBeat = g1.getMetricalBeatSpan().get(2);

        Map<Beat, Key> chain = new HashMap<>();

        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND));

        firstBeat = firstBeat.getNextBeat();
        //firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND));

        firstBeat = firstBeat.getNextBeat();
        //firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST));
        firstBeat = firstBeat.getNextBeat();
        chain.put(firstBeat, new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND));

        List<Group> groups = new ArrayList<>();
        groups.add(g1);
        groups.add(g2);
        groups.add(g3);

        PitchGenerator.TestProbe probe = PitchGenerator.getInstance().new TestProbe();

        return probe.filterByGPR3a(chain, groups);
    }

    private Group createGroup() throws GroupingWellFormednessException {
        Beat b1 = getFirstFourBeat();

        List<Beat> beats = new ArrayList<>();
        beats.add(b1);

        while (b1.getNextBeat() != null) {
            b1 = b1.getNextBeat();
            beats.add(b1);

        }

        BaseGroup b = new BaseGroup(beats);

        return b;
    }

    private Beat getFirstFourBeat() {
        Beat b1 = new Beat(3);
        Beat b2 = new Beat(1);
        Beat b3 = new Beat(2);
        Beat b4 = new Beat(1);

        return b1;
    }

    @Test
    public void testCadenceRelation() {
        //strong example
        assertTrue("Strong Test 1", strongCadence1());
        assertTrue("Strong Test 2", strongCadence2());
        //weak example
        assertTrue("Weak Test", weakCadence());
    }

    private boolean strongCadence1() {
        //dominant - tonic cadence
        PitchGenerator.TestProbe probe = PitchGenerator.getInstance().new TestProbe();

        Scale localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR);

        AttackEvent parent = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.SECOND, DurationEnum.TWO_BEATS);
        AttackEvent child = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);

        return probe.assessCadenceRelation(parent, child, localScale);
    }

    private boolean strongCadence2() {
        PitchGenerator.TestProbe probe = PitchGenerator.getInstance().new TestProbe();

        Scale localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR);

        AttackEvent parent = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.SECOND, DurationEnum.TWO_BEATS);
        AttackEvent child = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);

        return probe.assessCadenceRelation(child, parent, localScale);
    }

    private boolean weakCadence() {
        PitchGenerator.TestProbe probe = PitchGenerator.getInstance().new TestProbe();

        Scale localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR);

        AttackEvent parent = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.SECOND, DurationEnum.TWO_BEATS);
        AttackEvent child = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);

        return !probe.assessCadenceRelation(parent, child, localScale);
    }

    @Test
    public void testFindLowestBranchLevel() {
        Beat.breakChain();

        ProlongationalBranch origBranch;
        ProlongationalBranch topBranch = new ProlongationalBranch(new Beat(1), 0);
        origBranch = topBranch;

        ProlongationalBranch subBranch = new ProlongationalBranch(new Beat(1), 1);
        topBranch.addChildBranch(subBranch);
        topBranch = subBranch;

        subBranch = new ProlongationalBranch(new Beat(1), 2);
        topBranch.addChildBranch(subBranch);
        topBranch = subBranch;

        subBranch = new ProlongationalBranch(new Beat(1), 3);
        topBranch.addChildBranch(subBranch);
        topBranch = subBranch;

        PitchGenerator.TestProbe probe = PitchGenerator.getInstance().new TestProbe();

        assertEquals(3, probe.findLowestBranchLevel(origBranch));

    }

    @Test
    public void testAssessTimeSpanPreference() {
        assertTrue("Strong Test 1", strongTimeSpanPreference1());
        assertTrue("Weak Test 1", weakTimeSpanPreference());
    }

    private boolean weakTimeSpanPreference() {
        Map<Beat, Key> chain = new HashMap<>();

        Scale localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR);

        Beat.breakChain();

        Beat b1 = new Beat(1);
        Beat b2 = new Beat(3);
        Beat b3 = new Beat(1);
        Beat b4 = new Beat(2);

        CadencedTimeSpanReductionBranch topBranch = new CadencedTimeSpanReductionBranch(b4, b3, 0);
        TimeSpanReductionBranch subBranch = new TimeSpanReductionBranch(b1, 1);
        topBranch.addChildBranch(subBranch);
        TimeSpanReductionBranch subBranch2 = new TimeSpanReductionBranch(b2, 2);
        subBranch.addChildBranch(subBranch2);

        chain.put(b4, new Key(KeyLetterEnum.C, KeyPositionEnum.THIRD));
        chain.put(b3, new Key(KeyLetterEnum.G, KeyPositionEnum.THIRD));

        chain.put(b1, new Key(KeyLetterEnum.A, KeyPositionEnum.THIRD));
        chain.put(b2, new Key(KeyLetterEnum.C, KeyPositionEnum.SECOND));

        ProlongationalBranch topPBranch = new ProlongationalBranch(b4, 0);

        MetricalContainer beats = new MetricalContainer();
        beats.addBeat(b1);
        beats.addBeat(b2);
        beats.addBeat(b3);
        beats.addBeat(b4);

        List<Beat> g1 = new ArrayList<>();
        g1.add(b1);
        g1.add(b2);

        List<Beat> g2 = new ArrayList<>();
        g2.add(b3);
        g2.add(b4);

        BaseGroup bg = new BaseGroup(g1);
        BaseGroup bg2 = new BaseGroup(g2);

        List<Group> groups = new ArrayList<>();
        groups.add(bg);
        groups.add(bg2);

        HighLevelGroup hg = new HighLevelGroup(groups);

        ProlongationalGroup pGroup = new ProlongationalGroup(groups);

        GrammarContainer grammar = new GrammarContainer(topPBranch, topBranch, beats, hg, pGroup);

        PitchGenerator.TestProbe probe = PitchGenerator.getInstance().new TestProbe();

        return !probe.assessTimeSpanPreference(chain, localScale, grammar);
    }

    private boolean strongTimeSpanPreference1() {
        Map<Beat, Key> chain = new HashMap<>();

        Scale localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR);

        Beat.breakChain();

        Beat b1 = new Beat(1);
        Beat b2 = new Beat(3);
        Beat b3 = new Beat(1);
        Beat b4 = new Beat(2);

        CadencedTimeSpanReductionBranch topBranch = new CadencedTimeSpanReductionBranch(b4, b3, 0);
        TimeSpanReductionBranch subBranch = new TimeSpanReductionBranch(b1, 1);
        topBranch.addChildBranch(subBranch);
        TimeSpanReductionBranch subBranch2 = new TimeSpanReductionBranch(b2, 2);
        subBranch.addChildBranch(subBranch2);

        chain.put(b4, new Key(KeyLetterEnum.C, KeyPositionEnum.THIRD));
        chain.put(b3, new Key(KeyLetterEnum.G, KeyPositionEnum.THIRD));

        chain.put(b1, new Key(KeyLetterEnum.F, KeyPositionEnum.SECOND));
        chain.put(b2, new Key(KeyLetterEnum.Af, KeyPositionEnum.FIRST));

        ProlongationalBranch topPBranch = new ProlongationalBranch(b4, 0);

        MetricalContainer beats = new MetricalContainer();
        beats.addBeat(b1);
        beats.addBeat(b2);
        beats.addBeat(b3);
        beats.addBeat(b4);

        List<Beat> g1 = new ArrayList<>();
        g1.add(b1);
        g1.add(b2);

        List<Beat> g2 = new ArrayList<>();
        g2.add(b3);
        g2.add(b4);

        BaseGroup bg = new BaseGroup(g1);
        BaseGroup bg2 = new BaseGroup(g2);

        List<Group> groups = new ArrayList<>();
        groups.add(bg);
        groups.add(bg2);

        HighLevelGroup hg = new HighLevelGroup(groups);

        ProlongationalGroup pGroup = new ProlongationalGroup(groups);

        GrammarContainer grammar = new GrammarContainer(topPBranch, topBranch, beats, hg, pGroup);

        PitchGenerator.TestProbe probe = PitchGenerator.getInstance().new TestProbe();

        return probe.assessTimeSpanPreference(chain, localScale, grammar);

    }

    @Test
    public void testAssessProlongationalCorrectness() {
        assertTrue("Incorrect Examples", incorrectProlongationalCorrectness());
        assertTrue("Correct Examples", correctProlongationalCorrectness());
    }

    private boolean incorrectProlongationalCorrectness() {
        ProlongationalBranch topBr = new ProlongationalBranch(new Beat(1), 0, ProlongationalTypeEnum.PROGRESSION);
        ProlongationalBranch subBr = new ProlongationalBranch(new Beat(2), 1, ProlongationalTypeEnum.STRONG_PROLONGATION);

        topBr.addChildBranch(subBr);

        Map<Beat, Key> chain = new HashMap<>();

        chain.put(topBr.getAssociatedBeat(), new Key(KeyLetterEnum.A, KeyPositionEnum.THIRD));
        chain.put(subBr.getAssociatedBeat(), new Key(KeyLetterEnum.AS, KeyPositionEnum.FOURTH));

        List<ProlongationalBranch> branches = new ArrayList<>();
        
        branches.add(subBr);

        PitchGenerator.TestProbe probe = PitchGenerator.getInstance().new TestProbe();

        if (probe.assessProlongationalCorrectness(branches, chain)) {
            return false;
        }

        chain.put(subBr.getAssociatedBeat(), new Key(KeyLetterEnum.A, KeyPositionEnum.FOURTH));

        if (probe.assessProlongationalCorrectness(branches, chain)) {
            return false;
        }
        
        topBr = new ProlongationalBranch(new Beat(1), 0, ProlongationalTypeEnum.PROGRESSION);
         subBr = new ProlongationalBranch(new Beat(2), 1, ProlongationalTypeEnum.PROGRESSION);

        topBr.addChildBranch(subBr);
        chain = new HashMap<>();

        chain.put(topBr.getAssociatedBeat(), new Key(KeyLetterEnum.A, KeyPositionEnum.THIRD));
        chain.put(subBr.getAssociatedBeat(), new Key(KeyLetterEnum.A, KeyPositionEnum.THIRD));

       branches = new ArrayList<>();
        
        branches.add(subBr);
        
         if (probe.assessProlongationalCorrectness(branches, chain)) {
            return false;
        }

        return true;
    }

    private boolean correctProlongationalCorrectness() {
        ProlongationalBranch topBr = new ProlongationalBranch(new Beat(1), 0, ProlongationalTypeEnum.PROGRESSION);
        ProlongationalBranch subBr = new ProlongationalBranch(new Beat(2), 1, ProlongationalTypeEnum.STRONG_PROLONGATION);

        topBr.addChildBranch(subBr);

        Map<Beat, Key> chain = new HashMap<>();

        chain.put(topBr.getAssociatedBeat(), new Key(KeyLetterEnum.A, KeyPositionEnum.THIRD));
        chain.put(subBr.getAssociatedBeat(), new Key(KeyLetterEnum.A, KeyPositionEnum.THIRD));

        List<ProlongationalBranch> branches = new ArrayList<>();
        
        branches.add(subBr);

        PitchGenerator.TestProbe probe = PitchGenerator.getInstance().new TestProbe();

        if (!probe.assessProlongationalCorrectness(branches, chain)) {
            return false;
        }

        topBr = new ProlongationalBranch(new Beat(1), 0, ProlongationalTypeEnum.PROGRESSION);
        subBr = new ProlongationalBranch(new Beat(2), 1, ProlongationalTypeEnum.PROGRESSION);

        topBr.addChildBranch(subBr);

        chain = new HashMap<>();
        
        branches = new ArrayList<>();
        
        branches.add(subBr);

        chain.put(topBr.getAssociatedBeat(), new Key(KeyLetterEnum.A, KeyPositionEnum.THIRD));
        chain.put(subBr.getAssociatedBeat(), new Key(KeyLetterEnum.C, KeyPositionEnum.THIRD));

        if (!probe.assessProlongationalCorrectness(branches, chain)) {
            return false;
        }

        return true;
    }

}
