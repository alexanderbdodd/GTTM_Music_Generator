/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manipulators;

import Elements.*;
import java.util.*;

/**
 * Singleton class that constructs chords associated with given ChordEnum type.
 *
 * @author Alexander Dodd
 */
public class ChordConstructor {

    private static ChordConstructor instance = null;

    /**
     *
     * @return the singleton instance of this class
     */
    public static ChordConstructor getInstance() {
        if (instance == null) {
            instance = new ChordConstructor();
        }

        return instance;
    }

    /**
     *
     * @param rootKey The root key for the chord to be constructed
     * @param chord the chordenum representing the chord type to be constructed
     * @return the constructed chord. 
     */
    public Chord constructChord(Key rootKey, ChordEnum chord) {

        List<Key> keys = new ArrayList<>();
        keys.add(rootKey);

        for (IntervalEnum interval : chord.getIntervals()) {
      
                Key intervalKey = IntervalConstructor.getInstance()
                        .getKeyAtIntervalDistance(rootKey, interval, true);
                keys.add(intervalKey);
            
        }

        return new Chord(keys);

    }

}
