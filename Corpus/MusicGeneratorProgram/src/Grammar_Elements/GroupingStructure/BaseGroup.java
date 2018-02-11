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

/**
 * A base group of the grouping structure analysis. Maps directly over a span of
 * contiguous beats.
 *
 * @author Alexander Dodd
 */
public class BaseGroup implements Group, Serializable{

    private List<Beat> metricalSpan;

    // a set of beats that have already been mapped by another base group object
    private final static Set<Beat> beats = new HashSet<>();

    /**
     *
     * @param metricalSpan a span of beats that is contained within this base
     * group structure
     * @throws GroupingWellFormednessException Thrown if various grouping well
     * formedness rules are violated, including grouping non-contiguous beats
     * and grouping beats that have already been grouped within another base
     * group structure.
     */
    public BaseGroup(List<Beat> metricalSpan)
            throws GroupingWellFormednessException {
        WellFormednessAnalyser.getInstance().verifyGWFR1(metricalSpan);
        this.metricalSpan = metricalSpan;
        verifyNoOverlappingGroups();

    }

    /**
     * This ensures that a newly created low-level group does not overlap with
     * another group at any stage.
     *
     * @throws GroupingWellFormednessException
     */
    private void verifyNoOverlappingGroups()
            throws GroupingWellFormednessException {
        List<Beat> addedBeats = new ArrayList<>();

        for (Beat b : metricalSpan) {
            if (beats.contains(b)) {
                for (Beat b2 : addedBeats) {
                    beats.remove(b2);
                }

                throw new GroupingWellFormednessException("Overlapping grouping. Beat at position: " + b.getPosition());
            }
            beats.add(b);
            addedBeats.add(b);
        }

    }

    /**
     * Resets the list of Beat objects already mapped.
     */
    public static void clearBeatList() {
        beats.clear();
    }

    /**
     *
     * @return the span of metrical beats mapped by this group
     */
    public List<Beat> getMetricalBeatSpan() {
        return Collections.unmodifiableList(metricalSpan);
    }

}
