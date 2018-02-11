/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Phrase_Generator;

import Elements.DurationEnum;
import Grammar_Elements.*;
import Grammar_Elements.GroupingStructure.*;
import Grammar_Elements.MetricalStructure.*;
import Grammar_Elements.ReductionBranches.*;
import java.util.List;

/**
 * An abstract class that provides methods for use in music generation using
 * GTTM style musical grammars
 *
 * @author Alexander Dodd
 */
abstract public class Generator {

    private ProlongationalBranch topPBranch;
    private TimeSpanReductionBranch topTBranch;

    private MetricalContainer mContainer;

    private ProlongationalGroup pGroup;
    private HighLevelGroup gGroup;

    public Generator() {

    }


    /**Returns an int indicating how many beats a given DurationEnum spans
     *
     * @param duration the DurationEnum to assess the span distance
     * @return the amount of beats spanned after the instantiation of the duration, not
     * including its original instantiation beat
     */
    protected int durationToBeat(DurationEnum duration) {
       
            return duration.getLength() - 1;
    }

    /**
     * Used to assign the GrammarContainer elements to the fields of the Generator
     * @param grammar the GrammarContainer to be added to the generator
     */
    protected void assignFieldVariables(GrammarContainer grammar) {
        topPBranch = grammar.getTopProlongationalReductionBranch();
        topTBranch = grammar.getTopTimeSpanReductionBranch();

        pGroup = grammar.getTopProlongationalGroup();
        gGroup = grammar.getTopLevelGroup();

        mContainer = grammar.getMetricalStructure();

    }

    /**
     * 
     * @return the top prolongational branch of the prolongational reduction
     */
    public ProlongationalBranch getTopPBranch() {
        return topPBranch;
    }

    /**
     * 
     * @return the top time span branch of the time span reduction
     */
    public TimeSpanReductionBranch getTopTBranch() {
        return topTBranch;
    }

    /**
     * 
     * @return the metrical structure of the grammar
     */
    public MetricalContainer getmContainer() {
        return mContainer;
    }

    /**
     * 
     * @return the top prolongational group of the grammar
     */
    public ProlongationalGroup getpGroup() {
        return pGroup;
    }

    /**
     * 
     * @return the top high level group of the grammar
     */
    public HighLevelGroup getgGroup() {
        return gGroup;
    }

}
