package uk.ac.kent.computing.gttm.Music_Generator.PhraseGenerator;

import java.util.*;

import uk.ac.kent.computing.gttm.Elements.AttackEvent;
import uk.ac.kent.computing.gttm.Elements.DurationEnum;
import uk.ac.kent.computing.gttm.Elements.Event;
import uk.ac.kent.computing.gttm.Elements.Key;
import uk.ac.kent.computing.gttm.Elements.KeyLetterEnum;
import uk.ac.kent.computing.gttm.Elements.KeyPositionEnum;
import uk.ac.kent.computing.gttm.Elements.RestEvent;
import uk.ac.kent.computing.gttm.GTTM_Analyser.AnalyserUtils;
import uk.ac.kent.computing.gttm.GTTM_Analyser.GroupingStructureAnalyser;
import uk.ac.kent.computing.gttm.Grammar_Elements.GrammarContainer;
import uk.ac.kent.computing.gttm.Grammar_Elements.ExceptionClasses.GroupingWellFormednessException;
import uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure.Group;
import uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure.HighLevelGroup;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;

/**Generates durational candidates based on the grouping structure constraints
 * of the grammar and the durations solution provided by the DurationsGenerator
 *
 * @author Alexander Dodd
 */
public class GroupingGenerator extends Generator {

    private static GroupingGenerator instance = null;

    private int recursionCounter = 0;

    private List<Beat> pitchBeats = null;

    /**
     * 
     * @return the singleton instance of this class
     */
    public static GroupingGenerator getInstance() {
        if (instance == null) {
            instance = new GroupingGenerator();
        }

        return instance;
    }

    private GroupingGenerator() {
        super();
    }

    /**
     * Creates a map of beats against duration solutions, each map is mapped against an
     * Integer indicating how much each solution matches the metrical structure solution.
     * Solutions are determined using the grouping structure constraints of the grammar
     * @param grammar the grammar for use in generating the durational candidates
     * @param candidateChains the candidate pitch chains for use in generating durational candidates
     * @param durationsMap the durations solution generated using the metrical structure constraints
     * @return a map of Integers against a map of Beat against Event objects. The event objects represent
     * candidate musical Events constructed using the grouping durational constraints, the candidate pitch chains
     * and the metrical structure durations solution. Each Integer represents how far each beat-event map 
     * matches the metrical structure constraints
     */
    public List<Pair<Integer, Map<Beat, Event>>> applyGroupingConstraints(GrammarContainer grammar,
            List<Map<Beat, Key>> candidateChains, Map<Beat, Event> durationsMap) {

        assignFieldVariables(grammar);

        pitchBeats = new ArrayList<>();

        for (Beat b : candidateChains.get(0).keySet()) {
            pitchBeats.add(b);
        }

        //filter out chains which undermine grouping structure
        //candidateChains = filterChains(candidateChains);
        //applies the metrical outline to each chain and creates a map of beats against events
        //for each chain
        Map<Integer, Map<Group, List<Pair>>> durationsByLevel
                = generateCandidatesForEachLevel();

        //produces a list of all preferable duration combinations
        List<Map<Beat, DurationEnum>> map
                = generateCandidateDurationCombinations(durationsByLevel);

        //produces a map which maps the compatability of each of the generated
        //duration chains against the metrical duration map. A higher integer value
        //represents a higher preference.
        Map<Integer, List<Map<Beat, DurationEnum>>> matchedDurations
                = sortByMostMetricallyPreferable(map, durationsMap);

        //filters the candidate pitch chains which don't meet grouping preference rules
        //constraints.
        List<Pair<Integer, Map<Beat, Event>>> filteredChains
                = filterCandidates(matchedDurations, candidateChains);

        if (!filteredChains.isEmpty()) {
            Map<Beat, Event> temp = filteredChains.get(0).second;

            System.out.println();

            for (Beat b : getmContainer().getMetricalBeatsList()) {
                System.out.println();
                System.out.print(b.getPosition() + ": ");
                if (temp.get(b) == null) {
                    System.out.print("null");
                } else {
                    System.out.print(temp.get(b).getDurationEnum());
                }
            }
        }

        return filteredChains;

    }

