/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches;

import java.io.Serializable;

import uk.ac.kent.computing.gttm.Grammar_Elements.ExceptionClasses.BranchingWellFormednessException;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;

/**A special branch of the time-span reduction which describes a two element cadence
 * within the grouping structure hierarchy.
 *
 *
 * @author Alexander Dodd
 */
public class CadencedTimeSpanReductionBranch extends TimeSpanReductionBranch 
implements Serializable{

    private Beat cadenceEnd;
    private Beat cadenceStart;

    /**
     * 
     * @param cadenceEnd The final beat of the cadence
     * @param cadenceStart The first beat of the cadence
     * @param level the level of this branching structure
     * @throws BranchingWellFormednessException Thrown if a branching structure constraint
     * is violated.
     */
    public CadencedTimeSpanReductionBranch(Beat cadenceEnd, Beat cadenceStart, int level) throws BranchingWellFormednessException {
        super(cadenceEnd, level);

        this.cadenceEnd = cadenceEnd;
        this.cadenceStart = cadenceStart;

        if (beats.contains(cadenceStart) || cadenceEnd == cadenceStart) {
            throw new BranchingWellFormednessException("Error constructing cadenced branch."
                    + "Attempted to try to associated two branches with one pitch event.");
        } else {
            beats.add(cadenceStart);
        }

    }

    /**
     * 
     * @return the final beat of the cadence
     */
    public Beat getCadenceEnd() {
        return cadenceEnd;
    }

    /**
     * 
     * @return the first beat of the cadence
     */
    public Beat getCadenceStart() {
        return cadenceStart;
    }
    
   

}
