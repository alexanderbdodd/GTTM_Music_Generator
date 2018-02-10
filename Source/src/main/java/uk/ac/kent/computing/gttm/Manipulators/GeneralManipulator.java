
package uk.ac.kent.computing.gttm.Manipulators;

import java.util.ArrayList;
import java.util.List;

import uk.ac.kent.computing.gttm.Elements.KeyLetterEnum;

/**A singleton object which provides a series of methods for manipulating musical elements.
 *
 * @author Alexander Dodd
 */
public class GeneralManipulator {
    
    /**
     * Cycles the given KeyLetterEnum array such that the KeyLetterEnum key is 
     * the first element of the array. The array is converted into a List object.
     * 
     * @param key the KeyLetterEnum to be cycle to the front of the array
     * @param noteArray the array of KeyLetterEnums to be rearranged
     * @return a List object representing the rearranged list.
     */
    public static List<KeyLetterEnum> rearrangeNotes(KeyLetterEnum key, KeyLetterEnum[] noteArray) {

        List<KeyLetterEnum> bNotes = new ArrayList<>();
        List<KeyLetterEnum> eNotes = new ArrayList<>();

        int i = 0;
        while (!key.equals(noteArray[i])) {
            eNotes.add(noteArray[i]);
            i++;
        }

        while (i < noteArray.length) {
            bNotes.add(noteArray[i]);
            i++;
        }

        bNotes.addAll(eNotes);
        return bNotes;

    }
    
   
    
}
