
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GTTM_Analyser;

import Elements.*;
import Grammar_Elements.GrammarContainer;
import Grammar_Elements.GroupingStructure.*;
import Grammar_Elements.MetricalStructure.*;
import Grammar_Elements.ReductionBranches.*;
import Manipulators.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander
 */
public class RatingsGeneratorTest {
    
    public RatingsGeneratorTest() {
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
     * Test of obtainMetricalRating method, of class RatingsGenerator.
     */
    @Test
    public void testObtainMetricalRating() {
       assertTrue("Weak Test", weakMetricalRatingTest());
       assertTrue("Strong Test", strongMetricalRatingTest());
    }
    
    private boolean weakMetricalRatingTest()
    {
        GrammarContainer grammar 
                = createMetricalGrammar();
        
        /*Metrical Span structure:
                      
        Beat b1 = new Beat(1);
        Beat b2 = new Beat(3);
        Beat b3 = new Beat(1);
        Beat b4 = new Beat(2);
        Beat b5 = new Beat(1);
        Beat b6 = new Beat(3);
        Beat b7 = new Beat(1);
        Beat b8 = new Beat(2);
        Beat b9 = new Beat(1);
        Beat b10 = new Beat(3);
             
        Pitch Beats: b2, b4, b6, b9
        */
        
        List<Event> eventStream = new ArrayList<>();
        
        eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.SEVENTH, DurationEnum.ONE_BEAT));
        eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.SIXTH, DurationEnum.ONE_BEAT));
        eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
         eventStream.add(new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.EIGHTH, DurationEnum.ONE_BEAT));
         eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
         eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.EIGHTH, DurationEnum.TWO_BEATS));
       
        
        double rating = RatingsGenerator.getInstance().obtainMetricalRating(grammar, 
                new EventStream(eventStream, ScaleConstructor.getInstance()
                        .constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR)));
        
        if(rating < 0.5)
        {
            return true;
        }
        else{
            return false;
        }
    }
    
    private boolean strongMetricalRatingTest()
    {
        GrammarContainer grammar 
                = createMetricalGrammar();
        
        /*Metrical Span structure:
                      
        Beat b1 = new Beat(1);
        Beat b2 = new Beat(3);
        Beat b3 = new Beat(1);
        Beat b4 = new Beat(2);
        Beat b5 = new Beat(1);
        Beat b6 = new Beat(3);
        Beat b7 = new Beat(1);
        Beat b8 = new Beat(2);
        Beat b9 = new Beat(1);
        Beat b10 = new Beat(3);
             
        Pitch Beats: b2, b4, b6, b9
        */
        
        List<Event> eventStream = new ArrayList<>();
        
        eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.SEVENTH, DurationEnum.TWO_BEATS));
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.SIXTH, DurationEnum.TWO_BEATS));
        eventStream.add(new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.SEVENTH, DurationEnum.ONE_BEAT));
         eventStream.add(new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.EIGHTH, DurationEnum.THREE_BEATS));
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.EIGHTH, DurationEnum.ONE_BEAT));
        eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
        
        
        double rating = RatingsGenerator.getInstance().obtainMetricalRating(grammar, 
                new EventStream(eventStream, ScaleConstructor.getInstance()
                        .constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR)));
        
        if(rating > 0.5)
        {
            return true;
        }
        else{
            return false;
        }
    }
    
        /*
    AttackEvents = b1, b4, b5, b9, b10
    GroupingStructure = basegroup1[b1 - b4], basegroup2[b5-b10]
    */
    private GrammarContainer createMetricalGrammar()
    {
        Beat b1 = new Beat(1);
        Beat b2 = new Beat(3);
        Beat b3 = new Beat(1);
        Beat b4 = new Beat(2);
        Beat b5 = new Beat(1);
        Beat b6 = new Beat(3);
        Beat b7 = new Beat(1);
        Beat b8 = new Beat(2);
        Beat b9 = new Beat(1);
        Beat b10 = new Beat(3);
        
          /* Beat b1 = new Beat(1);
        Beat b2 = new Beat(3);
        Beat b3 = new Beat(1);
        Beat b4 = new Beat(2);
        Beat b5 = new Beat(1);
        Beat b6 = new Beat(3);
        Beat b7 = new Beat(1);
        Beat b8 = new Beat(2);
        Beat b9 = new Beat(1);
        Beat b10 = new Beat(3);
             
        Pitch Beats: b2, b4, b6, b9*/
        
        TimeSpanReductionBranch topTBranch
                = new TimeSpanReductionBranch(b6, 0);          
         TimeSpanReductionBranch subBranch3
                = new TimeSpanReductionBranch(b9, 3);  
        TimeSpanReductionBranch subBranch1
                = new TimeSpanReductionBranch(b2, 3);                
        TimeSpanReductionBranch subBranch2
                = new TimeSpanReductionBranch(b4, 2);
       
        topTBranch.addChildBranch(subBranch2);
        topTBranch.addChildBranch(subBranch3);
        subBranch2.addChildBranch(subBranch1);
              
        
        ProlongationalBranch topPBranch 
                = new ProlongationalBranch(b9, 0);        
        ProlongationalBranch subPBranch1 
                = new ProlongationalBranch(b2, 1);
        ProlongationalBranch subPBranch2
                = new ProlongationalBranch(b6, 2);                
        ProlongationalBranch subPBranch3
                = new ProlongationalBranch(b4, 3);
               
        
        topPBranch.addChildBranch(subPBranch1);
        topPBranch.addChildBranch(subPBranch2);
        subPBranch1.addChildBranch(subPBranch3);
       
                
       MetricalContainer mContainer
                = new MetricalContainer();
        
       mContainer.addBeat(b1);
       mContainer.addBeat(b2);
       mContainer.addBeat(b3);
       mContainer.addBeat(b4);
       mContainer.addBeat(b5);
       mContainer.addBeat(b6);
       mContainer.addBeat(b7);
       mContainer.addBeat(b8);
       mContainer.addBeat(b9);
       mContainer.addBeat(b10);
       
       List<Beat> beats = new ArrayList<>();
       
       beats.add(b1);
       beats.add(b2);
       beats.add(b3);
       beats.add(b4);
       beats.add(b5);
       beats.add(b6);
       beats.add(b7);
       beats.add(b8);
       beats.add(b9);
       beats.add(b10);       
       
       ProlongationalBaseGroup pBGroup 
               = new ProlongationalBaseGroup(beats);
       
       List<Group> pgroups = new ArrayList<>();
       pgroups.add(pBGroup);
       ProlongationalGroup pGroup 
               = new ProlongationalGroup(pgroups);
       
       List<Beat> gbeats1 = new ArrayList<>();
       gbeats1.add(b1);
       gbeats1.add(b2);
       gbeats1.add(b3);
       gbeats1.add(b4);
       gbeats1.add(b5);
             
       List<Beat> gbeats2 = new ArrayList<>();
       gbeats2.add(b6);
       gbeats2.add(b7);
       gbeats2.add(b8);
       gbeats2.add(b9);
       gbeats2.add(b10);
       
       BaseGroup g1 = new BaseGroup(gbeats1);
       BaseGroup g2 = new BaseGroup(gbeats2);
       
       List<Group> bgroups = new ArrayList<>();
       bgroups.add(g1);
       bgroups.add(g2);
       
       HighLevelGroup gstructure = new HighLevelGroup(bgroups);
       
       GrammarContainer grammar 
               = new GrammarContainer(topPBranch, topTBranch, mContainer,
               gstructure, pGroup);
       
       return grammar;
    }
    

    /**
     * Test of obtainTimeSpanReductionRating method, of class RatingsGenerator.
     */
    @Test
    public void testObtainTimeSpanReductionRating() {
     assertTrue(weakTimeSpanReductionTest());
     assertTrue(strongTimeSpanReductionTest());
    }
    
    private boolean strongTimeSpanReductionTest()
    {
           GrammarContainer grammar 
                = createStrongGroupingStructureTestsGrammar();
        
        /*Time Span structure:
                      
        CadencedTimeSpanReductionBranch topTBranch 
                = new CadencedTimeSpanReductionBranch(b9, b10, 0);        
        TimeSpanReductionBranch subBranch1
                = new TimeSpanReductionBranch(b1, 1);                
        TimeSpanReductionBranch subBranch2
                = new TimeSpanReductionBranch(b4, 2);
        TimeSpanReductionBranch subBranch3
                = new TimeSpanReductionBranch(b5, 3);
        topTBranch.addChildBranch(subBranch1);
        topTBranch.addChildBranch(subBranch3);
        subBranch1.addChildBranch(subBranch2);
        */
        
        List<Event> eventStream = new ArrayList<>();
        
        eventStream.add(new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.SEVENTH, DurationEnum.THREE_BEATS));
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.SIXTH, DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.SEVENTH, DurationEnum.THREE_BEATS));
        eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.EIGHTH, DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.EIGHTH, DurationEnum.ONE_BEAT));
        
        double rating = RatingsGenerator.getInstance().obtainTimeSpanReductionRating(grammar, 
                new EventStream(eventStream, ScaleConstructor.getInstance()
                        .constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR)));
        
        if(rating > 0.5)
        {
            return true;
        }
        else{
            return false;
        }
        
        
    }
    
    private boolean weakTimeSpanReductionTest()
    {
           GrammarContainer grammar 
                = createStrongGroupingStructureTestsGrammar();
        
        /*Time Span structure:
                      
        CadencedTimeSpanReductionBranch topTBranch 
                = new CadencedTimeSpanReductionBranch(b9, b10, 0);        
        TimeSpanReductionBranch subBranch1
                = new TimeSpanReductionBranch(b1, 1);                
        TimeSpanReductionBranch subBranch2
                = new TimeSpanReductionBranch(b4, 2);
        TimeSpanReductionBranch subBranch3
                = new TimeSpanReductionBranch(b5, 3);
        topTBranch.addChildBranch(subBranch1);
        topTBranch.addChildBranch(subBranch3);
        subBranch1.addChildBranch(subBranch2);
        */
        
        List<Event> eventStream = new ArrayList<>();
        
        eventStream.add(new AttackEvent(KeyLetterEnum.GS, KeyPositionEnum.SECOND, DurationEnum.THREE_BEATS));
        eventStream.add(new AttackEvent(KeyLetterEnum.FS, KeyPositionEnum.SEVENTH, DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.EIGHTH, DurationEnum.THREE_BEATS));
        eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.Bf, KeyPositionEnum.FIRST, DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.ONE_BEAT));
        
        double rating = RatingsGenerator.getInstance().obtainTimeSpanReductionRating(grammar, 
                new EventStream(eventStream, ScaleConstructor.getInstance()
                        .constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR)));
        
        if(rating < 0.5)
        {
            return true;
        }
        else{
            return false;
        }
        
        
    }
    
    /**
     * Test of obtainProlongationalReductionRating method, of class RatingsGenerator.
     */
    @Test
    public void testObtainProlongationalReductionRating() {
        assertTrue(strongProlongationalReductionTest());
        assertTrue(weakProlongationalReductionTest());
    }
    
    private boolean weakProlongationalReductionTest()
    {
          GrammarContainer grammar 
                = createStrongGroupingStructureTestsGrammar();
        
        /*Prolongational structure:
        
        ProlongationalBranch topPBranch 
                = new ProlongationalBranch(b10, 0);        
        ProlongationalBranch subPBranch1 
                = new ProlongationalBranch(b1, 1);
        ProlongationalBranch subPBranch2
                = new ProlongationalBranch(b9, 2);                
        ProlongationalBranch subPBranch3
                = new ProlongationalBranch(b4, 3);
        ProlongationalBranch subPBranch4
                = new ProlongationalBranch(b5, 4);  
        topPBranch.addChildBranch(subPBranch1);
        topPBranch.addChildBranch(subPBranch2);
        subPBranch1.addChildBranch(subPBranch3);
        subPBranch3.addChildBranch(subPBranch4);
        */
        
        List<Event> eventStream = new ArrayList<>();
        
       eventStream.add(new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.SECOND, DurationEnum.THREE_BEATS));
        eventStream.add(new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.SECOND, DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.A, KeyPositionEnum.SECOND, DurationEnum.THREE_BEATS));
        eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.SECOND, DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.EIGHTH, DurationEnum.ONE_BEAT));
       
        
        
        double rating = RatingsGenerator.getInstance().obtainProlongationalReductionRating(grammar, 
                new EventStream(eventStream, ScaleConstructor.getInstance()
                        .constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR)));
        
        if(rating < 0.5)
        {
            return true;
        }
        else{
            return false;
        }
    }
    
    private boolean strongProlongationalReductionTest()
    {
        GrammarContainer grammar 
                = createStrongGroupingStructureTestsGrammar();
        
        /*Prolongational structure:
        
        ProlongationalBranch topPBranch 
                = new ProlongationalBranch(b10, 0);        
        ProlongationalBranch subPBranch1 
                = new ProlongationalBranch(b1, 1);
        ProlongationalBranch subPBranch2
                = new ProlongationalBranch(b9, 2);                
        ProlongationalBranch subPBranch3
                = new ProlongationalBranch(b4, 3);
        ProlongationalBranch subPBranch4
                = new ProlongationalBranch(b5, 4);  
        topPBranch.addChildBranch(subPBranch1);
        topPBranch.addChildBranch(subPBranch2);
        subPBranch1.addChildBranch(subPBranch3);
        subPBranch3.addChildBranch(subPBranch4);
        */
        
        List<Event> eventStream = new ArrayList<>();
        
        
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.THREE_BEATS));
        eventStream.add(new AttackEvent(KeyLetterEnum.FS, KeyPositionEnum.EIGHTH, DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.FS, KeyPositionEnum.EIGHTH, DurationEnum.THREE_BEATS));
        eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FIRST, DurationEnum.ONE_BEAT));
        
        double rating = RatingsGenerator.getInstance().obtainProlongationalReductionRating(grammar, 
                new EventStream(eventStream, ScaleConstructor.getInstance()
                        .constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR)));
        
        if(rating > 0.5)
        {
            return true;
        }
        else{
            return false;
        }
        
        
    }

    /**
     * Test of obtainGroupingStructureRating method, of class RatingsGenerator.
     */
    @Test
    public void testObtainGroupingStructureRating() {
       assertTrue(strongGroupingStructureTests());
       assertTrue(weakGroupingStructureTests());
    }
    
    private boolean strongGroupingStructureTests()
    {
         GrammarContainer grammar 
                = createStrongGroupingStructureTestsGrammar();
        
        List<Event> eventStream = new ArrayList<>();
        
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.SECOND, DurationEnum.ONE_BEAT));
        eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
        eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.SECOND, DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.SECOND, DurationEnum.THREE_BEATS));
        eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.SEVENTH, DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.SEVENTH, DurationEnum.ONE_BEAT));
        
       double rating = RatingsGenerator.getInstance().obtainGroupingStructureRating(grammar, new EventStream(eventStream, 
                ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR)));
        
       if(rating > 0.5)
       {
           return true;
       }
       else{
           return false;
       }
    }
    
    private boolean weakGroupingStructureTests()
    {
        GrammarContainer grammar 
                = createWeakGroupingStructureTestsGrammar();
        
        List<Event> eventStream = new ArrayList<>();
        
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.SECOND, DurationEnum.THREE_BEATS));
        eventStream.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.SEVENTH, DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.SEVENTH, DurationEnum.THREE_BEATS));
        eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.ONE_BEAT));
        eventStream.add(new AttackEvent(KeyLetterEnum.E, KeyPositionEnum.FIRST, DurationEnum.ONE_BEAT));
        
       double rating = RatingsGenerator.getInstance().obtainGroupingStructureRating(grammar, new EventStream(eventStream, 
                ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR)));
        
       if(rating < 0.5)
       {
           return true;
       }
       else{
           return false;
       }
        
    }
    
    /*
    AttackEvents = b1, b4, b5, b9, b10
    GroupingStructure = basegroup1[b1 - b4], basegroup2[b5-b10]
    */
    private GrammarContainer createStrongGroupingStructureTestsGrammar()
    {
        Beat b1 = new Beat(1);
        Beat b2 = new Beat(3);
        Beat b3 = new Beat(1);
        Beat b4 = new Beat(2);
        Beat b5 = new Beat(1);
        Beat b6 = new Beat(3);
        Beat b7 = new Beat(1);
        Beat b8 = new Beat(2);
        Beat b9 = new Beat(1);
        Beat b10 = new Beat(3);
        
        CadencedTimeSpanReductionBranch topTBranch 
                = new CadencedTimeSpanReductionBranch(b9, b10, 0);        
        TimeSpanReductionBranch subBranch1
                = new TimeSpanReductionBranch(b1, 1);                
        TimeSpanReductionBranch subBranch2
                = new TimeSpanReductionBranch(b4, 2);
        TimeSpanReductionBranch subBranch3
                = new TimeSpanReductionBranch(b5, 3);
        topTBranch.addChildBranch(subBranch1);
        topTBranch.addChildBranch(subBranch3);
        subBranch1.addChildBranch(subBranch2);
                
        
        ProlongationalBranch topPBranch 
                = new ProlongationalBranch(b10, 0);        
        ProlongationalBranch subPBranch1 
                = new ProlongationalBranch(b1, 1);
        ProlongationalBranch subPBranch2
                = new ProlongationalBranch(b9, 2);                
        ProlongationalBranch subPBranch3
                = new ProlongationalBranch(b4, 3);
        ProlongationalBranch subPBranch4
                = new ProlongationalBranch(b5, 4);       
        
        topPBranch.addChildBranch(subPBranch1);
        topPBranch.addChildBranch(subPBranch2);
        subPBranch1.addChildBranch(subPBranch3);
        subPBranch3.addChildBranch(subPBranch4);
                
       MetricalContainer mContainer
                = new MetricalContainer();
        
       mContainer.addBeat(b1);
       mContainer.addBeat(b2);
       mContainer.addBeat(b3);
       mContainer.addBeat(b4);
       mContainer.addBeat(b5);
       mContainer.addBeat(b6);
       mContainer.addBeat(b7);
       mContainer.addBeat(b8);
       mContainer.addBeat(b9);
       mContainer.addBeat(b10);
       
       List<Beat> beats = new ArrayList<>();
       
       beats.add(b1);
       beats.add(b2);
       beats.add(b3);
       beats.add(b4);
       beats.add(b5);
       beats.add(b6);
       beats.add(b7);
       beats.add(b8);
       beats.add(b9);
       beats.add(b10);       
       
       ProlongationalBaseGroup pBGroup 
               = new ProlongationalBaseGroup(beats);
       
       List<Group> pgroups = new ArrayList<>();
       pgroups.add(pBGroup);
       ProlongationalGroup pGroup 
               = new ProlongationalGroup(pgroups);
       
       List<Beat> gbeats1 = new ArrayList<>();
       gbeats1.add(b1);
       gbeats1.add(b2);
       gbeats1.add(b3);
       gbeats1.add(b4);
       gbeats1.add(b5);
             
       List<Beat> gbeats2 = new ArrayList<>();
       gbeats2.add(b6);
       gbeats2.add(b7);
       gbeats2.add(b8);
       gbeats2.add(b9);
       gbeats2.add(b10);
       
       BaseGroup g1 = new BaseGroup(gbeats1);
       BaseGroup g2 = new BaseGroup(gbeats2);
       
       List<Group> bgroups = new ArrayList<>();
       bgroups.add(g1);
       bgroups.add(g2);
       
       HighLevelGroup gstructure = new HighLevelGroup(bgroups);
       
       GrammarContainer grammar 
               = new GrammarContainer(topPBranch, topTBranch, mContainer,
               gstructure, pGroup);
       
       return grammar;
    }
    
    /*
    AttackEvents = b1, b4, b5, b9, b10
    GroupingStructure = basegroup1[b1 - b4], basegroup2[b5-b10]
    */
    private GrammarContainer createWeakGroupingStructureTestsGrammar()
    {
        Beat b1 = new Beat(1);
        Beat b2 = new Beat(3);
        Beat b3 = new Beat(1);
        Beat b4 = new Beat(2);
        Beat b5 = new Beat(1);
        Beat b6 = new Beat(3);
        Beat b7 = new Beat(1);
        Beat b8 = new Beat(2);
        Beat b9 = new Beat(1);
        Beat b10 = new Beat(3);
        
        CadencedTimeSpanReductionBranch topTBranch 
                = new CadencedTimeSpanReductionBranch(b9, b10, 0);        
        TimeSpanReductionBranch subBranch1
                = new TimeSpanReductionBranch(b1, 1);                
        TimeSpanReductionBranch subBranch2
                = new TimeSpanReductionBranch(b4, 2);
        TimeSpanReductionBranch subBranch3
                = new TimeSpanReductionBranch(b5, 3);
        topTBranch.addChildBranch(subBranch1);
        topTBranch.addChildBranch(subBranch3);
        subBranch1.addChildBranch(subBranch2);
                
        
        ProlongationalBranch topPBranch 
                = new ProlongationalBranch(b10, 0);        
        ProlongationalBranch subPBranch1 
                = new ProlongationalBranch(b1, 1);
        ProlongationalBranch subPBranch2
                = new ProlongationalBranch(b9, 2);                
        ProlongationalBranch subPBranch3
                = new ProlongationalBranch(b4, 3);
        ProlongationalBranch subPBranch4
                = new ProlongationalBranch(b5, 4);       
        
        topPBranch.addChildBranch(subPBranch1);
        topPBranch.addChildBranch(subPBranch2);
        subPBranch1.addChildBranch(subPBranch3);
        subPBranch3.addChildBranch(subPBranch4);
                
       MetricalContainer mContainer
                = new MetricalContainer();
        
       mContainer.addBeat(b1);
       mContainer.addBeat(b2);
       mContainer.addBeat(b3);
       mContainer.addBeat(b4);
       mContainer.addBeat(b5);
       mContainer.addBeat(b6);
       mContainer.addBeat(b7);
       mContainer.addBeat(b8);
       mContainer.addBeat(b9);
       mContainer.addBeat(b10);
       
       List<Beat> beats = new ArrayList<>();
       
       beats.add(b1);
       beats.add(b2);
       beats.add(b3);
       beats.add(b4);
       beats.add(b5);
       beats.add(b6);
       beats.add(b7);
       beats.add(b8);
       beats.add(b9);
       beats.add(b10);       
       
       ProlongationalBaseGroup pBGroup 
               = new ProlongationalBaseGroup(beats);
       
       List<Group> pgroups = new ArrayList<>();
       pgroups.add(pBGroup);
       ProlongationalGroup pGroup 
               = new ProlongationalGroup(pgroups);
       
       List<Beat> gbeats1 = new ArrayList<>();
       gbeats1.add(b1);
       gbeats1.add(b2);
       gbeats1.add(b3);
       gbeats1.add(b4);
             
       List<Beat> gbeats2 = new ArrayList<>();
       gbeats2.add(b5);
       gbeats2.add(b6);
       gbeats2.add(b7);
       gbeats2.add(b8);
       gbeats2.add(b9);
       gbeats2.add(b10);
       
       BaseGroup g1 = new BaseGroup(gbeats1);
       BaseGroup g2 = new BaseGroup(gbeats2);
       
       List<Group> bgroups = new ArrayList<>();
       bgroups.add(g1);
       bgroups.add(g2);
       
       HighLevelGroup gstructure = new HighLevelGroup(bgroups);
       
       GrammarContainer grammar 
               = new GrammarContainer(topPBranch, topTBranch, mContainer,
               gstructure, pGroup);
       
       return grammar;
    }

    /**
     * Test of obtainCadenceRating method, of class RatingsGenerator.
     */
    @Test
    public void testObtainCadenceRating() {
        assertTrue("Strong Test", strongCadenceExample());
        assertTrue("Weak Test", weakCadenceExample());
    }
    
    private boolean weakCadenceExample()
    {
       AttackEvent ev1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
       AttackEvent ev2 = new AttackEvent(KeyLetterEnum.D, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
       
       List<Event> eventStream = new ArrayList<>();
       eventStream.add(ev1);
       eventStream.add(ev2);
       
       GrammarContainer grammar
               = constructCadenceGrammar();
       
       if(RatingsGenerator.getInstance()
               .obtainCadenceRating(grammar, new EventStream(eventStream, 
                       ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR)))
           != 0.0)
       {
           return false;
       }
       
       eventStream.clear();
       
       eventStream.add(ev2);
       eventStream.add(ev1);
       
       if(RatingsGenerator.getInstance()
               .obtainCadenceRating(grammar, new EventStream(eventStream, 
                       ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR)))
           != 0.0)
       {
           return false;
       }
       
       return true;
    }
    
    private boolean strongCadenceExample()
    {
        
       AttackEvent ev1 = new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
       AttackEvent ev2 = new AttackEvent(KeyLetterEnum.G, KeyPositionEnum.FOURTH, DurationEnum.ONE_BEAT);
       
       List<Event> eventStream = new ArrayList<>();
       eventStream.add(ev1);
       eventStream.add(ev2);
       
       GrammarContainer grammar
               = constructCadenceGrammar();
       
       if(RatingsGenerator.getInstance()
               .obtainCadenceRating(grammar, new EventStream(eventStream, 
                       ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR)))
           != 1.0)
       {
           return false;
       }
       
       eventStream.clear();
       
       eventStream.add(ev2);
       eventStream.add(ev1);
       
       if(RatingsGenerator.getInstance()
               .obtainCadenceRating(grammar, new EventStream(eventStream, 
                       ScaleConstructor.getInstance().constructScale(KeyLetterEnum.C, ScaleModeEnum.MAJOR)))
           != 1.0)
       {
           return false;
       }
       
       return true;
    }
    
    private GrammarContainer constructCadenceGrammar()
    {
        Beat b1 = new Beat(1);
        Beat b2 = new Beat(3);
        
        CadencedTimeSpanReductionBranch topTBranch 
                = new CadencedTimeSpanReductionBranch(b1, b2, 0);
        
        ProlongationalBranch topPBranch 
                = new ProlongationalBranch(b2, 0);
        
        ProlongationalBranch subBranch 
                = new ProlongationalBranch(b1, 1);
        
        topPBranch.addChildBranch(subBranch);
        
        MetricalContainer mContainer
                = new MetricalContainer();
        
       mContainer.addBeat(b1);
       mContainer.addBeat(b2);
       
       List<Beat> beats = new ArrayList<>();
       
       beats.add(b1);
       beats.add(b2);
       
       ProlongationalBaseGroup pBGroup 
               = new ProlongationalBaseGroup(beats);
       
       List<Group> pgroups = new ArrayList<>();
       pgroups.add(pBGroup);
       ProlongationalGroup pGroup 
               = new ProlongationalGroup(pgroups);
       
       List<Beat> gbeats1 = new ArrayList<>();
       gbeats1.add(b1);
       
       List<Beat> gbeats2 = new ArrayList<>();
       gbeats2.add(b2);
       BaseGroup g1 = new BaseGroup(gbeats1);
       BaseGroup g2 = new BaseGroup(gbeats2);
       
       List<Group> bgroups = new ArrayList<>();
       bgroups.add(g1);
       bgroups.add(g2);
       
       HighLevelGroup gstructure = new HighLevelGroup(bgroups);
       
       GrammarContainer grammar 
               = new GrammarContainer(topPBranch, topTBranch, mContainer,
               gstructure, pGroup);
       
       return grammar;
    }
    
    
    
}
