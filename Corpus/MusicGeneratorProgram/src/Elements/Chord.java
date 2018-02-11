/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Elements;

import java.util.*;

/**
 * A class that encapsulates multiple keys to be instantiated at the same point
 * in time.
 *
 * @author Alexander Dodd
 */
public class Chord {

    private List<Key> chord;

    /**
     * Creates a deep copy of a Chord object.
     *
     * @param chrd the Chord object to copy
     * @return a copy of the Chord object
     */
    public static Chord copyChord(Chord chrd) {
        Chord chrdCopy = new Chord();
        for (Key k : chrd.getKeys()) {
            Key keyCopy = new Key(k.getKeyLetterEnum(), k.getPosition());
            chrdCopy.addNote(keyCopy);
        }

        return chrdCopy;
    }

    /**
     *
     * @param chordList A list of Key objects to be included as part of the
     * Chord object
     */
    public Chord(List<Key> chordList) {
        this.chord = chordList;
        arrangeByLowestPitch();
    }

    /**
     * Create an empty Chord object
     */
    public Chord() {
        chord = new ArrayList<>();
        arrangeByLowestPitch();
    }

    /**
     * Arranges all the Keys that make up this Chord object such that
     * they are in order of lowest pitch to highest pitch
     */
    private void arrangeByLowestPitch() {
        List<Key> retChord = new ArrayList<>();

        while (!chord.isEmpty()) {

            retChord.add(findLowestPitch(chord));

            chord.remove(findLowestPitch(chord));

        }

        chord = retChord;
    }

    /**
     * Identifies the lowest pitch of this Chord object
     * @param chrd the List of Key objects from which to identify the lowest pitch
     * @return the lowest pitch (Key)
     */
    private Key findLowestPitch(List<Key> chrd) {

        List<Key> contenders = new ArrayList<>();

        KeyPositionEnum lowestPosition = chrd.get(0).getPosition();

        for (Key k : chrd) {
            if (k.getPosition().getPosition() < lowestPosition.getPosition()) {
                lowestPosition = k.getPosition();
                contenders = new ArrayList<>();
                contenders.add(k);
            } else {
                if (k.getPosition() == lowestPosition) {
                    contenders.add(k);
                }
            }
        }

        Key lowestNote = contenders.get(0);

        for (Key k : contenders) {
            if (k.getKeyLetterEnum().getKeyPosition() < lowestNote.getKeyLetterEnum().getKeyPosition()) {
                lowestNote = k;
            }
        }

        return lowestNote;

    }

    /**
     * Add a new pitch to the collection of pitches contained within this Chord
     * @param key the pitch to add to the chord
     */
    public void addNote(Key key) {
        chord.add(key);
        arrangeByLowestPitch();
    }

    /**
     * 
     * @return all the pitches contained within this Chord object
     */
    public List<Key> getKeys() {
        return chord;
    }

}
