/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.GTTM_Analyser;

import java.util.*;
import org.apache.commons.math3.stat.StatUtils;

import uk.ac.kent.computing.gttm.Elements.AttackEvent;
import uk.ac.kent.computing.gttm.Elements.Event;
import uk.ac.kent.computing.gttm.Elements.EventStream;
import uk.ac.kent.computing.gttm.Elements.Scale;
import uk.ac.kent.computing.gttm.Grammar_Elements.GrammarContainer;
import uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure.Group;
import uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure.HighLevelGroup;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.Branch;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.CadencedTimeSpanReductionBranch;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.ProlongationalBranch;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.ProlongationalTypeEnum;

/**
 * A class that is used to generating ratings indicating how well a given
 * EventStream fits a given Grammar object.
 *
 * @author Alexander Dodd
 */
public class RatingsGenerator {
    
    public final static double PROLONGATIONAL_RATING_WEIGHT = 1.0;
    public final static double TIMESPAN_RATING_WEIGHT = 1.0;
    public final static double METRICAL_RATING_WEIGHT = 1.0;
    public final static double GROUPING_STRUCTURE_RATING_WEIGHT = 1.0;

    private static RatingsGenerator instance = null;

    private List<Beat> pitchBeats;
    private Map<Beat, Event> beatMap;

    public static RatingsGenerator getInstance() {
        if (instance == null) {
            instance = new RatingsGenerator();
        }

        return instance;
    }

    private RatingsGenerator() {

    }

    /**
     * Used to provide an overall rating indicating how well a given EventStream
     * object fits the grammar given by the GrammarContainer object.
     *
     * @param grammar the GrammarContainer object containing the grammar against
     * which to compare the EventStream object
     * @param stream the EventStream object containing the stream of music which
     * is to be compared with the given GrammarContainer object
     * @return a rating indicating how well the given EventStream object fits
     * the grammar given by the GrammarContainer object. A rating of below 0.5
     * indicates that the EventStream largely violates the grammar structure,
     * and a rating equal to or greater than 0.5 indicates that the EventStream
     * object largely does not violate the given grammar structure. A value of
     * 1.0 indicates that there is no violation of the grammar structure. A
     * value of 0.0 indicates a complete violation of the grammar structure.
     */
    public double obtainRating(GrammarContainer grammar, EventStream stream) {

        return (((obtainMetricalRating(grammar, stream) * METRICAL_RATING_WEIGHT) + (obtainProlongationalReductionRating(grammar, stream) *PROLONGATIONAL_RATING_WEIGHT)
                + (obtainTimeSpanReductionRating(grammar, stream) * TIMESPAN_RATING_WEIGHT) + (obtainGroupingStructureRating(grammar, stream) * GROUPING_STRUCTURE_RATING_WEIGHT))
                / (METRICAL_RATING_WEIGHT + PROLONGATIONAL_RATING_WEIGHT + GROUPING_STRUCTURE_RATING_WEIGHT + TIMESPAN_RATING_WEIGHT));
    }

    public double obtainMetricalRating(GrammarContainer grammar, EventStream stream) {

        createEventMap(grammar, stream);
        setPitchBeats();
        double[] ratingsList = new double[calculateNumberAttackEvents(stream)];

        int highestMetricalStrength = grammar.getMetricalStructure().findHighestMetricalStrength();
        int i = 0;

        List<Branch> branches = grammar.getTopTimeSpanReductionBranch().getAllSubBranches();
        branches.add(0, grammar.getTopTimeSpanReductionBranch());

        //checking all the pitch beats to see how well they rate against MPR5a
        for (Beat beat : beatMap.keySet()) {
            if (beatMap.get(beat) != null
                    && beatMap.get(beat).getClass() == AttackEvent.class) {
                double beatRelativeStrength = (100 / highestMetricalStrength) * beat.getBeatStrength();
                beatRelativeStrength /= 100;
                double rating = MetricalStructureAnalyser.getInstance().assessMPR5a(stream.getEventList(),
                        (AttackEvent) beatMap.get(beat));

                int level = 0;

                for (Branch br : branches) {
                    if (br.getAssociatedBeat() == beat) {
                        level = br.getLevel();
                        break;
                    }
                }

                rating += MetricalStructureAnalyser.getInstance().assessMPR5e(grammar.getTopTimeSpanReductionBranch(),
                        beatMap, level, (AttackEvent) beatMap.get(beat));
                
                
                rating = rating / 2;

                //adjusting the  rating depending on the strength of the beat
                double diff = 0.0 + beatRelativeStrength;
                double base = (1 * rating);
                base = base * diff;
                rating = 0 + base;

                ratingsList[i] = rating;
                i++;

            }
        }
        return StatUtils.mean(ratingsList);
    }

