/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GTTM_Analyser.Exceptions;

/**
 *
 * @author Alexander
 */
public class EventMapConstructionException extends RuntimeException{
    
    public EventMapConstructionException(String errorMessage)
    {
        super(errorMessage);
    }
    
}
