/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GTTM_Analyser;

import Elements.*;
import Grammar_Elements.ExceptionClasses.GroupingWellFormednessException;
import Grammar_Elements.GroupingStructure.*;
import Grammar_Elements.MetricalStructure.Beat;
import Grammar_Elements.ReductionBranches.Branch;
import java.util.*;

/**
 *
 * @author Alexander Dodd
 */
public class AnalyserUtils {

    private static AnalyserUtils instance;

    public static AnalyserUtils getInstance() {
        if (instance == null) {
            instance = new AnalyserUtils();
        }
        return instance;
    }

    private AnalyserUtils() {

    }

   /**
     * Works out the distance between two beats.
     *
     * @param firstBeat the beat from which to assess the distance
     * @param nextBeat the beat to find the distance to
     * @return the distance between the two beats, or -1 if the nextBeat is in a
     * position before the firstBeat
     *
     */
    public int distanceToBeat(Beat firstBeat, Beat nextBeat) {
        if (firstBeat.getPosition() > nextBeat.getPosition()) {
            return -1;
        } else {

            return nextBeat.getPosition() - firstBeat.getPosition();
        }

    }
    
    
    

    /**
     * Determines the distance to the next pitch beat from the given beat
     *
     * @param beat the beat from which to assess the distance
     * @param pitchBeats the list of beats allocated as pitch beats
     * @return the distance to the next pitch beat
     */
    public int distanceToNextPitch(Beat beat, List<Beat> pitchBeats) {
        int distance = 0;
        Beat currentBeat = beat;

        while (currentBeat.getNextBeat() != null
                && !pitchBeats.contains(currentBeat.getNextBeat())) {
            distance++;
            currentBeat = currentBeat.getNextBeat();
        }

        return distance;

    }

    /*
     Get all the boundary beats within the whole grammatical structure
     */
    public List<Beat> getAllBoundaryBeats(HighLevelGroup gGroup, List<Beat> pitchBeats) {
        List<Group> baseGroups = new ArrayList<>();

        List<HighLevelGroup> groups = new ArrayList<>();
        groups.add(gGroup);

        HighLevelGroup gr;
        while (!groups.isEmpty()) {
            gr = groups.get(0);
            groups.remove(gr);

            for (Group group : gr.getSubGroups()) {
                if (group.getClass() == BaseGroup.class) {
                    baseGroups.add(group);
                } else {
                    groups.add((HighLevelGroup) group);
                }
            }

        }
        return identifyBoundaryBeats(baseGroups, pitchBeats);
    }

    /**
     * Identifies the beats which constitute boundary pitch events of the given
     * groups and returns them as a list
     *
     * @param groups the groups to be searched for boundary pitches beats
     * @param pitchBeats the list of beats allocated as pitch instantiation beats
     * @return a list of boundary beats
     */
    public List<Beat> identifyBoundaryBeats(List<Group> groups, List<Beat> pitchBeats) {
        List<Beat> beats = new ArrayList<>();

        //for all the specified groups
        for (Group group : groups) {

            //invoke the identifyBoundaryBeat method for the group
            Beat beat = identifyBoundaryBeat(group, pitchBeats);

            //make sure the beat returned is not null, 
            //and add it to the beats list
            if (beat != null) {
                beats.add(beat);
            }

        }

        return beats;

    }

    /**
     * Used to identify the boundary beat of a group. A boundary beat is the
     * last beat of the group which lands on the inception of a pitch event.
     *
     * @param group the group to be checked
     * @param pitchBeats a list of all beats which are on the inception of a
     * pitch event
     * @param pitchBeats the list of beats allocated as pitch instantiation beats
     * @return the beat identified as the boundary. If no pitch beat is found,
     * then null is returned.
     */
    public Beat identifyBoundaryBeat(Group group, List<Beat> pitchBeats) {
        int spanSize = group.getMetricalBeatSpan().size();
        Beat currentBeat = group.getMetricalBeatSpan().get(spanSize - 1);

        //while the group contains the beat to be assessed
        while (group.getMetricalBeatSpan().contains(currentBeat)) {

            //if the beat is a pitch beat, return the beat
            if (pitchBeats.contains(currentBeat)) {
                return currentBeat;
            }

            //reduce the position to search
            spanSize--;

            //if the position is now less than or equal to zero, return null
            //to indicate no beat was found
            if (spanSize <= 0) {
                return null;
            }

            //if (group.getMetricalBeatSpan().get(spanSize - 1) != null) {
            currentBeat = group.getMetricalBeatSpan().get(spanSize - 1);
            // }

        }

        return null;
    }

