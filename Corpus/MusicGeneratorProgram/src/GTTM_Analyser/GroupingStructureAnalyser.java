package GTTM_Analyser;

import Elements.*;
import GTTM_Analyser.Exceptions.GroupingAnalysisException;
import Manipulators.IntervalConstructor;
import java.util.*;

/**
 * A class that performs grouping structure analysis on a given set of
 * consecutive AttackPoints. This works primarily by performing preference rules
 * analysis.
 *
 * At this point in time the analyser is restricted to grouping structure
 * preference rules 2 and 3.
 *
 * @author Alexander Dodd
 */
public class GroupingStructureAnalyser {

    //set of weightings for performing GPR3 analysis
    public final static double GPR3A_WEIGHT = 1;
    public final static double GPR3B_WEIGHT = 0.5; //this should be stronger in actuality
    public final static double GPR3C_WEIGHT = 0.5;
    public final static double GPR3D_WEIGHT = 1;

    //set of weightings for performing GPR2 analysis
    public final static double GPR2A_WEIGHT = 1.5;
    public final static double GPR2B_WEIGHT = 1.5;

    public final static double GPR3_WEIGHT = 1;
    public final static double GPR2_WEIGHT = 2;

    private static GroupingStructureAnalyser instance = null;

    /**
     *
     * @return the singleton instance of this class.
     */
    public static GroupingStructureAnalyser getInstance() {
        if (instance == null) {
            instance = new GroupingStructureAnalyser();
        }

        return instance;
    }

    private GroupingStructureAnalyser() {
    }

    /**
     * Applies GRP2 analysis to a set of four notes (n1, n2, n3, and n4). This
     * is used to judge how strongly the note n2 rates as a boundary note for a
     * group based on the relative proximities of the four AttackEvents.
     *
     * @param events a list of events to be used for analysing GPR2. The list of
     * events must contain exactly four AttackPoints (n1, n2, n3, and n4), with
     * any other events counting as rest events.
     * @return a rating between 0 and 1 indicating how strongly the note n2
     * constitutes a boundary. A rating of 0 indicates that the note n2 does not
     * constitute a boundary in any strong way. A rating of 0.5 indicates that
     * the n2 forms a mildly strong boundary. A rating of 1 indicates that the
     * note n2 forms a very strong boundary.
     */
    public double assessGPR2(List<Event> events) {

        double r1 = assessGPR2a(events);
        double r2 = assessGPR2b(events);

        //the ratings for GPR2a and GPR2b are held to be equal in value
        //in this implementation. This method will need to be updated
        //if they aren't to be held as equally important.
        return ((r1 * GPR2A_WEIGHT) + (r2 * GPR2B_WEIGHT)) / (GPR2A_WEIGHT + GPR2B_WEIGHT);

    }

    /**
     * Assesses the interval of time between pos1 and pos2 for use with GPR2a
     *
     * @param pos1 the attack event position in the list of events, the end
     * point of which is used to start calculating the distance of time between
     * pos1 and pos2
     * @param pos2 the attack event position in the list of events, the
     * beginning point of which is used to start calculating the distance of
     * time between pos1 and pos2
     * @param events the list of events from which the attack events pos1 and
     * pos2 are contained.
     * @return a sum of the interval of time. This is assessed using the value
     * of DurationEnums associated with each event.
     */
    private int assessExclusionDistance(int pos1, int pos2, List<Event> events) {
        int length = 0;

        if ((pos2 - pos1) == 0) {
            return length;
        } else {
            int i = pos1 + 1;

            while (i < pos2) {
                length += events.get(i).getDurationEnum().getLength();
                i++;
            }
        }
        return length;
    }

    /**
     * This returns a calculation of the durational length between attack point at
     * position 1 and attack point at position 2.
     *
     * @param pos1 the attack point from which to assess the durational length
     * @param pos2 the attack point acting as the end point from which to assess
     * durational length
     * @param events the list of events containing the attack events at pos1 and
     * pos2
     * @return the value of the durational length between attack point at position 1 and
     * the attack point at position 2 in the list of events
     */
    private int assessInclusionDistance(int pos1, int pos2, List<Event> events) {
        int length = 0;

        if ((pos2 - pos1) == 0) {
            length = events.get(pos2).getDurationEnum().getLength();
        } else {
            int i = pos1;

            while (i < pos2) {
                length += events.get(i).getDurationEnum().getLength();
                i++;
            }
        }

        return length;
    }

