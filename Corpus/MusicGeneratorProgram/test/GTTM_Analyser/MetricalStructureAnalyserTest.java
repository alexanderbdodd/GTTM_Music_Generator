/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GTTM_Analyser;

import Elements.*;
import GTTM_Analyser.Exceptions.MetricalAnalyserException;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Used to analyse a set of contiguous beats to assess how well as given
 * AttackEvent is a candidate for landing on a strong beat.
 *
 * @author Alexander Dodd
 */
public class MetricalStructureAnalyserTest {

    public MetricalStructureAnalyserTest() {
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
     * Test of getInstance method, of class MetricalStructureAnalyser.
     */
    @Test
    public void testGetInstance() {
        MetricalStructureAnalyser instance = MetricalStructureAnalyser.getInstance();
        assertNotNull(instance);
        assertSame(MetricalStructureAnalyser.getInstance(), instance);
    }

    /**
     * Test of assessMPR5 method, of class MetricalStructureAnalyser.
     */
    @Test
    public void testAssessMPR5a() {
        MetricalStructureAnalyser instance = MetricalStructureAnalyser.getInstance();

        List<Event> events = createAttackEventList();

        AttackEvent strong = (AttackEvent) events.get(0);

        double rating = instance.assessMPR5a(events, strong);

        //strong test
        assertTrue(checkEquals(rating, 1));

        rating = instance.assessMPR5a(events, (AttackEvent) events.get(1));
        
        //mild test
        assertTrue(checkEquals(rating, 0.5));
        
        events = createAttackEventList2();
        rating = instance.assessMPR5a(events, (AttackEvent) events.get(1));
        
        //weak test
        assertTrue(checkEquals(rating, 0.0));
        
    }

    private boolean checkEquals(double rating, double value) {
        if (rating == value) {
            return true;
        } else {
            return false;
        }
    }

    private List<Event> createAttackEventList() {
        List<Event> events = new ArrayList<>();

        AttackEvent event1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FIVE_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event5 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event6 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event7 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event8 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event9 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event10 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event11 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event12 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);

        event1.setDynamic(DynamicsEnum.PP);
        event2.setDynamic(DynamicsEnum.PP);
        event3.setDynamic(DynamicsEnum.PP);
        event4.setDynamic(DynamicsEnum.PP);

        event1.setArticulationValue(ArticulationEnum.SLUR);
        event2.setArticulationValue(ArticulationEnum.SLUR);
        event3.setArticulationValue(ArticulationEnum.SLUR);
        event4.setArticulationValue(ArticulationEnum.SLUR);

        event5.setDynamic(DynamicsEnum.FF);
        event6.setDynamic(DynamicsEnum.PP);
        event7.setDynamic(DynamicsEnum.PP);
        event8.setDynamic(DynamicsEnum.MP);
        event9.setDynamic(DynamicsEnum.PP);
        event10.setDynamic(DynamicsEnum.MP);
        event11.setDynamic(DynamicsEnum.MP);
        event12.setDynamic(DynamicsEnum.MP);

        event5.setArticulationValue(ArticulationEnum.NONE);
        event6.setArticulationValue(ArticulationEnum.STACCATO);
        event7.setArticulationValue(ArticulationEnum.STACCATO);
        event8.setArticulationValue(ArticulationEnum.NONE);
        event9.setArticulationValue(ArticulationEnum.STACCATO);
        event10.setArticulationValue(ArticulationEnum.NONE);
        event11.setArticulationValue(ArticulationEnum.NONE);
        event12.setArticulationValue(ArticulationEnum.NONE);

        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);
        events.add(event5);
        events.add(event6);
        events.add(event7);
        events.add(event8);
        events.add(event9);
        events.add(event10);
        events.add(event11);
        events.add(event12);
        
