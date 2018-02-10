/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Elements;

/**Provides an interface for Event classes. In GTTM, Events encapsulate the instantiation of
 * various musical elements, such as rests and pitches.
 *
 * @author Alexander Dodd
 */
public interface Event {
    
    /**
     * 
     * @return the duration associated with the Event
     */
    public DurationEnum getDurationEnum();
    
    
}
