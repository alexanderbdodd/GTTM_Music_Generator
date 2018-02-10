package uk.ac.kent.computing.gttm.GTTM_Analyser;

import java.util.*;
import org.apache.commons.math3.stat.StatUtils;

import uk.ac.kent.computing.gttm.Elements.ArticulationEnum;
import uk.ac.kent.computing.gttm.Elements.AttackEvent;
import uk.ac.kent.computing.gttm.Elements.DurationEnum;
import uk.ac.kent.computing.gttm.Elements.DynamicsEnum;
import uk.ac.kent.computing.gttm.Elements.Event;
import uk.ac.kent.computing.gttm.GTTM_Analyser.Exceptions.MetricalAnalyserException;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.Branch;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.TimeSpanReductionBranch;

/**
 * A singleton class providing methods for analysis of metrical preference
 * rules. At the moment this class only includes preference rules for MPR5 a, b
 * and d.
 *
 *
 * MPR5 has different intrinsic strengths not described in great detail in the
 * GTTM theory. These intrinsic strengths will need to be developed empirically.
 *
 *
 * @author Alexander Dodd
 */
public class MetricalStructureAnalyser {

    private final static double MPR5A_WEIGHT = 1;
    private final static double MPR5B_WEIGHT = 1;
    private final static double MPR5C_WEIGHT = 1;
    private final static double MPR5D_WEIGHT = 1;
    private final static double MPR5E_WEIGHT = 1;
    private final static double MPR5F_WEIGHT = 1;

    private final static double MPR5_WEIGHT = 1;

    private static MetricalStructureAnalyser instance = null;

    private List<Event> previouslyUsedEventList = null;
    private double dynamicDurationsMean = 0.0;
    private double articulationMean = 0.0;
    private double slurMean = 0.0;

    private boolean articulationMeanSet = false;
    private boolean slurMeanSet = false;
    private boolean dynamicMeanSet = false;

    /**
     *
     * @return the singleton instance of this class
     */
    public static MetricalStructureAnalyser getInstance() {
        if (instance == null) {
            instance = new MetricalStructureAnalyser();
        }

        return instance;
    }

    private MetricalStructureAnalyser() {
    }

    /**
     * Provides a rating based on application of the MPR5 rule. MPR5 contains a
     * combination of different sub-considerations. This implementation makes
     * use of MPR5a, MPR5b, and MPR5 c.
     *
     * @param events a list of Event objects which act as the context of Events
     * mapped onto the metrical beat span
     * @param event an AttackEvent to be considered for candidacy of landing on
     * a strong beat
     * @return a rating of how strongly a given event constitutes a candidate
     * for landing on a strong beat.
     */
    public double assessMPR5(List<Event> events, AttackEvent event)
            throws MetricalAnalyserException {
        double rating1 = assessMPR5a(events, event);
        double rating2 = assessMPR5b(events, event);
        double rating3 = assessMPR5d(events, event);
        double rating4 = assessMPR5c(events, event);

        return ((rating1 * MPR5A_WEIGHT) + (rating2 * MPR5B_WEIGHT) + (rating3 * MPR5D_WEIGHT)
                + (rating4 * MPR5C_WEIGHT))
                / (MPR5A_WEIGHT + MPR5B_WEIGHT + MPR5D_WEIGHT + MPR5C_WEIGHT);

    }

    /**
     * Provides a rating that describes how well the AttackEvent event ranks as
     * a candidate for landing on a strong beat based on the mean length of
     * events in the list events. This is an implementation of MPR5a
     *
     * @param events the list of events from which to calculate mean length
     * @param event the event to judge as a candidate for a strong beat
     * @return a rating that indicates the candidacy of the given AttackEvent
     * for landing on a strong beat. A rating of 0 indicates that the given
     * AttackEvent is not a suitable candidate for landing on a strong beat. A
     * rating of 1 indicates that the given AttackEvent is a very strong
     * candidate for landing on a strong beat.
     */
    public double assessMPR5a(List<Event> events, AttackEvent event) {

        DurationEnum meanDuration = getMeanDuration(events);

        return getRatingValue(meanDuration, event);

    }

