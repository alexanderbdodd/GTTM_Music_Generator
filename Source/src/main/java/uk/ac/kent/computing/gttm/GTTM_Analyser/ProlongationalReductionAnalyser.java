/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.GTTM_Analyser;

import java.util.*;

import uk.ac.kent.computing.gttm.Elements.AttackEvent;
import uk.ac.kent.computing.gttm.Elements.AttackEventChord;
import uk.ac.kent.computing.gttm.Elements.Key;
import uk.ac.kent.computing.gttm.Elements.KeyLetterEnum;
import uk.ac.kent.computing.gttm.Elements.Scale;
import uk.ac.kent.computing.gttm.GTTM_Analyser.Exceptions.ProlongationalReductionAnalysisException;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.Branch;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.ProlongationalBranch;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.ProlongationalTypeEnum;
import uk.ac.kent.computing.gttm.Manipulators.IntervalConstructor;
import uk.ac.kent.computing.gttm.Manipulators.IntervalEnum;
import uk.ac.kent.computing.gttm.Manipulators.ScaleConstructor;

/**
 * A singleton class that is used to perform prolongational reduction analysis
 * on a set of inputs.
 *
 * Only implements PRPR3 at the present time.
 *
 * @author Alexander Dodd
 */
public class ProlongationalReductionAnalyser {

    //weightings for the different stability types
    public final static double BRANCHING_STABILITY_WEIGHT = 1.0;
    public final static double MELODIC_STABILITY_A_WEIGHT = 2.5;
    public final static double MELODIC_STABILITY_B_WEIGHT = 2.0;
    public final static double PITCH_STABILITY_WEIGHT = 1.0;
    public final static double HARMONIC_STABILITY_A_WEIGHT = 1.0;
    public final static double HARMONIC_STABILITY_B_WEIGHT = 1.0;

    private static ProlongationalReductionAnalyser instance = null;

    /**
     *
     * @return the singleton object associated with this class
     */
    public static ProlongationalReductionAnalyser getInstance() {
        if (instance == null) {
            instance = new ProlongationalReductionAnalyser();
        }

        return instance;
    }

    private ProlongationalReductionAnalyser() {

    }

    /**
     * Provides an assessment of the stability of a given ProlongationalBranch
     * in relation to a given parent. This is assessed using the stability
     * criteria described in the GTTM. 1. Branching stability, 2. Melodic
     * Stability, 3. Pitch stability, 4. Harmonic stability.
     *
     * @param parentBranch the parent against which to assess the subBranch's
     * stability.
     * @param subBranch a branch to be assessed for stability in relation to the
     * parent branch.
     * @param progression true if the sub branch pitch constitutes a progression
     * from the parent branch pitch
     * @return a rating based on stability of the given branch with its parent
     * @throws ProlongationalReductionAnalysisException if the given branch is
     * not a sub branch
     */
    public double assessPRPR3(Beat parentBranch, Beat subBranch, boolean progression)
            throws ProlongationalReductionAnalysisException {

        //at the moment these ratings are treated as being equal in 
        //value on the analysis level. The theory suggests that
        //it is likely some will be more important than others.
        double branchRating = assessBranchingStability(parentBranch, subBranch);
        double melodicRating = assessMelodicStabilityA(parentBranch, subBranch);
        double melodicRatingb = assessMelodicStabilityB(parentBranch, subBranch);
        double harmonicRating = assessHarmonicRelationA(parentBranch.getAttackEvent(),
                subBranch.getAttackEvent()) * HARMONIC_STABILITY_A_WEIGHT;

        if (progression) {
            harmonicRating += (assessHarmonicRelationB(parentBranch.getAttackEvent(),
                    subBranch.getAttackEvent(), isRightBranching(parentBranch, subBranch))
                    * HARMONIC_STABILITY_B_WEIGHT);
        }

        double harmonicWeight;
        if (progression) {
            harmonicWeight = HARMONIC_STABILITY_A_WEIGHT + HARMONIC_STABILITY_B_WEIGHT;
        } else {
            harmonicWeight = HARMONIC_STABILITY_A_WEIGHT;
        }

        double pitchRating;
        try {
            pitchRating = assessPitchStability(parentBranch, subBranch);
        } catch (ProlongationalReductionAnalysisException e) {
            pitchRating = -1;
        }

        //if a pitch rating has not been established, calculate 
        //rating excluding the pitch rating value
        if (pitchRating == -1) {
            return (((branchRating * BRANCHING_STABILITY_WEIGHT)
                    + (melodicRating * MELODIC_STABILITY_A_WEIGHT)
                    + (melodicRatingb * MELODIC_STABILITY_B_WEIGHT)
                    + (harmonicRating))
                    / (BRANCHING_STABILITY_WEIGHT + MELODIC_STABILITY_A_WEIGHT
                    + MELODIC_STABILITY_B_WEIGHT));
            //else calculate the rating including the pitch rating
        } else {
            return (((branchRating * BRANCHING_STABILITY_WEIGHT)
                    + (melodicRating * MELODIC_STABILITY_A_WEIGHT)
                    + (melodicRatingb * MELODIC_STABILITY_B_WEIGHT)
                    + (pitchRating * PITCH_STABILITY_WEIGHT)
                    + harmonicRating)
                    / (BRANCHING_STABILITY_WEIGHT + MELODIC_STABILITY_A_WEIGHT
                    + MELODIC_STABILITY_B_WEIGHT + PITCH_STABILITY_WEIGHT
                    + harmonicWeight));
        }
    }

