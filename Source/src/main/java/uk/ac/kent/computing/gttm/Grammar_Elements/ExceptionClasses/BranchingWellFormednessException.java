/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Grammar_Elements.ExceptionClasses;

/**
 *
 * @author Alexander
 */
public class BranchingWellFormednessException extends RuntimeException{
    
    public BranchingWellFormednessException(String errorMessage)
    {
        super(errorMessage);
    }
    
}