    /**
     * Assesses a given AttackEvent against a context of Events to assess
     * whether the given AttackEvent constitutes a candidate for a strong beat.
     * This is judged based on whether the given AttackEvent constitutes an
     * AttackEvent that is at the inception of a relatively long duration of a
     * dynamic.
     *
     * @param events the context of Event objects against which to assess the
     * given AttackEvent
     * @param event AttackEvent to judge for candidacy for falling on a strong
     * beat.
     * @return a rating for how well the given AttackEvent is a candidate for
     * landing on a strong beat.
     */
    public double assessMPR5b(List<Event> events, AttackEvent event)
            throws MetricalAnalyserException {
        int dynamicDuration = dynamicDuration(events, event);
        double mean = getMeanDynamicDuration(events);

        int difference = (int) (dynamicDuration - mean);

        if (difference == 0) {
            return 0.5;
        }
        if (difference < 0) {
            return 0;
        }
        if (difference > 0) {
            return 1;
        }

        return 0;

    }

    /*Calculates the mean durational value of a given set of Event objects.
     * 
     */
    private double getMeanDynamicDuration(List<Event> events)
            throws MetricalAnalyserException {

        List<Double> durationsList = new ArrayList<>();

        //collect all the durational values from the list of Events
        for (Event e : events) {
            if (e.getClass() == AttackEvent.class) {
                AttackEvent aE = (AttackEvent) e;

                double length = dynamicDuration(events, aE);
                if (length != 0) {
                    durationsList.add(length);
                }
            }
        }

        double[] durationsArray = new double[durationsList.size()];

        //put the values into an array
        int i = 0;
        for (Double d : durationsList) {
            durationsArray[i] = d;
            i++;
        }

        //calculate the mean
        dynamicDurationsMean = StatUtils.mean(durationsArray);

        return dynamicDurationsMean;
    }

    /*Calculates the length of a given inceptions of a dynamic.
     * If the given AttackEvent is not an inception of a dynamic,
     * then return 0.
     */
    private int dynamicDuration(List<Event> events, AttackEvent event)
            throws MetricalAnalyserException {

        //check whether it's an inception. If not, return 0.
        if (!checkDynamicInception(events, event)) {
            return 0;
        }

        int length = event.getDurationEnum().getLength();

        Iterator<Event> it = events.iterator();
        boolean durationStart = false;
        boolean calculating = false;

        while (it.hasNext()) {
            Event ev = it.next();

            //check if ev is the event object to be considered
            if (ev == event) {
                calculating = true;
            }

            //check to see if there's a dynamic change
            if (calculating) {
                if (ev.getClass() == AttackEvent.class) {
                    AttackEvent aEv = (AttackEvent) ev;
                    if (aEv.getDynamic() != event.getDynamic()) {
                        calculating = false;
                        break;
                    }
                }
            }

            if (calculating) {
                length += ev.getDurationEnum().getLength();
            }

        }

        return length;
    }

    private boolean checkDynamicInception(List<Event> events, AttackEvent event)
            throws MetricalAnalyserException {
        if (event.getDynamic() == DynamicsEnum.MP) {
            return false;
        }

        if (!events.contains(event)) {
            throw new MetricalAnalyserException("The AttackEvent is not contained "
                    + "in the list");
        }

        if (events.get(0) == event) {
            return true;
        }

        Iterator<Event> it = events.iterator();

        boolean isDynamic = false;
        AttackEvent firstEv = (AttackEvent) events.get(0);
        DynamicsEnum lastDynamic = firstEv.getDynamic();

        while (it.hasNext()) {
            Event ev = it.next();
            if (ev.getClass() == AttackEvent.class) {
                AttackEvent aEv = (AttackEvent) ev;

                if (aEv.getDynamic() != DynamicsEnum.MP
                        && aEv.getDynamic() == lastDynamic) {
                    lastDynamic = aEv.getDynamic();
                    isDynamic = true;
                } else {
                    lastDynamic = aEv.getDynamic();
                    isDynamic = false;
                }
            }

            //if event occurs during an already initiated dynamic period, return false
            //else return true.
            if (event == ev) {
                if (isDynamic) {
                    return false;
                } else {
                    return true;
                }
            }

        }

        return false;

    }

