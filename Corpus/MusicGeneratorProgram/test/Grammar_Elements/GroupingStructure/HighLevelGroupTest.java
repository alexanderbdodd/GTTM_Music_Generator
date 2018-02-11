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
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander
 */
public class HighLevelGroupTest {

    public HighLevelGroupTest() {
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
        
        assertTrue(constructorTest1());
        assertTrue(constructorTest2());
        assertTrue(constructorTest3());

    }

    private boolean constructorTest1() {
        List<Beat> beats = new ArrayList<>();
        List<Beat> beats2 = new ArrayList<>();

        Beat beat1 = new Beat(1);
        Beat beat2 = new Beat(2);
        Beat beat3 = new Beat(1);
        Beat beat4 = new Beat(3);
        Beat beat5 = new Beat(1);

        beats.add(beat1);
        beats.add(beat2);

        beats2.add(beat3);
        beats2.add(beat4);
        beats2.add(beat5);

        BaseGroup group = null;
        BaseGroup group2 = null;

        try {
            group = new BaseGroup(beats);
            group2 = new BaseGroup(beats2);
        } catch (Exception e) {
            fail("Construction for subgroups error");
        }

        List<Group> subGroup = new ArrayList<Group>();

        subGroup.add(group);
        subGroup.add(group2);

        try {
            HighLevelGroup group3 = new HighLevelGroup(subGroup);
            return true;
        } catch (GroupingWellFormednessException e) {
            return false;
        }
    }

    private boolean constructorTest2() {
        List<Group> subGroup2 = new ArrayList<Group>();

        List<Beat> beats = new ArrayList<>();
        List<Beat> beats2 = new ArrayList<>();

        Beat beat1 = new Beat(1);
        Beat beat2 = new Beat(2);
        Beat beat3 = new Beat(1);
        Beat beat4 = new Beat(3);
        Beat beat5 = new Beat(1);

        beats.add(beat1);
        beats.add(beat2);

        beats2.add(beat3);
        beats2.add(beat4);
        beats2.add(beat5);

        BaseGroup group = null;
        BaseGroup group2 = null;

        subGroup2.add(group);

        try {
            HighLevelGroup group3 = new HighLevelGroup(subGroup2);
            return false;
        } catch (GroupingWellFormednessException e) {
            return true;
        }

    }

    private boolean constructorTest3() {
        List<Beat> beats3 = new ArrayList<>();
        List<Beat> beats4 = new ArrayList<>();
        List<Beat> beats5 = new ArrayList<>();

        Beat beat6 = new Beat(1);
        Beat beat7 = new Beat(2);
        Beat beat8 = new Beat(1);
        Beat beat9 = new Beat(3);
        Beat beat10 = new Beat(1);

        beats3.add(beat6);
        beats3.add(beat7);

        beats4.add(beat8);
        beats4.add(beat9);

        beats5.add(beat10);

        BaseGroup group3 = null;
        BaseGroup group4 = null;
        BaseGroup group5 = null;

        try {
            group3 = new BaseGroup(beats3);
            group4 = new BaseGroup(beats4);
            group5 = new BaseGroup(beats5);
        } catch (GroupingWellFormednessException e) {
            System.out.println("ERROR MESSAGE: " + e.getMessage());
            return false;

        }

        List<Group> subGroup3 = new ArrayList<Group>();

        subGroup3.add(group3);
        subGroup3.add(group4);

        try {
            HighLevelGroup group6 = new HighLevelGroup(subGroup3);
            List<Group> subGroup4 = new ArrayList<Group>();

            subGroup4.add(group6);
            subGroup4.add(group5);

            HighLevelGroup group7 = new HighLevelGroup(subGroup4);

            return true;
        } catch (GroupingWellFormednessException e) {
            return false;
        }
    }

  
}
