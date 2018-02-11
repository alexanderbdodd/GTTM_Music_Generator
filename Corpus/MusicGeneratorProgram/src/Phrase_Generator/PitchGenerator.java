/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Phrase_Generator;

import Elements.*;
import GTTM_Analyser.*;
import Grammar_Elements.GrammarContainer;
import Grammar_Elements.GroupingStructure.Group;
import Grammar_Elements.MetricalStructure.Beat;
import Grammar_Elements.ReductionBranches.*;
import Elements.Pair;
import java.util.*;

/**
 * The PitchGenerator is used to create list of candidate chains of pitches
 * which match the constraints of the TimeSpanReduction and
 * ProlongationalReduction preference rules.
 *
 * @author Alexander Dodd
 */
public class PitchGenerator extends Generator {

    //some grammars are too constrained to generate music that fits
    //constraints from
    //in other cases, there's far too many candidates and the program hangs
    public final static double BASE_PROLONGATIONAL_VALUE = 0.55;
    public final static double BASE_TIMESPANREDUCTION_VALUE = 0.501;
    
    //used to set whether the base time-span value should be rigidly stuck to.
    public final static boolean TIMESPAN_PREFERENCE_STRICTNESS = false;

    //describes which checks should be performed upon pitch chains
    public final static boolean CHECK_TIMESPAN_REDUCTION = true;
    public final static boolean CHECK_PROLONGATIONAL_REDUCTION = true;
    public final static boolean CHECK_GROUPING_STRUCTURE = true;
    public final static boolean CHECK_CADENCE_RELATION = true;
    public final static boolean CHECK_PROLONGATIONAL_BRANCHING = false;

    //sets whether recusion should be restricted or not, and the recursion limit.
    public final static boolean RECURSION_RESTRICTION = true;
    public final static int RECURSION_LIMIT = 50000;

    private static PitchGenerator instance = null;
    private int chainNo = 0;

    /**
     *
     * @return the singleton instance of this class
     */
    public static PitchGenerator getInstance() {
        if (instance == null) {
            instance = new PitchGenerator();
        }

        return instance;
    }

    private PitchGenerator() {

    }

    /**
     * This method employs the time-span reduction and prolongational reduction
     * preference rules in order to generate a list of maps, where each map
     * constitutes one possible set of pitch events within a piece of music.
     * Each map provides a mapping of a Beat object against a Key object, where
     * the Beat object represents which Beat the pitch event is instantiated on,
     * and the Key object represents the pitch value.
     *
     * @param grammar the grammar to be used in constructing the pitch event
     * maps
     * @param localScale the scale to be used in generating the pitch events
     * @return a list of Map objects mapping Beat against Key.
     */
    public List<Map<Beat, Key>> getCandidatePitchChains(GrammarContainer grammar,
            Scale localScale) {
        assignFieldVariables(grammar);

        List<Map<Beat, Key>> candidateChains = null;

        //choosing a middle position
        KeyPositionEnum position = KeyPositionEnum.FIRST;
        while (position.getPosition() <= 2
                || position.getPosition() >= 6) {
            position = NoteGenerator.getInstance().getRandomPosition();

        }

        Key lastKey = new Key(localScale.getTonic(), position);

        int count = 0;

        while (candidateChains == null || candidateChains.isEmpty()) {

            //establishing potential prolongational reduction solutions
            Map<Beat, List<Key>> candidates = generateProlongationalCandidates(lastKey, localScale);
            if (candidates == null) {
                return null;
            }
            for (Beat b : candidates.keySet()) {
                Collections.shuffle(candidates.get(b));
            }
            candidates = filterForCadence(candidates, localScale);
            if (candidates == null) {
                return null;
            }

            //create candidate chains of beats which demonstrate preferable time span
            //reduction features
            chainNo = 0;
            candidateChains = generateCandidateTimeSpanChains(lastKey, localScale, candidates);
            if (candidateChains == null || candidateChains.isEmpty()) {
                if (count > 0) {
                    return null;
                }
                count++;
            }
        }

        return candidateChains;
    }