        int i = 0;
        while(i < 20)
        {
            AttackEvent eventN = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
            eventN.setDynamic(DynamicsEnum.MP);
            eventN.setArticulationValue(ArticulationEnum.NONE);
            events.add(eventN);
            i++;
        }

        
        return events;
    }
    
    private List<Event> createAttackEventList2() {
        List<Event> events = new ArrayList<>();

        AttackEvent event1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FOUR_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FOUR_BEATS);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FOUR_BEATS);
        AttackEvent event5 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FOUR_BEATS);
        AttackEvent event6 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event7 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FOUR_BEATS);
        AttackEvent event8 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FOUR_BEATS);
        AttackEvent event9 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FOUR_BEATS);
        AttackEvent event10 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event11 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FOUR_BEATS);
        AttackEvent event12 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FOUR_BEATS);

        event1.setDynamic(DynamicsEnum.PP);
        event2.setDynamic(DynamicsEnum.PP);
        event3.setDynamic(DynamicsEnum.PP);
        event4.setDynamic(DynamicsEnum.PP);

        event1.setArticulationValue(ArticulationEnum.SLUR);
        event2.setArticulationValue(ArticulationEnum.SLUR);
        event3.setArticulationValue(ArticulationEnum.SLUR);
        event4.setArticulationValue(ArticulationEnum.SLUR);

        event5.setDynamic(DynamicsEnum.FF);
        event6.setDynamic(DynamicsEnum.FF);
        event7.setDynamic(DynamicsEnum.FF);
        event8.setDynamic(DynamicsEnum.FF);
        event9.setDynamic(DynamicsEnum.PP);
        event10.setDynamic(DynamicsEnum.PP);
        event11.setDynamic(DynamicsEnum.PP);
        event12.setDynamic(DynamicsEnum.PP);

        event5.setArticulationValue(ArticulationEnum.NONE);
        event6.setArticulationValue(ArticulationEnum.SLUR);
        event7.setArticulationValue(ArticulationEnum.SLUR);
        event8.setArticulationValue(ArticulationEnum.SLUR);
        event9.setArticulationValue(ArticulationEnum.SLUR);
        event10.setArticulationValue(ArticulationEnum.NONE);
        event11.setArticulationValue(ArticulationEnum.NONE);
        event12.setArticulationValue(ArticulationEnum.NONE);

        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);
        events.add(event5);
        events.add(event6);
        events.add(event7);
        events.add(event8);
        events.add(event9);
        events.add(event10);
        events.add(event11);
        events.add(event12);
        
        int i = 0;
        while(i < 20)
        {
            AttackEvent eventN = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FOUR_BEATS);
            eventN.setDynamic(DynamicsEnum.MP);
            eventN.setArticulationValue(ArticulationEnum.NONE);
            events.add(eventN);
            i++;
        }

        
        return events;
    }

    private List<Event> createAttackEventListSlur() {
        List<Event> events = new ArrayList<>();

        AttackEvent event1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.FIVE_BEATS);
        AttackEvent event2 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event3 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event4 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event5 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event6 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event7 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event8 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
        AttackEvent event9 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);

        event1.setDynamic(DynamicsEnum.PP);
        event2.setDynamic(DynamicsEnum.PP);
        event3.setDynamic(DynamicsEnum.PP);
        event4.setDynamic(DynamicsEnum.PP);

        event1.setArticulationValue(ArticulationEnum.SLUR);
        event2.setArticulationValue(ArticulationEnum.SLUR);
        event3.setArticulationValue(ArticulationEnum.SLUR);
        event4.setArticulationValue(ArticulationEnum.SLUR);

        event5.setDynamic(DynamicsEnum.MP);
        event6.setDynamic(DynamicsEnum.PP);
        event7.setDynamic(DynamicsEnum.PP);
        event8.setDynamic(DynamicsEnum.MP);
        event9.setDynamic(DynamicsEnum.PP);

        event5.setArticulationValue(ArticulationEnum.NONE);
        event6.setArticulationValue(ArticulationEnum.SLUR);
        event7.setArticulationValue(ArticulationEnum.SLUR);
        event8.setArticulationValue(ArticulationEnum.NONE);
        event9.setArticulationValue(ArticulationEnum.SLUR);

        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);
        events.add(event5);
        events.add(event6);
        events.add(event7);
        events.add(event8);
        events.add(event9);
        
          int i = 0;
        while(i < 30)
        {
            AttackEvent eventN = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
            eventN.setDynamic(DynamicsEnum.MP);
            eventN.setArticulationValue(ArticulationEnum.NONE);
            events.add(eventN);
            i++;
        }


        return events;
    }

    /**
     * Test of assessMPR5 method, of class MetricalStructureAnalyser.
     */
    @Test
    public void testAssessMPR5(){
        MetricalStructureAnalyser instance = MetricalStructureAnalyser.getInstance();

        List<Event> events = createAttackEventListSlur();

        AttackEvent strong = (AttackEvent) events.get(0);

        double rating = instance.assessMPR5(events, strong, null,0, null);
       
        //test strong preference
        assertTrue("Test 1", checkEquals(rating, 1.0));
        
        events = createAttackEventList2();
        rating = instance.assessMPR5(events, (AttackEvent) events.get(1), null,0, null);
        
        //test weak preference
        assertTrue("Test 2", checkEquals(rating, 0.0));

        
        //test no preference
        rating = instance.assessMPR5(events, (AttackEvent) events.get(0),null,0,null);
        
        assertTrue("Test 3", checkEquals(rating, 0.5));
    }

    /**
     * Test of assessMPR5b method, of class MetricalStructureAnalyser.
     */
    @Test
    public void testAssessMPR5b() throws Exception {
        MetricalStructureAnalyser instance = MetricalStructureAnalyser.getInstance();

        List<Event> events = createAttackEventList();

        AttackEvent strong = (AttackEvent) events.get(0);

        double rating = instance.assessMPR5b(events, strong);
        
        //test strong example
        assertTrue(checkEquals(rating, 1));

        rating = instance.assessMPR5b(events, (AttackEvent) events.get(1));
        
        //test weak example where pitch event is not an inception of a dynamic period
        assertTrue(checkEquals(rating, 0.0));
        
        //test weak example where pitch begins at inception of relatively short dynamic
        rating = rating = instance.assessMPR5b(events, (AttackEvent) events.get(8));
        assertTrue(checkEquals(rating, 0.0));
        
        
        events = createAttackEventList2();
        
        //test middling example where pitch even is inception of a dynamic
        //which is of mean value
        
        rating = instance.assessMPR5b(events, (AttackEvent) events.get(0));
        assertTrue(checkEquals(rating, 0.5));
        

    }

    /**
     * Test of assessMPR5c method, of class MetricalStructureAnalyser.
     */
    @Test
    public void testAssessMPR5c() throws MetricalAnalyserException {
        MetricalStructureAnalyser instance = MetricalStructureAnalyser.getInstance();

        List<Event> events = createAttackEventListSlur();

        AttackEvent strong = (AttackEvent) events.get(0);

        double rating = instance.assessMPR5c(events, strong);

        //strong example
        assertTrue("Strong", checkEquals(rating, 1));

        rating = instance.assessMPR5c(events, (AttackEvent) events.get(1));

        //weak example where the given pitch event isn't at the start of a 
        //period of articulation
        assertTrue("Weak", checkEquals(rating, 0.0));
        
        
      rating = instance.assessMPR5c(events, (AttackEvent) events.get(8));

        
        //weak example where the pitch event starts at the start of
        //a relatively short period of slurring.
        assertTrue("Weak", checkEquals(rating, 0.0));
        
         events = createAttackEventList2();
         rating = instance.assessMPR5c(events, (AttackEvent) events.get(0));

       
         //assess a middling value where all slurring is equal
        
         assertTrue("No preference", checkEquals(rating, 0.5));
        
    }

    /**
     * Test of assessMPR5d method, of class MetricalStructureAnalyser.
     */
    @Test
    public void testAssessMPR5d() throws Exception {
        MetricalStructureAnalyser instance = MetricalStructureAnalyser.getInstance();

        List<Event> events = createAttackEventList();

        AttackEvent strong = (AttackEvent) events.get(0);

        double rating = instance.assessMPR5d(events, strong);

        //test case of strong preference
        assertTrue(checkEquals(rating, 1));

        rating = instance.assessMPR5d(events, (AttackEvent) events.get(1));

        //test case of weak preference due to pitch not being inception of 
        //pattern of articulation
        assertTrue(checkEquals(rating, 0));
        
        rating = instance.assessMPR5d(events, (AttackEvent) events.get(5));
        
        //test case of weak preference due to pitch being inception
        //on relatively short duration of articulation.
        assertTrue(checkEquals(rating, 0));
        
        events = createAttackEventList2();
        
        rating = instance.assessMPR5d(events, (AttackEvent) events.get(0));
        
        //test case of middling preference due to pitch being inception
        //on average duration of articulation.
        assertTrue(checkEquals(rating, 0.5));
    }

}