    /**
     * A method that provides a basic assessment of the candidacy of an
     * AttackEvent for forming a boundary at the end of a group. This represents
     * an assessment using GPR2a. This method takes a list of events, which must
     * contain four AttackEvents(n1, n2, n3, and n4) with the rest forming
     * RestEvents. A rating is returned indicating the candidacy of the second
     * AttackEvent as the boundary of a group. This method doesn't yet take slur
     * events into account. GPR2a makes an assessment of the candidacy of an
     * AttackEvent to form the boundary event of a group based on the durations
     * between the end point of one AttackEvent and the start point of the
     * following AttackEvent.If the middle two AttackEvents have a greater
     * duration between them than the durations between the first two
     * AttackEvents and the last two, then this indicates a stronger candidate
     * for a grouping boundary.
     *
     * @param events a list of Event objects, which must contain exactly four
     * AttackEvent objects. Any other Event objects must be RestEvents.
     * @return a rating between 0.0 and 1.0 indicating the preference of
     * AttackEvent n2 as a boundary event for a group. A rating towards 1.0
     * indicates a strong preference for n2 constituting a boundary note of a
     * group on high levels. A rating near 0.5 indicates a weak preference as a
     * boundary of a group. A rating towards 0.0 indicates a low preference. A
     * rating of 0.0 indicates no preference whatever.
     * @throws GroupingAnalysisException if more or less than four AttackEvents
     * are contained within the events list.
     */
    public double assessGPR2a(List<Event> events) {
        Integer[] pos = getPositions(events);

        int interval1 = assessExclusionDistance(pos[0], pos[1], events);
        int interval2 = assessExclusionDistance(pos[1], pos[2], events);
        int interval3 = assessExclusionDistance(pos[2], pos[3], events);

        return getIntervalRating(interval1, interval2, interval3);

    }

    private Integer[] getPositions(List<Event> events) throws GroupingAnalysisException {
        checkForFourAttackEvents(events);
        Integer[] pos = new Integer[4];
        findAttackEventPosition(pos, events);

        return pos;

    }

    /**
     * A method that provides a basic assessment of the candidacy of an
     * AttackEvent for forming a boundary at the end of a group. This represents
     * an assessment using GPR2b. This method takes a list of events, which must
     * contain four AttackEvents(n1, n2, n3, and n4) with the rest forming
     * RestEvents. A rating is returned indicating the candidacy of the second
     * AttackEvent as the boundary of a group. GPR2b makes an assessment of the
     * candidacy of an AttackEvent to form the boundary event of a group based
     * on the durations between the instantiation of two contiguous
     * AttackEvents. If the middle two AttackEvents have a greater duration
     * between them than the durations between the first two AttackEvents and
     * the last two, then this indicates a stronger candidate for a grouping
     * boundary.
     *
     * @param events a list of Event objects, which must contain exactly four
     * AttackEvent objects. Any other Event objects must be RestEvents.
     * @return a rating between 0.0 and 1.0 indicating the preference of
     * AttackEvent n2 as a boundary event for a group. A rating towards 1.0
     * indicates a strong preference for n2 constituting a boundary note of a
     * group on high levels. A rating near 0.5 indicates a weak preference as a
     * boundary of a group. A rating towards 0.0 indicates a low preference. A
     * rating of 0.0 indicates no preference whatever.
     *
     * @throws GroupingAnalysisException if more or less than four AttackEvents
     * are contained within the events list.
     */
    public double assessGPR2b(List<Event> events) {
        Integer[] pos = getPositions(events);
        int interval1 = assessInclusionDistance(pos[0], pos[1], events);
        int interval2 = assessInclusionDistance(pos[1], pos[2], events);
        int interval3 = assessInclusionDistance(pos[2], pos[3], events);

        return getIntervalRating(interval1, interval2, interval3);

    }

