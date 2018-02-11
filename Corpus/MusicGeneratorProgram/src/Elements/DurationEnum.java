
package Elements;

/**Used to describe the length of a pitch event in terms of metrical beats.
 *
 * @author Alexander Dodd
 */
public enum DurationEnum {
    
    ONE_BEAT(1), TWO_BEATS(2), THREE_BEATS(3), FOUR_BEATS(4), FIVE_BEATS(5);
    
    //the amount of beats each durational value spans
    private final int length;
    
    /**
     * Sets up a DurationEnum with a specified duration length
     * @param length the length of the duration
     */
     DurationEnum(int length)
    {
        this.length = length;
    }
    
     /**
      * 
      * @return the length associated with the DurationEnum 
      */
    public int getLength()
    {
        return length;
    }
}
