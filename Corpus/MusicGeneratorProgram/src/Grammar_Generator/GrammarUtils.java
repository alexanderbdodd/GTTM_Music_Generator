package Grammar_Generator;


import Grammar_Elements.MetricalStructure.Beat;
import Grammar_Elements.MetricalStructure.MetricalContainer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**Provides a set of methods for use in constructing GTTM grammars.
 *
 * @author Alexander Dodd
 */
public class GrammarUtils {

    private static GrammarUtils instance = null;

    /**
     * 
     * @return the singleton instance of this class
     */
    public static GrammarUtils getInstance() {
        if (instance == null) {
            instance = new GrammarUtils();
        }

        return instance;
    }

    private GrammarUtils() {

    }

    /**
     * Constructs a MetricalContainer object from a list of beats
     * @param beats the list of Beat objects to be contained within the MetricalContainer
     * @return the MetricalContainer object
     */
    public MetricalContainer createMetricalContainer(List<Beat> beats) {
        MetricalContainer container = new MetricalContainer();

        for (Beat beat : beats) {
            container.addBeat(beat);
        }

        return container;
    }

    
    /**Constructs a span of Beats that fit the metrical constructor constraints.
     *      
     * @param repeat how many times to repeat the structure
     * @param highestStrength the highest strength of the beat structure
     * @return the list of beats
     */
    public List<Beat> constructBeatSpan(int repeat, int highestStrength) {
        List<Beat> beats = new ArrayList<>();

        int currentRepeat = 0;
        
        Random rand = new Random();
        
        boolean weak = rand.nextBoolean();

        while (currentRepeat < repeat) {
            beats.addAll(constructRepeatingPattern(highestStrength, weak));

            currentRepeat++;
        }

        return beats;
    }

    /**Constructs a repeating pattern of beats based on the highest strength value.
     * This is a simple implementation. In the future it would be important to
     * look at constructing structures which start on different strengths of beat
     * 
     * @param highestStrength the highest strength Beat value
     * @param weakStart true if the structure is to start on a weak beat, else false
     * @return the list of Beat objects
     */
    private List<Beat> constructRepeatingPattern(int highestStrength, boolean weakStart) {
        List<Beat> beats = new ArrayList<>();
        int currentLevel = highestStrength;
        
       weakStart = false;

        while (currentLevel > 0) {
            if(weakStart)
            {
                beats.add(new Beat(1));
                weakStart = false;
                
                if(currentLevel == 1)
                {
                    break;
                }
                
            }
            else{
            Beat beat = new Beat(currentLevel);
            beats.add(beat);
            currentLevel--;
            weakStart = true;
            }
            
        }

        return beats;
    }
    
    /**
     * Constructs a repeating pattern of Beat objects starting on a random strength beat
     * @param highestStrength the highest strength Beat value
     * @param repeatNo the amount of times to repeat the structure
     * @return the list of Beat objects representing the metrical structure
     */
     public List<Beat> constructRepeatingPattern(int highestStrength, int repeatNo) {

        //working out which strength of beat to start on.
        Random rand = new Random();
        int startStrength = rand.nextInt(highestStrength) + 1;

        List<Integer> strengths = new ArrayList<>();
        strengths.add(startStrength);
        int lastStrength = startStrength;
        int nextStrength = startStrength + 1;
        int highStrengthCount = 0;
        if (strengths.get(0) > 1) {
            highStrengthCount++;
        }

        while (highStrengthCount < highestStrength) {
            while (nextStrength <= highestStrength && highStrengthCount < highestStrength) {
                if (lastStrength == 1) {
                    strengths.add(nextStrength);
                    lastStrength = nextStrength;
                    nextStrength++;
                    highStrengthCount++;
                } else {
                    strengths.add(1);
                    lastStrength = 1;
                }
            }
                       
            nextStrength = 2;
        }
        
        if(strengths.get(0) != 1)
        {
            strengths.add(1);
        }

        List<Beat> beats = new ArrayList<>();

        int count = 0;
        while (count < repeatNo) {
            for (Integer i : strengths) {
                beats.add(new Beat(i));
            }
            count++;
        }

        return beats;
    }
}
