
package uk.ac.kent.computing.gttm.Manipulators;

/**A set of enum types representing various intervallic distances. Each enum 
 * has an associated int value representing the intervallic distance in half-steps.
 *
 * @author Alexander Dodd
 */
public enum IntervalEnum {
    
    PERFECTUNISON(0),
    MINOR2ND(1),
    MAJOR2ND(2),
    MINOR3RD(3),
    MAJOR3RD(4),
    PERFECT4TH(5),
    PERFECT5TH(7),
    MINOR6TH(8),
    MAJOR6TH(9),
    MINOR7TH(10),
    MAJOR7TH(11),
    PERFECTOCTAVE(12);
    
    private int interval;
    
    IntervalEnum(int interval)
    {
        this.interval = interval;
    }
    
    /**
     * 
     * @return the intervallic distance in half steps
     */
    public int getIntervalDistance()
    {
        return interval;
    }
    
    
}