    public double obtainTimeSpanReductionRating(GrammarContainer grammar, EventStream stream) {

        List<Branch> branches = grammar.getTopTimeSpanReductionBranch().getAllSubBranches();
        branches.add(0, grammar.getTopTimeSpanReductionBranch());

        return ((branchAnalyser(branches, grammar.getTopProlongationalReductionBranch(), grammar, stream, 1)
                + obtainCadenceRating(grammar, stream)) / 2);
               

    }

    public double obtainProlongationalReductionRating(GrammarContainer grammar, EventStream stream) {

        List<Branch> branches = grammar.getTopProlongationalReductionBranch().getAllSubBranches();
        branches.add(0, grammar.getTopProlongationalReductionBranch());

        double rating = branchAnalyser(branches, grammar.getTopProlongationalReductionBranch(), grammar, stream, 0);

        rating += checkBranchingCorrectness(grammar, stream);
        rating /= 2;
        
        return rating;
    }

    private double branchAnalyser(List<Branch> branches, Branch topBranch, GrammarContainer grammar,
            EventStream stream, int branchType) {
        createEventMap(grammar, stream);
        setPitchBeats();
        branches.add(0, topBranch);

        double[] ratingsArray = new double[branches.size()];
        int i = 0;

        for (Branch branch : branches) {
            int level = branch.getLevel();
            if (beatMap.get(branch.getAssociatedBeat()) == null
                    || beatMap.get(branch.getAssociatedBeat())
                    .getClass() != AttackEvent.class) {
                ratingsArray[i] = 0;
                i++;
                continue;
            }

            AttackEvent parentEv;
            AttackEvent childEvent
                    = (AttackEvent) beatMap.get(branch.getAssociatedBeat());

            Branch parent;

            if (branch.getParent() != null) {
                //parent = branch.getParent();
                parent = topBranch;
                if (beatMap.get(parent.getAssociatedBeat()) == null
                        || beatMap.get(parent.getAssociatedBeat()).getClass()
                        != AttackEvent.class) {
                    ratingsArray[i] = 0;
                    i++;
                    continue;
                }

                parentEv = (AttackEvent) beatMap.get(branch.getParent().getAssociatedBeat());

            } else {
                parentEv = (AttackEvent) beatMap.get(topBranch.getAssociatedBeat());
                parent = topBranch;
            }

            double parentRating;

            if (branch == topBranch) {
                parentRating = 1.0;
            } else {
                if (branchType == 0) {
                    
                    ProlongationalBranch pBr = (ProlongationalBranch) branch;
                    boolean progression = false;
                    if (pBr.getProlongationType() == ProlongationalTypeEnum.PROGRESSION) {
                        progression = true;
                    }

                    parentRating = ProlongationalReductionAnalyser.getInstance()
                            .assessPRPR3(parentEv, childEvent, ProlongationalReductionAnalyser.getInstance()
                                    .isRightBranching(branch.getAssociatedBeat(), parent.getAssociatedBeat()), progression, stream.getLocalScale());
                } else {
                    parentRating = TimeSpanReductionAnalyser.getInstance()
                            .getTSPR2andTSPR3(parentEv, childEvent, stream.getLocalScale());
                }
            }
            
            //adjusting for depth of branch
            double diffInclusion = level * 0.02;
            parentRating += diffInclusion;
            
            if(parentRating > 1.0)
            {
                parentRating = 1.0;
            }

            ratingsArray[i] = parentRating;
            i++;
        }

        return StatUtils.mean(ratingsArray);
    }
    
    public double checkBranchingCorrectness(GrammarContainer grammar, EventStream stream)
    {
        createEventMap(grammar, stream);
        setPitchBeats();
        
        List<Branch> branches = grammar.getTopProlongationalReductionBranch().getAllSubBranches();
        branches.add(0, grammar.getTopProlongationalReductionBranch());
        
        List<ProlongationalBranch> pBranches = new ArrayList<>();
        for(Branch br : branches)
        {
            pBranches.add((ProlongationalBranch) br);
        }
        
        double[] ratings = new double[grammar.getTopProlongationalReductionBranch().getAllSubBranches().size()];
        int pos = 0;
        
        for(ProlongationalBranch br : pBranches)
        {
            if(br.getParent() == null)
            {
                continue;
            }
            
           AttackEvent parentEv = (AttackEvent) beatMap.get(br.getParent().getAssociatedBeat());
           AttackEvent childEv = (AttackEvent) beatMap.get(br.getAssociatedBeat());
           
           if(br.getProlongationType() == ProlongationalTypeEnum.PROGRESSION)
           {
               if(parentEv.compare(parentEv, childEv) == 0)
               {
                   ratings[pos] = 0;
               }
               else{
                   ratings[pos] = 1;
               }
           }
           else{
               if(parentEv.compare(parentEv, childEv) == 0)
               {
                   ratings[pos] = 1;
               }
               else{
                   ratings[pos] = 0;
               }
           }
           
           pos++;
        }
        
        return StatUtils.mean(ratings);
        
    }