    /**
     * Assesses how likely a given AttackEvent will land on a strong beat in the
     * metrical structure given the context of Event objects.
     *
     * This method provides an assessment based on MPR5c, which makes an
     * assessment based on whether the given AttackEvent marks an inception of a
     * slur. If it is, it assesses whether it is the inception of a relatively
     * long slur.
     *
     * @param events the context of Event objects against which to assess the
     * candidacy of the AttackEvent
     * @param event the AttackEvent to be assessed
     * @return a rating based on how likely the given AttackEvent is to land on
     * a strong beat. If the AttackEvent is the inception of a relatively long
     * slur, a ranking of 1.0 will be awarded. If it is not an inception, or is
     * not of a relatively long duration, return 0.0. If is an average
     * candidate, return 0.5.
     * @throws MetricalAnalyserException thrown if the given AttackEvent is not
     * contained within the list of Event objects.
     */
    public double assessMPR5c(List<Event> events, AttackEvent event)
            throws MetricalAnalyserException {
        int articulationDuration = slurDuration(events, event);
        double mean = getMeanSlurDuration(events);

        int difference = (int) (articulationDuration - mean);

        if (difference == 0) {
            return 0.5;
        }
        if (difference < 0) {
            return 0;
        }
        if (difference > 0) {
            return 1;
        }

        return 0;
    }

    private double getMeanSlurDuration(List<Event> events)
            throws MetricalAnalyserException {

        List<Double> durationsList = new ArrayList<>();

        for (Event e : events) {
            if (e.getClass() == AttackEvent.class) {
                AttackEvent aE = (AttackEvent) e;

                double length = slurDuration(events, aE);
                if (length != 0) {
                    durationsList.add(length);
                }
            }
        }

        double[] durationsArray = new double[durationsList.size()];

        int i = 0;
        for (Double d : durationsList) {
            durationsArray[i] = d;
            i++;
        }

        slurMean = StatUtils.mean(durationsArray);

        return slurMean;
    }

    private int slurDuration(List<Event> events, AttackEvent event)
            throws MetricalAnalyserException {
        if (!checkSlurInception(events, event)) {
            return 0;
        }

        int length = 0;

        Iterator<Event> it = events.iterator();
        boolean durationStart = false;
        boolean calculating = false;

        while (it.hasNext()) {
            Event ev = it.next();

            if (ev == event) {
                calculating = true;
            }

            if (calculating) {
                if (ev.getClass() == AttackEvent.class) {
                    AttackEvent aEv = (AttackEvent) ev;
                    if (aEv.getArticulationValue() != event.getArticulationValue()) {
                        calculating = false;
                        break;
                    }
                }
            }

            if (calculating) {
                length += ev.getDurationEnum().getLength();
            }

        }

        return length;
    }

    private boolean checkSlurInception(List<Event> events, AttackEvent event)
            throws MetricalAnalyserException {
        if (event.getArticulationValue() != ArticulationEnum.SLUR) {
            if (event.getArticulationValue() != ArticulationEnum.SLURALT) {
                return false;
            }
        }

        if (!events.contains(event)) {
            throw new MetricalAnalyserException("The AttackEvent is not contained "
                    + "in the list");
        }

        if (events.get(0) == event) {
            return true;
        }

        Iterator<Event> it = events.iterator();

        boolean isArticulation = false;
        AttackEvent firstEv = (AttackEvent) events.get(0);
        ArticulationEnum lastArticulation = firstEv.getArticulationValue();

        while (it.hasNext()) {
            Event ev = it.next();

            if (event == ev) {
                if (isArticulation) {
                    return false;
                } else {
                    return true;
                }
            }

            if (ev.getClass() == AttackEvent.class) {
                AttackEvent aEv = (AttackEvent) ev;

                if (aEv.getArticulationValue() == ArticulationEnum.SLUR
                        || aEv.getArticulationValue() == ArticulationEnum.SLURALT
                        && aEv.getArticulationValue() == lastArticulation) {
                    lastArticulation = aEv.getArticulationValue();
                    isArticulation = true;
                } else {
                    lastArticulation = aEv.getArticulationValue();
                    isArticulation = false;
                }
            }

        }

        return false;

    }