    /**
     * Provides an assessment of the stability of a given ProlongationalBranch
     * in relation to a given parent. This is assessed using the stability
     * criteria described in the GTTM. 1. Branching stability, 2. Melodic
     * Stability, 3. Pitch stability, 4. Harmonic stability.
     *
     * @param parentBranch the parent against which to assess the subBranch's
     * stability.
     * @param subBranch a branch to be assessed for stability in relation to the
     * parent branch.
     * @param localScale the local scale against which to judge pitch stability
     * @param progression true if the sub branch pitch constitutes a progression
     * from the parent branch pitch
     * @return a rating based on stability of the given branch with its parent
     * @throws ProlongationalReductionAnalysisException if the given branch is
     * not a sub branch
     */
    public double assessPRPR3(Beat parentBranch, Beat subBranch, Scale localScale, boolean progression)
            throws ProlongationalReductionAnalysisException {

        //at the moment these ratings are treated as being equal in 
        //value on the analysis level. The theory suggests that
        //it is likely some will be more important than others.
        double branchRating = assessBranchingStability(parentBranch, subBranch);
        double melodicRating = assessMelodicStabilityA(parentBranch, subBranch);
        double melodicRatingb = assessMelodicStabilityB(parentBranch, subBranch);
        double harmonicRating = assessHarmonicRelationA(parentBranch.getAttackEvent(),
                subBranch.getAttackEvent()) * HARMONIC_STABILITY_A_WEIGHT;

        if (progression) {
            harmonicRating += (assessHarmonicRelationB(parentBranch.getAttackEvent(),
                    subBranch.getAttackEvent(), isRightBranching(subBranch, parentBranch)) * HARMONIC_STABILITY_B_WEIGHT);
        }

        double pitchRating;
        try {
            pitchRating = assessPitchStability(subBranch, localScale);
        } catch (ProlongationalReductionAnalysisException e) {
            pitchRating = -1;
        }

        double harmonicWeight;
        if (progression) {
            harmonicWeight = HARMONIC_STABILITY_A_WEIGHT + HARMONIC_STABILITY_B_WEIGHT;
        } else {
            harmonicWeight = HARMONIC_STABILITY_A_WEIGHT;
        }

        //if a pitch rating has not been established, calculate 
        //rating excluding the pitch rating value
        if (pitchRating == -1) {
            return (((branchRating * BRANCHING_STABILITY_WEIGHT)
                    + (melodicRating * MELODIC_STABILITY_A_WEIGHT)
                    + (melodicRatingb * MELODIC_STABILITY_B_WEIGHT)
                    + (harmonicRating))
                    / (BRANCHING_STABILITY_WEIGHT + MELODIC_STABILITY_A_WEIGHT
                    + MELODIC_STABILITY_B_WEIGHT
                    + harmonicWeight));
            //else calculate the rating including the pitch rating
        } else {
            return (((branchRating * BRANCHING_STABILITY_WEIGHT)
                    + (melodicRating * MELODIC_STABILITY_A_WEIGHT)
                    + (melodicRatingb * MELODIC_STABILITY_B_WEIGHT)
                    + (pitchRating * PITCH_STABILITY_WEIGHT)
                    + harmonicRating)
                    / (BRANCHING_STABILITY_WEIGHT + MELODIC_STABILITY_A_WEIGHT
                    + MELODIC_STABILITY_B_WEIGHT + PITCH_STABILITY_WEIGHT
                    + harmonicWeight));
        }
    }