    /**
     * A method that provides a rating based on differences in intervals. At the
     * moment the implementation is basic and will need refining to make it more
     * accurate for future use.
     *
     * @param interval1 The interval between the first two AttackEvents
     * @param interval2 The interval between the middle two AttackEvents
     * @param interval3 The interval between the last two AttackEvents
     * @return a rating based on how well interval2 indicates a boundary. A
     * rating of 1.0 indicates that interval2 is larger than interval1 and
     * interval2. A rating of 0.0 indicates that interval2 is not larger than
     * interval1 or interval2.
     *
     */
    private double getIntervalRating(int interval1, int interval2, int interval3) {
        //if the intervals are all the same, then GPR2 is weak and thus return a rating of 0
        if (interval1 == interval2 && interval2 == interval3) {
            return 0.5;
        }

        //if interval2 is greater than interval1 and interval3 then return a positive rating
        //based on how large the interval is.
        if (interval1 < interval2 && interval3 < interval2 && interval1 == interval3) {
            int intervalSize = interval2 - interval1;
            if (intervalSize > 8) {
                return 1;
            }
            if (intervalSize > 6) {
                return 0.8;
            }
            if (intervalSize > 4) {
                return 0.7;
            }
            if (intervalSize > 2) {
                return 0.6;
            }
        }

        //if the intervals 1 and 3 are less than the interval 2, but are not equal in value,
        //return a value of 0.8 to indicate a fairly strong boundary candidate.
        //this will need revising at a later stage.
        if (interval1 < interval2 && interval3 < interval2) {
            int intervalDif = Math.abs(interval1 - interval3);
            int intervalSize = interval2 - Math.max(interval1, interval3);
            if (intervalDif < 2 && intervalSize > 3) {
                return 0.9;
            }
            if (intervalDif < 5 && intervalSize > 3) {
                return 0.7;
            }
            return 0.6;
        }

        //checks to see if any of the other intervals are greater than that of
        //interval 2. If so, return 0.
        if (interval1 > interval2 || interval3 > interval2) {
            return 0;
        }

        //if the above conditions are not met, return a rating of 0.
        return 0;
    }

    /**
     * Finds the position of the attack events within a list of events and
     * updates the list of integers.
     *
     * @param pos an array of size 4 that contains the positions for four attack
     * events
     * @param events the list of events that contains the attack events to
     * calculate the positions for
     */
    private void findAttackEventPosition(Integer[] pos, List<Event> events) {

        Integer firstAttackEvent = -1;
        Integer secondAttackEvent = -1;
        Integer thirdAttackEvent = -1;
        Integer fourthAttackEvent = -1;

        int i = 0;

        while (i < events.size()) {
            if (events.get(i).getClass() == AttackEvent.class) {
                if (firstAttackEvent == -1) {
                    firstAttackEvent = i;
                } else {
                    if (secondAttackEvent == -1) {
                        secondAttackEvent = i;
                    } else {
                        if (thirdAttackEvent == -1) {
                            thirdAttackEvent = i;
                        } else {
                            fourthAttackEvent = i;
                        }

                    }
                }
            }

            i++;
        }

        pos[0] = firstAttackEvent;
        pos[1] = secondAttackEvent;
        pos[2] = thirdAttackEvent;
        pos[3] = fourthAttackEvent;

    }

    /**
     * Checks that the list of events contains exactly four AttackEvent objects
     *
     * @param events the list of events to check for AttackEvent objects
     * @throws GroupingAnalysisException if the list does not contain exactly
     * four AttackEvents
     */
    private void checkForFourAttackEvents(List<Event> events) {

        int no = 0;

        for (Event e : events) {
            if (e.getClass() == AttackEvent.class) {
                no++;
            }
        }

        if (no != 4) {
            throw new GroupingAnalysisException("Number of attackevents not equal to four."
                    + "Cannot perform analysis");
        }

    }

