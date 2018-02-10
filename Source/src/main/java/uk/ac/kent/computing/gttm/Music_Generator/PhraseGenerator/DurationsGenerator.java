/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Music_Generator.PhraseGenerator;

import java.util.*;
import org.apache.commons.math3.stat.StatUtils;

import uk.ac.kent.computing.gttm.Elements.DurationEnum;
import uk.ac.kent.computing.gttm.Elements.Event;
import uk.ac.kent.computing.gttm.Elements.RestEvent;
import uk.ac.kent.computing.gttm.GTTM_Analyser.AnalyserUtils;
import uk.ac.kent.computing.gttm.Grammar_Elements.GrammarContainer;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.Branch;

/**
 *
 * @author Alexander Dodd
 */
public class DurationsGenerator extends Generator {

    private static DurationsGenerator instance = null;

    public static DurationsGenerator getInstance() {
        if (instance == null) {
            instance = new DurationsGenerator();
        }

        return instance;
    }

    private DurationsGenerator() {
        super();
    }

    /**
     * Constructs a map of Beats against durations which best fit the metrical
     * preference rules.
     * 
     * @param grammar the grammatical structure with which to assess the durational
     * structure
     * @return a map of Beat objects against durational values which represents the
     * durational structure which meets the metrical preference rules constraints
     */
    public Map<Beat, Event> getCandidateEventStreams(GrammarContainer grammar) {
        assignFieldVariables(grammar);

        List<Branch> branches = new ArrayList<>();
        branches.addAll(getTopPBranch().getAllSubBranches());
        branches.add(getTopPBranch());

        //create a list of all the beats which are associated with a pitch event
        List<Beat> pitchBeats = new ArrayList<>();

        for (Branch branch : branches) {
            pitchBeats.add(branch.getAssociatedBeat());
        }

        return adjustPitchDurations(pitchBeats);

    }

    private Map<Beat, Event> adjustPitchDurations(List<Beat> pitchBeats) {
        Map<Beat, Event> events = fillEvents();

        List<Double> averages = calculateAveragePitchBeatStrength(pitchBeats);

        //for all the pitch related beats
        for (Beat b : pitchBeats) {
            //get the strength of the beat
            double strength = b.getBeatStrength();

            RestEvent pitchEvent = null;

            int distanceNextPitch = distanceToNextPitch(b, pitchBeats);

            //if the pitch is of average strength
            if (averages.contains(strength)
                    && distanceNextPitch >= durationToBeat(DurationEnum.QUARTER)) {
                pitchEvent = new RestEvent(DurationEnum.QUARTER);
            } else {
                //if the strength is average, but the distance to the next beat is smaller
                //then the duration of the beat, find the largest possible duration
                //below the average that will fit
                if (averages.contains(strength)
                        && distanceNextPitch < durationToBeat(DurationEnum.QUARTER)) {
                    if (distanceNextPitch >= durationToBeat(DurationEnum.EIGHTH)) {
                        pitchEvent = new RestEvent(DurationEnum.EIGHTH);
                    } else {
                        pitchEvent = new RestEvent(DurationEnum.SIXTEENTH);
                    }
                } else {
                    //else, if the strength is less than average find a suitable duration
                    if (strength < averages.get(0)) {
                        int distance = (int) (averages.get(0) - strength);

                        if (distance > 1 || distanceNextPitch < durationToBeat(DurationEnum.EIGHTH)) {
                            pitchEvent = new RestEvent(DurationEnum.SIXTEENTH);
                        } else {
                            pitchEvent = new RestEvent(DurationEnum.EIGHTH);
                        }
                        //else, if the strength is greater than average
                    } else {
                        int distance = (int) (strength - averages.get(averages.size() - 1));

                        if (distance > 1 && distanceNextPitch >= durationToBeat(DurationEnum.WHOLE)) {
                            pitchEvent = new RestEvent(DurationEnum.WHOLE);
                        } else if(distanceNextPitch >= durationToBeat(DurationEnum.HALF)){
                            pitchEvent = new RestEvent(DurationEnum.HALF);
                        }
                        else{
                            if(distanceNextPitch >= durationToBeat(DurationEnum.QUARTER))
                            {
                                pitchEvent = new RestEvent(DurationEnum.QUARTER);
                            }
                            else{
                                if(distanceNextPitch >= durationToBeat(DurationEnum.EIGHTH))
                                {
                                    pitchEvent = new RestEvent(DurationEnum.EIGHTH);
                                }
                                else{
                                    pitchEvent = new RestEvent(DurationEnum.SIXTEENTH);
                                }
                            }
                        }
                    }
                }
            }

            events.put(b, pitchEvent);
        }

        for (Beat b : pitchBeats) {

            int spanDistance = AnalyserUtils.getInstance().getDurationBeatExpansion(events.get(b).getDurationEnum());

            int count = 0;
            Beat currentBeat = b;

            //this is going through the list of events and removing any rests
            //that come after relatively long durations of time.
            //if within the duration span another pitch even is found then the loop
            //is broken. Thus, this doesn't necessarily produce perfectly preferable
            //metrical durations.
            while (count != spanDistance) {
                //if the next beat is null, then this signifies the end of the
                //musical segment, so no rests need to be deleted
                if (currentBeat.getNextBeat() != null) {
                    currentBeat = currentBeat.getNextBeat();
                    count++;
                    //if it's a pitch event, break the loop and don't remove
                    if (pitchBeats.contains(currentBeat)) {
                        break;
                    } else {
                        //if it's not a pitch event, make it null
                        events.put(currentBeat, null);
                    }
                } else {
                    break;
                }

            }

        }

        return events;
    }

    private List<Double> calculateAveragePitchBeatStrength(List<Beat> pitchBeats) {
        List<Integer> strengths = new ArrayList<>();

        for (Beat b : pitchBeats) {
            strengths.add(b.getBeatStrength());
        }

        double[] strengthsArray = new double[strengths.size()];

        int i = 0;
        while (i < strengthsArray.length) {
            strengthsArray[i] = strengths.get(i);
            i++;
        }

        double[] averages = StatUtils.mode(strengthsArray);

        List<Double> averagesList = new ArrayList<>();

        for (double dbl : averages) {
            averagesList.add(dbl);
        }

        Collections.sort(averagesList);

        return averagesList;

    }

    private Map<Beat, Event> fillEvents() {
        Map<Beat, Event> events = new HashMap<>();
        for (Beat b : getmContainer().getMetricalBeatsList()) {

            int strength = b.getBeatStrength();

            RestEvent rest = new RestEvent(DurationEnum.SIXTEENTH);

            events.put(b, rest);
        }

        return events;
    }

  

}
