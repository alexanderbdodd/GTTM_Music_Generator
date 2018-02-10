/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Music_Generator.PhraseGenerator;

import java.util.*;

import uk.ac.kent.computing.gttm.Elements.Event;
import uk.ac.kent.computing.gttm.Elements.EventStream;
import uk.ac.kent.computing.gttm.Elements.Key;
import uk.ac.kent.computing.gttm.Elements.Scale;
import uk.ac.kent.computing.gttm.GTTM_Analyser.RatingsGenerator;
import uk.ac.kent.computing.gttm.Grammar_Elements.GrammarContainer;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;
import uk.ac.kent.computing.gttm.Music_Generator.NoteGenerator;
import uk.ac.kent.computing.gttm.Music_Generator.PhraseGenerator.GroupingGenerator.Pair;

/**
 *
 * @author Alexander
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

    public EventStream constructMonophonicPhrase(GrammarContainer grammar)
    {
          assignFieldVariables(grammar);
        
        //assign durational values which meet the metrical and grouping structure constraints
        Map<Beat, Event> durations = DurationsGenerator.getInstance().getCandidateEventStreams(grammar);

        List<GroupingGenerator.Pair<Integer, Map<Beat, Event>>> filteredChains = null;
        

        //getting a list of pitch event chains
        int count = 0;
        Scale localScale = null;
        while (filteredChains == null || filteredChains.isEmpty()) {

            if (count > 0) {
                return null;
            }

            localScale = NoteGenerator.getInstance().createRandomDiatonicScale();
     //   localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.FS, ScaleModeEnum.MAJOR);
            
            
            List<Map<Beat, Key>> candidateChains;
            //create candidate chains of pitch events
            candidateChains = PitchGenerator.getInstance()
                    .getCandidatePitchChains(grammar,
                            localScale);
            if (candidateChains == null) {
                return null;
            }
            
            System.out.println("Filtering candidates");

            //filtering out chains which don't fit grouping generator constraints
            filteredChains
                    = GroupingGenerator.getInstance()
                    .applyGroupingConstraints(grammar, candidateChains, durations);

            count++;
        }
       
        Random rand = new Random();
        
        int highestMatch = -1;
        
        for(Pair<Integer, Map<Beat, Event>> i : filteredChains)
        {
            if(i.first > highestMatch)
            {
                highestMatch = i.first;
            }
        }
        
       List<Map<Beat, Event>> maps = new ArrayList<>();
       
       for(Pair<Integer, Map<Beat, Event>> i : filteredChains)
       {
           if(i.first == highestMatch)
           {
               maps.add(i.second);
           }
       }
       
       
       Map<Beat, Event> eventMap = maps.get(rand.nextInt(maps.size()));
       
       List<Event> eventStream = new ArrayList<>();
       
       for(Beat b : grammar.getMetricalStructure().getMetricalBeatsList())
       {
           if(eventMap.get(b).getDurationEnum() != null)
           {
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

        assignFieldVariables(grammar);
        
        //assign durational values which meet the metrical and grouping structure constraints
        Map<Beat, Event> durations = DurationsGenerator.getInstance().getCandidateEventStreams(grammar);

        List<GroupingGenerator.Pair<Integer, Map<Beat, Event>>> filteredChains = null;
        

        //getting a list of pitch event chains
        int count = 0;
        Scale localScale = null;
        while (filteredChains == null || filteredChains.isEmpty()) {

            if (count > 0) {
                return null;
            }

            localScale = NoteGenerator.getInstance().createRandomDiatonicScale();
     //   localScale = ScaleConstructor.getInstance().constructScale(KeyLetterEnum.FS, ScaleModeEnum.MAJOR);
            
            
            List<Map<Beat, Key>> candidateChains;
            //create candidate chains of pitch events
            candidateChains = PitchGenerator.getInstance()
                    .getCandidatePitchChains(grammar,
                            localScale);
            if (candidateChains == null) {
                return null;
            }
            
            System.out.println("Filtering candidates");

            //filtering out chains which don't fit grouping generator constraints
            filteredChains
                    = GroupingGenerator.getInstance()
                    .applyGroupingConstraints(grammar, candidateChains, durations);

            count++;
        }
       
        return filterEventMaps(grammar, filteredChains, localScale);
    }
    
    private Map<Double, EventStream> filterEventMaps(GrammarContainer grammar,
            List<GroupingGenerator.Pair<Integer, Map<Beat, Event>>> filteredChains, Scale localScale) {
        Map<Double, EventStream> eventstreamMap = new HashMap<>();

        int highestMetricalValue = 0;

        List<Event> eventStream = new ArrayList<>();

        double highest = 0.0;
        double lowest = 1.0;

        for (GroupingGenerator.Pair<Integer, Map<Beat, Event>> pair : filteredChains) {
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