    private List<Pair<Integer, Map<Beat, Event>>> filterCandidates(Map<Integer, List<Map<Beat, DurationEnum>>> matchedDurations,
            List<Map<Beat, Key>> candidateChains) {

        //create a list of all the matches and arranges them in descending
        //order of greatness
        List<Integer> matches = new ArrayList<>();

        matches.addAll(matchedDurations.keySet());

        Collections.sort(matches);
        Collections.reverse(matches);

        //candidateChains = filterChains(candidateChains);
        if (candidateChains.isEmpty()) {
            return null;
        }

        List<Pair<Integer, Map<Beat, Event>>> filteredChains = new ArrayList<>();

        int metricalPreference = matches.size();

        //start from the highest metrical match working downwards
        for (Integer match : matches) {
            metricalPreference--;

            Map<Beat, Event> eventMap = new HashMap<>();

            for (Map<Beat, DurationEnum> map : matchedDurations.get(match)) {
                for (Map<Beat, Key> pitchMap : candidateChains) {

                    eventMap.clear();

                    for (Beat beat : pitchMap.keySet()) {
                        eventMap.put(beat, new AttackEvent(pitchMap.get(beat), map.get(beat)));
                    }

                    for (Beat beat : map.keySet()) {
                        if (!eventMap.containsKey(beat)) {
                            eventMap.put(beat, new RestEvent(map.get(beat)));
                        }
                    }

                    if (checkConstraints(eventMap)) {

                        Map<Beat, Event> eventMapCopy = new HashMap<>();

                        for (Beat beat : eventMap.keySet()) {
                            eventMapCopy.put(beat, eventMap.get(beat));
                        }
                        filteredChains.add(new Pair<>(metricalPreference, eventMapCopy));

                        if (filteredChains.size() > 50000) {
                            return filteredChains;
                        }

                    }
                }

            }
        }

        return filteredChains;
    }

    //this is merging the map of beats against durations produced by the metrical
    //generator, and the map of beatd against durations produced by the grouping
    //analysis.
    private Map<Integer, List<Map<Beat, DurationEnum>>>
            sortByMostMetricallyPreferable(List<Map<Beat, DurationEnum>> maps,
                    Map<Beat, Event> durationsMap) {

        Map<Beat, DurationEnum> newMap;
        List<Map<Beat, DurationEnum>> newMaps = new ArrayList<>();
        Map<Integer, List<Map<Beat, DurationEnum>>> ret = new HashMap<>();

        int matches = 0;

        if (maps.size() == 0) {
            newMap = new HashMap<>();
            for (Beat b : durationsMap.keySet()) {
                if (durationsMap.get(b) == null) {
                    newMap.put(b, null);
                } else {
                    newMap.put(b, durationsMap.get(b).getDurationEnum());
                }
                matches++;
            }

            newMaps.add(newMap);
            ret.put(matches, newMaps);
        }

        for (Map<Beat, DurationEnum> map : maps) {

            newMap = new HashMap<>();
            DurationEnum lastDuration = null;
            int durationExpansion = 0;
            //go through all the beats of the container
            for (Beat b : getmContainer().getMetricalBeatsList()) {

                //checking to see if a grouping duration was assigned
                if (map.containsKey(b)) {

                    if (durationsMap.get(b) == null || map.get(b) == null) {
                        if (durationsMap.get(b) == null && map.get(b) == null) {
                            matches++;
                            newMap.put(b, null);

                        } else if (map.get(b) == null) {
                            newMap.put(b, null);
                        } else {
                            newMap.put(b, map.get(b));
                        }

                    } else if (durationsMap.get(b).getDurationEnum() == map.get(b)) {
                        matches++;
                        newMap.put(b, map.get(b));
                        lastDuration = newMap.get(b);
                        durationExpansion = 0;
                    } else {
                        newMap.put(b, map.get(b));
                        lastDuration = newMap.get(b);
                        durationExpansion = 0;
                    }

                } //if there was no assignment of the beat in the map
                //fill in the unfilled beats
                else {
                    if (lastDuration == null) {
                        if (durationsMap.get(b) == null) {

                            newMap.put(b, DurationEnum.SIXTEENTH);
                        } else {

                            newMap.put(b, durationsMap.get(b).getDurationEnum());
                        }
                        lastDuration = newMap.get(b);
                        durationExpansion = 0;
                        matches++;
                    } else {

                        int lastDurationDistance = durationToBeat(lastDuration);

                        if (durationExpansion > lastDurationDistance) {
                            if (durationsMap.get(b) == null) {
                                newMap.put(b, DurationEnum.SIXTEENTH);
                            } else {
                                matches++;
                                newMap.put(b, durationsMap.get(b).getDurationEnum());
                                lastDuration = newMap.get(b);
                                durationExpansion = 0;
                            }
                        } else {
                            if (durationsMap.get(b) == null) {
                                matches++;
                            }
                            newMap.put(b, null);
                        }

                    }

                }

                durationExpansion++;
            }

            //gets stuck here?
            newMaps.add(newMap);

            if (ret.containsKey(matches)) {
                ret.get(matches).add(newMap);
            } else {
                ret.put(matches, newMaps);
                newMaps = new ArrayList<>();
            }
        }

        return ret;

    }

