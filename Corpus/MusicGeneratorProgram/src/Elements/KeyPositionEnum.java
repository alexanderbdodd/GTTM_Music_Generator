
package Elements;

/**Describes the position of a KeyLetterEnum in order to describe a specific pitch position.
 *
 * @author Alexander Dodd
 */
public enum KeyPositionEnum{

    FIRST(1), SECOND(2), THIRD(3), FOURTH(4), FIFTH(5), SIXTH(6), SEVENTH(7), EIGHTH(8);

    private int position;

    /**
     * 
     * @param position the position the KeyPositionEnum represents
     */
   private KeyPositionEnum(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    /**
     * Returns the position after the given position. If the input position is
     * the last position, then the first position is returned.
     *
     * @param currentPosition the position from which to find the next position
     * @return the position after the current position
     */
    public static KeyPositionEnum getNextPosition(KeyPositionEnum currentPosition) {
        
       boolean returning = false;
       for(KeyPositionEnum keyPos : KeyPositionEnum.values())
       {
           if(returning)
           {
               return keyPos;
           }
           if(keyPos == currentPosition)
           {
               returning = true;
           }
       }
       
       return null;
       
    }

    /**
     * Returns the position before the given position. If the input position is
     * the last position, then the first position is returned.
     *
     * @param currentPosition the position from which to find the last position
     * @return the position before the current position
     */
    public static KeyPositionEnum getLastPosition(KeyPositionEnum currentPosition) {
     
       KeyPositionEnum last = KeyPositionEnum.values()[KeyPositionEnum.values().length - 1];
       for(KeyPositionEnum keyPos : KeyPositionEnum.values())
       {
           if(keyPos == currentPosition)
           {
               return last;
           }
           else{
               last = keyPos;
           }
       }
       
       return KeyPositionEnum.values()[KeyPositionEnum.values().length];
    }
   

}
