package Manipulators;

import Elements.*;
import java.util.*;

/**
 * Class following the singleton pattern which is used to construct intervals.
 *
 * @author Alexander Dodd
 */
public class IntervalConstructor {

    /**
     *
     * @return the singleton object associated with this class
     */
    public static IntervalConstructor getInstance() {
        if (instance == null) {
            instance = new IntervalConstructor();
        }

        return instance;
    }

    private static IntervalConstructor instance = null;

    private IntervalConstructor() {
    }

    /**
     * Returns the a Key object that exists at the interval distance from the
     * given root Key.
     *
     * @param root the root key of the interval
     * @param interval the interval distance from the root
     * @param ascending true if the search direction should be upwards, false if
     * it should be downwards.
     * @return the Key object at the interval distance
     */
    public Key getKeyAtIntervalDistance(Key root, IntervalEnum interval, boolean ascending) {
        KeyLetterEnum k = getKeyAtIntervalDistance(root.getKeyLetterEnum(), interval, ascending);

        KeyPositionEnum position = root.getPosition();

        if (ascending) {

                if(k.getKeyPosition() <= root.getKeyLetterEnum().getKeyPosition()
                        && interval != IntervalEnum.PERFECTUNISON)
                  try {
                    position = KeyPositionEnum.getNextPosition(position);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new NoteOutOfBoundsRuntimeException("Note position outside of specified range.");
                }

            }
        else {
            if (k.getKeyPosition() >= root.getKeyLetterEnum().getKeyPosition()
                    && interval != IntervalEnum.PERFECTUNISON) {

                try {
                    position = KeyPositionEnum.getLastPosition(position);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new NoteOutOfBoundsRuntimeException("Note position outside of specified range.");
                }
            }
        }

        return new Key(k, position);
    }

    /**
     * Returns the a KeyLetterEnum reference that exists at the interval
     * distance from the given root KeyLetterEnum.
     *
     * @param root the root position of the interval
     * @param interval the interval distance
     * @param ascending true if the search direction should be upwards, false if
     * it should be downwards.
     * @return the KeyLetterEnum reference that exists at the interval distance
     * from the root
     */
    public KeyLetterEnum getKeyAtIntervalDistance(KeyLetterEnum root, IntervalEnum interval,
            boolean ascending) {

        List<KeyLetterEnum> letters = GeneralManipulator.rearrangeNotes(root, KeyLetterEnum.values());

        List<KeyLetterEnum> trueIntervals = new ArrayList<>();

        for (int i : KeyLetterEnum.getUniquePositions()) {
            boolean seen = false;
            for (KeyLetterEnum l : letters) {
                if (l.getKeyPosition() == i) {
                    if (!seen) {
                        trueIntervals.add(l);
                        seen = true;
                    }
                }
            }
        }

        if (!ascending) {
            Collections.reverse(trueIntervals);
        }
        trueIntervals = GeneralManipulator
                .rearrangeNotes(root, trueIntervals.toArray(new KeyLetterEnum[0]));

        trueIntervals.add(trueIntervals.get(0));

        return trueIntervals.get(interval.getIntervalDistance());

    }

    /**
     * Calculates the intervallic distance between key1 and key2
     *
     * @param key1 the first key from which to work out the distance
     * @param key2 the second key from which to work out the distance
     * @return the distance between the two keys. A negative value indicates
     * that key2 is in a position before key1; a positive value indicates that
     * key2 is in a position after key1. A value of 0 indicates that both keys
     * are in the same position.
     */
    public int calculateDistance(Key key1, Key key2) {
        if (key1.compare(key1, key2) == -1) {
            int distance = 0;

            boolean found = false;
            KeyLetterEnum[] temp = KeyLetterEnum.getNextNote(key1.getKeyLetterEnum());

            while (!found) {
                distance++;
                for (KeyLetterEnum key : temp) {
                    if (key == key2.getKeyLetterEnum()) {
                        found = true;
                    }
                }
                temp = KeyLetterEnum.getNextNote(temp[0]);
            }
            int positionDif = key2.getPosition().getPosition() - key1.getPosition().getPosition();

            if (key1.getPosition() == key2.getPosition()) {
                return distance;
            } else if (positionDif == 1 && key2.getKeyLetterEnum().getKeyPosition() <= key1.getKeyLetterEnum().getKeyPosition()) {
                return distance;

            } else {
             

                return distance + (positionDif * 12);
            }

        }

        if (key1.compare(key1, key2) == 0) {
            return 0;
        }

        if (key1.compare(key1, key2) == 1) {
            int distance = 0;

            boolean found = false;
            KeyLetterEnum[] temp = KeyLetterEnum.getPreviousNote(key1.getKeyLetterEnum());

            while (!found) {
                distance--;
                for (KeyLetterEnum key : temp) {
                    if (key == key2.getKeyLetterEnum()) {
                        found = true;
                    }
                }
                temp = KeyLetterEnum.getPreviousNote(temp[0]);
            }

            if (key1.getPosition() == key2.getPosition()) {
                return distance;
            } else {
                              
                int positionDif = key2.getPosition().getPosition() - key1.getPosition().getPosition();
                positionDif++;
                return distance + (positionDif * 12);
            }
        }

        return 0;

    }

}
