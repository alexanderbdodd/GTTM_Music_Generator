/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Phrase_Generator;

import Elements.*;
import GTTM_Analyser.*;
import Grammar_Elements.GrammarContainer;
import Grammar_Elements.MetricalStructure.*;
import Elements.Pair;
import java.util.*;

/**The Phrase_Generator class is used to generate musical phrases based on a given
 * grammar structure
 *
 * @author Alexander Dodd
 */
public class PhraseGenerator extends Generator {

    private static PhraseGenerator instance;

    /**
     *
     * @return the singleton instance of this class
     */
    public static PhraseGenerator getInstance() {
        if (instance == null) {
            instance = new PhraseGenerator();
        }

        return instance;
    }

    private PhraseGenerator() {
        super();
    }

    private List<Pair<Integer, Map<Beat, Event>>> generateMusicSolutions(GrammarContainer grammar,
            Scale localScale) {
        assignFieldVariables(grammar);

        List<Pair<Integer, Map<Beat, Event>>> filteredChains = null;

        //getting a list of pitch event chains
        int count = 0;
        
        while (filteredChains == null || filteredChains.isEmpty()) {

            if (count > 0) {
                return null;
            }

            
         //localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.FS, ScaleModeEnum.MAJOR);

            List<Map<Beat, Key>> candidateChains;
            //create candidate chains of pitch events
            candidateChains = PitchGenerator.getInstance()
                    .getCandidatePitchChains(grammar,
                            localScale);
            if (candidateChains == null) {
                return null;
            }

            System.out.println("Filtering candidates to fit rhythm solutions");
            
            
            //filtering out chains which don't fit grouping generator constraints
            filteredChains
                    = DurationsGenerator.getInstance()
                    .applyGroupingConstraints(grammar, candidateChains);

            count++;
        }

        return filteredChains;
    }

    /**
     * Constructs a monophonic phrase based on the input grammar structure
     * @param grammar the grammar structure for use in constructing the phrase
     * @return an EventStream containing a monophonic phrase solution, or null if no
     * solution could be found
     */
    public EventStream constructMonophonicPhrase(GrammarContainer grammar) {
        
       Scale localScale = NoteGenerator.getInstance().createRandomDiatonicScale();
        List<Pair<Integer, Map<Beat, Event>>> filteredChains
                = generateMusicSolutions(grammar, localScale);
        
        if(filteredChains == null)
        {
            return null;
        }
        
        System.out.println("Solutions found. Picking a solution at random.");
        System.out.println();
            
        Random rand = new Random();

        int highestMatch = -1;

        for (Pair<Integer, Map<Beat, Event>> i : filteredChains) {
            if (i.first > highestMatch) {
                highestMatch = i.first;
            }
        }

        List<Map<Beat, Event>> maps = new ArrayList<>();

        for (Pair<Integer, Map<Beat, Event>> i : filteredChains) {
            if (i.first == highestMatch) {
                maps.add(i.second);
            }
        }

        Map<Beat, Event> eventMap = maps.get(rand.nextInt(maps.size()));

        List<Event> eventStream = new ArrayList<>();

        for (Beat b : grammar.getMetricalStructure().getMetricalBeatsList()) {
            if (eventMap.get(b).getDurationEnum() != null) {
                eventStream.add(eventMap.get(b));
            }
        }

        return new EventStream(eventStream, localScale);
    }

    /**
     * Constructs a monophonic phrase from the given input grammar.
     *
     * @param grammar the grammatical structure to be used in generating the
     * melody
     * @return an EventStream containing a monophonic phrase that fits the
     * grammatical structure. Return null if no phrase can be constructed from
     * the grammar
     */
    public Map<Double, EventStream> constructMonophonicPhrasePairs(GrammarContainer grammar) {

        Scale localScale = NoteGenerator.getInstance().createRandomDiatonicScale();
        List<Pair<Integer, Map<Beat, Event>>> filteredChains
                = generateMusicSolutions(grammar, localScale);
        
        if(filteredChains == null)
        {
            return null;
        }
        
        System.out.println("Solutions found. Finding highest and lowest rated solutions.");
        System.out.println();
        
        return filterEventMaps(grammar, filteredChains, localScale);
    }

    private Map<Double, EventStream> filterEventMaps(GrammarContainer grammar,
            List<Pair<Integer, Map<Beat, Event>>> filteredChains, Scale localScale) {
        Map<Double, EventStream> eventstreamMap = new HashMap<>();

        int highestMetricalValue = 0;

        List<Event> eventStream = new ArrayList<>();

        double highest = 0.0;
        double lowest = 1.0;

        for (Pair<Integer, Map<Beat, Event>> pair : filteredChains) {
            for (Beat b : grammar.getMetricalStructure().getMetricalBeatsList()) {
                if (pair.second.get(b) != null && pair.second.get(b).getDurationEnum() != null) {
                    eventStream.add(pair.second.get(b));
                }
            }

            EventStream stream = new EventStream(eventStream, localScale);
            eventStream = new ArrayList<>();

            double rating = RatingsGenerator.getInstance().obtainRating(grammar, stream);
            eventstreamMap.put(rating, stream);

            if (rating > highest) {
                highest = rating;
            }
            if (rating < lowest) {
                lowest = rating;
            }

        }

        List<Double> removeDbl = new ArrayList<>();

        for (Double rating : eventstreamMap.keySet()) {
            if (rating != highest && rating != lowest) {
                removeDbl.add(rating);

            }
        }

        for (Double rating : removeDbl) {
            eventstreamMap.remove(rating);
        }

        return eventstreamMap;
    }
}
