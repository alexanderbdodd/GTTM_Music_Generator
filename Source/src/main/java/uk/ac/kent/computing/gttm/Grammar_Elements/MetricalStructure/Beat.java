package uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import uk.ac.kent.computing.gttm.Elements.AttackEvent;
import uk.ac.kent.computing.gttm.Elements.KeyLetterEnum;
import uk.ac.kent.computing.gttm.Grammar_Elements.ExceptionClasses.MetricalWellFormednessException;

/**
 * A class that encapsulates the lowest level elements of the metrical structure
 * hierarchy.
 *
 * @author Alexander Dodd
 */
public class Beat implements Comparator, Serializable {

    private final int metricalLevel;
    private Beat nextBeat;
    private Beat previousBeat = null;
    private transient AttackEvent attckEvent = null;
    private int position;

    private static Beat lastBeat = null;

    /**
     *
     *
     * @param metricalStrength the strength of this beat
     */
    public Beat(int metricalStrength) {

        if (metricalStrength < 1) {
            throw new MetricalWellFormednessException("Metrical values must be 1 or greater");
        }
        metricalLevel = metricalStrength;
        if (lastBeat != null) {
            lastBeat.setNextBeat(this);
            previousBeat = lastBeat;
        }
        lastBeat = this;

    }

    /**
     * Create a Beat object associated with an AttackEvent
     *
     * @param metricalStrength the metrical strength of the beat
     * @param attackEvent the AttackEvent associated with the beat
     */
    public Beat(int metricalStrength, AttackEvent attackEvent) {

        this(metricalStrength);
        attckEvent = attackEvent;

    }

    /**
     * Sets the next beat associated with this beat
     *
     * @param nextBeat the beat after this beat
     */
    protected void setNextBeat(Beat nextBeat) {
        if (nextBeat != null) {
            this.nextBeat = nextBeat;
        }
    }

    /**
     * Breaks the chain of beats. Any new beats created after this one will
     * start a new chain of positions starting from 0;
     */
    public static void breakChain() {
        lastBeat = null;
    }

    /**
     *
     * @return the beat after this one
     */
    public Beat getNextBeat() {
        return nextBeat;
    }

    /**
     *
     * @return the beat preceding this one. If no beat precedes, then return
     * null.
     */
    public Beat getPreviousBeat() {
        return previousBeat;
    }

    /**
     *
     * @return the strength of this beat. A lower value indicates a lower
     * strength.
     */
    public int getBeatStrength() {
        return metricalLevel;
    }

    public AttackEvent getAttackEvent() {
        return attckEvent;
    }

    /**
     * Sets an AttackEvent to be associated with the beat
     *
     * @param attckEvent the AttackEvent to associate with the Beat
     */
    public void setAttackEvent(AttackEvent attckEvent) {
        this.attckEvent = attckEvent;
    }

    /**
     *
     * @return the metrical position of the beat
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the beat as being at a given position
     *
     * @param position the position of the beat
     */
    public void setPosition(int position) {
        if (position >= 0) {
            this.position = position;
        } else {
            throw new MetricalWellFormednessException("Position can't "
                    + "be less than 0");
        }
    }

    /**
     * Returns a span of beats between Beat1 and Beat2
     *
     * @param beat1 the first beat of the beat span
     * @param beat2 the last beat of the beat span
     * @return all the beats between beat 1 and beat2, including beat1 and beat2
     */
    public static List<KeyLetterEnum> getBeatSpan(Beat beat1, Beat beat2) {
        Beat b1;
        Beat b2;

        if (beat1.getPosition() < beat2.getPosition()) {
            b1 = beat1;
            b2 = beat2;
        } else {
            b1 = beat2;
            b2 = beat1;
        }

        List<KeyLetterEnum> beats = new ArrayList<>();

        Beat currentBeat = b1;
        boolean exhausted = false;

        while (!exhausted) {
            if (currentBeat == null) {
                throw new MetricalWellFormednessException("Beat has no following beat");
            }

            if (currentBeat.getAttackEvent() != null) {
                beats.add(currentBeat.getAttackEvent().getNote());

            }

            if (currentBeat == b2) {
                exhausted = true;
            } else {
                currentBeat = currentBeat.getNextBeat();
            }

        }

        return beats;
    }

    @Override
    public int compare(Object o1, Object o2) {
        Beat b1 = (Beat) o1;
        Beat b2 = (Beat) o2;

        if (b1.getPosition() < b2.getPosition()) {
            return -1;
        }
        if (b1.getPosition() > b2.getPosition()) {
            return 1;
        } else {
            return 0;
        }
    }

}
