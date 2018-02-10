/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure;

import java.util.List;

import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;

/**An interface for an object that acts as a group.
 *
 * @author Alexander Dodd
 */
public interface Group{

   

    /**
     * 
     * @return the span of metrical beats mapped by this group
     */
    public List<Beat> getMetricalBeatSpan();

}