    //check combinations of durations. Each successively higher level should
    //have a successively lower GPR rating.
    private List<Map<Beat, DurationEnum>>
            generateCandidateDurationCombinations(Map<Integer, Map<Group, List<Pair>>> durationsByLevel) {
        List<Map<Beat, DurationEnum>> combinations = new ArrayList<>();

        Map<Group, List<Pair>> allGroups = new HashMap<>();

        //creating a list of all the groups and their candidate duration maps
        for (Integer level : durationsByLevel.keySet()) {

            for (Group gr : durationsByLevel.get(level).keySet()) {
                allGroups.put(gr, durationsByLevel.get(level).get(gr));
            }
        }

        List<Group> groups = new ArrayList<>();

        //create a list of all the groups
        for (Group group : allGroups.keySet()) {
            groups.add(group);
        }

        Map<Beat, DurationEnum> durationChain = new HashMap<>();

        //sift through all of the candidates to find fitting solutions
        recursivelyChainDurationalPeriods(0, combinations, allGroups,
                durationChain, groups);

        return combinations;

    }
    /*
     Goes through all of the groups and generates different combinations of durations 
     for use in filtering against the metrical structure recommendations.
    
     */

    private void recursivelyChainDurationalPeriods(int pos, List<Map<Beat, DurationEnum>> durationCandidates,
            Map<Group, List<Pair>> groupCandidates, Map<Beat, DurationEnum> durationChain, List<Group> groups) {

        //if the position is less than the group list size,
        //perform recursive method.
        //else, it's the end condition of the recusion.
        if (pos < groups.size()) {
            Group group = groups.get(pos);

            //check to see there is a list of candidates mapped against the group
            if (groupCandidates.containsKey(group)) {

                //for all the candidate matches
                for (Pair pair : groupCandidates.get(group)) {
                    Map<Beat, DurationEnum> pairMap
                            = (Map<Beat, DurationEnum>) pair.first;

                    boolean overlap = false;

                    List<Beat> seenBeats = new ArrayList<>();

                    //go through all the beats of the candidate map
                    for (Beat beat : pairMap.keySet()) {

                        //if the chain under construction already has the beat
                        //in its map
                        if (durationChain.containsKey(beat)) {
                            //if the mapping is  not the same, continue, break from the loop
                            if (durationChain.get(beat) != pairMap.get(beat)) {
                                //durationChain.put(beat, pairMap.get(beat));
                                overlap = true;
                                break;
                            }
                            //else add the beat ot the duration chain
                        } else {
                            seenBeats.add(beat);
                            durationChain.put(beat, pairMap.get(beat));
                        }
                    }

                    //if there was no overlapping, call the method recursively
                    if (!overlap) {
                        int pos2 = pos + 1;
                        recursivelyChainDurationalPeriods(pos2, durationCandidates,
                                groupCandidates, durationChain, groups);
                    }

                    //remove all the mappings which have been added
                    for (Beat beat : seenBeats) {
                        durationChain.remove(beat);
                    }

                }

                //else go to the next group
            } else {
                int pos2 = pos + 1;
                recursivelyChainDurationalPeriods(pos2, durationCandidates,
                        groupCandidates, durationChain, groups);
            }

            //end condition
        } else {
            //copy the durationChain map and add it to the list of candidate duration chains.

            Map<Beat, DurationEnum> durationChainCopy = new HashMap<>();

            for (Beat b : durationChain.keySet()) {
                durationChainCopy.put(b, durationChain.get(b));
            }

            durationCandidates.add(durationChainCopy);

        }

    }