    private boolean testCandidacy(Map<Beat, Key> chain, List<ProlongationalBranch> branches,
            Scale localScale) {

        List<Branch> tBranches = getTopTBranch().getAllSubBranches();
        tBranches.add(getTopTBranch());

        if (CHECK_CADENCE_RELATION) {

            for (Branch branch : tBranches) {
                if (branch.getClass() == CadencedTimeSpanReductionBranch.class) {
                    CadencedTimeSpanReductionBranch cBranch = (CadencedTimeSpanReductionBranch) branch;
                    Beat start = cBranch.getCadenceStart();
                    Beat finish = cBranch.getCadenceEnd();

                    if (!assessCadenceRelation(chain.get(start), chain.get(finish), localScale)) {
                        return false;
                    }
                }
            }
        }

        if (CHECK_PROLONGATIONAL_BRANCHING) {
            if (!assessProlongationalCorrectness(branches, chain)) {
                return false;
            }
        }

        if (CHECK_TIMESPAN_REDUCTION) {

            if (!assessTimeSpanPreference(chain, localScale)
                    && TIMESPAN_PREFERENCE_STRICTNESS) {
                return false;
            }
        }

        if (CHECK_GROUPING_STRUCTURE) {

            if (!filterByGPR3a(chain, getgGroup().getSubGroups())
                    && !filterByNonGroupingBoundaryPitch(chain)) {
                return false;
            }
        }

        return true;

    }

    private boolean filterByNonGroupingBoundaryPitch(Map<Beat, Key> chain) {

        List<Beat> chainBeats = new ArrayList<>();

        for (Beat b : chain.keySet()) {
            chainBeats.add(b);
        }

        Collections.sort(chainBeats, chainBeats.get(0));

        List<Beat> boundaryBeats = AnalyserUtils.getInstance().getAllBoundaryBeats(getgGroup(), chainBeats);

        for (Beat b : chainBeats) {
            if (!boundaryBeats.contains(b) && chain.containsKey(b)) {
                List<Beat> beatSpan = AnalyserUtils.getInstance().getBeatSpanFromBeat(b, chainBeats);
                if (beatSpan != null) {

                    List<Event> eventSpan = AnalyserUtils.getInstance().createPitchEventList(chain, beatSpan);

                    double rating = GroupingStructureAnalyser.getInstance()
                            .assessGPR3a(eventSpan);

                    if (rating >= 0.5) {
                        return false;
                    }
                }

            }
        }

        return true;

    }

    //goes through the specified grouping level to ensure GPR3a is met
    /*
     This is used to check through a given chain of Key objects in accordance with a 
     given grouping level to filter out chains which don't meet the GPR3a constraint
     that the intervallic distance between n2-n3 is greater than that between
     n1-n2 and n3-n4.
     */
    private boolean filterByGPR3a(Map<Beat, Key> chain,
            List<Group> groupingLevel) {

        boolean preferable = true;

        searchGroup:
        //for each group on the given level of the grouping hierarchy
        for (Group gr : groupingLevel) {
            int size = 1;
            //check that the amount of beats contained within the group is greater than
            //the minimum needed number of beats
            if (gr.getMetricalBeatSpan().size() >= 2) {
                Beat beat = gr.getMetricalBeatSpan().get(gr.getMetricalBeatSpan().size() - size);

                //while the beat is not a pitch event beat
                while (!chain.containsKey(beat)) {
                    //increment the size
                    size++;
                    //check that the beats of the group is not smaller than the size value
                    if (gr.getMetricalBeatSpan().size() >= size) {
                        //assign a new beat to be considered
                        beat = gr.getMetricalBeatSpan().get(gr.getMetricalBeatSpan().size() - size);
                    } else {
                        //if the size is greater, then no assessment can be performed
                        //so set the breakLoop boolean to true to indicate that no meaningful assessment
                        //of intervallic distance can be performed on this group
                        //and the loop should be broken
                        continue searchGroup;
                        //return true;

                    }

                }

                Beat firstBeat = null;

                //looping through the beats in the group to find
                //the penultimate pitch event in the group
                while (!chain.containsKey(firstBeat)) {
                    size++;
                    if (gr.getMetricalBeatSpan().size() >= size) {
                        firstBeat = gr.getMetricalBeatSpan().get(gr.getMetricalBeatSpan().size() - size);
                    } else {
                        //if no pitch event can be found, jump to the next group to search
                        continue searchGroup;
                    }
                }

                //creating a list of events for checking against the gpr3a rule
                List<Event> events = new ArrayList<>();

                events.add(new AttackEvent(chain.get(firstBeat), DurationEnum.ONE_BEAT));

                //loop round adding all the pitch events
                //until four AttackEvents are contained
                int counter = 1;
                while (counter < 4 && firstBeat.getNextBeat() != null) {
                    firstBeat = firstBeat.getNextBeat();
                    if (chain.containsKey(firstBeat)) {
                        counter++;
                        events.add(new AttackEvent(chain.get(firstBeat), DurationEnum.ONE_BEAT));

                    }

                }

                //make sure the events list size is 4
                if (events.size() == 4) {
                    //get the gpr3a rating
                    double rating = GroupingStructureAnalyser.getInstance().assessGPR3a(events);

                    //if the rating is greater than 0.5, then there is a preference
                    if (rating > 0.5) {

                        preferable = true;
                    } else {
                        return false;
                    }
                } else {
                    continue searchGroup;
                }

            } else {
                //if the group isn't greater than minimum size, 
                //continue round the group
                continue searchGroup;
            }
        }

        return preferable;
    }

