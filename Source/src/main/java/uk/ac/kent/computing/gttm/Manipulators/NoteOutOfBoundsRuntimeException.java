package uk.ac.kent.computing.gttm.Manipulators;

/**A class extending the RuntimeException class which is thrown if a Key object's position is
 * outside of the supported range.
 *
 * @author Alexander Dodd
 */
public class NoteOutOfBoundsRuntimeException extends RuntimeException{
    
    /**
     * 
     * @param errorDetail The error message to be associated with this exception object
     */
    public NoteOutOfBoundsRuntimeException(String errorDetail)
    {
        super(errorDetail);
    }
    
}
