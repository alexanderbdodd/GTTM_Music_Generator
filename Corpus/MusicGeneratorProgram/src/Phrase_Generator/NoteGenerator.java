/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Phrase_Generator;

import Elements.*;
import Manipulators.*;
import java.util.Random;


/**A class used to randomly generate various musical components.
 *
 * @author Alexander Dodd
 */
public class NoteGenerator {
    
    private static NoteGenerator instance = null;
    
    private Random rand = new Random();
    
    /**
     * 
     * @return the singleton instance of this class
     */
    public static NoteGenerator getInstance()
    {
        if(instance == null)
        {
            instance = new NoteGenerator();
        }
        
        return instance;
    }
    
    private NoteGenerator()
    {}
    
    /**
     * 
     * @return a randomly chosen Key object
     */
    public Key getRandomKey()
    {
        KeyLetterEnum letter = getRandomKeyLetter();
        KeyPositionEnum position = getRandomPosition();
        
        
        return new Key(letter, position);
               
    }
    
    /**
     * 
     * @return a randomly generated KeyLetterEnum 
     */
    public KeyLetterEnum getRandomKeyLetter()
    {
        return KeyLetterEnum.values()[rand.nextInt(KeyLetterEnum.values().length)];
    }
    
    /**
     * 
     * @return a randomly generated DurationEnum
     */
    public DurationEnum getRandomDuration()
    {
        return DurationEnum.values()[rand.nextInt(DurationEnum.values().length)];
    }
    
    /**
     * 
     * @return a randomly generated KeyPositionEnum
     */
    public KeyPositionEnum getRandomPosition()
    {
        return KeyPositionEnum.values()[rand.nextInt(KeyPositionEnum.values().length)];
    }
    
    /**
     * 
     * @return a randomly generated AttackEvent object
     */
    public AttackEvent getRandomAttackEvent()
    {
        return new AttackEvent(getRandomKey(), getRandomDuration());
    }
    
    /**
     * 
     * @param scale the scale from which to generate the AttackEvent
     * @return the randomly generated AttackEvent
     */
    public AttackEvent getRandomAttackEventFromScale(Scale scale)
    {
        KeyLetterEnum letter = scale.getNotes().get(rand.nextInt(scale.getNotes().size()));
        
        return new AttackEvent(letter, getRandomPosition(), getRandomDuration());
        
        
        
    }
    
    /**
     * 
     * @return a randomly chosen diatonic scale in either Major or Natural Minor
     */
    public Scale createRandomDiatonicScale()
    {
        KeyLetterEnum ev = getRandomKeyLetter();
        int randomNum = rand.nextInt(2);
        
        return randomNum == 0 ? ScaleConstructor.getInstance().constructScale(ev, ScaleModeEnum.MAJOR) : 
                ScaleConstructor.getInstance().constructScale(ev, ScaleModeEnum.NATURALMINOR);
              
    }
    
    
    
}