    /*
     This is used to assess whether the two Key objects consitute a cadence or not.
     Returns true if they do, and false if not.
     */
    private boolean assessCadenceRelation(Key start, Key finish, Scale localScale) {

        if (finish.getKeyLetterEnum() == localScale.getDominant()
                && start.getKeyLetterEnum() == localScale.getTonic()) {
            return true;
        } else {
            if (start.getKeyLetterEnum() == localScale.getDominant()
                    && finish.getKeyLetterEnum() == localScale.getTonic()) {
                return true;
            }

        }
        return false;
    }

    private Map<Beat, List<Key>> filterForCadence(Map<Beat, List<Key>> candidates, Scale localScale) {
        if (candidates == null) {
            return null;
        }

        if (CHECK_CADENCE_RELATION) {
            CadencedTimeSpanReductionBranch cBranch = null;
            if (getTopTBranch().getClass() != CadencedTimeSpanReductionBranch.class) {
                for (Branch br : getTopTBranch().getAllSubBranches()) {
                    if (br.getClass() == CadencedTimeSpanReductionBranch.class) {
                        cBranch = (CadencedTimeSpanReductionBranch) br;
                    }
                }

            } else {
                cBranch = (CadencedTimeSpanReductionBranch) getTopTBranch();
            }

            if (cBranch == null) {
                return candidates;
            } else {
                List<Key> startKeys = new ArrayList<>();
                List<Key> endKeys = new ArrayList<>();

                for (Key key : candidates.get(cBranch.getCadenceEnd())) {
                    for (Key key2 : candidates.get(cBranch.getCadenceStart())) {
                        if (assessCadenceRelation(key2, key, localScale)) {
                            startKeys.add(key2);
                            endKeys.add(key);
                        }
                    }
                }

                if (startKeys.isEmpty() || endKeys.isEmpty()) {
                    return null;
                } else {
                    candidates.put(cBranch.getCadenceEnd(), endKeys);
                    candidates.put(cBranch.getCadenceStart(), startKeys);

                    return candidates;
                }

            }

        } else {
            return candidates;
        }
    }

