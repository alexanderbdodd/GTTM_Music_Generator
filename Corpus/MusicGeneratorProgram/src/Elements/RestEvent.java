
package Elements;

import java.io.Serializable;

/**
 *Extends the Event class to describe a musical rest.
 * 
 * @author Alexander Dodd
 */
public class RestEvent implements Event{
    
    private DurationEnum duration;
    
    /**
     * 
     * @param length the duration describing how long the rest should last
     */
    public RestEvent(DurationEnum length)
    {
        this.duration = length;
    }

    @Override
    public DurationEnum getDurationEnum() {
        return duration; 
    }
    
}
