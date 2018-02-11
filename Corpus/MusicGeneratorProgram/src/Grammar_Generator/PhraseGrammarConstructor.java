package Grammar_Generator;

import GTTM_Analyser.AnalyserUtils;
import Grammar_Elements.GrammarContainer;
import Grammar_Elements.GroupingStructure.*;
import Grammar_Elements.MetricalStructure.Beat;
import Grammar_Elements.MetricalStructure.MetricalContainer;
import Grammar_Elements.ReductionBranches.*;
import java.io.Serializable;
import java.util.*;
import org.apache.commons.math3.stat.StatUtils;

/**
 * A singleton class that creates a randomly generated grammar in prolongational
 * normative form.
 *
 * @author Alexander Dodd
 */
public class PhraseGrammarConstructor {

    private static PhraseGrammarConstructor instance = null;
    private Random rand = new Random();

    /**
     *
     * @return the singleton instance of this class
     */
    public static PhraseGrammarConstructor getInstance() {
        if (instance == null) {
            instance = new PhraseGrammarConstructor();
        }

        return instance;
    }

    private PhraseGrammarConstructor() {
    }

    /**
     * Generates the grammar for a phrase of tonal music. This adheres to the
     * prolongational normal form described by the prolongational reduction
     * preference rules.
     *
     * @return a GrammarContainer object which describes the phrase's structure
     */
    public GrammarContainer constructPhraseGrammar() {
        //construct metrical container
        MetricalContainer mContainer = constructMetricalContainer();
       
        //allocate pitch events
        List<Beat> pitchBeats = allocatePitchBeats(mContainer);
        
        //allocate grouping structure
        HighLevelGroup groupingStructure = allocateGroupingStructure(mContainer, pitchBeats);
    
        //construct timespan reduction
        TimeSpanReductionBranch topTBranch = allocateTimeSpanReduction(pitchBeats, mContainer);
     
        //construct prolongational reduction
        ProlongationalBranch topPBranch = allocateNormativeProlongationalReduction(pitchBeats, topTBranch);
       
        //designate the whole metrical span as the prolongational group
        ProlongationalGroup pGroup = createProlongationalGroup(mContainer.getMetricalBeatsList());
       
        //put all aspects of the analysis into a grammar container and return it

        return new GrammarContainer(topPBranch, topTBranch, mContainer, groupingStructure, pGroup);

    }

    /**
     * Creates a prolongational group. At this stage, the prolongational group
     * spans the whole metrical structure.
     *
     * @param span the span of beats for the prolongational group to contain
     * @return the prolongational group object
     */
    private ProlongationalGroup createProlongationalGroup(List<Beat> span) {
        ProlongationalBaseGroup base = new ProlongationalBaseGroup(span);
        List<Group> groups = new ArrayList<>();
        groups.add(base);

        return new ProlongationalGroup(groups);
    }

