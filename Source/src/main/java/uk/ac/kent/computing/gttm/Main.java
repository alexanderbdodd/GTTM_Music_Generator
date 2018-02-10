package uk.ac.kent.computing.gttm;

import java.io.*;
import java.text.*;
import java.util.*;

import uk.ac.kent.computing.gttm.Elements.DurationEnum;
import uk.ac.kent.computing.gttm.Elements.EventStream;
import uk.ac.kent.computing.gttm.Elements.RestEvent;
import uk.ac.kent.computing.gttm.GTTM_Analyser.GroupingStructureAnalyser;
import uk.ac.kent.computing.gttm.GTTM_Analyser.ProlongationalReductionAnalyser;
import uk.ac.kent.computing.gttm.GTTM_Analyser.RatingsGenerator;
import uk.ac.kent.computing.gttm.GTTM_Analyser.TimeSpanReductionAnalyser;
import uk.ac.kent.computing.gttm.GrammarIllustrator.IllustratorFrame;
import uk.ac.kent.computing.gttm.Grammar_Elements.GrammarContainer;
import uk.ac.kent.computing.gttm.Grammar_Generator.PhraseGrammarConstructor;
import uk.ac.kent.computing.gttm.Logger.Logger;
import uk.ac.kent.computing.gttm.Music_Generator.PhraseGenerator.PhraseGenerator;
import uk.ac.kent.computing.gttm.Music_Generator.PhraseGenerator.PitchGenerator;
import uk.ac.kent.computing.gttm.Player.Player;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Alexander Dodd
 */
public class Main {

	public static void main(String[] args) {

		// printGrammar();
		// generateMusicPairs();
		while (true) {
			generateMusicFragment();
		}

	}

	private static void generateMusicFragment() {
		EventStream stream = null;

		GrammarContainer grammar = null;

		int no = 1;
		while (stream == null) {

            System.out.println("** GENERATING NEW GRAMMAR **");

			grammar = PhraseGrammarConstructor.getInstance().constructPhraseGrammar();

			System.out.println();

			int i = 0;

			stream = PhraseGenerator.getInstance().constructMonophonicPhrase(grammar);

		}

		IllustratorFrame ex = new IllustratorFrame(grammar);
		ex.setVisible(true);

		System.out.println();
        System.out.println();
		Player.playEventStream(stream);

        System.out.println();

	}

