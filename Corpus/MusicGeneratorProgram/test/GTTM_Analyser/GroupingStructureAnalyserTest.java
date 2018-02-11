/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GTTM_Analyser;

import Elements.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander
 */
public class GroupingStructureAnalyserTest {

    public GroupingStructureAnalyserTest() {
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
     * Test of getInstance method, of class GroupingStructureAnalyser.
     */
    @Test
    public void testGetInstance() {
        GroupingStructureAnalyser instance = GroupingStructureAnalyser.getInstance();
        assertNotNull(instance);
        assertSame(GroupingStructureAnalyser.getInstance(), instance);
    }

    /**
     * Test of applyGPR2 method, of class GroupingStructureAnalyser.
     */
    @Test
    public void testAssessGPR2() throws Exception {
        GroupingStructureAnalyser instance = GroupingStructureAnalyser.getInstance();

        double rating = instance.assessGPR2(getEventsWithStrongGPR2());

        //strong preference test
        if (rating == 1) {
            assertTrue(true);
        } else {
            fail();
        }

        List<Event> events = getEvenlyFormedAttackEventsWithRest();
        rating = instance.assessGPR2a(events);

        //no preference test
        if (rating == 0.5) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

        events = getEvenlyFormedAttackEvents();
        rating = instance.assessGPR2a(events);

        //no preference test
        if (rating == 0.5) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

        //weak preference test (unpreferable)
        events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FOUR_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        RestEvent rest = new RestEvent(DurationEnum.FIVE_BEATS);

        events.add(event);
        events.add(rest);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        rating = instance.assessGPR2(events);

        assertEquals(0.0, rating, 0.0);

    }

    //returns a list of attack events, with no additional events, where the second attack event
    //for GPR2b
    //should form a strong boundary candidate
    private List<Event> getWellFormedAttackEvents() {
        List<Event> events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FIVE_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);

        events.add(event);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        return events;
    }

    private List<Event> getWellFormedAttackEventsWithRest() {
        List<Event> events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        RestEvent rest = new RestEvent(DurationEnum.FIVE_BEATS);

        events.add(event);
        events.add(event2);
        events.add(rest);
        events.add(rest);
        events.add(rest);
        events.add(rest);
        events.add(rest);
        events.add(rest);
        events.add(event3);
        events.add(event4);

        return events;
    }

    private List<Event> getEvenlyFormedAttackEventsWithRest() {
        List<Event> events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        RestEvent rest = new RestEvent(DurationEnum.FOUR_BEATS);

        events.add(event);
        events.add(rest);
        events.add(event2);
        events.add(rest);
        events.add(event3);
        events.add(rest);
        events.add(event4);
        events.add(rest);

        return events;
    }

    private List<Event> getEvenlyFormedAttackEvents() {
        List<Event> events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        //  RestEvent rest = new RestEvent(DurationEnum.HALF);

        events.add(event);

        events.add(event2);

        events.add(event3);

        events.add(event4);

        return events;
    }

    private List<Event> getEventsWithStrongGPR2() {
        List<Event> events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FIFTH, DurationEnum.FIVE_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        RestEvent rest = new RestEvent(DurationEnum.FIVE_BEATS);

        events.add(event);
        events.add(event2);
        events.add(rest);
        events.add(rest);
        events.add(rest);
        events.add(rest);
        events.add(event3);
        events.add(event4);

        return events;
    }

    /**
     * Test of assessGPR2a method, of class GroupingStructureAnalyser.
     */
    @Test
    public void testAssessGPR2a() throws Exception {
        List<Event> events = getWellFormedAttackEventsWithRest();
        GroupingStructureAnalyser instance = GroupingStructureAnalyser.getInstance();

        double rating = instance.assessGPR2a(events);

        System.out.println(rating);

        //strong example
        if (rating == 1.0) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

        events = getEvenlyFormedAttackEventsWithRest();
        rating = instance.assessGPR2a(events);

        //middling example
        if (rating == 0.5) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

        events = getEvenlyFormedAttackEvents();
        rating = instance.assessGPR2a(events);

        //middling example
        if (rating == 0.5) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

        events = getUnevenIntervals();
        rating = instance.assessGPR2a(events);

        if (rating == 0.6) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

         //weak example
        events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.FIVE_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        RestEvent rest = new RestEvent(DurationEnum.TWO_BEATS);

        events.add(event);
        events.add(rest);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        rating = instance.assessGPR2a(events);

        //weak example
        if (rating == 0.0) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

    }

