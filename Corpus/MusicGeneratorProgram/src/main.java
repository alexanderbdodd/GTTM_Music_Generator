
import Elements.*;
import Phrase_Generator.PitchGenerator;
import Phrase_Generator.PhraseGenerator;
import GTTM_Analyser.*;
import GrammarIllustrator.IllustratorFrame;
import Grammar_Elements.GrammarContainer;
import Grammar_Generator.*;
import Logger.Logger;
import Player.Player;
import java.io.*;
import java.text.*;
import java.util.*;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *  
 * @author Alexander Dodd
 */
public class main {

    public static void main(String[] args) {

        //creates and plays a pair of musical fragments. One fragment
        //represents the highest rated solution to a grammar, the second
        //represents the lowest rated solution.
        generateMusicPairs();
        
        //generates a single musical phrase
        //generateMusicFragment();
          
       
       
    }      

    private static void generateMusicFragment() {
        EventStream stream = null;

        GrammarContainer grammar = null;

        while (stream == null) {
            
          
            System.out.println("Generating a Grammar Structure");

            grammar = PhraseGrammarConstructor.getInstance().constructPhraseGrammar();

            System.out.println("Metrical Structure Length: " + grammar.getMetricalStructure()
                    .getMetricalBeatsList().size() + " beats");
            System.out.println();
            int count = 0;

             
             System.out.println("Attempting to find music solution to grammar structure");
             System.out.println();
            while (stream == null && count < 20) {

                stream = PhraseGenerator.getInstance()
                        .constructMonophonicPhrase(grammar);

                count++;
            }

            if (stream == null) {
                System.out.println("No solution found for grammar");
                System.out.println("------------------");
            }
        }

        printGrammar(grammar);
        
        printRatings(grammar,stream,false);

        
        double rating = RatingsGenerator.getInstance().obtainRating(grammar, stream);
        Player.playEventStream(stream);
        Player.writeEventStream(stream, "Musical Fragments/" + rating);
        System.out.println();
        
        
        System.exit(0);
    }

    private static void generateMusicPairs() {
        int count = 0;

        Logger.getInstance().setWriteOutFileName("ratings.txt");

        //can be adjusted to produce multiple pairs. Set at 1 for demonstration purposes
        while (count < 1){
            Map<Double, EventStream> streamMap = null;

            GrammarContainer grammar = null;

            int no = 1;
            while (streamMap == null) {
                System.out.println("Generating a Grammar Structure");

                grammar = PhraseGrammarConstructor.getInstance().constructPhraseGrammar();

                System.out.println("Metrical Structure Length: " + grammar.getMetricalStructure()
                        .getMetricalBeatsList().size() + " beats");
                System.out.println();

                int i = 0;
                    
                
                System.out.println("Attempting to find music solution to grammar structure");
                System.out.println();
                
                while (i < 1 && streamMap == null) {
                    streamMap = PhraseGenerator.getInstance()
                            .constructMonophonicPhrasePairs(grammar);
                    i++;
                }

                if (streamMap == null) {
                    no++;
                    System.out.println("No solution found for grammar");
                    System.out.println("------------------");
                }

            }

            printGrammar(grammar);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();

            logAnalysisValues();

            for (Double r : streamMap.keySet()) {

                Logger.getInstance().updateLastMessage("Key: " + streamMap.get(r).getLocalScale().getTonic()
                        + " " + streamMap.get(r).getLocalScale().getScaleModeEnum() + '\n' + '\n' + '\n', true);

                EventStream stream = streamMap.get(r);

                Logger.getInstance().updateLastMessage("Grammar no " + no + '\n', true);

               
                Player.playEventStream(stream);
                //  stream = HomophonyConstructor.getInstance().constructHomophony(grammar, stream);
                //Player.playEventStream(stream);
                
                double rating = RatingsGenerator.getInstance().obtainRating(grammar, stream);
                printRatings(grammar,stream,true);

                String time = dateFormat.format(cal.getTime());
                time = time.replace("/", "-");
                time = time.replace(" ", "-");
                time = time.replace(":", "-");
                time = time.concat("-" + rating);

                Logger.getInstance().updateLastMessage("Saved as: " + time + ".midi" + '\n', true);

                Player.writeEventStream(stream, "Musical Fragments/" + time);

                Logger.getInstance().updateLastMessage("-----------------------------------" + '\n', true);

            }

            Logger.getInstance().updateLastMessage("-----------------------------------" + '\n' + '\n', true);
            count++;
            Logger.getInstance().updateLastMessage("-----------------------------------" + '\n' + '\n', true);

        }

        System.exit(0);
    }

    private static void printRatings(GrammarContainer grammar, EventStream stream, boolean log) {

        double rating = RatingsGenerator.getInstance().obtainRating(grammar, stream);
        Logger.getInstance().updateLastMessage("The rating for this piece is: " + rating, log);
        Logger.getInstance().updateLastMessage("Metrical Rating: " + RatingsGenerator.getInstance().obtainMetricalRating(grammar, stream), log);
        Logger.getInstance().updateLastMessage("Time Span Reduction Rating: " + RatingsGenerator.getInstance().obtainTimeSpanReductionRating(grammar, stream), log);
        Logger.getInstance().updateLastMessage("Prolongational Reduction Rating: " + RatingsGenerator.getInstance().obtainProlongationalReductionRating(grammar, stream), log);
        Logger.getInstance().updateLastMessage("Grouping Structure Rating: " + RatingsGenerator.getInstance().obtainGroupingStructureRating(grammar, stream), log);
        Logger.getInstance().updateLastMessage("Cadence Rating: " + RatingsGenerator.getInstance().obtainCadenceRating(grammar, stream), log);

    }

