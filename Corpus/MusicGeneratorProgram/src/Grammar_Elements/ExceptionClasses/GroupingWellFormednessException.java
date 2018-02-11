/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grammar_Elements.ExceptionClasses;

/**
 *
 * @author Alexander
 */
public class GroupingWellFormednessException extends RuntimeException{
    
    public GroupingWellFormednessException(String errorMessage)
    {
        super(errorMessage);
    }
    
}
