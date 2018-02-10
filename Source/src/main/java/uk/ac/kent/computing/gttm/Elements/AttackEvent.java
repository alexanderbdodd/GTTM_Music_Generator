/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Elements;

/**
 * A class used to describe instantiations of a pitch event
 *
 * @author Alexander Dodd
 */
public class AttackEvent extends Key implements Event {

    private DurationEnum duration;
    private DynamicsEnum dynamicValue;
    private ArticulationEnum articulationValue = ArticulationEnum.NONE;

    /**
     * Creates an AttackEvent object.
     *
     * @param letter the key of the pitch
     * @param position the position of the pitch
     * @param length the duration of the pitch
     */
    public AttackEvent(KeyLetterEnum letter, KeyPositionEnum position, DurationEnum length) {

        super(letter, position);
        duration = length;
        dynamicValue = DynamicsEnum.MP;

    }

    /**
     * Create an AttackEvent from a Key object
     *
     * @param key the pitch of the AttackEvent
     * @param length the duration of the AttackEvent
     */
    public AttackEvent(Key key, DurationEnum length) {
        super(key);
        duration = length;
        dynamicValue = DynamicsEnum.MP;
    }

    /**
     * Creates an AttackEvent with a dynamic
     *
     * @param letter the key of the pitch
     * @param position the position of the pitch
     * @param length the duration of the pitch
     * @param dynamic the dynamic value to associate with the event
     */
    public AttackEvent(KeyLetterEnum letter, KeyPositionEnum position, DurationEnum length, DynamicsEnum dynamic) {

        super(letter, position);
        duration = length;
        dynamicValue = dynamic;

    }

    /**
     * Create an AttackEvent with a dynamic value.
     *
     * @param key the pitch of the AttackEvent
     * @param length the duration of the AttackEvent
     * @param dynamic the dynamic value to associate with the event
     */
    public AttackEvent(Key key, DurationEnum length, DynamicsEnum dynamic) {
        super(key);
        duration = length;
        dynamicValue = dynamic;
    }

    /**
     * 
     * @return the DurationEnum associated with this AttackEvent 
     */
    public DurationEnum getDurationEnum() {
        return duration;
    }

    /**
     * 
     * @return the DynamicsEnum associated with this AttackEvent 
     */
    public DynamicsEnum getDynamic() {
        return dynamicValue;
    }

    /**
     * Set a new dynamic value for this AttackEvent
     * @param dynamic the dynamic value to give the AttackEvent
     */
    public void setDynamic(DynamicsEnum dynamic) {
        dynamicValue = dynamic;
    }

    /**
     * 
     * @return the ArticulationEnum associated with this AttackEvent
     */
    public ArticulationEnum getArticulationValue() {
        return articulationValue;
    }

    /**
     * Set an articulation value to associate with this AttackEvent
     * 
     * @param articulationValue the ArticulationEnum to associate with this AttackEvent 
     */
    public void setArticulationValue(ArticulationEnum articulationValue) {
        this.articulationValue = articulationValue;
    }

}
