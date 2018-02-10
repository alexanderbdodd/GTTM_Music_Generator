/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;

/**Describes a high level group of the prolongational grouping structure.
 *
 * @author Alexander Dodd
 */
public class ProlongationalGroup implements Group, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4657611881076030523L;
	private List<Group> subgroups;

    /**
     * 
     * @param subgroups the subgroups contained by this grouping structure
     */
    public ProlongationalGroup(List<Group> subgroups) {
        this.subgroups = subgroups;
    }
 
    @Override
    public List<Beat> getMetricalBeatSpan() {
        List<Beat> beats = new ArrayList<>();

        for (Group g : subgroups) {

            for (Beat b : g.getMetricalBeatSpan()) {
                beats.add(b);
            }
        }
        
        return Collections.unmodifiableList(beats);
    }
    
    /**
     * 
     * @return the list of subgroups.
     */
    public List<Group> getSubgroups() {
        return subgroups;
    }
    
    
    
    

}
