/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.GTTM_Analyser;

import java.util.List;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;

import uk.ac.kent.computing.gttm.Elements.AttackEvent;
import uk.ac.kent.computing.gttm.Elements.KeyLetterEnum;
import uk.ac.kent.computing.gttm.Elements.Scale;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;
import uk.ac.kent.computing.gttm.Manipulators.IntervalConstructor;
import uk.ac.kent.computing.gttm.Manipulators.ScaleConstructor;

/**
 * A singleton class that provides assessment of the time-span reduction
 * preference rules. At the moment this class provides implementations for TSPR2
 * and TSPR3.
 *
 * @author Alexander Dodd
 */
public class TimeSpanReductionAnalyser {

    public static final double CONSONANCE_WEIGHT = 0.5;
    public static final double TONIC_RELATION_WEIGHT = 1;

    public static final double TSPR2_WEIGHT = 1;
    public static final double TSPR3_WEIGHT = 0.5;

    private static TimeSpanReductionAnalyser instance = null;

    /**
     *
     * @return the singleton instance of this class
     */
    public static TimeSpanReductionAnalyser getInstance() {
        if (instance == null) {
            instance = new TimeSpanReductionAnalyser();
        }

        return instance;
    }

    private TimeSpanReductionAnalyser() {
    }

    public double getTSPR2andTSPR3(AttackEvent parent, AttackEvent child, Scale localScale) {
        double tspr2 = assessTSPR2(parent, child, localScale);
        double tspr3 = assessTSPR3a(parent, child);

        return (((tspr2 * TSPR2_WEIGHT) + (tspr3 * TSPR3_WEIGHT))
                / (TSPR2_WEIGHT + TSPR3_WEIGHT));
    }

    /**
     * Assesses how preferable the proposed child AttackEvent would constitute a
     * child branch of the branch associated with the proposed parent
     * AttackEvent. This takes into consideration the consonance of the child
     * event with the proposed parent, and the relation to the local tonic of
     * the two events. A child branch which is relatively consonant with the
     * given parent AttackEvent and relative further away from the tonic than
     * the given parent AttackEvent is preferred.
     *
     * @param parent the AttackEvent to be judged as belonging to the parent
     * branch of the child AttackEvent
     * @param child the AttackEvent under consideration for its relation to the
     * parent AttackEvent
     * @param localScale the local scale against which to consider the child
     * AttackEvent's relation to the tonic
     * @return a rating based on how strongly the given parent Beat rates as a
     * parent branch of the child Beat. A rating towards 1.0 indicates a strong
     * relation according to TSPR2. A rating towards 0.0 indicates a very weak
     * relation. A rating near to 0.5 indicates a mild relation.
     */
    public double assessTSPR2(AttackEvent parent, AttackEvent child, Scale localScale) {
        double consonance = assessConsonance(parent, child);

        double tonicRelation = assessTonicRelationBetweenEvents(parent, child, localScale);

        return ((consonance * CONSONANCE_WEIGHT) + (tonicRelation * TONIC_RELATION_WEIGHT))
                / (CONSONANCE_WEIGHT + TONIC_RELATION_WEIGHT);
    }

    /**
     * Assesses how preferable the proposed child AttackEvent would constitute a
     * child branch of the branch associated with the proposed parent
     * AttackEvent. This takes into consideration the consonance of the child
     * event with the proposed parent, and the relation to the local tonic of
     * the two events. A child branch which is relatively consonant with the
     * given parent AttackEvent and relative further away from the tonic than
     * the given parent AttackEvent is preferred.
     *
     * @param parentBeat the Beat to be judged as being the parent branch of the
     * child AttackEvent
     * @param childBeat the Beat under consideration for its relation to the
     * parent AttackEvent
     * @return a rating based on how strongly the given parent Beat rates as a
     * parent branch of the child Beat. A rating towards 1.0 indicates a strong
     * relation according to TSPR2. A rating towards 0.0 indicates a very weak
     * relation. A rating near to 0.5 indicates a mild relation.
     */
    public double assessTSPR2(Beat parentBeat, Beat childBeat) {

        AttackEvent parent = parentBeat.getAttackEvent();
        AttackEvent child = childBeat.getAttackEvent();

        double consonance = assessConsonance(parent, child);

        List<KeyLetterEnum> keySpan = Beat.getBeatSpan(parentBeat, childBeat);
        List<Scale> localScale = ScaleConstructor.getInstance().assessDiatonicScale(keySpan);

        return assessTSPR2(parent, child, localScale.get(0));

    }