    /**
     * Provides an assessment of the stability of a given ProlongationalBranch
     * in relation to a given parent. This is assessed using the stability
     * criteria described in the GTTM. 1. Branching stability, 2. Melodic
     * Stability, 3. Pitch stability, 4. Harmonic stability.
     *
     * @param subBranch a branch to be assessed for stability in relation to its
     * parent.
     * @param progression true if the sub branch pitch constitutes a progression
     * from the parent branch pitch
     * @return a rating based on stability of the given branch with its parent
     * @throws ProlongationalReductionAnalysisException if the given branch is
     * not a sub branch
     */
    public double assessPRPR3(ProlongationalBranch subBranch, boolean progression)
            throws ProlongationalReductionAnalysisException {
        if (subBranch.getParent() == null) {
            throw new ProlongationalReductionAnalysisException("Attempting to "
                    + "assess branching stability of a top level branch");
        }

        ProlongationalBranch parent = (ProlongationalBranch) subBranch.getParent();

        return assessPRPR3(parent.getAssociatedBeat(), subBranch.getAssociatedBeat(), progression);
    }

    /**
     * Provides an assessment of the stability of a given ProlongationalBranch
     * in relation to a given parent. This is assessed using the stability
     * criteria described in the GTTM. 1. Branching stability, 2. Melodic
     * Stability, 3. Pitch stability, 4. Harmonic stability.
     *
     * @param parent the parent AttackEvent
     * @param child the child AttackEvent
     * @param rightBranching true if the child is a right branch of the parent,
     * false if it is left branching
     * @param localScale the local scale against which to judge pitch stability
     * @param progression true if the sub branch pitch constitutes a progression
     * @return a rating between 0.0 and 1.0. A rating below 0.5 indicates that
     * the child AttackEvent forms a non preferable branching relation to the proposed
     * parent AttackEvent (i.e. it isn't a candidate child of the parent event). A rating of
     * 0.5 indicates no preference for its being a child. A rating above 0.5 indicates a preferable
     * relation of the child event being a branch off of the branch of the parent event.
     */
    public double assessPRPR3(AttackEvent parent, AttackEvent child, boolean rightBranching,
            boolean progression, Scale localScale) {
        Beat.breakChain();
        Beat b1 = new Beat(2);
        Beat b2 = new Beat(1);

        b1.setPosition(0);
        b2.setPosition(1);

        if (rightBranching) {
            b1.setAttackEvent(parent);
            b2.setAttackEvent(child);

            return assessPRPR3(b1, b2, progression);
        } else {
            b1.setAttackEvent(child);
            b2.setAttackEvent(parent);

            return assessPRPR3(b2, b1, progression);
            //return assessPRPR3(b1, b2, localScale, progression);
        }

    }

