/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Music_Generator.PhraseGenerator;

import java.util.List;

import uk.ac.kent.computing.gttm.Elements.DurationEnum;
import uk.ac.kent.computing.gttm.Grammar_Elements.GrammarContainer;
import uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure.HighLevelGroup;
import uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure.ProlongationalGroup;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.MetricalContainer;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.ProlongationalBranch;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.TimeSpanReductionBranch;

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

    /**
     * Works out the distance between two beats.
     *
     * @param firstBeat the beat from which to assess the distance
     * @param nextBeat the beat to find the distance to
     * @return the distance between the two beats, or -1 if the nextBeat is in a
     * position before the firstBeat
     *
     */
    protected int distanceToBeat(Beat firstBeat, Beat nextBeat) {
        if (firstBeat.getPosition() > nextBeat.getPosition()) {
            return -1;
        } else {

            return nextBeat.getPosition() - firstBeat.getPosition();
        }

    }

    /**
     * Determines the distance to the next pitch beat from the given beat
     *
     * @param beat the beat from which to assess the distance
     * @param pitchBeats the list of beats allocated as pitch beats
     * @return the distance to the next pitch beat
     */
    protected int distanceToNextPitch(Beat beat, List<Beat> pitchBeats) {
        int distance = 0;
        Beat currentBeat = beat;

        while (currentBeat.getNextBeat() != null
                && !pitchBeats.contains(currentBeat.getNextBeat())) {
            distance++;
            currentBeat = currentBeat.getNextBeat();
        }

        return distance;

    }

    /**Returns an int indicating how many beats a given DurationEnum spans
     *
     * @param duration the DurationEnum to assess the span distance
     * @return the amount of beats spanned after the instantiation of the duration, not
     * including its original instantiation beat
     */
    protected int durationToBeat(DurationEnum duration) {
        switch (duration) {
            case SIXTEENTH:
                return 0;
            case EIGHTH:
                return 1;
            case QUARTER:
                return 2;
            case HALF:
                return 3;
            case WHOLE:
                return 4;
        }

        return 0;
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