    //REVISE. COULD MALFUNCTION
    //generates candidate durations for all groups within the grammar    
    private Map<Integer, Map<Group, List<Pair>>> generateCandidatesForEachLevel() {
        Map<Integer, Map<Group, List<Pair>>> map = new HashMap<>();

        //the grouping level of the candidates
        Integer level = 1;

        //generate all the candidates for top hierarchical level
        map.put(level, createDurationCandidates(getgGroup(), 0.5));

        HighLevelGroup gr = getgGroup();

        //loops round going down through the grouping hierarchies 
        //and generates all durational candidates for each group
        //of the hierarchy
        while (true) {
            List<Group> nextLevel = new ArrayList<>();
            //goes through all the subgroups of the higher level group
            for (Group gr2 : gr.getSubGroups()) {
                //adds all the sub groups of all the high level groups to the list
                if (gr2.getClass() == HighLevelGroup.class) {
                    nextLevel.addAll(AnalyserUtils.getInstance().getNextLevelGroups((HighLevelGroup) gr2,
                            AnalyserUtils.getInstance().identifyBoundaryBeats(gr.getSubGroups(), pitchBeats), pitchBeats));
                }
            }

            HighLevelGroup tempGroup = null;

            //if no high level groups were found, break the loop
            if (nextLevel.isEmpty()) {
                break;
            }

            try {
                tempGroup = new HighLevelGroup(nextLevel);
            } catch (GroupingWellFormednessException e) {
                //      if (nextLevel.get(0).getClass() == HighLevelGroup.class) {
                tempGroup = (HighLevelGroup) nextLevel.get(0);
                //    } else {
                //       break;
                //    }
            }
            level++;

            //put the generated candidates along with the level into the map
            map.put(level, createDurationCandidates(tempGroup, 0.5));

            //set gr to the temp group
            gr = tempGroup;
        }

        return map;

    }

    /**
     * Creates duration candidates maps for each of the groups. For use in
     * assessing best fitting durational values.
     *
     * @param groups the groups to be checked
     * @param minRating the minimum rating that constitutes a pass
     * @return a map of each group against its candidate solutions
     */
    private Map<Group, List<Pair>> createGroupDurationCandidates(List<Group> groups, double minRating) {

        Map<Group, List<Pair>> map = new HashMap<>();

        for (Group group : groups) {
            List<Pair> pairList = createGroupDurationVariants(group, minRating);
            if (pairList != null) {
                map.put(group, pairList);
            }
        }

        return map;

    }

    private Map<Group, List<Pair>> createDurationCandidates(HighLevelGroup group, double minRating) {
        Map<Group, List<Pair>> groupCandidates
                = createGroupDurationCandidates(group.getSubGroups(), minRating);

        // List<Pair> noPreferenceCandidates
        //         = generateNoBoundaryPreferenceList();
        return groupCandidates;
    }

    /**
     * Analyses a group to find candidate durations for the beats that fall
     * across its end boundary.
     *
     * @param group the group whose boundary is to be checked
     * @param minRating the minimum rating to accept
     * @return a list of all candidate durations for group, else null if no
     * boundary span could be created
     */
    private List<Pair> createGroupDurationVariants(Group group, double minRating) {

        List<Pair> candidateMaps = new ArrayList<>();
        Map<Beat, DurationEnum> durationMap = new HashMap<>();
        List<Beat> beats = getEventBeatSpan(group);

        if (beats != null) {
            for (Beat beat : beats) {
                durationMap.put(beat, null);
            }

            recursionCounter = 0;

            recursivelyCheckBoundaryPreferences(durationMap, candidateMaps, beats,
                    0, true, minRating);

            return candidateMaps;

        } else {
            return null;
        }

    }