    /**
     * This is used to identify the span of beats that surround a boundary beat.
     * The boundary beat need not necessarily be an actual boundary beat.
     *
     * @param boundaryBeat the beat to be used in constructing the beat span
     * @param group the greatest group to constrain the search for the beats
     * @param pitchBeats the list of beats allocated as pitch instantiation beats
     * within
     * @return the span of beats
     */
    public List<Beat> getBeatSpanFromBoundary(Beat boundaryBeat, Group group, 
            List<Beat> pitchBeats) {
       
        Collections.sort(pitchBeats, boundaryBeat);
        
        List<Beat> beats = new ArrayList<>();
        Beat currentBeat = boundaryBeat;

        //find the index of the boundary beat
        int beatPos = group.getMetricalBeatSpan().indexOf(currentBeat);
        beatPos--;

        boolean found = false;

        //finding the first pitch beat of the span
        //while the beat position is within the group's span of beats
        while (beatPos >= 0 && beatPos < group.getMetricalBeatSpan().size()) {
            currentBeat = group.getMetricalBeatSpan().get(beatPos);

            //check whether it constitutes a pitch beat
            //if so, break out of the while loop
            if (pitchBeats.contains(currentBeat)) {
                found = true;
                break;
            } else {
                beatPos--;
            }
        }

        //if no first beat was found, return null for the beat span list
        if (!found) {
            return null;
        } //else begin searching for the next pitch beat
        //to be refactored
        else {
            beatPos = 1;

            beats.add(currentBeat);

            //loop until 4 pitch beats have been identified
            while (beatPos < 4) {
                if (currentBeat.getNextBeat() == null) {
                    //if the next beat is null, then the constraint of
                    //finding four pitch beats can't be met,
                    //so return null
                    return null;
                } else {

                    currentBeat = currentBeat.getNextBeat();

                    //if it's a pitch beat, increase the total of found pitch beats
                    if (pitchBeats.contains(currentBeat)) {
                        beatPos++;
                    }

                    //add the beat to the span
                    beats.add(currentBeat);
                }
            }

        }

        //adding all the beats after the final pitch beat which
        //occur before the next inception of a pitch beat
        while (currentBeat.getNextBeat() != null
                && !pitchBeats.contains(currentBeat.getNextBeat())) {
            currentBeat = currentBeat.getNextBeat();

            beats.add(currentBeat);
        }

        //sort of the beats so that they are in position order
        Collections.sort(beats, beats.get(0));

        return beats;
    }

    public List<Beat> getBeatSpanFromBeat(Beat b, List<Beat> pitchBeats) {
        if (!pitchBeats.contains(b)) {
            return null;
        }

        Beat firstSpanBeat = null;

        if (b.getPreviousBeat() == null) {
            return null;
        } else {
            firstSpanBeat = b.getPreviousBeat();
        }

        while (!pitchBeats.contains(firstSpanBeat)) {
            if (firstSpanBeat.getPreviousBeat() != null) {
                firstSpanBeat = firstSpanBeat.getPreviousBeat();
            } else {
                return null;
            }
        }

        List<Beat> beatSpan = new ArrayList<>();

        beatSpan.add(firstSpanBeat);
        int pitchCount = 1;

        while (pitchCount < 4) {
            if (firstSpanBeat.getNextBeat() != null) {
                firstSpanBeat = firstSpanBeat.getNextBeat();

                if (pitchBeats.contains(firstSpanBeat)) {
                    pitchCount++;

                }

                beatSpan.add(firstSpanBeat);

            } else {
                return null;
            }
        }
        
        if(firstSpanBeat.getNextBeat() != null)
        {
            firstSpanBeat = firstSpanBeat.getNextBeat();
            while(!pitchBeats.contains(firstSpanBeat))
            {
                beatSpan.add(firstSpanBeat);
                
                if(firstSpanBeat.getNextBeat() != null)
                {
                    firstSpanBeat = firstSpanBeat.getNextBeat();
                }
                else{
                    break;
                }
            }
        }

        Collections.sort(beatSpan, beatSpan.get(0));

        return beatSpan;

    }
    
     public List<Event> createPitchEventList(Map<Beat, Key> chain, List<Beat> beatSpan) {
        List<Event> events = new ArrayList<>();

        for (Beat beat : beatSpan) {
           
            if(chain.containsKey(beat)){
            events.add(new AttackEvent(chain.get(beat), DurationEnum.TWO_BEATS));
            }
        }

        return events;
    }
     
     public List<Event> getEventStreamFragment(Map<Beat, Event> beatMap,
             Beat startBeat, Beat endBeat)
     {
         List<Event> events = new ArrayList<>();
         
         while(startBeat != endBeat)
         {
             if(beatMap.get(startBeat) != null)
             {
                 events.add(beatMap.get(startBeat));
             }
             startBeat = startBeat.getNextBeat();
         }
         
          if(beatMap.get(endBeat) != null)
             {
                 events.add(beatMap.get(endBeat));
             }
        
         
         return events;
     }
     
