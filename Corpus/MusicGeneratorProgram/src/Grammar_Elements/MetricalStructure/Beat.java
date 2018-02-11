package Grammar_Elements.MetricalStructure;

import Elements.*;
import Grammar_Elements.ExceptionClasses.MetricalWellFormednessException;
import java.io.Serializable;
import java.util.*;

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
   // private transient AttackEvent attckEvent = null;
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
