/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manipulators;

/**The list of chord types. Each chord type is associated with an array of IntervalEnum references
 * representing the interval combinations which comprise the chord.
 *
 * @author Alexander Dodd
 */
public enum ChordEnum {

    MAJOR_TRIAD(new IntervalEnum[]{IntervalEnum.MAJOR3RD, IntervalEnum.PERFECT5TH}),
    MINOR_TRIAD(new IntervalEnum[]{IntervalEnum.MINOR3RD, IntervalEnum.PERFECT5TH});

    private IntervalEnum[] combo;

    /**
     * 
     * @param combo the combination of intervals to construct the Chord
     */
    private ChordEnum(IntervalEnum[] combo) {
        this.combo = combo;
    }
    
    /**
     * 
     * @return the IntervalEnum array that contains the intervals that comprise the chord
     */
    public IntervalEnum[] getIntervals()
    {
        return combo;
        
    }

}
