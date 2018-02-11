/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Elements;

import Manipulators.*;
import java.util.*;

/**A concrete Scale extends the Scale class to encapsulate a Scale made up of
 * specific pitches.
 *
 * @author Alexander Dodd
 */
public class ConcreteScale extends Scale{
    
  private  List<Key> scaleKeys = new ArrayList<>();

  /**
   * Creates a concrete scale object
   * @param scaleNotes all the keys of the scale
   * @param scaleName the type of the scale
   * @param position the position of the tonic pitch
   * @throws NoteOutOfBoundsException if the scale contains any pitches
   * that are below the first position or above the eighth position.
   */
    public ConcreteScale(List<KeyLetterEnum> scaleNotes, ScaleModeEnum scaleName, KeyPositionEnum position) throws NoteOutOfBoundsException {
        super(scaleNotes, scaleName);
        
        KeyLetterEnum lastKey = null;
        
        
        for (KeyLetterEnum let : scaleNotes) {
            if (lastKey == null) {
                lastKey = let;
            } else {
                if (let.getKeyIdentity() < lastKey.getKeyIdentity()) {

                                   
                        position = KeyPositionEnum.getNextPosition(position);
                        lastKey = let;
                   

                }
            }
            scaleKeys.add(new Key(let, position));
        }
        
        if(scaleNotes.get(0).getKeyIdentity() < lastKey.getKeyIdentity())
        {
            KeyPositionEnum.getNextPosition(position);
        }
        scaleKeys.add(new Key(scaleNotes.get(0), position));
    }
    
    /**
     * 
     * @return a list of all the pitches contained within the scale
     */
    public List<Key> getScaleKeys()
    {
        return scaleKeys;
    }
    
}