    /**
     * Provides an analysis of two AttackEvents to see what kind of
     * prolongational branching type the second AttackEvent constitutes in
     * relation to the first event.
     *
     * @param parent the AttackEvent to be judged as a parent branch
     * @param child the AttackEvent to be considered as a child branch of the
     * parent.
     *
     * @return the ProlongationalTypeEnum that constitutes the branching type of
     * the child AttackEvent from the parent AttackEvent
     */
    public ProlongationalTypeEnum assessProlongationalType(AttackEvent parent, AttackEvent child) {
        ProlongationalTypeEnum progType;

        if (parent == null) {
            return ProlongationalTypeEnum.PROGRESSION;
        }

        if (child.compare(child, parent) == 0) {
            if (child.getClass() != AttackEventChord.class
                    && parent.getClass() != AttackEventChord.class) {
                progType = ProlongationalTypeEnum.STRONG_PROLONGATION;
            } else {
                AttackEventChord subChrd = (AttackEventChord) child;
                AttackEventChord parentEventChrd = (AttackEventChord) parent;

                if (subChrd.getChord().getKeys()
                        .equals(parentEventChrd.getChord().getKeys())) {
                    progType = ProlongationalTypeEnum.STRONG_PROLONGATION;
                } else {
                    progType = ProlongationalTypeEnum.WEAK_PROLONGATION;
                }
            }
        } else {

            progType = ProlongationalTypeEnum.PROGRESSION;
        }

        return progType;
    }

    /**
     * Provides a rating as to how stable a branch a given AttackEvent forms
     * with a given parent AttackEvent.
     *
     * If the given sub branch beat would constitute a right branch from the
     * parent beat, then a beat that forms a strong prolongation with the
     * proposed parent returns a strong rating, a weak prolongation returns a
     * middling rating, and a progression returns a rating of 0.0.
     *
     * If the given sub branch beat would constitute a left branch from the
     * parent beat, then a beat that forms a progression to the parent beat
     * would return a strong rating, a weak prolongation returns a middling
     * rating, and a strong prolongation return a rating of 0.0.
     *
     * @param parentBranch an AttackEvent to be judged as the parent pitch event
     * to a given subBranch.
     * @param subBranch An AttackEvent to be judged as the child pitch event of
     * the given parentBranch beat.
     * @return a rating between 0.0 and 1.0 describing how stable a branch the
     * subBranch beat forms from the given parentBranch. A rating of 1.0
     * indicates very strong stability. A rating of 0.0 indicates very weak
     * stability. A rating of 0.5 indicates mild stability.
     *
     */
    public double assessBranchingStability(Beat parentBranch, Beat subBranch) {

        boolean rightBranching = isRightBranching(subBranch, parentBranch);

        ProlongationalTypeEnum progType;

        AttackEvent sub = subBranch.getAttackEvent();
        AttackEvent parentEvent = parentBranch.getAttackEvent();

        progType = assessProlongationalType(parentEvent, sub);

        if (rightBranching && progType == ProlongationalTypeEnum.STRONG_PROLONGATION) {
            return 1;
        }
        if (rightBranching && progType == ProlongationalTypeEnum.PROGRESSION) {
            return 0.2;
        }

        if (!rightBranching && progType == ProlongationalTypeEnum.STRONG_PROLONGATION) {
            return 0.2;
        }

        if (!rightBranching && progType == ProlongationalTypeEnum.PROGRESSION) {
            return 1;
        }

        //if the counter reaches here, then the branch forms a weak prolongation, 
        // which has a middle ranking stability in this implementation.
        return 0.5;
    }

    /**
     * Provides a rating as to how stable a branch a given AttackEvent forms
     * with a given parent AttackEvent.
     *
     * If the given sub branch beat would constitute a right branch from the
     * parent beat, then a beat that forms a strong prolongation with the
     * proposed parent returns a strong rating, a weak prolongation returns a
     * middling rating, and a progression returns a rating of 0.0.
     *
     * If the given sub branch beat would constitute a left branch from the
     * parent beat, then a beat that forms a progression to the parent beat
     * would return a strong rating, a weak prolongation returns a middling
     * rating, and a strong prolongation return a rating of 0.0.
     *
     *
     * @param subBranch A sub branch, the associated AttackEvent to be judged
     * against the parent branch's AttackEvent for stability.
     * @return a rating between 0.0 and 1.0 describing how stable a branch the
     * subBranch beat forms from the given parentBranch. A rating of 1.0
     * indicates very strong stability. A rating of 0.0 indicates very weak
     * stability. A rating of 0.5 indicates mild stability.
     * @throws ProlongationalReductionAnalysisException thrown if the sub branch
     * does not have a parent branch
     */
    public double assessBranchingStability(ProlongationalBranch subBranch)
            throws ProlongationalReductionAnalysisException {
        if (subBranch.getParent() == null) {
            throw new ProlongationalReductionAnalysisException("Attempting to "
                    + "assess branching stability of a top level branch");
        }

        ProlongationalBranch parent = (ProlongationalBranch) subBranch.getParent();

        return assessBranchingStability(parent.getAssociatedBeat(), subBranch.getAssociatedBeat());
    }