    /**
     * Test of assessGPR2b method, of class GroupingStructureAnalyser.
     */
    @Test
    public void testAssessGPR2b() throws Exception {
        List<Event> events = getWellFormedAttackEvents();
        GroupingStructureAnalyser instance = GroupingStructureAnalyser.getInstance();

        double rating = instance.assessGPR2b(events);

        System.out.println(rating);

        //only greater than 0.5 because at the moment only five_beat durations are 
        //implemented
        if (rating > 0.5) {
            assertTrue(true);
        } else {
            fail();
        }

        events = getEvenlyFormedAttackEventsWithRest();
        rating = instance.assessGPR2b(events);

        if (rating == 0.5) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

        events = getEvenlyFormedAttackEvents();
        rating = instance.assessGPR2b(events);

        if (rating == 0.5) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

        events = getUnevenIntervals();
        rating = instance.assessGPR2b(events);

        if (rating == 0.6) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

         //weak example
        events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.FIVE_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);

        events.add(event);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        rating = instance.assessGPR2b(events);
        //weak example
        if (rating == 0.0) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    /**
     * Test of assessGPR3a method, of class GroupingStructureAnalyser.
     */
    @Test
    public void testAssessGPR3a() throws Exception {
        List<Event> events = getWellFormedAttackEvents();
        GroupingStructureAnalyser instance = GroupingStructureAnalyser.getInstance();

        double rating = instance.assessGPR3a(events);

        System.out.println(rating);

        //checking strong example which is ascending.
        assertEquals(1.0, rating, 0.0);

        events = getEvenlyFormedAttackEvents();
        rating = instance.assessGPR3a(events);

        events = getEvenlyFormedAttackEvents();
        rating = instance.assessGPR3a(events);

        events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FIVE_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);

        events.add(event);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        rating = instance.assessGPR3a(events);

        //checking weak example
        assertEquals(0.0, rating, 0.0);

        events = getDescendingAttackEvent();
        rating = instance.assessGPR3a(events);

        //checking strong example which is descending.
        assertEquals(1.0, rating, 0.0);

        events = new ArrayList<>();

