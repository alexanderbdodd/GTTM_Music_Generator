/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure;

import java.io.Serializable;
import java.util.*;

import uk.ac.kent.computing.gttm.Grammar_Elements.ExceptionClasses.MetricalWellFormednessException;

/**
 * A container for the metrical structure of this piece.
 *
 * @author Alexander Dodd
 */
public class MetricalContainer implements Serializable{

    private final List<Beat> beats;
    private int serial = 0;

    public MetricalContainer() {
        beats = new ArrayList<>();
    }

    /**
     *
     * @param nextBeat adds the next beat in the metrical structure hierarchy
     * @throws MetricalWellFormednessException thrown if a metrical well
     * formedness constraint is violated
     */
    public void addBeat(Beat nextBeat) throws MetricalWellFormednessException {
        if (!beats.isEmpty()) {
            beats.get(beats.size() - 1).setNextBeat(nextBeat);
        }

        //assign position in the structure
        nextBeat.setPosition(serial);
        serial++;

        beats.add(nextBeat);

        verifyMWFR3();

    }

    public List<Beat> getMetricalBeatsList() {
        return Collections.unmodifiableList(beats);
    }

    public Beat getBeatAtPosition(int position) {
        if (position < beats.size() && position >= 0) {
            return beats.get(position);
        }
        return null;

    }

    /**
    *Used to verify that strong beats must be spaced either two or three beats apart.
    * MWFR3 well-formedness rule.
    */
    private void verifyMWFR3() {

        int i = 0;
        while (i < beats.size()) {
            if (i + 1 <= beats.size() - 1) {
                if (beats.get(i).getBeatStrength() == beats.get(i + 1).getBeatStrength()) {

                    throw new MetricalWellFormednessException("Violation of MWFR 3. Repeated strength "
                            + "across neighbouring beats");

                }

            }
            i++;
        }

        int base = 0;
        for (Beat b : beats) {

            if (base < b.getBeatStrength()) {
                base = b.getBeatStrength();
            }

        }

        List<Beat> twoLevels = new ArrayList<>();

        boolean exhausted = false;

        while (!exhausted) {

            for (Beat b : beats) {
                if (b.getBeatStrength() == base || b.getBeatStrength() == base - 1) {
                    twoLevels.add(b);
                }
            }

            i = 0;

            while (i < twoLevels.size()) {
                Beat b = twoLevels.get(i);
                if (b.getBeatStrength() == base) {
                    if ((i + 1) <= twoLevels.size() - 1) {
                        Beat next = twoLevels.get(i + 1);

                        //checking to see if the next occurence of the base is spaced at either two 
                        //or three beats from the last occurence of the base.
                        if ((i + 2) <= twoLevels.size() - 1) {
                            if (twoLevels.get(i + 2).getBeatStrength() != base) {
                                if ((i + 3) <= twoLevels.size() - 1) {
                                    if (twoLevels.get(i + 3).getBeatStrength() != base) {
                                        throw new MetricalWellFormednessException("Incorrect distribution of beats");
                                    }
                                }
                            }
                        }

                    }

                }
                i++;
            }

            List<Beat> reducedList = new ArrayList<>();

            for (Beat b : twoLevels) {
                if (b.getBeatStrength() != base) {
                    reducedList.add(b);
                }
            }

            exhausted = true;

            twoLevels = reducedList;
            base--;

            for (Beat b : reducedList) {
                if (b.getBeatStrength() != base) {
                    exhausted = false;
                }
            }

        }

    }

    public Beat getLastBeatInSpan() {
        return beats.get(beats.size() - 1);
    }

    public int findHighestMetricalStrength() {
        int level = 0;
        for (Beat b : getMetricalBeatsList()) {
            if (b.getBeatStrength() > level) {
                level = b.getBeatStrength();
            }
        }

        return level;
    }
    
    public int findLowestMetricalStrength()
    {
         int level = 99999;
        for (Beat b : getMetricalBeatsList()) {
            if (b.getBeatStrength() < level) {
                level = b.getBeatStrength();
            }
        }

        return level;
    }

}
