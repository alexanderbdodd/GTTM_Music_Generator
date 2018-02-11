/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grammar_Elements.GroupingStructure;

import Grammar_Elements.MetricalStructure.Beat;
import java.io.Serializable;
import java.util.*;

/**Describes a high level group of the prolongational grouping structure.
 *
 * @author Alexander Dodd
 */
public class ProlongationalGroup implements Group, Serializable {

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