    //beat map should be null for each beat to begin with
    /**
     * A recursive method designed to generate valid durational values for all
     * of the given beats which fit the GPR durational constraint rules.
     *
     * A List is created containing all of the duration variants which are
     * preferably rated.
     *
     *
     * @param eventMap a map of beats against prospective durational values. A
     * mapping of a beat against a null value indicates that the given beat is
     * contained within the duration of a prior pitch event
     * @param candidateMaps the list of candidate maps which fit the GPR
     * constraints
     * @param beats a list of all the beats to check through
     * @param pos the position within the beats list to check for the next beat
     * @param maximise set to true if the rating is to be found greater than the
     * minimum rating, and false if it's to be found below the minimum rating.
     * (for use in generate unpreferable durational values).
     * @param minRating the minimum rating to be used in judging whether a
     * durational map is preferable or not
     */
    private void recursivelyCheckBoundaryPreferences(Map<Beat, DurationEnum> eventMap,
            List<Pair> candidateMaps, List<Beat> beats, int pos, boolean maximise,
            double minRating) {

        if (pos < beats.size()) {

            //for each possible durational value
            for (DurationEnum duration : DurationEnum.values()) {
                //check how many beats the proposed duration will take
                int beatExpansion = durationToBeat(duration);
                Beat beat = beats.get(pos);
                //check how many beats until next pitch
                int distanceExpansion = distanceToNextPitch(beat, pitchBeats);
                //if duration won't fit, jump to next beat
                if (beatExpansion > distanceExpansion) {
                    continue;
                } else {
                    //put the beat along with the duration into the map
                    eventMap.put(beat, duration);

                    int i = 0;
                    boolean end = false;
                    int pos2 = pos + 1;
                    //updating event map with null values for all beats contained within duration
                    while (i != beatExpansion) {

                        pos2++;
                        beat = beat.getNextBeat();
                        eventMap.put(beat, null);
                        i++;
                        if (pos2 >= beats.size()) {
                            end = true;
                            break;
                        }
                    }

                    recursivelyCheckBoundaryPreferences(eventMap, candidateMaps, beats, pos2, maximise, minRating);
                    if (PitchGenerator.RECURSION_RESTRICTION && candidateMaps.size() > 25000
                            || PitchGenerator.RECURSION_RESTRICTION && recursionCounter > 999999) {
                        return;
                    }
                }
            }
        } else {

            /*   for (Beat b : getmContainer().getMetricalBeatsList()) {
             if (eventMap.containsKey(b)) {
             System.out.print(eventMap.get(b));
             System.out.print(" ");
             }
             }

             System.out.println();*/
            //if at the end of the beats list, the map is complete and
            //assess how well it fits the constraints.
            checkDurationalRules(candidateMaps, eventMap, maximise, minRating);

        }

    }

    //checks all the relevant durational rules of the GPR constraints and
    //adds the duration map to the candidate maps if constraints are met.
    private void checkDurationalRules(List<Pair> candidateMaps,
            Map<Beat, DurationEnum> eventMap, boolean maximise, double minRating) {
        List<Event> events = createEventSpan(eventMap);

        //generate ratings for all of the durational rules.
        double ratingGPR2a = GroupingStructureAnalyser.getInstance()
                .assessGPR2a(events);
        double ratingGPR2b = GroupingStructureAnalyser.getInstance()
                .assessGPR2b(events);
        double ratingGPR3d = GroupingStructureAnalyser.getInstance()
                .assessGPR3d(events);

        double[] ratings = new double[3];
        ratings[0] = ratingGPR2a;
        ratings[1] = ratingGPR2b;
        ratings[2] = ratingGPR3d;

        if (maximise) {
            if (ratingGPR2a > minRating || ratingGPR2b > minRating || ratingGPR3d > minRating) {
                candidateMaps.add(new Pair<Map<Beat, DurationEnum>, double[]>(copyMap(eventMap), ratings));
            } else {
                recursionCounter++;
            }
        } else {
            if (ratingGPR2a < minRating && ratingGPR2b < minRating && ratingGPR3d < minRating) {
                candidateMaps.add(new Pair<Map<Beat, DurationEnum>, double[]>(copyMap(eventMap), ratings));
            } else {
                recursionCounter++;
            }
        }
    }