    /**
     * Takes a list with exactly four AttackEvents and any number of RestEvents.
     * Assesses whether the intervallic distance between the first two attack
     * events and the last two attack events is smaller than the distance
     * between the middle two attack events. This is a basic implementation. It
     * only checks to see if the intervallic distance between the middle two
     * notes is greater than that between the first two notes and last two
     * notes, but doesn't make any judgements based on how great the intervallic
     * distance is.
     *
     * @param events a list containing exactly four AttackEvent objects and any
     * number of RestEvent objects
     * @return a rating between 0.0 and 1.0 indicating how strongly the given
     * set of AttackEvents fit GPR3a. A rating towards 1.0 indicates a strong
     * preference for n2 constituting a boundary note of a group on high levels.
     * A rating near 0.5 indicates a weak preference as a boundary of a group. A
     * rating towards 0.0 indicates a low preference. A rating of 0.0 indicates
     * no preference whatever.
     */
    public double assessGPR3a(List<Event> events) {
        Integer[] pos = getPositions(events);

        int intervalDis1 = Math.abs(IntervalConstructor.getInstance()
                .calculateDistance((AttackEvent) events.get(pos[0]),
                        (AttackEvent) events.get(pos[1])));

        int intervalDis2 = Math.abs(IntervalConstructor.getInstance()
                .calculateDistance((AttackEvent) events.get(pos[1]),
                        (AttackEvent) events.get(pos[2])));

        int intervalDis3 = Math.abs(IntervalConstructor.getInstance()
                .calculateDistance((AttackEvent) events.get(pos[2]),
                        (AttackEvent) events.get(pos[3])));

        if (intervalDis2 > intervalDis1 && intervalDis2 > intervalDis3) {
            return 1;
        }
        if (intervalDis2 == intervalDis1 && intervalDis2 == intervalDis3) {
            return 0.5;
        } else {
            return 0;
        }

    }