    /**
     * Assesses the melodic stability of a given subBranch beat with a given
     * parentBranch beat. This is assessed in accordance with the rule that 'a
     * connection between two events is melodically more stable if the interval
     * between them is smaller (with the exception of the octave which is
     * relatively stable).'
     *
     * @param parentBranch an AttackEvent to be judged as the parent pitch event
     * to a given subBranch.
     * @param subBranch An AttackEvent to be judged as the child pitch event of
     * the given parentBranch beat.
     * @return a rating between 0.0 and 1.0 describing how stable a branch the
     * subBranch beat forms from the given parentBranch. A rating of 1.0
     * indicates very strong stability. A rating of 0.0 indicates very weak
     * stability. A rating of 0.5 indicates mild stability.
     */
    public double assessMelodicStabilityA(Beat parentBranch, Beat subBranch) {

        AttackEvent sub = subBranch.getAttackEvent();
        AttackEvent parent = parentBranch.getAttackEvent();

        if (sub.compare(sub, parent) == 0
                || (sub.getPosition() == parent.getPosition() && sub.getNote() == parent.getNote())
                || (sub.getPosition().getPosition() == (parent.getPosition().getPosition() + 1)
                && sub.getNote() == parent.getNote())
                || (sub.getPosition().getPosition() == (parent.getPosition().getPosition() - 1)
                && sub.getNote() == parent.getNote())) {
            return 1;
        }

        int distance = Math.abs(IntervalConstructor.getInstance()
                .calculateDistance(parent, sub));

        double rating = 1;

        int i = 0;

        while (i < distance) {

            rating = rating - 0.05;
            i++;

        }

        if (rating < 0) {
            rating = 0.0;
        }

        double rounded = Math.round(rating * 10);
        double divided = rounded / 10;
        return divided;

    }

    /**
     * Assesses the melodic stability of a given subBranch beat with a given
     * parentBranch beat. This is assessed in accordance with the rule that 'a
     * connection between two events is melodically more stable if the interval
     * between them is smaller (with the exception of the octave which is
     * relatively stable).'
     *
     * @param subBranch A sub branch, the associated AttackEvent to be judged
     * against the parent branch's AttackEvent for stability.
     *
     * @return a rating between 0.0 and 1.0 describing how stable a branch the
     * subBranch beat forms from the given parentBranch. A rating of 1.0
     * indicates very strong stability. A rating of 0.0 indicates very weak
     * stability. A rating of 0.5 indicates mild stability.
     */
    public double assessMelodicStabilityA(Branch subBranch)
            throws ProlongationalReductionAnalysisException {
        if (subBranch.getParent() == null) {
            throw new ProlongationalReductionAnalysisException("Attempting to "
                    + "assess branching stability of a top level branch");
        }

        ProlongationalBranch parent = (ProlongationalBranch) subBranch.getParent();

        return assessMelodicStabilityA(parent.getAssociatedBeat(),
                subBranch.getAssociatedBeat());
    }