    /**
     * Assesses how likely a given AttackEvent will land on a strong beat in the
     * metrical structure given the context of Event objects.
     *
     * This method provides an assessment based on MPR5d, which makes an
     * assessment based on whether the given AttackEvent marks an inception of a
     * pattern of articulation. If it is, it assesses whether it is the
     * inception of a relatively long pattern of articulation.
     *
     * @param events the context of Event objects against which to assess the
     * candidacy of the AttackEvent
     * @param event the AttackEvent to be assessed
     * @return a rating based on how likely the given AttackEvent is to land on
     * a strong beat. If the AttackEvent is the inception of a relatively long
     * pattern of inception, a ranking of 1.0 will be awarded. If it is not an
     * inception, or is not of a relatively long duration, return 0.0. If is an
     * average candidate, return 0.5.
     * @throws MetricalAnalyserException thrown if the given AttackEvent is not
     * contained within the list of Event objects.
     */
    public double assessMPR5d(List<Event> events, AttackEvent event)
            throws MetricalAnalyserException {
        int articulationDuration = articulationDuration(events, event);
        double mean = getMeanArticulationDuration(events);

        int difference = (int) (articulationDuration - mean);

        if (difference == 0) {
            return 0.5;
        }
        if (difference < 0) {
            return 0;
        }
        if (difference > 0) {
            return 1;
        }

        return 0;
    }

    private double getMeanArticulationDuration(List<Event> events)
            throws MetricalAnalyserException {

        List<Double> durationsList = new ArrayList<>();

        for (Event e : events) {
            if (e.getClass() == AttackEvent.class) {
                AttackEvent aE = (AttackEvent) e;

                double length = articulationDuration(events, aE);
                if (length != 0) {
                    durationsList.add(length);
                }
            }
        }

        double[] durationsArray = new double[durationsList.size()];

        int i = 0;
        for (Double d : durationsList) {
            durationsArray[i] = d;
            i++;
        }

        articulationMean = StatUtils.mean(durationsArray);

        return articulationMean;
    }

    private int articulationDuration(List<Event> events, AttackEvent event)
            throws MetricalAnalyserException {
        if (!checkArticulationInception(events, event)) {
            return 0;
        }

        int length = 0;

        Iterator<Event> it = events.iterator();
        boolean durationStart = false;
        boolean calculating = false;

        while (it.hasNext()) {
            Event ev = it.next();

            if (ev == event) {
                calculating = true;
            }

            if (calculating) {
                if (ev.getClass() == AttackEvent.class) {
                    AttackEvent aEv = (AttackEvent) ev;
                    if (aEv.getArticulationValue() != event.getArticulationValue()) {
                        calculating = false;
                        break;
                    }
                }
            }

            if (calculating) {
                length += ev.getDurationEnum().getLength();
            }

        }

        return length;
    }