    private static void printGrammar(GrammarContainer grammar) {

        IllustratorFrame ex = new IllustratorFrame(grammar);
        ex.setVisible(true);

    }

    private static GrammarContainer getSerialisedGrammar(String location) {
        try {
            FileInputStream fileIn = new FileInputStream(location);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            GrammarContainer grammar = (GrammarContainer) in.readObject();
            in.close();
            fileIn.close();

            return grammar;
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {

        }

        return null;
    }

    private static void logAnalysisValues() {
        Logger.getInstance().updateLastMessage("Prolongational Strictness: "
                + PitchGenerator.CHECK_PROLONGATIONAL_REDUCTION, true);
        Logger.getInstance().updateLastMessage("Time-span reduction strictness: "
                + PitchGenerator.CHECK_TIMESPAN_REDUCTION, true);
        Logger.getInstance().updateLastMessage("Prolongational branching strictness: "
                + PitchGenerator.CHECK_PROLONGATIONAL_BRANCHING, true);
        Logger.getInstance().updateLastMessage("Recursion limit: "
                + PitchGenerator.RECURSION_RESTRICTION, true);
        Logger.getInstance().updateLastMessage("Time span reduction checking: "
                + PitchGenerator.CHECK_TIMESPAN_REDUCTION, true);
        Logger.getInstance().updateLastMessage("Cadence checking: "
                + PitchGenerator.CHECK_CADENCE_RELATION, true);

        Logger.getInstance().updateLastMessage("\n", true);

        Logger.getInstance().updateLastMessage("Prolongational analysis values: \n", true);
        Logger.getInstance().updateLastMessage("Branching Stability Weight: "
                + ProlongationalReductionAnalyser.BRANCHING_STABILITY_WEIGHT, true);
        Logger.getInstance().updateLastMessage("Harmonic Stability A Weight: "
                + ProlongationalReductionAnalyser.HARMONIC_STABILITY_A_WEIGHT, true);
        Logger.getInstance().updateLastMessage("Harmonic Stability B Weight: "
                + ProlongationalReductionAnalyser.HARMONIC_STABILITY_B_WEIGHT, true);
        Logger.getInstance().updateLastMessage("Melodic Stability A Weight: "
                + ProlongationalReductionAnalyser.MELODIC_STABILITY_A_WEIGHT, true);
        Logger.getInstance().updateLastMessage("Melodic Stability B Weight: "
                + ProlongationalReductionAnalyser.MELODIC_STABILITY_B_WEIGHT, true);
        Logger.getInstance().updateLastMessage("Pitch Stability Weight: "
                + ProlongationalReductionAnalyser.PITCH_STABILITY_WEIGHT + "\n \n", true);

        Logger.getInstance().updateLastMessage("Time-Span Reduction analysis values: \n", true);
        Logger.getInstance().updateLastMessage("TSPR2 Weight: "
                + TimeSpanReductionAnalyser.TSPR2_WEIGHT, true);
        Logger.getInstance().updateLastMessage("TSPR3 Weight: "
                + TimeSpanReductionAnalyser.TSPR3_WEIGHT, true);
        Logger.getInstance().updateLastMessage("Consonance Weight: "
                + TimeSpanReductionAnalyser.CONSONANCE_WEIGHT, true);
        Logger.getInstance().updateLastMessage("Tonic Relation Weight: "
                + TimeSpanReductionAnalyser.TONIC_RELATION_WEIGHT + "\n \n", true);

        Logger.getInstance().updateLastMessage("Grouping Structure Analysis Values: \n", true);
        Logger.getInstance().updateLastMessage("GPR2A Weight: "
                + GroupingStructureAnalyser.GPR2A_WEIGHT, true);
        Logger.getInstance().updateLastMessage("GPR2B Weight: "
                + GroupingStructureAnalyser.GPR2B_WEIGHT, true);
        Logger.getInstance().updateLastMessage("GPR2 Weight: "
                + GroupingStructureAnalyser.GPR2_WEIGHT, true);
        Logger.getInstance().updateLastMessage("GPR3A Weight: "
                + GroupingStructureAnalyser.GPR3A_WEIGHT, true);
        Logger.getInstance().updateLastMessage("GPR3B Weight: "
                + GroupingStructureAnalyser.GPR3B_WEIGHT, true);
        Logger.getInstance().updateLastMessage("GPR3C Weight: "
                + GroupingStructureAnalyser.GPR3C_WEIGHT, true);
        Logger.getInstance().updateLastMessage("GPR3D Weight: "
                + GroupingStructureAnalyser.GPR3D_WEIGHT, true);
        Logger.getInstance().updateLastMessage("GPR3 Weight: "
                + GroupingStructureAnalyser.GPR3_WEIGHT + "\n \n", true);

    }

}
