package Manipulators;

/**A class extending the Exception class which is thrown if a Key object's position is
 * outside of the supported range.
 *
 * @author Alexander Dodd
 */
public class NoteOutOfBoundsException extends Exception{
    
    /**
     * 
     * @param errorDetail The error message to be associated with this exception object
     */
    public NoteOutOfBoundsException(String errorDetail)
    {
        super(errorDetail);
    }
    
}
