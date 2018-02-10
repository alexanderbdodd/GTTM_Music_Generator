/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Player;

import java.util.*;
import jm.JMC;
import jm.constants.Pitches;
import jm.music.data.*;
import uk.ac.kent.computing.gttm.Elements.AttackEvent;
import uk.ac.kent.computing.gttm.Elements.Chord;
import uk.ac.kent.computing.gttm.Elements.DurationEnum;
import uk.ac.kent.computing.gttm.Elements.Key;
import uk.ac.kent.computing.gttm.Elements.RestEvent;

/**Adapts the MusicGenerator program to the JMusic interface. 
 *
 * @author Alexander Dodd
 */
public class JMusicAdaptor {

    private static JMusicAdaptor instance = null;

    /**
     * 
     * @return the singleton instance of this class
     */
    public static JMusicAdaptor getInstance() {
        if (instance == null) {
            instance = new JMusicAdaptor();
        }

        return instance;

    }
        
    private JMusicAdaptor() {

    }

    /**
     * Constructs an array of Note objects from a given Chord object for use in playing
     * chords
     * @param chrd the Chord of pitches to be added to the array
     * @param duration the duration of the Chord
     * @return an array of Note objects
     */
    public Note[] constructChordArray(Chord chrd, DurationEnum duration) {
        Note[] noteArray = new Note[chrd.getKeys().size()];

        int i = 0;
        while (i < chrd.getKeys().size()) {
            noteArray[i] = convertAttackEvent(new AttackEvent(chrd.getKeys().get(i).getNote(), chrd.getKeys().get(i).getPosition(), duration));
            i++;
        }

        return noteArray;
    }

    /**
     * Converts a GTTM AttackEvent object into a JMusic Note object
     * @param note the AttackEvent to be converted into a Note object
     * @return the converted Note object
     */
    public Note convertAttackEvent(AttackEvent note) {
        int pitch = findPitch(note);
        Note jNote;
        jNote = new Note(pitch, findDuration(note.getDurationEnum()));

        return jNote;
    }
    
   
    /**Converts a GTTM RestEvent object into a JMusic Rest object
     * 
     * @param rest the GTTM RestEvent to be converted into a JMusic Rest
     * @return the converted Rest object
     */
    public Rest convertRestEvent(RestEvent rest) {
        return new Rest(findDuration(rest.getDurationEnum()));
    }

    private double findDuration(DurationEnum durationEnum) {
        switch (durationEnum) {
            case SIXTEENTH:
                return JMC.SIXTEENTH_NOTE;
            case EIGHTH:
                return JMC.EIGHTH_NOTE;
            case QUARTER:
                return JMC.QUARTER_NOTE;
            case HALF:
                return JMC.HALF_NOTE;
            case WHOLE:
                return JMC.WHOLE_NOTE;
            default:
                return 0.0;
        }
    }

