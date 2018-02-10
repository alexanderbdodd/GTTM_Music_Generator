/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.ac.kent.computing.gttm.GTTM_Analyser.WellFormednessAnalyser;
import uk.ac.kent.computing.gttm.Grammar_Elements.ExceptionClasses.GroupingWellFormednessException;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;

/**Describes a base group of a prolongational grouping hierarchy. Groups beats
 * of the metrical span directly.
 *
 * @author Alexander Dodd
 */
public class ProlongationalBaseGroup implements Group, Serializable {

    private List<Beat> beatSpan;
    private final static Set<Beat> beats = new HashSet<>();
    
    /**
     * 
     * @param beatSpan the span of metrical beats grouped under this group.
     * @throws GroupingWellFormednessException thrown if well formedness rules are
     * violated
     */
    public ProlongationalBaseGroup(List<Beat> beatSpan) throws GroupingWellFormednessException {
        
        WellFormednessAnalyser.getInstance().verifyGWFR1(beatSpan);
        this.beatSpan = beatSpan;
        verifyNoOverlappingGroups();
    }

    @Override
    public List<Beat> getMetricalBeatSpan() {
        return Collections.unmodifiableList(beatSpan);
    }
    
      /**
     * This ensures that a newly created low-level group does not overlap with another group at any stage.
     * @throws GroupingWellFormednessException 
     */
   private void verifyNoOverlappingGroups() 
           throws GroupingWellFormednessException
   {
       for(Beat b : beatSpan)
       {
           if(beats.contains(b))
           {
               throw new GroupingWellFormednessException("Overlapping grouping");
           }
           beats.add(b);
       }
       
   }
    
}
