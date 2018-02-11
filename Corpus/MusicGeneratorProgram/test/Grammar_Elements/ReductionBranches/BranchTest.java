/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grammar_Elements.ReductionBranches;

import Elements.DurationEnum;
import Grammar_Elements.ExceptionClasses.BranchingWellFormednessException;
import Grammar_Elements.MetricalStructure.Beat;
import java.util.Map;
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
public class BranchTest {
    
    public BranchTest() {
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
     * Test of addChildBranch method, of class Branch.
     */
    @Test
    public void testAddChildBranch() throws Exception {
        
         assertTrue(testAddChild1());
         assertTrue(testAddChild2());
         assertTrue(testAddChild3());
         assertTrue(testAddChild4());
         assertTrue(testAddChild5());
         assertTrue(testAddChild6());
        
    }
    
    //test add one child branch which is well formed
    private boolean testAddChild1()
    {
       Beat beat = new Beat(2);
       Branch topLevelBranch = new BranchImpl(beat, 0);
       
       Beat beat2 = new Beat(3);
       
       Branch childBranch = new BranchImpl(beat2, 1);
       
       try{
           topLevelBranch.addChildBranch(childBranch);
           return true;
       }
       catch(BranchingWellFormednessException e)
       {
           return false;
       }
    }
    
    //check that adding a childbranch of a high level than parent result in exception
    private boolean testAddChild2()
    {
          Beat beat = new Beat(2);
       Branch topLevelBranch = new BranchImpl(beat, 1);
       
       Beat beat2 = new Beat(3);
       
       Branch childBranch = new BranchImpl(beat2, 0);
       
       try{
           topLevelBranch.addChildBranch(childBranch);
           return false;
       }
       catch(BranchingWellFormednessException e)
       {
           return true;
       }
    }
    
    //check to see that adding a child of the same level results in an exception error
     private boolean testAddChild3()
    {
          Beat beat = new Beat(2);
       Branch topLevelBranch = new BranchImpl(beat, 1);
       
       Beat beat2 = new Beat(3);
       
       Branch childBranch = new BranchImpl(beat2, 1);
       
       try{
           topLevelBranch.addChildBranch(childBranch);
           return false;
       }
       catch(BranchingWellFormednessException e)
       {
           return true;
       }
    }
     
     //test adding multiple subbranches in well formed way
      private boolean testAddChild4()
    {
          Beat beat = new Beat(2);
       Branch topLevelBranch = new BranchImpl(beat, 0);
       
       Beat beat2 = new Beat(3);
       
       Branch childBranch = new BranchImpl(beat2, 1);
       
       Beat beat3 = new Beat(1);
       
       Branch childBranch2 = new BranchImpl(beat3, 2);
       
       try{
           topLevelBranch.addChildBranch(childBranch);
           topLevelBranch.addChildBranch(childBranch2);
           return true;
       }
       catch(BranchingWellFormednessException e)
       {
           return false;
       }
    }
      
      //test adding multiple subbranches at same level fails
      private boolean testAddChild5()
    {
          Beat beat = new Beat(2);
       Branch topLevelBranch = new BranchImpl(beat, 0);
       
       Beat beat2 = new Beat(3);
       
       Branch childBranch = new BranchImpl(beat2, 1);
       
       Beat beat3 = new Beat(1);
       
       Branch childBranch2 = new BranchImpl(beat3, 1);
       
       try{
           topLevelBranch.addChildBranch(childBranch);
           topLevelBranch.addChildBranch(childBranch2);
           return false;
       }
       catch(BranchingWellFormednessException e)
       {
           return true;
       }
    }
      
          //test adding multiple subbranches at same level on different branches does not fail
      private boolean testAddChild6()
    {
       Beat beat = new Beat(2);
       Branch topLevelBranch = new BranchImpl(beat, 0);
       
       Beat beat2 = new Beat(3);
       
       Branch childBranch = new BranchImpl(beat2, 1);
       
       Beat beat3 = new Beat(1);
       
       Branch childBranch2 = new BranchImpl(beat3, 2);
       
       Beat beat4 = new Beat(2);
       
       Branch childBranch3 = new BranchImpl(beat4, 2);
       
       try{
           topLevelBranch.addChildBranch(childBranch);
           topLevelBranch.addChildBranch(childBranch2);
           childBranch.addChildBranch(childBranch3);
           return true;
       }
       catch(BranchingWellFormednessException e)
       {
           return false;
       }
    }
    

  

    /**
     * Test of getLevel method, of class Branch.
     */
    @Test
    public void testGetLevel() {
        
        Beat b = new Beat(1);
        
        Branch branch = new BranchImpl(b, 1);
        
        assertEquals(1, branch.getLevel());
   
    }

    /**
     * Test of getAssociatedBeat method, of class Branch.
     */
    @Test
    public void testGetAssociatedBeat() {
        
        Beat b = new Beat(1);
        
        Branch branch = new BranchImpl(b, 1);
        
        assertEquals(b, branch.getAssociatedBeat());

    }

    /**
     * Test of getChildBranches method, of class Branch.
     */
    @Test
    public void testGetChildBranches() {
        
        Beat b = new Beat(1);
        
        Branch branch = new BranchImpl(b, 1);
        
        assertNotNull(branch.getChildBranches());
        
        
  
    }

    public class BranchImpl extends Branch {

        public BranchImpl(Beat b, int i) {
            super(b, i);
        }
    }
    
}