    private int findPitch(Key key) {
        switch (key.getNote()) {
            case A:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.A1;
                    case SECOND:
                        return JMC.A2;
                    case THIRD:
                        return JMC.A3;
                    case FOURTH:
                        return JMC.A4;
                    case FIFTH:
                        return JMC.A5;
                    case SIXTH:
                        return JMC.A6;
                    case SEVENTH:
                        return JMC.A7;
                    case EIGHTH:
                        return JMC.A8;
                }
            case AS:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.AS1;
                    case SECOND:
                        return JMC.AS2;
                    case THIRD:
                        return JMC.AS3;
                    case FOURTH:
                        return JMC.AS4;
                    case FIFTH:
                        return JMC.AS5;
                    case SIXTH:
                        return JMC.AS6;
                    case SEVENTH:
                        return JMC.AS7;
                    case EIGHTH:
                        return JMC.AS8;
                }
            case Bf:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.BF1;
                    case SECOND:
                        return JMC.BF2;
                    case THIRD:
                        return JMC.BF3;
                    case FOURTH:
                        return JMC.BF4;
                    case FIFTH:
                        return JMC.BF5;
                    case SIXTH:
                        return JMC.BF6;
                    case SEVENTH:
                        return JMC.BF7;
                    case EIGHTH:
                        return JMC.BF8;
                }
            case BS:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.BS1;
                    case SECOND:
                        return JMC.BS2;
                    case THIRD:
                        return JMC.BS3;
                    case FOURTH:
                        return JMC.BS4;
                    case FIFTH:
                        return JMC.BS5;
                    case SIXTH:
                        return JMC.BS6;
                    case SEVENTH:
                        return JMC.BS7;
                    case EIGHTH:
                        return JMC.BS8;
                }
            case B:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.B1;
                    case SECOND:
                        return JMC.B2;
                    case THIRD:
                        return JMC.B3;
                    case FOURTH:
                        return JMC.B4;
                    case FIFTH:
                        return JMC.B5;
                    case SIXTH:
                        return JMC.B6;
                    case SEVENTH:
                        return JMC.B7;
                    case EIGHTH:
                        return JMC.B8;
                }
            case C:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.C1;
                    case SECOND:
                        return JMC.C2;
                    case THIRD:
                        return JMC.C3;
                    case FOURTH:
                        return JMC.C4;
                    case FIFTH:
                        return JMC.C5;
                    case SIXTH:
                        return JMC.C6;
                    case SEVENTH:
                        return JMC.C7;
                    case EIGHTH:
                        return JMC.C8;
                }
            case CS:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.CS1;
                    case SECOND:
                        return JMC.CS2;
                    case THIRD:
                        return JMC.CS3;
                    case FOURTH:
                        return JMC.CS4;
                    case FIFTH:
                        return JMC.CS5;
                    case SIXTH:
                        return JMC.CS6;
                    case SEVENTH:
                        return JMC.CS7;
                    case EIGHTH:
                        return JMC.CS8;
                }
            case Df:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.DF1;
                    case SECOND:
                        return JMC.DF2;
                    case THIRD:
                        return JMC.DF3;
                    case FOURTH:
                        return JMC.DF4;
                    case FIFTH:
                        return JMC.DF5;
                    case SIXTH:
                        return JMC.DF6;
                    case SEVENTH:
                        return JMC.DF7;
                    case EIGHTH:
                        return JMC.DF8;
                }
            case D:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.D1;
                    case SECOND:
                        return JMC.D2;
                    case THIRD:
                        return JMC.D3;
                    case FOURTH:
                        return JMC.D4;
                    case FIFTH:
                        return JMC.D5;
                    case SIXTH:
                        return JMC.D6;
                    case SEVENTH:
                        return JMC.D7;
                    case EIGHTH:
                        return JMC.D8;
                }
            case DS:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.DS1;
                    case SECOND:
                        return JMC.DS2;
                    case THIRD:
                        return JMC.DS3;
                    case FOURTH:
                        return JMC.DS4;
                    case FIFTH:
                        return JMC.DS5;
                    case SIXTH:
                        return JMC.DS6;
                    case SEVENTH:
                        return JMC.DS7;
                    case EIGHTH:
                        return JMC.DS8;
                }
            case Ef:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.EF1;
                    case SECOND:
                        return JMC.EF2;
                    case THIRD:
                        return JMC.EF3;
                    case FOURTH:
                        return JMC.EF4;
                    case FIFTH:
                        return JMC.EF5;
                    case SIXTH:
                        return JMC.EF6;
                    case SEVENTH:
                        return JMC.EF7;
                    case EIGHTH:
                        return JMC.EF8;
                }
            case E:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.E1;
                    case SECOND:
                        return JMC.E2;
                    case THIRD:
                        return JMC.E3;
                    case FOURTH:
                        return JMC.E4;
                    case FIFTH:
                        return JMC.E5;
                    case SIXTH:
                        return JMC.E6;
                    case SEVENTH:
                        return JMC.E7;
                    case EIGHTH:
                        return JMC.E8;
                }
            case ES:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.ES1;
                    case SECOND:
                        return JMC.ES2;
                    case THIRD:
                        return JMC.ES3;
                    case FOURTH:
                        return JMC.ES4;
                    case FIFTH:
                        return JMC.ES5;
                    case SIXTH:
                        return JMC.ES6;
                    case SEVENTH:
                        return JMC.ES7;
                    case EIGHTH:
                        return JMC.ES8;
                }
            case Ff:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.FF1;
                    case SECOND:
                        return JMC.FF2;
                    case THIRD:
                        return JMC.FF3;
                    case FOURTH:
                        return JMC.FF4;
                    case FIFTH:
                        return JMC.FF5;
                    case SIXTH:
                        return JMC.FF6;
                    case SEVENTH:
                        return JMC.FF7;
                    case EIGHTH:
                        return JMC.FF8;
                }
            case F:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.F0;
                    case SECOND:
                        return JMC.F2;
                    case THIRD:
                        return JMC.F3;
                    case FOURTH:
                        return JMC.F4;
                    case FIFTH:
                        return JMC.F5;
                    case SIXTH:
                        return JMC.F6;
                    case SEVENTH:
                        return JMC.F7;
                    case EIGHTH:
                        return JMC.F8;
                }
            case FS:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.FS1;
                    case SECOND:
                        return JMC.FS2;
                    case THIRD:
                        return JMC.FS3;
                    case FOURTH:
                        return JMC.FS4;
                    case FIFTH:
                        return JMC.FS5;
                    case SIXTH:
                        return JMC.FS6;
                    case SEVENTH:
                        return JMC.FS7;
                    case EIGHTH:
                        return JMC.FS8;
                }
            case Gf:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.GF1;
                    case SECOND:
                        return JMC.GF2;
                    case THIRD:
                        return JMC.GF3;
                    case FOURTH:
                        return JMC.GF4;
                    case FIFTH:
                        return JMC.GF5;
                    case SIXTH:
                        return JMC.GF6;
                    case SEVENTH:
                        return JMC.GF7;
                    case EIGHTH:
                        return JMC.GF8;
                }
            case G:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.G1;
                    case SECOND:
                        return JMC.G2;
                    case THIRD:
                        return JMC.G3;
                    case FOURTH:
                        return JMC.G4;
                    case FIFTH:
                        return JMC.G5;
                    case SIXTH:
                        return JMC.G6;
                    case SEVENTH:
                        return JMC.G7;
                    case EIGHTH:
                        return JMC.G8;
                }
            case GS:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.GS1;
                    case SECOND:
                        return JMC.GS2;
                    case THIRD:
                        return JMC.GS3;
                    case FOURTH:
                        return JMC.GS4;
                    case FIFTH:
                        return JMC.GS5;
                    case SIXTH:
                        return JMC.GS6;
                    case SEVENTH:
                        return JMC.GS7;
                    case EIGHTH:
                        return JMC.GS8;
                }
            case Af:
                switch (key.getPosition()) {
                    case FIRST:
                        return JMC.AF1;
                    case SECOND:
                        return JMC.AF2;
                    case THIRD:
                        return JMC.AF3;
                    case FOURTH:
                        return JMC.AF4;
                    case FIFTH:
                        return JMC.AF5;
                    case SIXTH:
                        return JMC.AF6;
                    case SEVENTH:
                        return JMC.AF7;
                    case EIGHTH:
                        return JMC.AF8;
                }

            default:
                return 0;
        }

    }

}