	private static void generateMusicPairs() {
		int count = 0;

		Logger.getInstance().setWriteOutFileName("ratings.txt");

		while (count < 10) {
			Map<Double, EventStream> streamMap = null;

			GrammarContainer grammar = null;

			int no = 1;
			while (streamMap == null) {

				grammar = PhraseGrammarConstructor.getInstance().constructPhraseGrammar();

				System.out.println();

				IllustratorFrame ex = new IllustratorFrame(grammar);
				ex.setVisible(true);

				int i = 0;

				while (i < 10 && streamMap == null) {
					streamMap = PhraseGenerator.getInstance().constructMonophonicPhrasePairs(grammar);
					i++;
				}
				// System.out.println();
				// System.out.println("Generating music from the grammar");
				if (streamMap == null) {
					// System.out.println();
					// System.out.println("Failed to find a solution to the generated grammar.");
					// System.out.println();
					no++;
				}

			}

			IllustratorFrame ex = new IllustratorFrame(grammar);
			ex.setVisible(true);

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();

			logAnalysisValues();

			for (Double r : streamMap.keySet()) {

				Logger.getInstance().updateLastMessage("Key: " + streamMap.get(r).getLocalScale().getTonic() + " "
						+ streamMap.get(r).getLocalScale().getScaleModeEnum() + '\n' + '\n' + '\n', true);

				EventStream stream = streamMap.get(r);

				double rating = RatingsGenerator.getInstance().obtainRating(grammar, stream);

				Logger.getInstance().updateLastMessage("The rating for this piece is: " + rating, true);
				Logger.getInstance().updateLastMessage(
						"Metrical Rating: " + RatingsGenerator.getInstance().obtainMetricalRating(grammar, stream),
						true);
				Logger.getInstance().updateLastMessage("Time Span Reduction Rating: "
						+ RatingsGenerator.getInstance().obtainTimeSpanReductionRating(grammar, stream), true);
				Logger.getInstance()
						.updateLastMessage("Prolongational Reduction Rating: "
								+ RatingsGenerator.getInstance().obtainProlongationalReductionRating(grammar, stream),
								true);
				Logger.getInstance().updateLastMessage("Grouping Structure Rating: "
						+ RatingsGenerator.getInstance().obtainGroupingStructureRating(grammar, stream), true);
				Logger.getInstance().updateLastMessage(
						"Cadence Rating: " + RatingsGenerator.getInstance().obtainCadenceRating(grammar, stream), true);

				Logger.getInstance().updateLastMessage("Grammar no " + no + '\n', true);

				// adding a rest at the end of the piece for midi quality purposes
				stream.getEventList().add(new RestEvent(DurationEnum.WHOLE));

				Player.playEventStream(stream);
				// stream = HomophonyConstructor.getInstance().constructHomophony(grammar,
				// stream);
				// Player.playEventStream(stream);

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

			try {
				String time = dateFormat.format(cal.getTime());
				time = time.replace("/", "-");
				time = time.replace(" ", "-");
				time = time.replace(":", "-");
				String fileName = "Grammars/grammar-" + time + ".ser";

				FileOutputStream fileOut = new FileOutputStream(fileName);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(grammar);
				out.close();
				fileOut.close();

				Logger.getInstance().updateLastMessage("Grammar saved as: " + fileName, true);

			} catch (IOException i) {
				i.printStackTrace();
			}

		}
	}

	private static void printGrammar() {
		try {
			FileInputStream fileIn = new FileInputStream("Grammars/grammar-2015-08-03-02-27-44.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			GrammarContainer grammar = (GrammarContainer) in.readObject();
			in.close();
			fileIn.close();

			IllustratorFrame ex = new IllustratorFrame(grammar);
			ex.setVisible(true);
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {

		}
	}

	private static void logAnalysisValues() {
		Logger.getInstance()
				.updateLastMessage("Prolongational Strictness: " + PitchGenerator.CHECK_PROLONGATIONAL_REDUCTION, true);
		Logger.getInstance()
				.updateLastMessage("Time-span reduction strictness: " + PitchGenerator.CHECK_TIMESPAN_REDUCTION, true);
		Logger.getInstance().updateLastMessage(
				"Prolongational branching strictness: " + PitchGenerator.CHECK_PROLONGATIONAL_BRANCHING, true);
		Logger.getInstance().updateLastMessage("Recursion limit: " + PitchGenerator.RECURSION_RESTRICTION, true);
		Logger.getInstance()
				.updateLastMessage("Time span reduction checking: " + PitchGenerator.CHECK_TIMESPAN_REDUCTION, true);
		Logger.getInstance().updateLastMessage("Cadence checking: " + PitchGenerator.CHECK_CADENCE_RELATION, true);

		Logger.getInstance().updateLastMessage("\n", true);

		Logger.getInstance().updateLastMessage("Prolongational analysis values: \n", true);
		Logger.getInstance().updateLastMessage(
				"Branching Stability Weight: " + ProlongationalReductionAnalyser.BRANCHING_STABILITY_WEIGHT, true);
		Logger.getInstance().updateLastMessage(
				"Harmonic Stability A Weight: " + ProlongationalReductionAnalyser.HARMONIC_STABILITY_A_WEIGHT, true);
		Logger.getInstance().updateLastMessage(
				"Harmonic Stability B Weight: " + ProlongationalReductionAnalyser.HARMONIC_STABILITY_B_WEIGHT, true);
		Logger.getInstance().updateLastMessage(
				"Melodic Stability A Weight: " + ProlongationalReductionAnalyser.MELODIC_STABILITY_A_WEIGHT, true);
		Logger.getInstance().updateLastMessage(
				"Melodic Stability B Weight: " + ProlongationalReductionAnalyser.MELODIC_STABILITY_B_WEIGHT, true);
		Logger.getInstance().updateLastMessage(
				"Pitch Stability Weight: " + ProlongationalReductionAnalyser.PITCH_STABILITY_WEIGHT + "\n \n", true);

		Logger.getInstance().updateLastMessage("Time-Span Reduction analysis values: \n", true);
		Logger.getInstance().updateLastMessage("TSPR2 Weight: " + TimeSpanReductionAnalyser.TSPR2_WEIGHT, true);
		Logger.getInstance().updateLastMessage("TSPR3 Weight: " + TimeSpanReductionAnalyser.TSPR3_WEIGHT, true);
		Logger.getInstance().updateLastMessage("Consonance Weight: " + TimeSpanReductionAnalyser.CONSONANCE_WEIGHT,
				true);
		Logger.getInstance().updateLastMessage(
				"Tonic Relation Weight: " + TimeSpanReductionAnalyser.TONIC_RELATION_WEIGHT + "\n \n", true);

		Logger.getInstance().updateLastMessage("Grouping Structure Analysis Values: \n", true);
		Logger.getInstance().updateLastMessage("GPR2A Weight: " + GroupingStructureAnalyser.GPR2A_WEIGHT, true);
		Logger.getInstance().updateLastMessage("GPR2B Weight: " + GroupingStructureAnalyser.GPR2B_WEIGHT, true);
		Logger.getInstance().updateLastMessage("GPR2 Weight: " + GroupingStructureAnalyser.GPR2_WEIGHT, true);
		Logger.getInstance().updateLastMessage("GPR3A Weight: " + GroupingStructureAnalyser.GPR3A_WEIGHT, true);
		Logger.getInstance().updateLastMessage("GPR3B Weight: " + GroupingStructureAnalyser.GPR3B_WEIGHT, true);
		Logger.getInstance().updateLastMessage("GPR3C Weight: " + GroupingStructureAnalyser.GPR3C_WEIGHT, true);
		Logger.getInstance().updateLastMessage("GPR3D Weight: " + GroupingStructureAnalyser.GPR3D_WEIGHT, true);
		Logger.getInstance().updateLastMessage("GPR3 Weight: " + GroupingStructureAnalyser.GPR3_WEIGHT + "\n \n", true);

	}

}