    //used to generate a span of event objects from the given map of durations
    //for use with the GPR duration rules analysis. Not suitable for, e.g. GPR3a
    private List<Event> createEventSpan(Map<Beat, DurationEnum> durations) {
        List<Event> events = new ArrayList<>();
        List<Beat> beats = new ArrayList<>();

        Beat comparator = null;

        //create a list of all the beats in the mao
        for (Beat beat : durations.keySet()) {
            if (durations.get(beat) != null) {
                beats.add(beat);
            }

            comparator = beat;
        }

        //sort in position order
        Collections.sort(beats, comparator);

        //checks each beat. if it's a pitch beat, adds an AttackEvent which has the 
        //duration given to it in the map
        for (Beat beat : beats) {
            if (pitchBeats.contains(beat)) {
                events.add(new AttackEvent(KeyLetterEnum.C, KeyPositionEnum.values()[0], durations.get(beat)));
            } else {
                //else just add a rest event with the given duration
                events.add(new RestEvent(durations.get(beat)));
            }
        }

        return events;

    }

    //copies the contents of a Map<Beat, DurationEnum>
    private Map<Beat, DurationEnum> copyMap(Map<Beat, DurationEnum> map) {
        Map<Beat, DurationEnum> copyMap = new HashMap<>();

        for (Beat beat : map.keySet()) {
            copyMap.put(beat, map.get(beat));
        }

        return copyMap;
    }

    private List<Beat> getEventBeatSpan(Group group) {

        Beat boundaryBeat = AnalyserUtils.getInstance().identifyBoundaryBeat(group, pitchBeats);
        return AnalyserUtils.getInstance().getBeatSpanFromBoundary(boundaryBeat, group, pitchBeats);
    }

    private boolean checkConstraints(Map<Beat, Event> eventMap) {

        Map<Integer, List<Group>> groupLevels = AnalyserUtils.getInstance()
                .getBoundaryEventsPerLevel(pitchBeats, getgGroup());

        List<Event> eventSpan = new ArrayList<>();

        double maxRating = 9999999;

        for (Integer level : groupLevels.keySet()) {
            double localLowestRating = 999999;

            for (Group gr : groupLevels.get(level)) {
                List<Beat> beatSpan = getEventBeatSpan(gr);
                if (beatSpan == null) {
                    break;
                }
                eventSpan.clear();

                for (Beat beat : beatSpan) {
                    if (eventMap.get(beat).getDurationEnum() != null) {
                        eventSpan.add(eventMap.get(beat));
                    }
                }
                GroupingStructureAnalyser instance = GroupingStructureAnalyser.getInstance();

                double ratingGPR2a = instance.assessGPR2a(eventSpan);
                double ratingGPR2b = instance.assessGPR2b(eventSpan);
                double ratingGPR3a = instance.assessGPR3a(eventSpan);
                double ratingGPR3d = instance.assessGPR3d(eventSpan);

                double totalRating = (((ratingGPR2a * GroupingStructureAnalyser.GPR2A_WEIGHT)
                        + (ratingGPR2b * GroupingStructureAnalyser.GPR2B_WEIGHT)
                        + (ratingGPR3a * GroupingStructureAnalyser.GPR3A_WEIGHT)
                        + (ratingGPR3d * GroupingStructureAnalyser.GPR3D_WEIGHT))
                        / (GroupingStructureAnalyser.GPR2A_WEIGHT + GroupingStructureAnalyser.GPR2B_WEIGHT
                        + GroupingStructureAnalyser.GPR3A_WEIGHT + GroupingStructureAnalyser.GPR3D_WEIGHT));

                if (totalRating >= maxRating || totalRating <= 0.5) {
                    return false;
                }

                if (localLowestRating > totalRating) {
                    localLowestRating = totalRating;
                }
            }

            maxRating = localLowestRating;
        }

        return true;
    }

    public static class Pair<T, T2> {

       public T first;
       public T2 second;

        Pair(T first, T2 second) {
            this.first = first;
            this.second = second;
        }
    }

}
