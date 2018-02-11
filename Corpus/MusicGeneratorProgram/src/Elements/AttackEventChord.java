/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Elements;

/**
 * A class extending the AttackEvent class to represent the instantiation of
 * multiple pitches
 *
 * @author Alexander Dodd
 */
public class AttackEventChord extends AttackEvent {

    private Chord chrd;

    /**
     * 
     * @param chrd the Chord to associate with the Event
     * @param length the durational length of the Event
     */
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