    private List<Map<Beat, Key>> generateCandidateTimeSpanChains(Key lastKey, Scale localScale,
            Map<Beat, List<Key>> candidates) {

        List<Branch> branches = new ArrayList<>();

        for (Branch branch : getTopTBranch().getAllSubBranches()) {
            branches.add(branch);
        }

        List<ProlongationalBranch> pBranches = new ArrayList<>();
        //create a list of all the prolongational branches
        for (Branch branch : getTopPBranch().getAllSubBranches()) {
            pBranches.add((ProlongationalBranch) branch);
        }
     //Collections.copy(branches, getTopPBranch().getAllSubBranches());
        //branches.add(0, getTopPBranch());

        //set up a map to act as the container for various chains of pitches
        Map<Beat, Key> chain = new HashMap<>();

        //set up the list of all the candidate chains
        List<Map<Beat, Key>> candidateChains = new ArrayList<>();

        //generate all variations of the chain and test
        for (Key key : candidates.get(getTopTBranch().getAssociatedBeat())) {
            chain = new HashMap<>();
            chain.put(getTopTBranch().getAssociatedBeat(), key);

            if (getTopTBranch().getClass() == CadencedTimeSpanReductionBranch.class) {
                CadencedTimeSpanReductionBranch cBr = (CadencedTimeSpanReductionBranch) getTopTBranch();

                for (Key key2 : candidates.get(cBr.getCadenceStart())) {
                    chain.put(cBr.getCadenceStart(), key2);
                    generateCandidateTimeSpanChains(branches.get(0), 1, branches, candidates,
                            chain, candidateChains, localScale, pBranches);
                }
            } else {

                generateCandidateTimeSpanChains(branches.get(0), 1, branches, candidates,
                        chain, candidateChains, localScale, pBranches);
            }
        }

        //return the list of candidate chains
        return candidateChains;

    }

    private boolean assessTimeSpanPreference(Map<Beat, Key> chain, Scale localScale) {

        Branch parentBranch = getTopTBranch();

        List<Branch> branches = parentBranch.getAllSubBranches();

        for (Branch br : branches) {
            parentBranch = br.getParent();

            double parentRating = TimeSpanReductionAnalyser
                    .getInstance().getTSPR2andTSPR3(new AttackEvent(chain.get(parentBranch.getAssociatedBeat()), DurationEnum.TWO_BEATS),
                            new AttackEvent(chain.get(br.getAssociatedBeat()), DurationEnum.TWO_BEATS), localScale);

            if (parentRating <= BASE_TIMESPANREDUCTION_VALUE) {

                return false;
            }

            for (Branch subBr : br.getChildBranches().values()) {
                double childRating = TimeSpanReductionAnalyser
                        .getInstance().getTSPR2andTSPR3(new AttackEvent(chain.get(parentBranch.getAssociatedBeat()), DurationEnum.TWO_BEATS),
                                new AttackEvent(chain.get(subBr.getAssociatedBeat()), DurationEnum.TWO_BEATS), localScale);

                if (childRating > parentRating) {

                    return false;
                }
            }
        }

        return true;
    }

