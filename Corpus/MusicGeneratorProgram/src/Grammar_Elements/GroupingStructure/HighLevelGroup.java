/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grammar_Elements.GroupingStructure;

import Grammar_Elements.MetricalStructure.Beat;
import Grammar_Elements.ExceptionClasses.GroupingWellFormednessException;
import Grammar_Elements.WellFormednessAnalyser;
import java.io.Serializable;
import java.util.*;

/**Groups a portion of the metrical beat span at a high level by grouping sub
 * groups.
 *
 * @author Alexander Dodd
 */
public class HighLevelGroup implements Group, Serializable {

    private List<Group> subgroups;

    /**
     * 
     * @param subgroups the subgroups contained within this group
     * @throws GroupingWellFormednessException thrown if any grouping well formedness rules
     * are violated.
     */
    public HighLevelGroup(List<Group> subgroups)
            throws GroupingWellFormednessException {
        WellFormednessAnalyser.getInstance().assessGroupingWellformedness(subgroups);
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
     * @return the groups contained by this group.
     */
    public List<Group> getSubGroups() {
        return Collections.unmodifiableList(subgroups);
    }
}
