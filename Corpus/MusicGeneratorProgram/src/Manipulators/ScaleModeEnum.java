package Manipulators;

/**
 * A list of enums representing different scale modes. Each enum is associated
 * with an int array which represents the intervallic distances from the tonic
 * of each note in the scale
 */
public enum ScaleModeEnum {

    MAJOR(new int[]{0, 2, 4, 5, 7, 9, 11}),
    NATURALMINOR(new int[]{0, 2, 3, 5, 7, 8, 10}),
    HARMONICMINOR(new int[]{0, 2, 3, 5, 7, 8, 11}),
    MELODICMINOR(new int[]{0, 2, 3, 5, 7, 9, 11}),
    DORIAN(new int[]{0, 2, 3, 5, 7, 9, 10}),
    PHRYGIAN(new int[]{0, 1, 3, 5, 7, 8, 10}),
    LYDIAN(new int[]{0, 2, 4, 6, 7, 9, 11}),
    MIXOLYDIAN(new int[]{0, 2, 4, 6, 7, 9, 10}),
    CHROMATIC(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});

    private final int[] intervals;

    private ScaleModeEnum(int[] intervals) {
        this.intervals = intervals;
    }

    /**
     *
     * @return an array of the interval values for the scale mode
     */
    public int[] getIntervals() {

        return intervals;
    }

}
