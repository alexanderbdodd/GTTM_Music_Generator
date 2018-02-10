
package uk.ac.kent.computing.gttm.Player;

import javax.sound.midi.Instrument;
import jm.music.data.*;
import jm.util.Play;
import jm.util.Write;
import uk.ac.kent.computing.gttm.Elements.AttackEvent;
import uk.ac.kent.computing.gttm.Elements.AttackEventChord;
import uk.ac.kent.computing.gttm.Elements.Chord;
import uk.ac.kent.computing.gttm.Elements.DurationEnum;
import uk.ac.kent.computing.gttm.Elements.Event;
import uk.ac.kent.computing.gttm.Elements.EventStream;
import uk.ac.kent.computing.gttm.Elements.Key;
import uk.ac.kent.computing.gttm.Elements.RestEvent;
import jm.JMC;

/**Used to create audible representations of EventStream objects
 * in the form of .midi format.
 *
 * @author Alexander Dodd
 */
public class Player implements JMC {

    /**
     * Plays a single AttackEvent object
     * @param ev the AttackEvent object to play
     */
    public static void playAttackEvent(AttackEvent ev) {

        if (ev.getClass() == AttackEvent.class) {
            Note note;
            note = JMusicAdaptor.getInstance().convertAttackEvent(ev);

            Play.midi(note);
        } else {
            AttackEventChord chrd = (AttackEventChord) ev;
            playChord(chrd.getChord(), chrd.getDurationEnum());
        }

    }

    /**
     * Plays a Key object for a given Duration
     * @param key the pitch to play
     * @param duration the duration of the pitch
     */
    public static void playKey(Key key, DurationEnum duration) {
        AttackEvent ev = new AttackEvent(key.getNote(), key.getPosition(), duration);
        playAttackEvent(ev);
    }
    
    /**Plays a Chord object for a given duration of time.
     * 
     * @param chrd the Chord to play
     * @param duration the duration to play the Chord for
     */
    public static void playChord(Chord chrd, DurationEnum duration) {
        CPhrase phr = new CPhrase();

        phr.addChord(JMusicAdaptor.getInstance().constructChordArray(chrd, duration));

        Part part = new Part();
        part.addCPhrase(phr);

        Play.midi(part);

    }

    /**Plays an EventStream object sequentially.
     * 
     * @param stream the EventStream to play
     */
    public static void playEventStream(EventStream stream) {
        CPhrase phr = new CPhrase();

        for (Event ev : stream.getEventList()) {
            if (ev.getClass() == AttackEvent.class) {
                Note[] n = new Note[1];
                n[0] = JMusicAdaptor.getInstance().convertAttackEvent((AttackEvent) ev);
                phr.addChord(n);
            }
            if (ev.getClass() == AttackEventChord.class) {
                AttackEventChord chrd = (AttackEventChord) ev;
                phr.addChord(JMusicAdaptor.getInstance().constructChordArray(chrd.getChord(), DurationEnum.EIGHTH));
            }
            if (ev.getClass() == RestEvent.class) {
                Note[] n = new Note[1];
                n[0] = JMusicAdaptor.getInstance().convertRestEvent((RestEvent) ev);
                phr.addChord(n);
            }
        }

        Score score = new Score(new Part(phr));

        Play.midi(score);
    }

    /**
     * Writes out an EventStream as a midi file.
     * @param stream the EventStream object to write as a midi file
     * @param name the filename to give the midi file
     */
    public static void writeEventStream(EventStream stream, String name) {
        CPhrase phr = new CPhrase();

        for (Event ev : stream.getEventList()) {
            if (ev.getClass() == AttackEvent.class) {
                Note[] n = new Note[1];
                n[0] = JMusicAdaptor.getInstance().convertAttackEvent((AttackEvent) ev);
                phr.addChord(n);
            }
            if (ev.getClass() == AttackEventChord.class) {
                AttackEventChord chrd = (AttackEventChord) ev;
                phr.addChord(JMusicAdaptor.getInstance().constructChordArray(chrd.getChord(), DurationEnum.EIGHTH));
            }
            if (ev.getClass() == RestEvent.class) {
                Note[] n = new Note[1];
                n[0] = JMusicAdaptor.getInstance().convertRestEvent((RestEvent) ev);
                phr.addChord(n);

            }
        }

        Score score = new Score(new Part(phr));

        name = name + ".midi";

        Write.midi(score, name);

    }
}