    /**
     * Allocates the timespan reduction branches according to the list of pitch
     * beats given as a parameter of the method. The Time Span reduction will
     * contain one cadence branch matching two of the end point of the pitch
     * beats list.
     *
     * @param pitchBeats the list of beats assigned as pitches
     * @param mContainer the metrical structure of the grammar
     * @return the top branch of the Time Span Reduction structure
     */
    private TimeSpanReductionBranch allocateTimeSpanReduction(List<Beat> pitchBeats, MetricalContainer mContainer) {
        Collections.sort(pitchBeats, pitchBeats.get(0));

        //choose the last pitch beat ending on a relatively strong beat as cadence end
        Collections.reverse(pitchBeats);
        TimeSpanReductionBranch topTBranch;

        Beat cadenceEnd = findCadenceBeats(pitchBeats, mContainer.findHighestMetricalStrength(), 0, true);

        //choose the cadence beginning by choosing another strong beat
        //which comes before the cadenceEnd
        Beat cadenceStart = findCadenceBeats(pitchBeats, mContainer.findHighestMetricalStrength(),
                pitchBeats.indexOf(cadenceEnd) + 1, false);

        TimeSpanReductionBranch cadenceBranch = new CadencedTimeSpanReductionBranch(cadenceEnd, cadenceStart, 0);
        topTBranch = cadenceBranch;

        //find the structural beginning. For simplicity, this is the first pitch beat
        Collections.sort(pitchBeats, pitchBeats.get(0));
        TimeSpanReductionBranch structuralBeginning = new TimeSpanReductionBranch(pitchBeats.get(0), 1);

        //structural ending (cadence branch) as parent of structural beginning (TSRPR 9)
        cadenceBranch.addChildBranch(structuralBeginning);

        //work out beats inbetween the structural beginning and the middle of the piece
        int middlePos = cadenceEnd.getPosition() % 2 == 0
                ? (cadenceEnd.getPosition() / 2) - 1
                : (cadenceEnd.getPosition() / 2);

        //find the nearest middle beat
        Beat pitchBeat = null;
        while (!pitchBeats.contains(mContainer.getBeatAtPosition(middlePos))
                || mContainer.getBeatAtPosition(middlePos).getPosition() 
                        >= cadenceStart.getPosition()) {
            middlePos--;
        }
        pitchBeat = mContainer.getBeatAtPosition(middlePos);

        //make sure it's not the same beat as the first one
        if (pitchBeat.getPosition() > structuralBeginning.getAssociatedBeat().getPosition()) {
            TimeSpanReductionBranch subBranch = null;

            subBranch = new TimeSpanReductionBranch(pitchBeat, 2);
           
            structuralBeginning.addChildBranch(subBranch);

            //adding beats inbetween the middle pitch and the the middle point 
            //between the structural beginning and the middle pitchto the sub branch
            int bottomPos = middlePos % 2 == 0 ? middlePos / 2 : (middlePos / 2) + 1;

            while (mContainer.getMetricalBeatsList().indexOf(pitchBeats.get(0)) >= bottomPos) {
                bottomPos++;
            }

            addInbetweenBranches(bottomPos, middlePos, pitchBeats, mContainer, subBranch);

            //adding beats inbetween the bottomPos and the first beat of metrical container
            addInbetweenBranches(mContainer.getMetricalBeatsList().indexOf(pitchBeats.get(0)) + 1,
                    bottomPos, pitchBeats, mContainer, structuralBeginning);

        }

        //work out beats for the end portion of the phrase
        //find the nearest middle beat to end
        pitchBeat = null;
        middlePos++;
        while (pitchBeat == null && mContainer.getBeatAtPosition(middlePos) != cadenceStart) {
            if (pitchBeats.contains(mContainer.getBeatAtPosition(middlePos))) {
                pitchBeat = mContainer.getBeatAtPosition(middlePos);
                break;
            }

            middlePos++;
        }

        if (pitchBeat != null) {
            //creating a branch to mark the beginning of the end of the piece
            TimeSpanReductionBranch endBeginning = new TimeSpanReductionBranch(pitchBeat, 2);
            cadenceBranch.addChildBranch(endBeginning);

            int bottomPos = middlePos + 1;
            int diff = ((mContainer.getMetricalBeatsList().indexOf(cadenceStart) - bottomPos) / 2);
            int endPos = bottomPos + diff;

            //add branches inbetween the start of the cadence and the endBeginning branch
            addInbetweenBranches(bottomPos, endPos, pitchBeats, mContainer, endBeginning);

            bottomPos = endPos;
            endPos = mContainer.getMetricalBeatsList().indexOf(cadenceStart);

            if (endPos > bottomPos) {

                //add branches inbetween middle point of endBeginning to cadenceStart point, to cadenceStart
                addInbetweenBranches(bottomPos, endPos, pitchBeats, mContainer, cadenceBranch);
            }
        }
        int bottomPos = mContainer.getMetricalBeatsList().indexOf(cadenceStart) + 1;
        int endPos = mContainer.getMetricalBeatsList().indexOf(cadenceEnd);

        if (endPos - bottomPos > 0) {
            //add branches for beats inbetween the cadence points
            addInbetweenBranches(bottomPos, endPos, pitchBeats, mContainer, cadenceBranch);
        }
        bottomPos = endPos + 1;
        endPos = mContainer.getMetricalBeatsList().size();

        if (endPos - bottomPos > 0) {
            //add branches for beats after the cadence
            addInbetweenBranches(bottomPos, endPos, pitchBeats, mContainer, cadenceBranch);
        }

        return topTBranch;

    }