    /**
     * Assesses how preferable a proposed parent AttackEvent constitutes the
     * parent branch of a given child AttackEvent by assessing how strongly the
     * given parent event constitutes a higher melodic pitch than the given
     * child event.
     *
     * @param parent the AttackEvent to be judged as belonging to the parent
     * branch of the child AttackEvent
     * @param child the AttackEvent under consideration for its relation to the
     * parent AttackEvent
     * @return a rating based on how strongly the given child Beat rates as a
     * child branch of the parent Beat. A rating towards 1.0 indicates a strong
     * preference according to TSPR3a. A rating towards 0.0 indicates a very
     * weak preference. A rating near to 0.5 indicates a mild preference.
     *
     */
    public double assessTSPR3a(AttackEvent parent, AttackEvent child) {
        if (parent.compare(parent, child) == 1) {
            //find the interval distance between the two events
            int distance = IntervalConstructor.getInstance().calculateDistance(child, parent);
            //high distance is preferred
            if (distance >= 12) {
                return 1;
            }
            if (distance > 6) {
                return 0.8;
            } else if (distance > 4) {
                return 0.7;
            } else {
                return 0.5;
            }
        }

        if (parent.compare(parent, child) == 0) {
            return 0.3;
        }

        if (parent.compare(parent, child) == -1) {
            return 0;
        }

        return 0;
    }

    /**
     * Assesses the consonance between two given AttackEvents objects. For use
     * in identifying how well two AttackEvents are related to each other. A
     * high rating indicates a high preference. A low rating indicates negative
     * preference.
     *
     * @param parent an AttackEvent to be judged as a potential parent of the
     * child event
     * @param child an AttackEvent to judge the consonance with the proposed
     * parent AttackEvent.
     * @return a rating between 0.0 and 1.0. A rating towards 1.0 indicates a
     * high preference. A rating towards 0.0 indicates a very low preference.
     */
    public double assessConsonance(AttackEvent parent, AttackEvent child) {

        //high consonance
        if (parent.getClass() == AttackEvent.class && child.getClass() == AttackEvent.class) {
            if (parent.getNote() == child.getNote()) {
                return 1;
            }

            if (parent.compare(parent, child) == -1) {

                //find the interval value and return the consonance ranking
                for (IntervalConsonantRanking i : IntervalConsonantRanking.values()) {
                    if (IntervalConstructor.getInstance()
                            .getKeyAtIntervalDistance(parent.getNote(), i.getInterval(), true).getKeyNumber()
                            == child.getNote().getKeyNumber()) {
                        return i.getRanking();
                    }
                }
            } else if (parent.compare(parent, child) == 1) {
                //find the interval value and return the consonance ranking
                for (IntervalConsonantRanking i : IntervalConsonantRanking.values()) {
                    if (IntervalConstructor.getInstance()
                            .getKeyAtIntervalDistance(parent.getNote(), i.getInterval(), true).getKeyNumber()
                            == child.getNote().getKeyNumber()) {
                        return i.getRanking();
                    }
                }
            }

            return 0;

        } else {
            //used to assess chord intervals
            throw new UnsupportedOperationException("No interval ranking "
                    + "implemented for chord events");
        }

    }

    /**
     * Assesses whether the proposed parent AttackEvent has a higher relation to
     * the given Scale's tonic than the proposed child AttackEvent.
     *
     * @param parent proposed parent AttackEvent
     * @param child proposed child AttackEvent
     * @param localTonic the Scale against which to assess the tonic relation
     * @return a rating indicating whether the parent AttackEvent has a higher
     * relation to the tonic or not. A rating towards 1.0 indicates that the
     * parent AttackEvent is more closely related to the tonic. A rating towards
     * 0.0 indicates that the child AttackEvent is more closely related to the
     * tonic. A rating of 0.5 indicates that both AttackEvents have the same
     * relational value to the tonic.
     */
    public double assessTonicRelationBetweenEvents(AttackEvent parent, AttackEvent child,
            Scale localTonic) {
        double parentRating = assessTonicRelation(parent, localTonic);
        double childRating = assessTonicRelation(child, localTonic);

        if (childRating > parentRating) {
            double rating = 0.5 - (childRating - parentRating);
            if (rating < 0) {
                rating = 0;
            }

            return rating;
            //return 0.0;
        }
        if (childRating == parentRating) {
            return 0.50;
        } else {

            double rating = 0.5 + (parentRating - childRating);
            if (rating > 1) {
                rating = 1;
            }
            return rating;
            // return 1.0;
        }
    }

    /**
     * Assesses the strength of the relation of a given AttackEvent to the local
     * tonic.
     *
     * @param child AttackEvent to be assessed.
     * @param localScale The Scale against which to judge how closely the given
     * AttackEvent is related to the tonic.
     * @return a rating between 1.0 and 0.0. A rating towards 1.0 indicates high
     * relationship, and a rating towards 0.0 indicates a low relationship.
     */
    public double assessTonicRelation(AttackEvent child, Scale localScale) {

        {
            if (localScale.getTonic() == child.getNote()) {
                return 1;
            }
            if (localScale.getDominant() == child.getNote()) {
                return 0.95;
            }
            if (localScale.getSubtonic() == child.getNote()) {
                return 0.85;
            }
            if (localScale.getSupertonic() == child.getNote()) {
                return 0.7;
            }
            if (localScale.getSubdominant() == child.getNote()) {
                return 0.65;
            }
            if (localScale.getMediant() == child.getNote()) {
                return 0.5;
            }
            if (localScale.getSubmediant() == child.getNote()) {
                return 0.4;
            }

            return 0;

        }

    }

}
