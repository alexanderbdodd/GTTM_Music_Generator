/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grammar_Elements.ReductionBranches;

import Elements.DurationEnum;
import Grammar_Elements.ExceptionClasses.BranchingWellFormednessException;
import Grammar_Elements.MetricalStructure.Beat;
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
public class TimeSpanReductionBranchTest {
    
    public TimeSpanReductionBranchTest() {
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
    public void testConstructor()
    {
        Beat b = new Beat(3);
        try{Branch br = new TimeSpanReductionBranch(b, 0);
        }
        catch(BranchingWellFormednessException e)
        {
            fail();
        }
        
        try{Branch br = new TimeSpanReductionBranch(b,1);
        fail();
        }
        catch(BranchingWellFormednessException e)
        {
            assertTrue(true);
        }
        
    }
    
}