    /**
     * Adds a series of TimeSpanBranch objects inbetween the given positions
     *
     * @param bottomPos the first position to include in the time span reduction
     * @param endPos the position after the last position to include in the time
     * span reduction
     * @param pitchBeats the list of beats assigned as pitch instantiation beats
     * @param mContainer the metrical structure of the grammar
     * @param topBranch the top branch of the added branches
     */
    private void addInbetweenBranches(int bottomPos, int endPos, List<Beat> pitchBeats,
            MetricalContainer mContainer, TimeSpanReductionBranch topBranch) {

        Collections.sort(pitchBeats, pitchBeats.get(0));

        int level = topBranch.getLevel() + 1;
        while (topBranch.getChildBranches().containsKey(level)) {
            level++;
        }
        endPos--;

        while (endPos >= bottomPos) {
            if (pitchBeats.contains(mContainer.getBeatAtPosition(endPos))) {
                TimeSpanReductionBranch newBranch
                        = new TimeSpanReductionBranch(mContainer.getBeatAtPosition(endPos),
                                level);
                level++;
                topBranch.addChildBranch(newBranch);
                topBranch = rand.nextBoolean() ? newBranch : newBranch;
            }

            endPos--;
        }
    }

    /**
     * Used to determine pitch beats which are candidates for forming cadence
     * pitch beats. A pitch beat is preferred as a cadence if it is near the end
     * of the span of metrical beats and lands on relatively strong beats
     *
     * @param pitchBeats the list of beats assigned as pitch beats
     * @param highestStrength the highest metrical structure of the piece
     * @param pos the position from which to search for the cadence beats
     * @param cadenceEnd if true, then the beat is meant to represent the
     * cadence end beat, else it represents the cadence start beat
     *
     * @return the candidate cadence beat
     */
    private Beat findCadenceBeats(List<Beat> pitchBeats, int highestStrength, int pos, boolean cadenceEnd) {
        Beat cadenceBeat = null;

        int finalPos;
        if (cadenceEnd) {
            finalPos = (pitchBeats.size() / 2) - 2;
        } else {
            finalPos = (pitchBeats.size() / 2) - 1;
        }

        int originalPos = pos;

        main:
        while (cadenceBeat == null) {
            while (cadenceBeat == null && pos < finalPos) {
                if (pitchBeats.get(pos).getBeatStrength() == highestStrength) {
                    cadenceBeat = pitchBeats.get(pos);
                    break main;
                } else {
                    pos++;
                }
            }
            highestStrength--;
            pos = originalPos;
        }

        return cadenceBeat;
    }

