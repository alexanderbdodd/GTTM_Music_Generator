/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grammar_Elements;

import Grammar_Elements.ExceptionClasses.BranchingWellFormednessException;
import Grammar_Elements.ExceptionClasses.GroupingWellFormednessException;
import Grammar_Elements.GroupingStructure.Group;
import Grammar_Elements.MetricalStructure.Beat;
import Grammar_Elements.MetricalStructure.MetricalContainer;
import Grammar_Elements.ReductionBranches.Branch;
import java.util.*;

/**
 *
 * @author Alexander Dodd
 */
public class WellFormednessAnalyser {

    private static WellFormednessAnalyser instance = null;

    public static WellFormednessAnalyser getInstance() {
        if (instance == null) {
            instance = new WellFormednessAnalyser();
        }

        return instance;

    }

    private WellFormednessAnalyser() {

    }

    /**
     * Verifies whether the grouping structure fits the GWFR 2 rules that states 
     * 'A piece constitutes a group'. The grouping structure should map the whole 
     * metrical structure.
     * 
     * @param metricalStructure the metrical structure to check
     * @param topLevelGroup the grouping structure to verify
     * 
     */
    public void verifyGWFR2(MetricalContainer metricalStructure, Group topLevelGroup)
            {
        boolean equality = metricalStructure.getMetricalBeatsList().equals(topLevelGroup.getMetricalBeatSpan());

        if (!equality) {
            throw new GroupingWellFormednessException("Top level group does not map whole metrical structure");
        }
    }

    /**Checks to ensure that branches do not overlap
     * 
     * @param branch the top branch of the branching structure
     * @param metricalStructure the metrical structure for use in checking crossing branches
     * 
     */
    public void checkBranchingWellFormedness(Branch branch, MetricalContainer metricalStructure)
             {

        checkBranchSubBranchesForOverLaps(branch, metricalStructure);

        TreeMap<Integer, Branch> orderedBranches = new TreeMap<>(branch.getChildBranches());
        for (Branch b : orderedBranches.values()) {
            checkBranchingWellFormedness(b, metricalStructure);
        }

    }

    private void checkBranchSubBranchesForOverLaps(Branch branch, MetricalContainer metricalStructure)
            throws BranchingWellFormednessException {

        TreeMap<Integer, Branch> orderedBranches = new TreeMap<>(branch.getChildBranches());

        List<Integer> levels = new ArrayList<>();

        for (Integer i : orderedBranches.keySet()) {
            levels.add(i);
        }

        int index = 0;

        while (index < (levels.size() - 1)) {

            Set<Beat> level1 = new HashSet<>();
            Set<Beat> level2 = new HashSet<>();

            level1 = collectInnerBeats(orderedBranches.get(levels.get(index)), level1);
            orderedBranches.remove(orderedBranches.firstKey());
            level2 = collectInnerBeats(orderedBranches.get(levels.get(index + 1)), level2);

            if (!compareBeatSpan(level1, level2, metricalStructure)) {
                throw new BranchingWellFormednessException("Branches overlap");
            }

            index++;

        }

    }

    private boolean compareBeatSpan(Set<Beat> level1, Set<Beat> level2, MetricalContainer metricalStructure) {

        Beat level1StartBeat = getEarliestBeat(level1, metricalStructure);
        Beat level1EndBeat = getLatestBeat(level1, metricalStructure);

        Set<Beat> span1 = getSpanBetweenBeats(level1StartBeat, level1EndBeat);

        Beat level2StartBeat = getEarliestBeat(level2, metricalStructure);
        Beat level2EndBeat = getLatestBeat(level2, metricalStructure);

        Set<Beat> span2 = getSpanBetweenBeats(level2StartBeat, level2EndBeat);

        for (Beat beat : span2) {
            if (span1.contains(beat)) {
                return false;
            }
        }

        return true;
    }

    private Set<Beat> getSpanBetweenBeats(Beat beat1, Beat beat2) {
        Set<Beat> span = new HashSet<>();

        Beat currentBeat = beat1;
        while (currentBeat != beat2.getNextBeat()) {
            span.add(currentBeat);
            currentBeat = currentBeat.getNextBeat();
        }

        return span;
    }

    private Beat getEarliestBeat(Set<Beat> beats, MetricalContainer metricalStructure) {
        int position = metricalStructure.getMetricalBeatsList().size();

        for (Beat beat : beats) {
            int index = 0;
            while (index < metricalStructure.getMetricalBeatsList().size()) {
                if (beat == metricalStructure.getMetricalBeatsList().get(index)) {
                    if (index < position) {
                        position = index;
                    }
                    break;
                }
                index++;
            }
        }

        return metricalStructure.getBeatAtPosition(position);

    }

    private Beat getLatestBeat(Set<Beat> beats, MetricalContainer metricalStructure) {
        int position = 0;

        for (Beat beat : beats) {
            int index = 0;
            while (index < metricalStructure.getMetricalBeatsList().size()) {
                if (beat == metricalStructure.getMetricalBeatsList().get(index)) {
                    if (index > position) {
                        position = index;
                    }
                    break;
                }
                index++;
            }
        }

        return metricalStructure.getBeatAtPosition(position);

    }

    private Set<Beat> collectInnerBeats(Branch branch, Set<Beat> beats) {

        beats.add(branch.getAssociatedBeat());

        if (branch.getChildBranches().isEmpty()) {
            return beats;
        }

        TreeMap<Integer, Branch> orderedBranches = new TreeMap<>(branch.getChildBranches());

        for (Integer i : orderedBranches.keySet()) {

            collectInnerBeats(orderedBranches.get(i), beats);

        }

        return beats;

    }

    /**
     * Verifies that the GWFR 1 rules is satisfied, which states that 
     * 'Any contiguous sequence of pitch-events, drum beats, or the like can constitute a group, and only contiguous sequences can constitute a group.'
     * Ensures that all the groups only span contiguous Beats objects.
     * @param beats the span of contiguous beats mapped by the grouping structure
     * 
     */
    public void verifyGWFR1(List<Beat> beats){
        Beat lastBeat = null;

        for (Beat beat : beats) {
            if (lastBeat != null) {
                if (lastBeat.getNextBeat() != beat) {
                    throw new GroupingWellFormednessException("Group does not map a sequence of beats");
                }

            }
            lastBeat = beat;
        }

    }

  /**Assesses whether the list of groups fit the GWFRs constraints.
   * 
   * @param subgroups all the groups to be checked
   * 
   */
    public void assessGroupingWellformedness(List<Group> subgroups)
         {

        verifyGWFR5(subgroups);

        List<Beat> beats = new ArrayList<Beat>();

        for (Group g : subgroups) {
            for (Beat b : g.getMetricalBeatSpan()) {
                beats.add(b);
            }
        }

        verifyGWFR1(beats);

    }

    /**Verifies that the given list of Group objects fits the GWFR 5 rule constraint, 
     * which is stated as  'If a group G1 contains a smaller group G2, then G1 must be exhaustively partitioned into smaller groups.'
     * 
     * Each HighLevelGroup must map more than one group.

     * 
     * @param subgroups the list of groups to check

     */
    public void verifyGWFR5(List<Group> subgroups) {
        if (subgroups.size() < 2) {
            throw new GroupingWellFormednessException("Grouping subgrouping structure must be an exhaustive partition "
                    + "of the group into smaller groups - GWFR 5");
        }
    }

}
