package uk.ac.kent.computing.gttm.Manipulators;

import java.util.*;
import java.util.Map.Entry;

import uk.ac.kent.computing.gttm.Elements.KeyLetterEnum;
import uk.ac.kent.computing.gttm.Elements.Scale;

/**
 * A class that implements the singleton pattern. Used to construct scales.
 *
 * @author Alexander Dodd
 */
public class ScaleConstructor {

    private static ScaleConstructor instance = null;
    public final KeyLetterEnum[] validKeys = KeyLetterEnum.values();

    /**
     *
     * @return the singleton instance of this class
     */
    public static ScaleConstructor getInstance() {
        if (instance == null) {
            instance = new ScaleConstructor();
        }
        return instance;
    }

    private HashMap<Pair, Pair> relativeMinorMap;

    private ScaleConstructor() {
        constructRelativeMinorMap();
    }

    private void constructRelativeMinorMap() {
        relativeMinorMap = new HashMap<>();

        relativeMinorMap.put(new Pair(KeyLetterEnum.C, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.A, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.G, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.E, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.D, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.B, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.A, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.FS, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.E, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.CS, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.Cf, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.Af, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.B, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.GS, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.Gf, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.Ef, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.FS, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.DS, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.Df, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.Bf, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.CS, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.AS, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.Af, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.F, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.Ef, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.C, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.Bf, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.G, ScaleModeEnum.NATURALMINOR));
        relativeMinorMap.put(new Pair(KeyLetterEnum.F, ScaleModeEnum.MAJOR),
                new Pair(KeyLetterEnum.D, ScaleModeEnum.NATURALMINOR));

    }

    /**
     * Constructs the relative minor scale of the given key
     *
     * @param letter the key from which to obtain the relative minor
     * @return the relative minor of the major scale of the key
     */
    public Scale getRelativeMinor(KeyLetterEnum letter) {

        for (Entry<Pair, Pair> pair : relativeMinorMap.entrySet()) {
            if (pair.getKey().key == letter) {
                return constructScale(pair.getValue().key, pair.getValue().scale);
            }
        }

        return null;
    }

    /**Constructs the relative major scale of the minor scale of the given key.
     *
     * @param letter the key from which to assess the relative major
     * @return the relative major of the given key
     */
    public Scale getRelativeMajor(KeyLetterEnum letter) {

        for (Pair l : relativeMinorMap.keySet()) {
            if (relativeMinorMap.get(l).key == letter) {

                return constructScale(l.key, l.scale);
            }
        }

        return null;

    }

    /**
     *  
     * Returns a Scale object which contains an ordered sequence of
     * KeyLetterEnums which represents the notes contained within the given key.
     *
     * @param scaleRoot the tonic of the scale
     * @param scaleMode the scale mode to be used in constructing the scale
     * @return a Scale object containing an ordered sequence of KeyLetterEnums
     * representing an ascending scale.
     */
    public Scale constructScale(KeyLetterEnum scaleRoot, ScaleModeEnum scaleMode) {
        int[] intervals = scaleMode.getIntervals();

        List<KeyLetterEnum> ns = GeneralManipulator.rearrangeNotes(scaleRoot, KeyLetterEnum.values());

        ArrayList<Integer> keyNumbers = new ArrayList<>();

        for (KeyLetterEnum letter : ns) {
            if (!keyNumbers.contains(letter.getKeyNumber())) {
                keyNumbers.add(letter.getKeyNumber());
            }
        }

        ArrayList<Integer> scale = new ArrayList<>();

        for (int i : intervals) {
            if (i < keyNumbers.size()) {
                scale.add(keyNumbers.get(i));
            }
        }

        List<KeyLetterEnum> scaleLetters = new ArrayList<>();

        List<KeyLetterEnum> contenders = new ArrayList<>();

        for (int i : scale) {
            for (KeyLetterEnum l : KeyLetterEnum.values()) {
                if (l.getKeyNumber() == i) {
                    contenders.add(l);
                }
            }

            if (contenders.size() == 1) {
                scaleLetters.add(contenders.get(0));

            } else {
                if (scaleLetters.size() == 0) {
                    scaleLetters.add(scaleRoot);
                } else {
                    String lastLetter = scaleLetters.get(scaleLetters.size() - 1).toString().substring(0, 1);
                    for (KeyLetterEnum cl : contenders) {
                        if (!lastLetter.equals(cl.toString().substring(0, 1))) {
                            scaleLetters.add(cl);
                            break;
                        }
                    }
                }

            }
            contenders.clear();
        }

        return new Scale(scaleLetters, scaleMode);
    }

    /**
     * Assesses which diatonic scales the given collection of keys are most
     * likely to belong to.
     *
     * @param e a list of keys
     * @return a list of likely diatonic scales
     */
    public List<Scale> assessDiatonicScale(List<KeyLetterEnum> e) {

        List<Scale> scales = new ArrayList<>();
        Scale scl;

        int highestMatching = 0;

        for (KeyLetterEnum key : KeyLetterEnum.values()) {
            scl = constructScale(key, ScaleModeEnum.MAJOR);
            int value = matching(scl, e);
            if (value > highestMatching) {
                highestMatching = value;
                scales.clear();
                scales.add(scl);
            } else {
                if (value == highestMatching) {
                    scales.add(scl);
                }
            }
            scl = constructScale(key, ScaleModeEnum.NATURALMINOR);

            value = matching(scl, e);
            if (value > highestMatching) {
                highestMatching = value;
                scales.clear();
                scales.add(scl);
            } else {
                if (value == highestMatching) {
                    scales.add(scl);
                }
            }

        }

        KeyLetterEnum tonic = e.get(0);

        for (Scale scls : scales) {
            if (scls.getTonic() == tonic) {
                scales.remove(scls);
                scales.add(0, scls);
                break;
            }
        }

        return scales;
    }

    private int matching(Scale scl, List<KeyLetterEnum> e) {

        Set<KeyLetterEnum> nonMatchingKeys = new HashSet<>();

        int nonMatching = 0;
        boolean matching = false;

        for (KeyLetterEnum key2 : e) {
            for (KeyLetterEnum key : scl.getNotes()) {
                if (key == key2) {
                    matching = true;
                }

            }
            if (!matching) {
                nonMatching++;
            }
            matching = false;
        }

        if (nonMatching == 0) {
            return 100;
        }
        if (nonMatching == e.size()) {
            return 0;
        }
        if (nonMatching >= (e.size()) * (3 / 4)) {
            return 80;
        }
        if (nonMatching >= (e.size()) * (1 / 2)) {
            return 50;
        }
        if (nonMatching >= (e.size() * (3 / 4))) {
            return 25;
        }
        return 10;

    }

    private class Pair {

        public KeyLetterEnum key;
        public ScaleModeEnum scale;

        private Pair(KeyLetterEnum key, ScaleModeEnum scale) {
            this.key = key;
            this.scale = scale;
        }

    }

}
