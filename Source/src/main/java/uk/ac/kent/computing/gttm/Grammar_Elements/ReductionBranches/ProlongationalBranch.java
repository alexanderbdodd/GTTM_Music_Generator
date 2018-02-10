/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import uk.ac.kent.computing.gttm.GTTM_Analyser.ProlongationalReductionAnalyser;
import uk.ac.kent.computing.gttm.Grammar_Elements.ExceptionClasses.BranchingWellFormednessException;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;

/**
 * Describes a branch of the prolongational reduction hierarchy.
 *
 * @author Alexander Dodd
 */
public class ProlongationalBranch extends Branch implements Serializable {

	private final static Set<Beat> beats = new HashSet<>();

	private ProlongationalTypeEnum type = null;

	/**
	 * Sets up a branch of the prolongational reduction analysis.
	 *
	 * @param pitchEvent
	 *            The associated pitch event
	 * @param level
	 *            the level of this branch. A value of 0 indicates that this is a
	 *            top level branch.
	 * @param type
	 *            the prolongational branching type.
	 * @throws BranchingWellFormednessException
	 *             thrown if a branching wellformedness constraint is violated.
	 */
	public ProlongationalBranch(Beat pitchEvent, int level, ProlongationalTypeEnum type)
			throws BranchingWellFormednessException {
		super(pitchEvent, level);

		if (beats.contains(pitchEvent)) {
			throw new BranchingWellFormednessException(
					"Attempted to try to associated " + "two branches with one pitch event.");
		} else {
			beats.add(pitchEvent);
		}

		this.type = type;
	}

	public ProlongationalBranch(Beat pitchEvent, int level) throws BranchingWellFormednessException {
		super(pitchEvent, level);

		if (beats.contains(pitchEvent)) {
			throw new BranchingWellFormednessException(
					"Attempted to try to associated " + "two branches with one pitch event.");
		} else {
			beats.add(pitchEvent);
		}

	}

	/**
	 *
	 * @return the prolongational branching type.
	 */
	public ProlongationalTypeEnum getProlongationType() {
		return type;
	}

	@Override
	public void addChildBranch(Branch branch) throws BranchingWellFormednessException {
		if (branch.getClass() == ProlongationalBranch.class) {
			super.addChildBranch(branch);
		} else {
			throw new BranchingWellFormednessException(
					"Error attempting to add a " + "non prolongational reduction branch");
		}
	}

	@Override
	protected void setParent(Branch parent) {
		super.setParent(parent); // To change body of generated methods, choose Tools | Templates.
		if (getLevel() != 0 && type == null) {
			type = ProlongationalReductionAnalyser.getInstance().assessProlongationalType(
					this.getParent().getAssociatedBeat().getAttackEvent(), this.getAssociatedBeat().getAttackEvent());
		}

	}

	/**
	 * 
	 * @param type
	 *            the prolongational type of this branch in relation to its parent
	 */
	public void setProlongationType(ProlongationalTypeEnum type) {
		this.type = type;
	}

}