    /*
     Creates a prolongational reduction with the following normative structure:
     1. A prolongational ending
     2. A prolongational beginning which is a prolongation (i.e. not a progression)
     of the prolongational ending.
     3. A right branching progression as the most important direct elaboration
     of the prolongational beginning.
     4. A left branching progression as the most important elaboration of 
     the first element of the cadence.
    
     This doesn't produce all possible well formed variations of prolongational structuring
     yet.
     */
    private ProlongationalBranch allocateNormativeProlongationalReduction(List<Beat> pitchBeats,
            TimeSpanReductionBranch tBranch) {
        //if the amount of pitches isn't greater than 4, then the normative preference 
        //can't apply, so return null
        if (pitchBeats.size() < 5) {
            return null;
        }

        ProlongationalBranch topPBranch = null;
        List<Beat> appliedBeats = new ArrayList<>();

        //finding the cadence branch of the timespan reduction
        List<Branch> branches = tBranch.getAllSubBranches();
        branches.add(tBranch);

        CadencedTimeSpanReductionBranch cadenceBranch = null;
        Iterator<Branch> it = branches.iterator();
        while (it.hasNext()) {
            Branch br = it.next();
            if (br.getClass() == CadencedTimeSpanReductionBranch.class) {
                cadenceBranch = (CadencedTimeSpanReductionBranch) br;
                break;
            }
        }

        if (cadenceBranch == null) {
            return null;
        }

        //allocating the last pitch beat as the prolongational ending
        Collections.sort(pitchBeats, pitchBeats.get(0));
        Collections.reverse(pitchBeats);

        ProlongationalBranch pEnding = new ProlongationalBranch(cadenceBranch.getCadenceEnd(), 0);
        topPBranch = pEnding;
        appliedBeats.add(cadenceBranch.getCadenceEnd());

        //allocating the first pitch beat as the prolongational beginning branch,
        //which is randomly either a strong or weak prolongation of the prolongational ending.
        Collections.sort(pitchBeats, pitchBeats.get(0));
        ProlongationalTypeEnum pType = null;
        if (rand.nextBoolean()) {
            pType = ProlongationalTypeEnum.STRONG_PROLONGATION;
        } else {
            pType = ProlongationalTypeEnum.WEAK_PROLONGATION;
        }

        ProlongationalBranch pBeginning = new ProlongationalBranch(pitchBeats.get(0), 1,
                pType);

        appliedBeats.add(pitchBeats.get(0));

        //assign the beginning as a branch of the ending
        topPBranch.addChildBranch(pBeginning);

        int endLevel = 2;

        //allocating the start of the cadence
        ProlongationalBranch cBranch = new ProlongationalBranch(cadenceBranch.getCadenceStart(), endLevel,
                ProlongationalTypeEnum.PROGRESSION);
        appliedBeats.add(cadenceBranch.getCadenceStart());

        pEnding.addChildBranch(cBranch);

        //finding the pitch beat that can form a left branching progression to
        //the cadence start
        int pos = 0;

        //finding the middle beat as the progression
        if ((pitchBeats.size() % 2) == 0) {
            pos = pitchBeats.size() / 2;
        } else {
            pos = pitchBeats.size() / 2 + 1;

        }

        Collections.sort(pitchBeats, pitchBeats.get(0));
        Beat endingBeat = pitchBeats.get(pos);

        //making sure it's a left branching structure
        if (endingBeat.getPosition() > cBranch.getAssociatedBeat().getPosition()) {
            return null;
        }

        //establish left branching progression of cadence
        ProlongationalBranch leftCadenceBranch = new ProlongationalBranch(endingBeat, (endLevel + 1),
                ProlongationalTypeEnum.PROGRESSION);
        cBranch.addChildBranch(leftCadenceBranch);
        appliedBeats.add(endingBeat);

        //creating branches for all the pitch beats inbetween the leftCadenceBranch and
        //the cadence start
        assignRandomIntermittantBranches(appliedBeats, leftCadenceBranch, pitchBeats, pos + 1, pitchBeats.indexOf(cBranch));

        //doing the same for the beats inbetween the cadence start and cadence end
        assignRandomIntermittantBranches(appliedBeats, cBranch, pitchBeats, pitchBeats.indexOf(cBranch.getAssociatedBeat()) + 1,
                pitchBeats.indexOf(pEnding));

        //doing the same for the beats after the cadence end branch, if any
        assignRandomIntermittantBranches(appliedBeats, pEnding, pitchBeats, pitchBeats.indexOf(pEnding.getAssociatedBeat()) + 1,
                pitchBeats.size());

        //establishing the most important rightBranching progression of the prolongational beginning
        //as the nearest middle beat
        pos = ((pitchBeats.size() % 2) == 0) ? ((pitchBeats.size() / 2) - 1) : (pitchBeats.size() / 2);
        ProlongationalBranch rightBranchStart = new ProlongationalBranch(pitchBeats.get(pos), 2,
                ProlongationalTypeEnum.PROGRESSION);
        pBeginning.addChildBranch(rightBranchStart);
        appliedBeats.add(pitchBeats.get(pos));

        //adding intermittant beats as child of rightBranchStart. This should be adjusted
        //later to allow possibility of more children of the pBeginning.
        int middle = pos / 2;
        if (pos <= 1) {
            assignRandomIntermittantBranches(appliedBeats, pBeginning, pitchBeats, 0, pos);
        } else {
            assignRandomIntermittantBranches(appliedBeats, pBeginning, pitchBeats, 1, middle);
            assignRandomIntermittantBranches(appliedBeats, rightBranchStart, pitchBeats, middle, pos);
        }

        return topPBranch;

    }

