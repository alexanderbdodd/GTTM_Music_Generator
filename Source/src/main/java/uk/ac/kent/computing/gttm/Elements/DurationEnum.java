/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Elements;

/**Used to describe the length of a pitch event.
 *
 * @author Alexander Dodd
 */
public enum DurationEnum {
    
    SIXTEENTH(1), EIGHTH(2), QUARTER(4), HALF(8), WHOLE(16);
    
    private int length;
    
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