    //this is used to check that branches that are meant to be progressions don't
    //form prolongations and vice versa.
    private boolean assessProlongationalCorrectness(List<ProlongationalBranch> branches,
            Map<Beat, Key> beatMap) {
        for (ProlongationalBranch branch : branches) {
            Beat beat = branch.getAssociatedBeat();

            if (branch.getProlongationType() == null) {
                continue;
            }

            Beat parentBeat = branch.getParent().getAssociatedBeat();
            if (branch.getProlongationType() == ProlongationalTypeEnum.STRONG_PROLONGATION
                    || branch.getProlongationType() == ProlongationalTypeEnum.WEAK_PROLONGATION) {

                if (beatMap.get(beat).compare(beatMap.get(beat), beatMap.get(parentBeat))
                        != 0) {
                    return false;
                }

                //only tests to see if the keyletter is the same, not the same pitch
                //  if (beatMap.get(beat).getNote().getKeyNumber() 
                //          != beatMap.get(parentBeat).getNote().getKeyNumber()) {
                //      return false;
                //  }
            } else {
                if (beatMap.get(beat).compare(beatMap.get(beat), beatMap.get(parentBeat)) == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    private void generateCandidateTimeSpanChains(Branch branch, int pos,
            List<Branch> branches, Map<Beat, List<Key>> candidates, Map<Beat, Key> chain, List<Map<Beat, Key>> chains,
            Scale localScale, List<ProlongationalBranch> pBranches) {
        //end condition
        if (pos >= branches.size()) {

            main:
            for (Key key : candidates.get(branch.getAssociatedBeat())) {
                chain.put(branch.getAssociatedBeat(), key);

                if (branch.getClass() == CadencedTimeSpanReductionBranch.class) {
                    CadencedTimeSpanReductionBranch cBr = (CadencedTimeSpanReductionBranch) branch;
                    for (Key key2 : candidates.get(cBr.getCadenceStart())) {
                        chain.put(cBr.getCadenceStart(), key2);

                        if (testCandidacy(chain, pBranches, localScale)) {
                            Map<Beat, Key> copyChain = new HashMap<>();
                            for (Beat b : chain.keySet()) {
                                copyChain.put(b, chain.get(b));
                            }
                            chains.add(copyChain);

                        } else {
                            chainNo++;
                        }

                    }
                } else {

                    if (testCandidacy(chain, pBranches, localScale)) {
                        Map<Beat, Key> copyChain = new HashMap<>();
                        for (Beat b : chain.keySet()) {
                            copyChain.put(b, chain.get(b));
                        }
                        chains.add(copyChain);

                    } else {
                        chainNo++;
                        //continue main;
                    }
                }
            }

        } //looping condition
        else {
            main:
            for (Key key : candidates.get(branch.getAssociatedBeat())) {
                chain.put(branch.getAssociatedBeat(), key);

                if (branch.getClass() == CadencedTimeSpanReductionBranch.class) {
                    CadencedTimeSpanReductionBranch cBr = (CadencedTimeSpanReductionBranch) branch;

                    for (Key key2 : candidates.get(cBr.getCadenceStart())) {
                        chain.put(cBr.getCadenceStart(), key2);
                        int pos2 = pos + 1;
                        Branch branch2 = branches.get(pos);

                        generateCandidateTimeSpanChains(branch2, pos2, branches, candidates,
                                chain, chains, localScale, pBranches);

                        if (chains.size() > RECURSION_LIMIT && RECURSION_RESTRICTION
                                || chainNo > RECURSION_LIMIT && RECURSION_RESTRICTION) {
                            return;
                        }

                    }

                } else {
                    int pos2 = pos + 1;
                    Branch branch2 = branches.get(pos);

                    generateCandidateTimeSpanChains(branch2, pos2, branches, candidates,
                            chain, chains, localScale, pBranches);

                    if (chains.size() > RECURSION_LIMIT && RECURSION_RESTRICTION
                            || chainNo > RECURSION_LIMIT && RECURSION_RESTRICTION) {
                        return;
                    }
                }
            }
        }

    }

    private Map<Beat, List<Key>> generateProlongationalCandidates(Key lastKey, Scale localScale) {
        //use the beat of the topPBranch as the most significant beat to work towards.
        Beat endEv = getTopPBranch().getAssociatedBeat();
        Map<Beat, List<Pair<Double, Key>>> beatMap = new HashMap<>();
        List<Pair<Double, Key>> candidates = new ArrayList<>();

        Pair<Double, Key> pair;
        pair = new Pair<>(1.0, lastKey);
        candidates.add(pair);
        beatMap.put(getTopPBranch().getAssociatedBeat(), candidates);

        AttackEvent lastEv = new AttackEvent(lastKey, DurationEnum.TWO_BEATS);

        for (Branch br : getTopPBranch().getAllSubBranches()) {

            //set up the first key to try
            Key firstKey = Key.getNextKey(null);
            candidates = new ArrayList<>();
            Beat beat = br.getAssociatedBeat();

            boolean progression = false;
            ProlongationalBranch pBr = (ProlongationalBranch) br;
            if (pBr.getProlongationType() == ProlongationalTypeEnum.PROGRESSION) {
                progression = true;
            }

            while (firstKey != null) {

                AttackEvent childEvent = new AttackEvent(firstKey, DurationEnum.TWO_BEATS);
                boolean rightBranching = ProlongationalReductionAnalyser.getInstance()
                        .isRightBranching(beat, endEv);

                double rating = ProlongationalReductionAnalyser.getInstance()
                        .assessPRPR3(lastEv, childEvent, rightBranching, progression, localScale);

                if (rating >= BASE_PROLONGATIONAL_VALUE || !CHECK_PROLONGATIONAL_REDUCTION) {
                    pair = new Pair<>(rating, firstKey);
                    candidates.add(pair);
                }
                firstKey = Key.getNextKey(firstKey);

            }

            if (candidates.isEmpty()) {
                return null;
            }
            beatMap.put(beat, candidates);

        }

        return filterCandidates(beatMap);

    }

    private Map<Beat, List<Key>> filterCandidates(Map<Beat, List<Pair<Double, Key>>> pairs) {

        Map<Beat, List<Key>> beatMap;
        if (!CHECK_PROLONGATIONAL_REDUCTION) {
            beatMap = new HashMap();
            for (Beat b : pairs.keySet()) {
                List<Key> keys = new ArrayList<>();
                for (Pair<Double, Key> pair2 : pairs.get(b)) {
                    keys.add(pair2.second);
                }
                beatMap.put(b, keys);
            }

        } else {

            beatMap = new HashMap<>();

            List<Key> candidates = new ArrayList<>();
            candidates.add(pairs.get(getTopPBranch().getAssociatedBeat()).get(0).second);

            beatMap.put(getTopPBranch().getAssociatedBeat(), candidates);

            int lowestLevel = findLowestBranchLevel(getTopPBranch());
            int highestLevel = getTopPBranch().getLevel();

            int diff = lowestLevel - highestLevel;
            Random rand = new Random();

            int level = highestLevel;

            double expansionValue = 0.00;
            boolean filled = false;

            int count = 0;

            main:
            while (!filled) {

                expansionValue += 0.03;
                double base = 1.0;
                double top = 1.0;
                while (level <= lowestLevel) {
                    top = base;
                    base -= expansionValue;

                    if (base <= BASE_PROLONGATIONAL_VALUE) {
                        base = BASE_PROLONGATIONAL_VALUE;
                    }
                    if (top <= BASE_PROLONGATIONAL_VALUE + 0.20) {
                        top = BASE_PROLONGATIONAL_VALUE + 0.20;
                    }

                    for (Branch branch : getTopPBranch().getAllSubBranches()) {
                        if (branch.getLevel() == level) {
                            candidates = new ArrayList<>();
                            Beat b = branch.getAssociatedBeat();

                            for (Pair<Double, Key> pair : pairs.get(b)) {
                                if (pair.first >= base && pair.first <= top) {
                                    candidates.add(pair.second);
                                }
                            }

                            if (candidates.isEmpty()) {
                                filled = false;
                                count++;
                                if (count > 1000000) {
                                    return null;
                                }
                                continue main;

                            } else {
                                filled = true;
                                beatMap.put(b, candidates);
                            }
                        }
                    }

                    level++;
                }

            }
        }

        return beatMap;

    }

    private int findLowestBranchLevel(Branch topBranch) {
        int lowestLevel = topBranch.getLevel();

        for (Branch br : topBranch.getAllSubBranches()) {
            if (br.getLevel() > lowestLevel) {
                lowestLevel = br.getLevel();
            }
        }

        return lowestLevel;

    }

    /**
     * A class for use in testing the private methods of the PitchGenerator. Not
     * for use outside testing.
     */
    public class TestProbe {

        public boolean filterByGPR3a(Map<Beat, Key> chain,
                List<Group> groupingLevel) {

            return PitchGenerator.this.filterByGPR3a(chain, groupingLevel);
        }

        public boolean assessCadenceRelation(AttackEvent parent, AttackEvent child, Scale localScale) {
            return PitchGenerator.getInstance().assessCadenceRelation(parent, child, localScale);
        }

        public int findLowestBranchLevel(Branch topBranch) {
            return PitchGenerator.getInstance().findLowestBranchLevel(topBranch);
        }

        public boolean assessTimeSpanPreference(Map<Beat, Key> chain, Scale localScale, GrammarContainer grammar) {
            assignFieldVariables(grammar);
            return PitchGenerator.getInstance().assessTimeSpanPreference(chain, localScale);
        }

        public boolean assessProlongationalCorrectness(List<ProlongationalBranch> branches,
                Map<Beat, Key> beatMap) {
            return PitchGenerator.getInstance().assessProlongationalCorrectness(branches, beatMap);
        }

    }
    
 
}