    /**
     * Assesses the diatonic stability of a given subBranch beat with a given
     * parentBranch beat. This is assessed in accordance with the rule that 'a
     * connection between two events is more stable if they involve or imply a
     * common diatonic collection.'
     *
     * @param parentBranch an AttackEvent to be judged as the parent pitch event
     * to a given subBranch.
     * @param subBranch An AttackEvent to be judged as the child pitch event of
     * the given parentBranch beat.
     * @return a rating between 0.0 and 1.0 describing how stable a branch the
     * subBranch beat forms from the given parentBranch. A rating of 1.0
     * indicates very strong stability. A rating of 0.0 indicates very weak
     * stability. A rating of 0.5 indicates mild stability.
     */
    public double assessPitchStability(Beat parentBranch, Beat subBranch)
            throws ProlongationalReductionAnalysisException {
        List<KeyLetterEnum> beatSpan = new ArrayList<>();

        boolean rightBranching = isRightBranching(subBranch, parentBranch);
        Beat b;
        Beat bEnd;
        if (!rightBranching) {
            b = subBranch;
            bEnd = parentBranch;

        } else {
            b = parentBranch;
            bEnd = subBranch;
        }

        while (b != bEnd) {
            if (b.getAttackEvent() != null) {
                beatSpan.add(b.getAttackEvent().getNote());
            }
            b = b.getNextBeat();
        }

        List<Scale> scales = ScaleConstructor.getInstance().assessDiatonicScale(beatSpan);

        if (scales.isEmpty()) {
            throw new ProlongationalReductionAnalysisException("No diatonic collection identified");
        } else {
            //at the moment, if there's more than one scale there's no implementation to decide which
            //scale is most likely to be a candidate. This implementation just chooses the first scale found.
            //In the future it would be a good idea to find the suggested scale based on the attack event associated
            //with the topmost branch.

            Branch topmostBranch;

            Scale chosenScale = null;

            for (Scale scl : scales) {
                if (scl.getTonic() == parentBranch.getAttackEvent().getNote()) {
                    chosenScale = scl;
                }
            }

            if (chosenScale == null) {
                chosenScale = scales.get(0);
            }

            if (chosenScale.getNotes().contains(subBranch.getAttackEvent().getNote())) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Assesses the diatonic stability of a given subBranch beat with a given
     * parentBranch beat. This is assessed in accordance with the rule that 'a
     * connection between two events is more stable if they involve or imply a
     * common diatonic collection.'
     *
     *
     * @param subBranch An AttackEvent to be judged as the child pitch event of
     * the given parentBranch beat.
     * @param localScale the scale against which to judge the stability of the
     * AttackEvent
     * @return a rating between 0.0 and 1.0 describing how stable a branch the
     * subBranch beat forms from the given parentBranch. A rating of 1.0
     * indicates very strong stability. A rating of 0.0 indicates very weak
     * stability. A rating of 0.5 indicates mild stability.
     */
    public double assessPitchStability(Beat subBranch, Scale localScale)
            throws ProlongationalReductionAnalysisException {

        if (localScale.getNotes().contains(subBranch.getAttackEvent().getNote())) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Assesses the diatonic stability of a given subBranch beat with a given
     * parentBranch beat. This is assessed in accordance with the rule that 'A
     * connection between two events is more stable if they involve or imply a
     * common diatonic collection.'
     *
     * @param subBranch A sub branch, the associated AttackEvent to be judged
     * against the parent branch's AttackEvent for stability.
     *
     * @return a rating between 0.0 and 1.0 describing how stable a branch the
     * subBranch beat forms from the given parentBranch. A rating of 1.0
     * indicates very strong stability. A rating of 0.0 indicates very weak
     * stability. A rating of 0.5 indicates mild stability.
     */
    public double assessPitchStability(Branch subBranch)
            throws ProlongationalReductionAnalysisException {
        if (subBranch.getParent() == null) {
            throw new ProlongationalReductionAnalysisException("Attempting to "
                    + "assess branching stability of a top level branch");
        }

        ProlongationalBranch parent = (ProlongationalBranch) subBranch.getParent();

        return assessPitchStability(parent.getAssociatedBeat(), subBranch.getAssociatedBeat());
    }

    /**
     * Assesses the melodic stability of a given subBranch beat with a given
     * parentBranch beat. This is assessed in accordance with the rule that 'an
     * ascending melodic progression is more stable as a right branching
     * structure; a descending one is more stable as a left branching
     * structure.'
     *
     * @param parentBranch an AttackEvent to be judged as the parent pitch event
     * to a given subBranch.
     * @param subBranch An AttackEvent to be judged as the child pitch event of
     * the given parentBranch beat.
     * @return a rating between 0.0 and 1.0 describing how stable a branch the
     * subBranch beat forms from the given parentBranch. A rating of 1.0
     * indicates very strong stability. A rating of 0.0 indicates very weak
     * stability. A rating of 0.5 indicates mild stability.
     */
    public double assessMelodicStabilityB(Beat parentBranch, Beat subBranch) {
        boolean rightBranching = isRightBranching(subBranch, parentBranch);

        AttackEvent sub = subBranch.getAttackEvent();
        AttackEvent parent = parentBranch.getAttackEvent();

        if (sub.compare(sub, parent) == 1) {
            if (rightBranching) {
                //ascending right branching structure is stable
                //return 1;
                return intervallicDistanceRating(parent, sub);
            } else {
                return 0;
            }
        }
        if (sub.compare(sub, parent) == 0) {
            //no ascension or decension
            return 0.5;
        }

        if (sub.compare(sub, parent) == -1) {
            if (!rightBranching) {
                //return 1;

                return intervallicDistanceRating(parent, sub);
            } else {
                return 0;
            }
        }

        return 0;
    }

    private double intervallicDistanceRating(AttackEvent parent, AttackEvent child) {
        int distance = Math.abs(IntervalConstructor.getInstance().calculateDistance(parent, child));

        //if there's no distance, then a very low rating
        if (distance == 0) {
            return 0;
        }

        //if the distance is quite small, then a highly preferable rating as this
        //sound more like steady ascension/descension
        if (distance <= 4) {
            return 1.0;
        }
        if (distance <= 6) {
            return 0.9;
        }
        if (distance <= 9) {
            return 0.8;
        }
        if (distance <= 11) {
            return 0.7;
        }
        if (distance <= 13) {
            return 0.65;
        }
        if (distance <= 15) {
            return 0.6;
        } else {
            return 0.55;
        }

    }

    /**
     * Assesses the melodic stability of a given subBranch beat with a given
     * parentBranch beat. This is assessed in accordance with the rule that 'an
     * ascending melodic progression is more stable as a right branching
     * structure; a descending one is more stable as a left branching
     * structure.'
     *
     * @param subBranch A sub branch, the associated AttackEvent to be judged
     * against the parent branch's AttackEvent for stability.
     *
     * @return a rating between 0.0 and 1.0 describing how stable a branch the
     * subBranch beat forms from the given parentBranch. A rating of 1.0
     * indicates very strong stability. A rating of 0.0 indicates very weak
     * stability. A rating of 0.5 indicates mild stability.
     */
    public double assessMelodicStabilityB(Branch subBranch)
            throws ProlongationalReductionAnalysisException {
        if (subBranch.getParent() == null) {
            throw new ProlongationalReductionAnalysisException("Attempting to "
                    + "assess branching stability of a top level branch");
        }

        ProlongationalBranch parent = (ProlongationalBranch) subBranch.getParent();

        return assessMelodicStabilityB(parent.getAssociatedBeat(), subBranch.getAssociatedBeat());
    }

    /**
     * Used to assess whether a given child beat would constitute a right branch
     * in relation to the given parent beat
     *
     * @param subBranch pitch sub beat
     * @param parentBranch pitch parent beat
     * @return true if right branching, false if not.
     */
    public boolean isRightBranching(Beat subBranch, Beat parentBranch) {

        if (subBranch.getPosition() > parentBranch.getPosition()) {
            return true;
        } else {

            return false;
        }
    }

    /**
     * Checks to ensure each level sub branch of a branch have progressively
     * lower PRPR3 ratings
     *
     * @param branch the topmost branch to check down through the hierarchy.
     * @return true if the branching is well formed, and false if it is not.
     */
    public boolean checkPRPR3ByLevel(ProlongationalBranch branch) {
        Beat topBeat = branch.getAssociatedBeat();

        List<Integer> levels = new ArrayList<>();

        levels.addAll(branch.getChildBranches().keySet());
        Collections.sort(levels);

        //check that each sub branch rates lower the lower they are in the hierarchy
        double rating = 200;

        for (Integer child : levels) {

            boolean progression = false;

            ProlongationalBranch pbr = (ProlongationalBranch) branch.getChildBranches().get(child);
            if (pbr.getProlongationType() == ProlongationalTypeEnum.PROGRESSION) {
                progression = true;
            }

            double childRating = assessPRPR3(topBeat, branch.getChildBranches()
                    .get(child).getAssociatedBeat(), progression);

            if (childRating >= rating) {
                return false;
            } else {
                rating = childRating;
            }
        }

        for (Integer child : levels) {
            if (!checkPRPR3ByLevel((ProlongationalBranch) branch.getChildBranches().get(child))) {
                return false;
            }
        }

        return true;

    }

    /**
     * Assesses the harmonic stability of a given child AttackEvent in relation
     * to the given parent AttackEvent according to the rule that 'a connection
     * between two events is harmonically more stable if their roots are closer
     * on the circle of fifths'.
     *
     * @param parentEvent the proposed parent AttackEvent
     * @param childEvent the proposed child AttackEvent
     * @return a rating between 1.0 and 0.0 indicating how preferable the given
     * child event constitutes a direct child branch of the given parent
     * AttackEvent. A rating greater than 0.5 indicates a preferable relation. A
     * rating of 0.5 indicates there there is no preference. A rating below 0.5
     * indicates a non-preferable relation.
     */
    public double assessHarmonicRelationA(AttackEvent parentEvent, AttackEvent childEvent) {
        if (parentEvent.getNote().getKeyNumber() == childEvent.getNote().getKeyNumber()) {
            return 1.0;
        }

        double rating = 1.0;
        KeyLetterEnum upKey = parentEvent.getNote();
        KeyLetterEnum downKey = parentEvent.getNote();

        KeyLetterEnum foundKey = null;

        boolean found = false;

        while (!found) {
            rating -= 0.15;
            upKey = IntervalConstructor.getInstance()
                    .getKeyAtIntervalDistance(upKey, IntervalEnum.PERFECT5TH, true);

            downKey = IntervalConstructor.getInstance()
                    .getKeyAtIntervalDistance(downKey, IntervalEnum.PERFECT5TH, false);

            if (upKey.getKeyNumber() == childEvent.getNote().getKeyNumber()
                    || downKey.getKeyNumber() == childEvent.getNote().getKeyNumber()) {
                found = true;

            }

            if (rating <= 0) {
                return 0;
            }

        }

        if (rating < 0) {
            rating = 0.0;
        }

        return rating;

    }

    /**
     * Assesses the harmonic stability of a given child AttackEvent in relation
     * to the given parent AttackEvent according to the rule that 'a progression
     * that ascends along the circle of fifths is most stable as a
     * right-branching structure; one that descends along the circle of fifths
     * is more stable as a left branching structure'.
     *
     * Events that aren't meant to constitute a progression should not be
     * assessed using this method
     *
     * @param parentEvent the proposed parent AttackEvent
     * @param childEvent the proposed child AttackEvent
     * @param rightBranching true if the childEvent is a right branch of the parentEvent, false
     * if it is a left branch
     * @return a rating between 1.0 and 0.0 indicating how preferable the given
     * child event constitutes a direct child branch of the given parent
     * AttackEvent. A rating greater than 0.5 indicates a preferable relation. A
     * rating of 0.5 indicates there there is no preference. A rating below 0.5
     * indicates a non-preferable relation.
     */
    public double assessHarmonicRelationB(AttackEvent parentEvent, AttackEvent childEvent,
            boolean rightBranching) {
        double rating = 1.0;

        //if they're the same note, then it's not a progression
        if (parentEvent.compare(parentEvent, childEvent) == 0) {
            return 0.0;
        }

        Key currentKey = parentEvent;
        boolean found = false;

        while (!found) {

            if (rightBranching) {
                try {
                    currentKey = IntervalConstructor.getInstance()
                            .getKeyAtIntervalDistance(currentKey, IntervalEnum.PERFECT5TH, true);
                } catch (Exception e) {
                    return 0.0;
                }
            } else {
                try {
                    currentKey = IntervalConstructor.getInstance()
                            .getKeyAtIntervalDistance(currentKey, IntervalEnum.PERFECT5TH, false);
                } catch (Exception e) {
                    return 0.0;
                }
            }

            if (currentKey.compare(currentKey, childEvent) == 0) {
                found = true;
                break;
            }

            rating -= 0.05;

            if (rating < 0.0) {
                return 0.0;
            }

        }

        if (rating < 0) {
            rating = 0;
        }

        return rating;
    }
}
