/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.GTTM_Analyser;

import uk.ac.kent.computing.gttm.Manipulators.IntervalEnum;

/**A collection of IntervalEnums and a ranking value which describes the consonance 
 * of the interval type.
 *
 * @author Alexander Dodd
 */
public enum IntervalConsonantRanking {

    //consonance rankings based on information found at:
    //http://music.stackexchange.com/questions/4439/is-there-a-way-to-measure-the-consonance-or-dissonance-of-a-chord
    //as well as http://www.harmony.org.uk/book/voice_leading/consonance_and_dissonance.htm
    OCTAVE_RANKING(IntervalEnum.PERFECTOCTAVE, 1), //perfect consonance
    PERFECTUNISON_RANKING(IntervalEnum.PERFECTUNISON, 1), //perfect consonance
    PERFECT5TH_RANKING(IntervalEnum.PERFECT5TH, 1), //perfect consonance
    PERFECT4TH_RANKING(IntervalEnum.PERFECT4TH, 0.4), //dissonant when not supported by lower third or fifth
    MAJOR3RD_RANKING(IntervalEnum.MAJOR3RD, 0.8),
    MINOR3RD_RANKING(IntervalEnum.MINOR3RD, 0.8),
    MAJOR6TH_RANKING(IntervalEnum.MAJOR6TH, 0.8),
    MINOR6TH_RANKING(IntervalEnum.MAJOR6TH, 0.8),
    MAJOR2ND_RANKING(IntervalEnum.MAJOR2ND, 0.3),
    MAJOR7TH_RANKING(IntervalEnum.MAJOR7TH, 0.1),
    MINOR7TH_RANKING(IntervalEnum.MINOR7TH, 0.3),
    MINOR2ND_RANKING(IntervalEnum.MINOR2ND, 0.1);

    private IntervalEnum interval;

    private double ranking;

    IntervalConsonantRanking(IntervalEnum interval, double ranking) {
        this.interval = interval;
        this.ranking = ranking;
    }

    /**
     * 
     * @return the associated IntervalEnum
     */
    public IntervalEnum getInterval() {
        return interval;
    }

    /**
     * 
     * @return the ranking for consonance
     */
    public double getRanking() {
        return ranking;
    }
    
   

}
