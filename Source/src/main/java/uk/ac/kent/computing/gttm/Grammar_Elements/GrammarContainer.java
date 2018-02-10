/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Grammar_Elements;

import java.io.Serializable;
import java.util.*;

import uk.ac.kent.computing.gttm.GTTM_Analyser.WellFormednessAnalyser;
import uk.ac.kent.computing.gttm.Grammar_Elements.ExceptionClasses.BranchingWellFormednessException;
import uk.ac.kent.computing.gttm.Grammar_Elements.ExceptionClasses.GroupingWellFormednessException;
import uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure.HighLevelGroup;
import uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure.ProlongationalGroup;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.MetricalContainer;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.ProlongationalBranch;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.TimeSpanReductionBranch;

/**
 *A class that acts as a container for a full GTTM analysis of a piece of music.
 * Well formedness constraints are checked during construction of this container.
 * @author Alexander Dodd
 */
public class GrammarContainer implements Serializable{

    //the topmost branch of the prolongational reduction analysis
    private final ProlongationalBranch topProlongationalReductionBranch;
    //the topmost branch of the time-span reduction analysis
    private final TimeSpanReductionBranch topTimeSpanReductionBranch;
    //the container for the metrical structure of the piece
    private final MetricalContainer metricalStructure;
    //the container for the topmost group of the grouping structure analysis
    private final HighLevelGroup topLevelGroup;
    //the topmost group of the prolongational grouping structure analysis.
    private final ProlongationalGroup topProlongationalGroup;

    /**
     * Constructs a grammar container object that encapsulates a grammatical analysis
     * of a piece
     * @param topProlongationalBranch the topmost branch of the prolongational analysis
     * @param topTimeSpanReductionBranch the topmost branch of the time-span reduction analysis
     * @param metricalStructure the metrical structure of the piece
     * @param topLevelGroup the topmost group of the grouping structure analysis
     * @param topProlongationalGroup the topmost group of the prolongational grouping structure analysis
     * @throws GroupingWellFormednessException Thrown if the grouping structure analysis does not span the 
     * whole metrical span
     * @throws BranchingWellFormednessException Thrown if various branching well formedness constraints are
     * violated.
     */
    public GrammarContainer(ProlongationalBranch topProlongationalBranch, TimeSpanReductionBranch topTimeSpanReductionBranch,
            MetricalContainer metricalStructure, HighLevelGroup topLevelGroup,
            ProlongationalGroup topProlongationalGroup)
            throws GroupingWellFormednessException, BranchingWellFormednessException {
        this.topProlongationalReductionBranch = topProlongationalBranch;
        this.topTimeSpanReductionBranch = topTimeSpanReductionBranch;
        this.metricalStructure = metricalStructure;
        this.topLevelGroup = topLevelGroup;
        this.topProlongationalGroup = topProlongationalGroup;

        checkWellFormedness();

    }

    private void checkWellFormedness()
            throws GroupingWellFormednessException, BranchingWellFormednessException {
        WellFormednessAnalyser analyser = WellFormednessAnalyser.getInstance();

        analyser.verifyGWFR2(metricalStructure, topLevelGroup);
        analyser.checkBranchingWellFormedness(this.topProlongationalReductionBranch, metricalStructure);
        analyser.checkBranchingWellFormedness(this.topTimeSpanReductionBranch, metricalStructure);

    }

    /**
     * 
     * @return the topmost prolongational reduction branch
     */
    public ProlongationalBranch getTopProlongationalReductionBranch() {
        return topProlongationalReductionBranch;
    }

    /**
     * 
     * @return the topmost time-span reduction branch
     */
    public TimeSpanReductionBranch getTopTimeSpanReductionBranch() {
        return topTimeSpanReductionBranch;
    }

    /**
     * 
     * @return the metrical container detailing the metrical analysis
     */
    public MetricalContainer getMetricalStructure() {
        return metricalStructure;
    }

    /**
     * 
     * @return the highest grouping structure group
     */
    public HighLevelGroup getTopLevelGroup() {
        return topLevelGroup;
    }

    /**
     * 
     * @return the highest level of the prolongational grouping structure
     */
    public ProlongationalGroup getTopProlongationalGroup() {
        return topProlongationalGroup;
    }

}