    /**
     * Randomly assigns prolongational branches between the given positions.
     *
     * @param appliedBeats Beats already assigned in the prolongational
     * reduction
     * @param topBranch the top prolongational branch of which to append the
     * intermittant branches to
     * @param pitchBeats the list of beats assigned as pitch beats
     * @param pos the first position from which to search for pitches to add to
     * the prolongational reduction
     * @param finalPos the position after the last beat to search for beats to
     * add to the prolongational reduction
     */
    private void assignRandomIntermittantBranches(List<Beat> appliedBeats,
            ProlongationalBranch topBranch, List<Beat> pitchBeats, int pos, int finalPos) {
        //creating branches for all the pitch beats inbetween the leftCadenceBranch and
        //the cadence start
        int pLevel = topBranch.getLevel() + 1;
        while (topBranch.getChildBranches().containsKey(pLevel)) {
            pLevel++;
        }

        Beat endingBeat;

        if (pos < pitchBeats.size()) {
            endingBeat = pitchBeats.get(pos);
        } else {
            return;
        }
        //randomly deciding whether it's to be a progression or a prolongation
        ProlongationalTypeEnum pType = rand.nextBoolean() ? ProlongationalTypeEnum.PROGRESSION : ProlongationalTypeEnum.WEAK_PROLONGATION;

        ProlongationalBranch parentBranch = topBranch;
        while (!appliedBeats.contains(endingBeat) && pos != finalPos) {
            ProlongationalBranch br = new ProlongationalBranch(endingBeat, pLevel, pType);
            parentBranch.addChildBranch(br);
            pLevel++;
            parentBranch = rand.nextBoolean() ? br : br;
            pType = rand.nextBoolean() ? ProlongationalTypeEnum.PROGRESSION : ProlongationalTypeEnum.WEAK_PROLONGATION;
            pos++;
            appliedBeats.add(endingBeat);
            if (pos < pitchBeats.size()) {
                endingBeat = pitchBeats.get(pos);
            } else {
                break;
            }
        }

    }

    /**
     * Allocates a grouping structure based on the list of the allocated pitch
     * beats and the metrical container. All base groups must contain at least
     * three pitches
     *
     * @param mContainer the metrical structure
     * @param pitchBeats the list of beats allocated as pitch beats
     * @return the grouping structure's top group, else null if no suitable
     * structure could be found
     */
    private HighLevelGroup allocateGroupingStructure(MetricalContainer mContainer,
            List<Beat> pitchBeats) {

        List<BaseGroup> baseGroups = createBaseGroups(mContainer, pitchBeats);

        List<Group> lastLevelGroups = new ArrayList<>();
        for (BaseGroup gr : baseGroups) {
            lastLevelGroups.add(gr);
        }

        while (lastLevelGroups.size() > 1) {

            List<Group> newLevelGroups = new ArrayList<>();
            List<Group> subGroups = new ArrayList<>();

            for (Group gr : lastLevelGroups) {

                subGroups.add(gr);
                if (subGroups.size() > 1 && rand.nextBoolean() || subGroups.size() > 4) {
                    HighLevelGroup hGroup = new HighLevelGroup(subGroups);
                    newLevelGroups.add(hGroup);
                    subGroups = new ArrayList<>();
                }

            }

            if (!subGroups.isEmpty()) {

                if (subGroups.size() > 1) {
                    HighLevelGroup hGroup = new HighLevelGroup(subGroups);
                    newLevelGroups.add(hGroup);

                } else {
                    newLevelGroups.add(subGroups.get(0));
                }
            }

            lastLevelGroups = newLevelGroups;

        }

        return (HighLevelGroup) lastLevelGroups.get(0);

    }

    private List<BaseGroup> createBaseGroups(MetricalContainer mContainer,
            List<Beat> pitchBeats) {
        List<List<Beat>> baseGroups = null;

        while (baseGroups == null || baseGroups.size() < 2) {
            baseGroups = allocateBaseGroupBeats(mContainer,
                    pitchBeats);
        }

        List<BaseGroup> baseGroupsList = new ArrayList<>();

        Beat firstBeat = mContainer.getBeatAtPosition(0);

        for (List<Beat> i : baseGroups) {
            List<Beat> groupSpan = new ArrayList<>();

            while (firstBeat != i.get(i.size() - 1)) {
                groupSpan.add(firstBeat);
                firstBeat = firstBeat.getNextBeat();

            }

            groupSpan.add(firstBeat);

            if (firstBeat.getNextBeat() != null) {
                firstBeat = firstBeat.getNextBeat();
                while (!pitchBeats.contains(firstBeat)) {
                    groupSpan.add(firstBeat);
                    if (firstBeat.getNextBeat() == null) {
                        break;
                    } else {
                        firstBeat = firstBeat.getNextBeat();
                    }
                }
            }

            BaseGroup bGroup = new BaseGroup(groupSpan);
            baseGroupsList.add(bGroup);

        }

        return baseGroupsList;
    }

