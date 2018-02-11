/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Elements;

import Manipulators.GeneralManipulator;
import java.util.*;

/**
 * The KeyLetterEnum is used to describe key positions within a scale for a
 * pitch, and is used in conjunction with the KeyPositionEnum to describe a
 * pitch position.
 *
 * @author Alexander Dodd
 */
public enum KeyLetterEnum {

    C(0, 0), CS(1, 1), Df(1, 2), D(2, 3), DS(3, 4), Ef(3, 5), E(4, 6), Ff(4, 7), ES(5, 8), F(5, 9), FS(6, 10),
    Gf(6, 11), G(7, 12), GS(8, 13), Af(8, 14), A(9, 15), AS(10, 16), Bf(10, 17), B(11, 18), Cf(11, 19), BS(0, 20);

    //describes the keyboard position of the key, and can be shared by different
    //keys. E.g. b sharp has the same key number as c
    private final int keyPosition;
    //a unique number associated with each key letter
    private final int keyIdentity;

    /**
     * 
     * @param keyPosition the key position of the KeyLetterEnum
     * @param keyIdentity the unique number identifier of the KeyLetterEnum
     */
    private KeyLetterEnum(int keyPosition, int keyIdentity) {
        this.keyPosition = keyPosition;
        this.keyIdentity = keyIdentity;
    }

    /*
     Returns a number that is shared by all keys in the same position. E.g. b sharp and c have the same key
     number
     */
    public final int getKeyPosition() {
        return keyPosition;
    }

    /*
     Returns a the key letter's unique number
     */
    public int getKeyIdentity() {
        return keyIdentity;
    }

    /**
     * 
     * @return an array of the unique key positions
     */
    public static int[] getUniquePositions() {
        int[] positions = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

        return positions;
    }

    /**
     * Returns an array of the next key letters after the given key letter.
     * @param current the key letter from which to assess the next note
     * @return an array of the next key letters enums directly after the given Key letter
     */
    public static KeyLetterEnum[] getNextNote(KeyLetterEnum current) {
        KeyLetterEnum[] array = new KeyLetterEnum[2];

        int nextNumber;

        if (current.getKeyPosition() + 1 < 12) {
            nextNumber = current.getKeyPosition() + 1;
        } else {
            nextNumber = 0;
        }
        int position = 0;
        boolean pastNext = false;
        int arrayPos = 0;

        KeyLetterEnum[] keysArray = GeneralManipulator.rearrangeNotes(current, KeyLetterEnum.values()).toArray(new KeyLetterEnum[0]);

        while (!pastNext) {
            if (keysArray[position].getKeyPosition() == nextNumber) {
                array[arrayPos] = keysArray[position];
                arrayPos++;
                position++;
                continue;
            }

            if (keysArray[position].getKeyPosition() == current.getKeyPosition()) {
                position++;
                continue;
            }

            pastNext = true;

            position++;
        }

        return array;

    }

    /**
     * Returns an array of the last key letters after the given key letter.
     * @param current the key letter from which to assess the previous notes
     * @return an array of the next key letters enums directly before the given Key letter
     */
    public static KeyLetterEnum[] getPreviousNote(KeyLetterEnum current) {
        KeyLetterEnum[] array = new KeyLetterEnum[2];

        int nextNumber;

        if (current.getKeyPosition() - 1 >= 0) {
            nextNumber = current.getKeyPosition() - 1;
        } else {
            nextNumber = 11;
        }
        int position = 0;
        boolean pastNext = false;
        int arrayPos = 0;

        List<KeyLetterEnum> keysArray = GeneralManipulator.rearrangeNotes(current, KeyLetterEnum.values());

        Collections.reverse(keysArray);

        keysArray = GeneralManipulator.rearrangeNotes(current, keysArray.toArray(new KeyLetterEnum[0]));

        while (!pastNext) {
            if (keysArray.get(position).getKeyPosition() == nextNumber) {
                array[arrayPos] = keysArray.get(position);
                arrayPos++;
                position++;
                continue;
            }

            if (keysArray.get(position).getKeyPosition() == current.getKeyPosition()) {
                position++;
                continue;
            }

            pastNext = true;

            position++;
        }

        return array;

    }

}
