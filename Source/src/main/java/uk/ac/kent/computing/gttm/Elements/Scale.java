
package uk.ac.kent.computing.gttm.Elements;



import java.util.*;

import uk.ac.kent.computing.gttm.Manipulators.ScaleModeEnum;

/**A class used to encapsulate a diatonic scale.
 *
 * @author Alexander Dodd
 */
public class Scale{
    
   private final ScaleModeEnum scaleEnum;
   private final List<KeyLetterEnum> notes;
   private KeyLetterEnum tonic;
    
   /**
    * Create a Scale object from a given list of KeyLetterEnum and a given
    * ScaleModeEnum
    * @param scaleNotes the list of keys to be included in the scale
    * @param scaleName the scale mode of the Scale object
    */
    public Scale(List<KeyLetterEnum> scaleNotes, ScaleModeEnum scaleName)
    {
        notes = scaleNotes;
        tonic = notes.get(0);
        this.scaleEnum = scaleName;
        
    }
  
    /**
     * 
     * @return a list of all the KeyLetterEnum objects associated with the Scale
     */
    public ArrayList<KeyLetterEnum> getNotes()
    {
        
        ArrayList<KeyLetterEnum> copyList = new ArrayList<>();
        
        notes.stream().forEach((l) -> {
            copyList.add(l);
       });
        
        return copyList;
             
    }
    /**
     * 
     * @return the scale mode of the Scale (e.g. Major or Minor)
     */
    public ScaleModeEnum getScaleModeEnum()
    {
        return scaleEnum;
    }
    
    /**
     * 
     * @return the dominant note of the scale
     */
    public KeyLetterEnum getDominant()
    {
       return notes.get(4);
    }
    
    /**
     * 
     * @return the tonic of the scale
     */
    public KeyLetterEnum getTonic()
    {
       return notes.get(0);
    }
    
    /**
     * 
     * @return the supertonic of the scale
     */
    public KeyLetterEnum getSupertonic()
    {
       return notes.get(1);
    }
    
    /**
     * 
     * @return the mediant of the scale
     */
    public KeyLetterEnum getMediant()
    {
       return notes.get(2);
    }
    
    /**
     * 
     * @return the subdominant of the scale
     */
    public KeyLetterEnum getSubdominant()
    {
       return notes.get(3);
    }
    
    /**
     * 
     * @return the submediant of the scale
     */
    public KeyLetterEnum getSubmediant()
    {
       return notes.get(5);
    }
    /**
     * 
     * @return the subtonic of the scale
     */
    public KeyLetterEnum getSubtonic()
    {
       return notes.get(6);
    }
    
    
    
    
}
