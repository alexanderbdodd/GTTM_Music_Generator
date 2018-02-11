package Player;

import Elements.*;
import java.util.ArrayList;
import java.util.List;
import jm.music.data.*;
import jm.util.*;
import jm.JMC;


/**
 * Used to create audible representations of EventStream objects in the form of
 * .midi format.
 *
 * @author Alexander Dodd
 */
public class Player implements JMC {

    /**
     * Plays a single AttackEvent object
     *
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
     *
     * @param key the pitch to play
     * @param duration the duration of the pitch
     */
    public static void playKey(Key key, DurationEnum duration) {
        AttackEvent ev = new AttackEvent(key.getKeyLetterEnum(), key.getPosition(), duration);
        playAttackEvent(ev);
    }

    /**
     * Plays a Chord object for a given duration of time.
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

    /**
     * Plays an EventStream object sequentially.
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
                phr.addChord(JMusicAdaptor.getInstance().constructChordArray(chrd.getChord(), DurationEnum.TWO_BEATS));
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
     *
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
                phr.addChord(JMusicAdaptor.getInstance().constructChordArray(chrd.getChord(), DurationEnum.TWO_BEATS));
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

    /**
     * Converts a .midi file into an EventStream object
     * @param location the location of the .midi file
     * @param localScale the scale to associate with the EventStream
     * @return the EventStream matching the .midi file.
     */
    public static EventStream convertMidiToEventStream(String location, Scale localScale) {

        Score score = new Score();
        Read.midi(score, location);

        return convertScoreToEventStream(score, localScale);
    }

    /**
     * Converts a JMusic Score object into an EventStream object.
     * @param score the Score object to convert
     * @param localScale the Scale object to associate with the EventStream
     * @return the EventStream matching the score.
     */
    public static EventStream convertScoreToEventStream(Score score, Scale localScale) {
        List<Event> eventStream = new ArrayList<>();

        for (Part part : score.getPartArray()) {
            for (Phrase phr : part.getPhraseArray()) {
                for (Note nt : phr.getNoteArray()) {
                    createEvent(nt, eventStream);
                }
            }
        }

        return new EventStream(eventStream, localScale);
    }

    private static void createEvent(Note nt, List<Event> eventStream) {
        if (nt.getPitch() > 0) {
           AttackEvent ev = (AttackEvent) JMusicAdaptor.getInstance().convertNoteToEvent(nt);
           
           double value = nt.getDuration() / 0.255;
           
             int count = 0;
            while (count <= value) {
                count++;
            }
            count -= 1;
            DurationEnum dur;
            if(count < DurationEnum.values().length){
            dur = DurationEnum.values()[count];
            }
            else{
                dur =DurationEnum.FOUR_BEATS;
            }
           eventStream.add(new AttackEvent(ev, dur));
            
           
        } else {
            double value = nt.getDuration() / 0.25;
            value += 0.05;

            int count = 0;
            while (count <= value) {
                count++;
            }
            count -= 2;

            int i = 0;
            while (i <= count) {
                eventStream.add(new RestEvent(DurationEnum.ONE_BEAT));
                i++;
            }

        }
    }

}