        event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FIVE_BEATS);
        event3 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        event4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);

        events.add(event);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        rating = instance.assessGPR3a(events);

        //checking example with no preference.
        assertEquals(0.5, rating, 0.0);

    }

    private List<Event> getDescendingAttackEvent() {
        List<Event> events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FIVE_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.THIRD, DurationEnum.TWO_BEATS);

        events.add(event);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        return events;

    }

    /**
     * Test of assessGPR3b method, of class GroupingStructureAnalyser.
     */
    @Test
    public void testAssessGPR3b() throws Exception {
        GroupingStructureAnalyser instance = GroupingStructureAnalyser.getInstance();

        double rating = instance.assessGPR3b(getDynamicVariationList());

        if (rating > 0.5) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

        rating = instance.assessGPR3b(getWellFormedAttackEvents());

        if (rating == 0) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    private List<Event> getDynamicVariationList() {
        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FIFTH, DurationEnum.FIVE_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);

        event3.setDynamic(DynamicsEnum.FF);
        event4.setDynamic(DynamicsEnum.FF);

        List<Event> events = new ArrayList<>();

        events.add(event);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        return events;
    }

    /**
     * Test of assessGPR3c method, of class GroupingStructureAnalyser.
     */
    @Test
    public void testAssessGPR3c() throws Exception {
        GroupingStructureAnalyser instance = GroupingStructureAnalyser.getInstance();

        double rating = instance.assessGPR3c(getArticulationDifference());
        //assessing strong candidacy
        if (rating == 1.0) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

        rating = instance.assessGPR3c(getWellFormedAttackEvents());
        //assessing mild candidacy
        if (rating == 0.5) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

        List<Event> ev = getWellFormedAttackEvents();
        AttackEvent aEv = (AttackEvent) ev.get(0);
        aEv.setArticulationValue(ArticulationEnum.STACCATO);

        rating = instance.assessGPR3c(ev);
        //assessing weak candidacy
        if (rating == 0.0) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

    }

    private List<Event> getArticulationDifference() {
        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FIFTH, DurationEnum.FIVE_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);

        event3.setArticulationValue(ArticulationEnum.STACCATO);
        event4.setArticulationValue(ArticulationEnum.STACCATO);

        List<Event> events = new ArrayList<>();

        events.add(event);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        return events;
    }

    /**
     * Test of assessGPR3d method, of class GroupingStructureAnalyser.
     */
    @Test
    public void testAssessGPR3d() throws Exception {

        GroupingStructureAnalyser instance = GroupingStructureAnalyser.getInstance();

        double rating = instance.assessGPR3d(getDifferentIntervalLengths());

        //strong example
        if (rating == 1.0) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

        List<Event> events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FIVE_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);

        events.add(event);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        rating = instance.assessGPR3d(getWellFormedAttackEvents());

        //weak example
        if (rating == 0.0) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

        events = new ArrayList<>();

        event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        event4 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);

        events.add(event);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        rating = instance.assessGPR3d(events);
        
        //middling example
        if (rating == 0.5) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

    }

    private List<Event> getDifferentIntervalLengths() {
        List<Event> events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.THREE_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FOURTH, DurationEnum.THREE_BEATS);

        events.add(event);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        return events;

    }

    /**
     * Test of assessGPR3 method, of class GroupingStructureAnalyser.
     */
    @Test
    public void testAssessGPR3() throws Exception {

        GroupingStructureAnalyser instance = GroupingStructureAnalyser.getInstance();

        double rating = instance.assessGPR3(getStrongGPR3());

        if (rating == 1) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }

    }

    private List<Event> getStrongGPR3() {
        List<Event> events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.THREE_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.THREE_BEATS);

        event3.setDynamic(DynamicsEnum.PP);
        event4.setDynamic(DynamicsEnum.PP);

        event3.setArticulationValue(ArticulationEnum.STACCATO);
        event4.setArticulationValue(ArticulationEnum.STACCATO);

        events.add(event);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        return events;

    }

    private List<Event> getStrongGPR3andGPR4Combo() {
        List<Event> events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.THREE_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.THREE_BEATS);
        RestEvent rest = new RestEvent(DurationEnum.FOUR_BEATS);
        RestEvent rest2 = new RestEvent(DurationEnum.ONE_BEAT);

        event3.setDynamic(DynamicsEnum.PP);
        event4.setDynamic(DynamicsEnum.PP);

        event3.setArticulationValue(ArticulationEnum.STACCATO);
        event4.setArticulationValue(ArticulationEnum.STACCATO);

        events.add(event);
        //  events.add(rest2);
        events.add(event2);
        events.add(rest);
        events.add(event3);
        events.add(event4);

        return events;
    }

    /**
     * Test of assessGPR3andGPR2Combination method, of class
     * GroupingStructureAnalyser.
     */
    @Test
    public void testAssessGPR3andGPR2Combination() throws Exception {

        GroupingStructureAnalyser instance = GroupingStructureAnalyser.getInstance();

        double rating = instance.assessGPR4(getStrongGPR3andGPR4Combo());

        if (rating > 0.5) {
            assertTrue(true);
        } else {
            fail();
        }

    }

    private List<Event> getUnevenIntervals() {
        List<Event> events = new ArrayList<>();

        AttackEvent event = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.Af, KeyPositionEnum.FIFTH, DurationEnum.TWO_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.AS, KeyPositionEnum.FOURTH, DurationEnum.TWO_BEATS);
        RestEvent rest = new RestEvent(DurationEnum.FOUR_BEATS);
        RestEvent rest2 = new RestEvent(DurationEnum.FIVE_BEATS);

        events.add(event);
        events.add(rest);
        events.add(event2);
        events.add(rest2);
        events.add(event3);
        events.add(event4);

        return events;
    }

}