    private List<List<Beat>> allocateBaseGroupBeats(MetricalContainer mContainer, List<Beat> pitchBeats) {
        List<List<Beat>> groupSpan = null;
        Random rand = new Random();

        Collections.sort(pitchBeats, pitchBeats.get(0));

        boolean wellformed = false;

        while (!wellformed) {
            groupSpan = new ArrayList<>();
            List<Beat> groupBeats = new ArrayList<>();
            int count = 0;

            for (Beat b : pitchBeats) {
                groupBeats.add(b);
                count++;

                if (count >= 3 && count < 5) {
                    if (rand.nextBoolean()) {
                        count = 0;
                        groupSpan.add(groupBeats);
                        groupBeats = new ArrayList<>();

                    }

                } else {
                    if (count > 5) {
                        count = 0;
                        groupSpan.add(groupBeats);
                        groupBeats = new ArrayList<>();
                    }
                }
            }

            if (!groupBeats.isEmpty()) {
                groupSpan.add(groupBeats);
            }

            wellformed = true;

            for (List<Beat> gI : groupSpan) {
                if (gI.size() < 3) {
                    wellformed = false;
                }

            }

        }

        return groupSpan;

    }

    /**
     * Allocates a list of beats as pitch beats based on metrical structure
     * strength.
     *
     * @param mContainer the metrical structure
     * @return a list of beats allocated as pitch beats
     */
    private List<Beat> allocatePitchBeats(MetricalContainer mContainer) {
        List<Beat> pitchBeats = new ArrayList<>();

        int noPitches = determineNumberPitchBeats(mContainer);

        List<Beat> seenBeats = new ArrayList<>();

        while (pitchBeats.size() != noPitches) {

            List<Beat> beatList = new ArrayList<>();

            for (Beat b : mContainer.getMetricalBeatsList()) {
                beatList.add(b);
            }
            Collections.shuffle(beatList);

            for (Beat beat : beatList) {

                if (pitchBeats.contains(beat)) {
                    continue;
                }

                if (assessPitchCandidacy(beat, mContainer.findHighestMetricalStrength())) {
                    pitchBeats.add(beat);

                    if (pitchBeats.size() == noPitches) {
                        break;
                    }
                }

            }
        }

        return pitchBeats;

    }

    /**
     * Randomly determines how many pitch beats should be allocated in the
     * grammar
     *
     * @param mContainer the metrical structure
     * @return a number of pitch beats between 7 and 30
     */
    private int determineNumberPitchBeats(MetricalContainer mContainer) {

        int noPitchBeats = 0;
        int containerSize = (mContainer.getMetricalBeatsList().size() / 2);
        containerSize = (containerSize) + rand.nextInt(containerSize);

        while (noPitchBeats < 7 || noPitchBeats > containerSize) {

            int highestStrength = mContainer.findHighestMetricalStrength();

            int noBeats = mContainer.getMetricalBeatsList().size();
            int halfSize = noBeats;

            noPitchBeats = noBeats - rand.nextInt(halfSize);
        }
        if (noPitchBeats > 10) {
            return determineNumberPitchBeats(mContainer);
        } else {
            return noPitchBeats;
        }
    }

    /*
     Assesses the relative candidacy of the beat as being one on which a pitch falls on.
     Returns true if the beat should be considered a pitch beat, and false if not. This
     is determined according to various constraints, but there is a degree of randomness
     as to how far the constraints will be followed.
    
     Takes into account the following constraints:
     1. A pitch is more likely to fall on a strong beat. MPR5a
     */
    private boolean assessPitchCandidacy(Beat beat, int highestStrength) {
        //the higher the rating, the more likely it is to be a candidate

        int score = highestStrength + 1;
        int value = score - beat.getBeatStrength();

        score = rand.nextInt(value);

        if (score == 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Constructs a randomly generated sequence of beats and adds them to a
     * metrical container.
     *
     * @return a metrical container of randomly generated length
     */
    private MetricalContainer constructMetricalContainer() {
        //   int metricalDistance = rand.nextInt(8);
        //    metricalDistance += 8;

        int metricalDistance = 4;

        int metricalStrength = 4; // rand.nextInt(0);
        //metricalStrength += 3;

        List<Beat> beats
                = GrammarUtils.getInstance()
                .constructBeatSpan(metricalDistance, metricalStrength);

        MetricalContainer m = GrammarUtils.getInstance().createMetricalContainer(beats);
   

        return m;
    }

}