     /*
     Calculates how many branches must be traversed from the top branch to get to
     the given child branch. The child branch must be on a lower level than the top branch.
     */
    public int calculateDistanceFromBranch(Branch topBranch, Branch childBranch) {
        if (topBranch == childBranch) {
            return 0;
        }
        int distance = 1;
        return calculateDistanceFromBranch(topBranch, childBranch, 1);
    }

    private int calculateDistanceFromBranch(Branch topBranch, Branch childBranch, int distance) {
        if (topBranch.getChildBranches().get(childBranch.getLevel()) == childBranch) {
            return distance;
        }

        distance++;

        for (Integer level : topBranch.getOrderedLevels()) {
            int solution = -1;
            solution = calculateDistanceFromBranch(topBranch.getChildBranches().get(level), childBranch, distance);
            if (solution != -1) {
                return solution;
            }
        }

        return -1;
    }
    
    /**
     * Divides the metrical structure into different spans per grouping structure level.
     * @param pitchBeats the range of pitch beats
     * @param gGroup the top grouping structure group
     * @return a map of grouping levels to groups.
     */
    public Map<Integer, List<Group>> getBoundaryEventsPerLevel(List<Beat> pitchBeats, HighLevelGroup gGroup) {
        Map<Integer, List<Group>> map = new HashMap<>();

        List<Beat> boundaryBeats = new ArrayList<>();
        List<Group> nextLevelGroup = gGroup.getSubGroups();

        Integer level = 1;
        while (!nextLevelGroup.isEmpty()) {

            boundaryBeats.addAll(AnalyserUtils.getInstance().identifyBoundaryBeats(nextLevelGroup, pitchBeats));

            map.put(level, nextLevelGroup);

            HighLevelGroup tempGroup = null;

            if (nextLevelGroup.size() > 1) {
                try {
                    tempGroup = new HighLevelGroup(nextLevelGroup);
                } catch (GroupingWellFormednessException e) {
                    System.out.println(e.getMessage());
                }

            } else {
                if (nextLevelGroup.get(0).getClass() == HighLevelGroup.class) {
                    tempGroup = (HighLevelGroup) nextLevelGroup.get(0);
                }
            }

            if (tempGroup != null) {
                nextLevelGroup = getNextLevelGroups(tempGroup, boundaryBeats, pitchBeats);

                level++;
            } else {
                break;
            }

        }

        return map;

    }

  
    //return the next lowest duration 
    private DurationEnum getNextLowestDuration(DurationEnum duration) {
        switch (duration) {
            case FIVE_BEATS:
                return DurationEnum.FOUR_BEATS;
            case FOUR_BEATS:
                return DurationEnum.THREE_BEATS;
            case THREE_BEATS:
                return DurationEnum.TWO_BEATS;
            case TWO_BEATS:
                return DurationEnum.ONE_BEAT;
            case ONE_BEAT:
                return null;

        }

        return null;
    }

    //creates a highlevel groups containing all of the subgroups
    //of all the groups in the groups list.
    private List<Group> createMockHighLevelGroup(List<Group> groups, List<Beat> pitchBeats) {
        List<Group> subGroups = new ArrayList<>();

        //for all the groups
        for (Group gr : groups) {
            //if the group is a highlevel group
            //add all the sublevel groups of the group to the list of subgroups
            if (gr.getClass() == HighLevelGroup.class) {
                HighLevelGroup hg = (HighLevelGroup) gr;
                subGroups.addAll(hg.getSubGroups());
            }
        }

        return subGroups;
    }

    //this goes through all the subgroups of the given HighLevelGroup and for each group,
    //provided it doesn't have a boundary beat contained within the boundary beats list,
    //adds it to the list of groups which constitute the next level of groups in the hierarchy
    public List<Group> getNextLevelGroups(HighLevelGroup group,
            List<Beat> boundaryBeats, List<Beat> pitchBeats) {
        List<Group> nextLevel = new ArrayList<>();

        //get all the sub-sub groups of the group and add them to the nextLevel beats
        //groups which have already seen boundary beats are not added.        
        for (Group group2 : createMockHighLevelGroup(group.getSubGroups(), pitchBeats)) {
            if (!boundaryBeats.contains(identifyBoundaryBeat(group2, pitchBeats))) {
                nextLevel.add(group2);
            }
        }

        return nextLevel;
    }
    
    
       /**
     * Converts a list of Double objects to an array of double values
     * @param list the List of Double objects
     * @return the array of double values
     */
    public double[] listToArray(List<Double> list) {
        double[] array = new double[list.size()];

        for (Double num : list) {
            array[list.indexOf(num)] = num;
        }

        return array;
    }
}