    /**
     * Designed to test a piece of music against a grammar to see how well it
     * fits the grouping structure constraints of the grammar.
     *
     * @param grammar the grammar to assess the piece of music again
     * @param stream the piece of music
     * @return a rating between 0.0 and 1.0 indicating how strongly the piece
     * matches the grouping structure constraints.
     */
    public double obtainGroupingStructureRating(GrammarContainer grammar, EventStream stream) {
        createEventMap(grammar, stream);
        setPitchBeats();
        List<Beat> boundaryBeats = AnalyserUtils.getInstance()
                .getAllBoundaryBeats(grammar.getTopLevelGroup(), pitchBeats);

        double ratingTotal = 0;
        int ratingsNo = 0;

        Map<Beat, Double> boundaryRatings = new HashMap<>();

        //go through all the beats of the beat map
        //for all the AttackEvent instantiations, check their boundary rating.
        for (Beat b : beatMap.keySet()) {
            if (beatMap.get(b) != null
                    && beatMap.get(b).getClass() == AttackEvent.class) {
                List<Beat> beatSpan = AnalyserUtils.getInstance().getBeatSpanFromBeat(b, pitchBeats);
                if (beatSpan != null) {
                    List<Event> events = AnalyserUtils.getInstance()
                            .getEventStreamFragment(beatMap, beatSpan.get(0), beatSpan.get(beatSpan.size() - 1));
                    //get a rating of how well the given attack event fits the boundary constraints
                    //at the moment each aspect of the rating is detailed specifically because
                    //dynamics and articulation hasn't been implemented in the music generator
                    //so this is not being tested for.

                    double rating = ((GroupingStructureAnalyser.getInstance().assessGPR2(events)
                            + GroupingStructureAnalyser.getInstance().assessGPR3a(events)
                            + GroupingStructureAnalyser.getInstance().assessGPR3d(events))
                            / (GroupingStructureAnalyser.GPR2_WEIGHT + GroupingStructureAnalyser.GPR3A_WEIGHT
                            + GroupingStructureAnalyser.GPR3D_WEIGHT));

                    //if the attack event isn't a boundary beat, then invert the rating
                    //such that a strong rating is inverted to a weak rating and vice versa
                    if (!boundaryBeats.contains(b)) {
                        rating = 1.0 - rating;
                    } else {
                        boundaryRatings.put(b, rating);
                    }

                    ratingTotal += rating;
                    ratingsNo++;

                }
            }
        }

        double ratingDiminish = 0.0;
        double lowestRating = 2.0;

        List<Group> topGroups = grammar.getTopLevelGroup().getSubGroups();
        List<Beat> seenBoundaryBeats = new ArrayList<>();

        List<Group> nextLevelGroups;

        while (topGroups != null && !topGroups.isEmpty()) {
            nextLevelGroups = new ArrayList<>();
            double localLowest = 2.0;
            for (Group gr : topGroups) {
                Beat b = AnalyserUtils.getInstance().identifyBoundaryBeat(gr, pitchBeats);
                if (boundaryRatings.get(b) != null && boundaryRatings.get(b) < localLowest) {
                    localLowest = boundaryRatings.get(b);
                }
                if (boundaryRatings.get(b) != null && boundaryRatings.get(b) >= lowestRating) {
                    ratingDiminish += 0.05;
                }
                seenBoundaryBeats.add(b);
                if (gr.getClass() == HighLevelGroup.class) {
                    nextLevelGroups.addAll(AnalyserUtils.getInstance().getNextLevelGroups((HighLevelGroup) gr,
                            seenBoundaryBeats, pitchBeats));
                }

            }
            lowestRating = localLowest;
            topGroups = nextLevelGroups;

        }

        ratingDiminish += checkGRP1(grammar.getTopLevelGroup(), pitchBeats);

        double ret = ((ratingTotal / ratingsNo) - ratingDiminish);

        if (ret < 0) {
            ret = 0;
        }

        return ret;
    }