    private boolean checkArticulationInception(List<Event> events, AttackEvent event)
            throws MetricalAnalyserException {
        if (event.getArticulationValue() == ArticulationEnum.NONE) {
            return false;
        }

        if (!events.contains(event)) {
            throw new MetricalAnalyserException("The AttackEvent is not contained "
                    + "in the list");
        }

        if (events.get(0) == event) {
            return true;
        }

        Iterator<Event> it = events.iterator();

        boolean isArticulation = false;
        AttackEvent firstEv = (AttackEvent) events.get(0);
        ArticulationEnum lastArticulation = firstEv.getArticulationValue();

        while (it.hasNext()) {
            Event ev = it.next();

            if (ev.getClass() == AttackEvent.class) {
                AttackEvent aEv = (AttackEvent) ev;

                if (aEv.getArticulationValue() != ArticulationEnum.NONE
                        && aEv.getArticulationValue() == lastArticulation) {
                    lastArticulation = aEv.getArticulationValue();
                    isArticulation = true;
                } else {
                    lastArticulation = aEv.getArticulationValue();
                    isArticulation = false;
                }
            }
            if (event == ev) {
                if (isArticulation) {
                    return false;
                } else {
                    return true;
                }
            }

        }

        return false;

    }

    public double assessMPR5e(TimeSpanReductionBranch topBranch, Map<Beat, Event> beatMap,
            int level, AttackEvent ev) {
        return assessMPR5a(getAllTimeSpanPitchEventsAtLevel(level, topBranch, beatMap), ev);
    }

    public List<Event> getAllTimeSpanPitchEventsAtLevel(int level, TimeSpanReductionBranch topBranch,
            Map<Beat, Event> beatMap) {
        List<Branch> branches = topBranch.getAllSubBranches();
        branches.add(0, topBranch);

        List<Branch> levelBranches = new ArrayList<>();
        for (Branch br : branches) {
            if (br.getLevel() == level) {
                levelBranches.add(br);
            }
        }

        List<Event> events = new ArrayList<>();
        for (Branch br : levelBranches) {
            if (beatMap.get(br.getAssociatedBeat()) != null) {

                events.add(beatMap.get(br.getAssociatedBeat()));
            }
        }

        return events;

    }

    /*
     Provides a rating based on the difference beween the mean DurationEnum
     and the duration of the AttackEvent object.
     */
    private double getRatingValue(DurationEnum meanDuration, AttackEvent event) {

        int intervalDistance = event.getDurationEnum().getLength() - meanDuration.getLength();

        double meanValue = meanDuration.getLength() / 16;

        if (intervalDistance == 0.0) {
            return 0.5;
        }
        if (intervalDistance == meanValue) {
            return 0.55;
        }
        if (intervalDistance == (meanValue * 2)) {
            return 0.65;
        }
        if (intervalDistance <= (meanValue * 4) && intervalDistance > 0) {
            return 0.80;
        }
        if (intervalDistance <= (meanValue * 10) && intervalDistance > 0) {
            return 0.90;
        }
        if (intervalDistance > (meanValue * 10) && intervalDistance > 0) {
            return 1.0;
        }
        if (intervalDistance >= (-1 * meanValue)) {
            return 0.45;
        }
        if (intervalDistance >= (meanValue * -2)) {
            return 0.35;
        }
        if (intervalDistance >= (meanValue * -4)) {
            return 0.20;
        }
        if (intervalDistance >= (meanValue * -10)) {
            return 0.10;
        }
        if (intervalDistance < (meanValue * -10)) {
            return 0.0;
        }

        return 0.0;
    }

    private DurationEnum getMeanDuration(List<Event> events) {
        double[] durations = new double[events.size()];

        int i = 0;

        while (i < durations.length) {
            if (events.get(i).getClass() == AttackEvent.class) {
                durations[i] = events.get(i).getDurationEnum().getLength();
            }
            i++;
        }

        double meanLength = StatUtils.mean(durations);

        DurationEnum meanEnum = DurationEnum.EIGHTH;
        double meanDistance = Math.abs(meanLength - DurationEnum.EIGHTH.getLength());

        for (DurationEnum enumV : DurationEnum.values()) {
            if (meanDistance > Math.abs(meanLength - enumV.getLength())) {
                meanEnum = enumV;
                meanDistance = Math.abs(meanLength - enumV.getLength());
            }

        }

        return meanEnum;
    }

}
