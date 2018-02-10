/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.Elements;

/**
 * A class extending the AttackEvent class to represent the instantiation of
 * multiple pitches
 *
 * @author Alexander Dodd
 */
public class AttackEventChord extends AttackEvent {

    private Chord chrd;

    public AttackEventChord(Chord chrd, DurationEnum length) {
        super(chrd.getKeys().get(0), length);

        this.chrd = chrd;
    }

    /**
     *
     * @return the pitches associated with this AttackEvent
     */
    public Chord getChord() {
        return chrd;
    }
}
