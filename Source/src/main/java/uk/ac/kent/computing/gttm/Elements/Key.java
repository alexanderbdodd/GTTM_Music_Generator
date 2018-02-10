/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Elements;

import java.util.Comparator;

/**
 * A Key class is used to encapsulates details concerning a single pitch event.
 *
 * @author Alexander Dodd
 */
public class Key implements Comparator {

    private final KeyLetterEnum key;
    private KeyPositionEnum position;

    /**
     * Create a Key object with the given KeyLetterEnum and KeyPositionEnum
     *
     * @param key the key position of the pitch
     * @param position the position of the pitch
     */
    public Key(KeyLetterEnum key, KeyPositionEnum position) {

        this.key = key;
        this.position = position;

    }

    /**
     * Copies a Key object using the given Key object
     *
     * @param key the Key object to copy
     */
    public Key(Key key) {
        this(key.getNote(), key.getPosition());
    }

    /**
     *
     * @return the KeyLetterEnum associated with the Key object
     */
    public KeyLetterEnum getNote() {
        return key;
    }

    /**
     *
     * @return the KeyPositionEnum associated with this Key object
     */
    public KeyPositionEnum getPosition() {
        return position;
    }

    @Override
    public int hashCode() {

        int hashcode = 0;
        hashcode = key.getKeyIdentity();
        hashcode += position.getPosition() * 100;

        return hashcode;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj.hashCode() == hashCode()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Provides comparison of two Key objects
     *
     * @param obj1 first Key object to compare
     * @param obj2 second Key object to compare
     * @return -1 if obj1 is in a lower position than obj2, 0 if they are in the
     * same position, and 1 if obj1 is in a greater position than obj2
     */
    public int compare(Object obj1, Object obj2) {

        Key o1 = (Key) obj1;
        Key o2 = (Key) obj2;

        if (o1.getNote() == KeyLetterEnum.BS && o1.getPosition() == KeyPositionEnum.getLastPosition(o2.getPosition())
                && o2.getNote() == KeyLetterEnum.C) {
            return 0;
        }

        if (o1.getNote() == KeyLetterEnum.C
                && o2.getPosition() == KeyPositionEnum.getNextPosition(o1.getPosition())
                && o2.getNote() == KeyLetterEnum.B) {
            return 0;
        }

        if (o1.getPosition().getPosition() > o2.getPosition().getPosition()) {
            return 1;
        }
        if (o1.getPosition().getPosition() < o2.getPosition().getPosition()) {
            return -1;
        } else {
            if (o1.getNote().getKeyNumber() == o2.getNote().getKeyNumber()) {
                return 0;
            } else if (o1.getNote().getKeyIdentity() < o2.getNote().getKeyIdentity()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    /**Returns the next pitch position above the given Key object
     *
     * @param lastKey the Key from which to locate the next Key position
     * @return the next pitch position after the given Key object
     */
    public static Key getNextKey(Key lastKey) {
        if (lastKey == null) {
            return new Key(KeyLetterEnum.C, KeyPositionEnum.FIRST);
        } else {
            KeyLetterEnum letter = KeyLetterEnum.getNextNote(lastKey.getNote())[0];
            KeyPositionEnum position;
            if (letter.getKeyNumber() == KeyLetterEnum.C.getKeyNumber()) {
                if (KeyPositionEnum.getNextPosition(lastKey.getPosition()) != null) {
                    position = KeyPositionEnum.getNextPosition(lastKey.getPosition());
                } else {
                    return null;
                }
            } else {
                position = lastKey.getPosition();
            }
            return new Key(letter, position);
        }

    }

    /**Sets a new position for the Key object
     * 
     * @param position the new position of the Key object
     */
    public void setPosition(KeyPositionEnum position) {
        this.position = position;
    }

}