    private double checkGRP1(HighLevelGroup topGroup, List<Beat> pitchBeats) {
        List<Group> groups = new ArrayList<>();
        groups.add(topGroup);

        double diminish = 0.0;

        while (!groups.isEmpty()) {
            List<Group> nextLevelGroups = new ArrayList<>();

            for (Group gr : groups) {
                if (!checkGroupHasMoreThanTwoPitchesGPR1(gr, pitchBeats)) {
                    diminish += 0.02;
                }
                if (gr.getClass() == HighLevelGroup.class) {
                    HighLevelGroup hGr = (HighLevelGroup) gr;
                    nextLevelGroups.addAll(hGr.getSubGroups());
                }

            }

            groups = nextLevelGroups;

        }

        return diminish;

    }

    private boolean checkGroupHasMoreThanTwoPitchesGPR1(Group gr, List<Beat> pitchBeats) {

        int count = 0;
        for (Beat b : gr.getMetricalBeatSpan()) {
            if (pitchBeats.contains(b)) {
                count++;
            }
        }
        if (count < 3) {
            return false;
        } else {
            return true;
        }

    }

    //returns a strong rating if a cadence branch constitutes a cadence
    public double obtainCadenceRating(GrammarContainer grammar, EventStream stream) {

        createEventMap(grammar, stream);
        setPitchBeats();

        List<Branch> branches = grammar.getTopTimeSpanReductionBranch().getAllSubBranches();
        branches.add(grammar.getTopTimeSpanReductionBranch());

        List<CadencedTimeSpanReductionBranch> cadences = new ArrayList<>();

        for (Branch br : branches) {
            if (br.getClass() == CadencedTimeSpanReductionBranch.class) {
                cadences.add((CadencedTimeSpanReductionBranch) br);
            }
        }

        if (cadences.isEmpty()) {
            return 1.0;
        } else {
            double[] ratingsArray = new double[cadences.size()];

            int i = 0;
            for (CadencedTimeSpanReductionBranch br : cadences) {
                if (checkIsCadence(br, stream.getLocalScale())) {
                    ratingsArray[i] = 1.0;
                } else {
                    ratingsArray[i] = 0.0;
                }
                i++;
            }

            return StatUtils.mean(ratingsArray);
        }

    }

    private boolean checkIsCadence(CadencedTimeSpanReductionBranch branch, Scale localScale) {
        Beat startBeat = branch.getCadenceStart();
        Beat endBeat = branch.getCadenceEnd();

        AttackEvent startEv = null;
        AttackEvent endEv = null;

        if (beatMap.get(startBeat) != null && beatMap.get(startBeat).getClass() == AttackEvent.class) {
            startEv = (AttackEvent) beatMap.get(startBeat);
        }

        if (beatMap.get(endBeat) != null && beatMap.get(endBeat).getClass() == AttackEvent.class) {
            endEv = (AttackEvent) beatMap.get(endBeat);
        }

        if (endEv == null || startEv == null) {
            return false;
        } else {
            if (startEv.getNote() == localScale.getDominant() && endEv.getNote() == localScale.getTonic()) {
                return true;
            } else {
                if (startEv.getNote() == localScale.getTonic() && endEv.getNote() == localScale.getDominant()) {
                    return true;
                } else {
                    return false;
                }
            }
        }

    }

    private void setPitchBeats() {

        pitchBeats = new ArrayList<>();

        for (Beat b : beatMap.keySet()) {
            if (beatMap.get(b) != null && beatMap.get(b).getClass() == AttackEvent.class) {
                pitchBeats.add(b);
            }
        }

    }

    private int calculateNumberPitches(GrammarContainer grammar) {
        Branch br = grammar.getTopProlongationalReductionBranch();

        int count = 1;
        count += br.getAllSubBranches().size();

        return count;

    }

    private int calculateNumberAttackEvents(EventStream stream) {
        int count = 0;
        for (Event ev : stream.getEventList()) {
            if (ev.getClass() == AttackEvent.class) {
                count++;
            }
        }

        return count;
    }

    public Map<Beat, Event> createEventMap(GrammarContainer grammar, EventStream stream) {
        List<Beat> beats = grammar.getMetricalStructure().getMetricalBeatsList();
        List<Event> events = stream.getEventList();

        beatMap = new HashMap<>();

        Beat currentBeat = beats.get(0);
        for (Event ev : events) {
            int expansion = AnalyserUtils.getInstance()
                    .getDurationBeatExpansion(ev.getDurationEnum());

            beatMap.put(currentBeat, ev);
            if (currentBeat.getNextBeat() == null) {
                break;
            }
            currentBeat = currentBeat.getNextBeat();

            int i = currentBeat.getPosition();

            while (currentBeat.getPosition() < i + expansion) {
                beatMap.put(currentBeat, null);
                currentBeat = currentBeat.getNextBeat();
                if(currentBeat == null)
                {
                    break;
                }
            }

        }

        return beatMap;
    }

}
