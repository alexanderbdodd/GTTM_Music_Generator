/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import uk.ac.kent.computing.gttm.Grammar_Elements.ExceptionClasses.BranchingWellFormednessException;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;

/**Describes a branch of the time-span reduction hierarchy
 *
 * @author Alexander Dodd
 */
public class TimeSpanReductionBranch extends Branch implements Serializable{

    protected final static Set<Beat> beats = new HashSet<>();

    /**
     * 
     * @param pitchEvent The associated pitch event
     * @param level the level of this branch. A value of 0 indicates that this is a top level branch.
     * @throws BranchingWellFormednessException thrown if a branching well formedness
     * constraint is violated
     */
    public TimeSpanReductionBranch(Beat pitchEvent, int level)
            throws BranchingWellFormednessException {
        super(pitchEvent, level);

        if (beats.contains(pitchEvent)) {
            throw new BranchingWellFormednessException("Attempted to try to associated two branches with one pitch event.");
        } else {
            beats.add(pitchEvent);
        }
    }

    @Override
    public void addChildBranch(Branch branch) 
            throws BranchingWellFormednessException {
        if(branch.getClass() == TimeSpanReductionBranch.class 
                || branch.getClass() == CadencedTimeSpanReductionBranch.class)
        {super.addChildBranch(branch); }
        else{
            throw new BranchingWellFormednessException("Error attempting to "
                    + "add a non time-span reduction branch");
        }
    }

}