    /**
     * Assesses GPR3b.Checks to see if n2-n3 represents a dynamic change
     * compared to n1-n2 and n3-n4.
     *
     * A basic implementation that just returns a rating of 1 if n2-n3 does
     * represent a change in dynamics, but n1-n2 and n3-n4 are the same dynamic
     * values. Else returns 0.
     *
     * @param events a list of Event objects, which must contain exactly four
     * AttackEvent objects and any number of other Event objects.
     * @return a rating between 1.0 and 0.0 based on how well the two middle
     * AttackEvent objects in the events list fits GPR3b constraints. A rating
     * towards 1.0 indicates a strong preference for n2 constituting a boundary
     * note of a group on high levels. A rating near 0.5 indicates a weak
     * preference as a boundary of a group. A rating towards 0.0 indicates a low
     * preference. A rating of 0.0 indicates no preference whatever.
     * @throws GroupingAnalysisException thrown if events doesn't contain
     * exactly four AttackEvent objects.
     */
    public double assessGPR3b(List<Event> events) {
        Integer[] pos = getPositions(events);

        AttackEvent ev = (AttackEvent) events.get(pos[0]);
        DynamicsEnum dynamic1 = ev.getDynamic();

        ev = (AttackEvent) events.get(pos[1]);
        DynamicsEnum dynamic2 = ev.getDynamic();

        ev = (AttackEvent) events.get(pos[2]);
        DynamicsEnum dynamic3 = ev.getDynamic();

        ev = (AttackEvent) events.get(pos[3]);
        DynamicsEnum dynamic4 = ev.getDynamic();

        if (dynamic1 == dynamic2 && dynamic3 == dynamic4 && dynamic2 != dynamic3) {
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * Assesses GPR3c.Checks to see if n2-n3 represents a change in articulation
     * compared to n1-n2 and n3-n4.
     *
     * A basic implementation that just returns a rating of 1 if n2-n3 does
     * represent a change in dynamics, but n1-n2 and n3-n4 are the same dynamic
     * values. Else returns 0.
     *
     * @param events a list of Event objects, which must contain exactly four
     * AttackEvent objects and any number of other Event objects.
     * @return a rating between 0.0 and 1.0 based on how strong a candidate for
     * a boundary AttackEvent n2 is. A rating of 0.0 indicates that another
     * AttackEvent in the group is a stronger candidate. A rating of 0.5
     * indicates that there is no preference. A rating of 1.0 indicates strong
     * candidacy.
     * @throws GroupingAnalysisException thrown if events doesn't contain
     * exactly four AttackEvent objects.
     *
     */
    public double assessGPR3c(List<Event> events) {
        Integer[] pos = getPositions(events);

        AttackEvent ev = (AttackEvent) events.get(pos[0]);
        ArticulationEnum articulation1 = ev.getArticulationValue();

        ev = (AttackEvent) events.get(pos[1]);
        ArticulationEnum articulation2 = ev.getArticulationValue();

        ev = (AttackEvent) events.get(pos[2]);
        ArticulationEnum articulation3 = ev.getArticulationValue();

        ev = (AttackEvent) events.get(pos[3]);
        ArticulationEnum articulation4 = ev.getArticulationValue();

        if (articulation1 == articulation2 && articulation3 == articulation4
                && articulation2 != articulation3) {
            return 1;
        }

        if (articulation1 == articulation2 && articulation2 == articulation3) {
            return 0.5;
        } else {
            return 0.0;
        }

    }

    /**
     * Assesses GPR3d. If n2 and n3 are of different lengths and both pairs n1,
     * n2 and n3, n4 do not differ in length, then prefer n2 as a boundary note.
     *
     *
     * @param events a list of Event objects, which must contain exactly four
     * AttackEvent objects and any number of other Event objects.
     * @return a rating based on how well the two middle AttackEvent objects in
     * the events list fits GPR3d constraints.
     * @throws GroupingAnalysisException thrown if events doesn't contain
     * exactly four AttackEvent objects.
     */
    public double assessGPR3d(List<Event> events)
            throws GroupingAnalysisException {
        Integer[] pos = getPositions(events);

        AttackEvent ev = (AttackEvent) events.get(pos[0]);
        DurationEnum duration1 = ev.getDurationEnum();

        ev = (AttackEvent) events.get(pos[1]);
        DurationEnum duration2 = ev.getDurationEnum();

        ev = (AttackEvent) events.get(pos[2]);
        DurationEnum duration3 = ev.getDurationEnum();

        ev = (AttackEvent) events.get(pos[3]);
        DurationEnum duration4 = ev.getDurationEnum();

        if (duration1 == duration2 && duration3 == duration4
                && duration2 != duration3) {
            return 1;
        }
        if (duration1 == duration2 && duration1 == duration3) {
            return 0.5;
        } else {
            return 0.0;
        }
    }

    private List<AttackEvent> filterAttackEvents(List<Event> events) {
        List<AttackEvent> attackEvents = new ArrayList<>();

        for (Event e : events) {
            if (e.getClass() == AttackEvent.class) {
                attackEvents.add((AttackEvent) e);
            }
        }

        return attackEvents;
    }

    /**
     * Performs an an assessment of a given set of Event objects according to
     * GPR3. This assesses a group of four AttackEvents (n1, n2, n3, and n4)
     * contained within the Event list and rates the preference for n2 to
     * constitute a boundary between two groups. This is assessed based on how
     * strongly the AttackEvent n2 represents a change in musical quality (i.e.
     * dynamic, articulation, length, and register) from the other three
     * AttackEvents.
     *
     * @param events a list of Event objects which must contain exactly four
     * AttackEvents objects, and any number of other Event objects.
     * @return a rating between 1.0 and 0.0 indicating how strongly n2
     * represents a boundary between two groups.
     * @throws GroupingAnalysisException thrown if the list of Event objects
     * does not contain exactly four AttackEvents.
     */
    public double assessGPR3(List<Event> events) {
        double rating1 = assessGPR3a(events);
        double rating2 = assessGPR3b(events);
        double rating3 = assessGPR3c(events);
        double rating4 = assessGPR3d(events);

        return ((rating1 * GPR3A_WEIGHT) + (rating2 * GPR3B_WEIGHT)
                + (rating3 * GPR3C_WEIGHT) + (rating4 * GPR3D_WEIGHT))
                / (GPR3A_WEIGHT + GPR3B_WEIGHT + GPR3C_WEIGHT + GPR3D_WEIGHT);
    }

    /**
     * Provides an assessment of a list of Event objects, containing exactly
     * four AttackEvent objects (n1, n2, n3, and n4) according to the rules of
     * GPR4, which provides a rating based on how well n2 rates according to
     * GPR2 and GPR3, giving preference to GPR2. GPR3 is considered to have
     * weaker precedence according to GTTM theory.
     *
     * @param events a list of Event objects which must contain exactly four
     * AttackEvent objects (n1, n2, n3, and n4), along with any number of other
     * Event objects.
     * @return a rating between 1.0 and 0.0 indicating how strongly n2
     * represents a boundary between two groups.
     */
    public double assessGPR4(List<Event> events) {
        double rating1 = assessGPR2(events);
        double rating2 = assessGPR3(events);

        return ((rating1 * GPR2_WEIGHT) + (rating2 * GPR3_WEIGHT))
                / (GPR2_WEIGHT + GPR3_WEIGHT);
    }
}
