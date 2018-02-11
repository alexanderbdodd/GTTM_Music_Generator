/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Phrase_Generator;

import Elements.*;
import GTTM_Analyser.RatingsGenerator;
import Grammar_Elements.GrammarContainer;
import Grammar_Elements.MetricalStructure.Beat;
import Grammar_Elements.ReductionBranches.*;
import Manipulators.*;
import java.util.*;

/**The Homophony class is used to construct homophonic solutions that fit
 * a given grammar structure and a given EventStream.
 *
 * @author Alexander Dodd
 */
public class HomophonyConstructor extends Generator {

    private static HomophonyConstructor instance = null;

    /**
     * 
     * @return the singleton instance of the class
     */
    public static HomophonyConstructor getInstance() {
        if (instance == null) {
            instance = new HomophonyConstructor();
        }

        return instance;
    }

    private HomophonyConstructor() {

    }

    /**Constructs homophony based on the GrammarContainer structure and the 
     * given EventStream object
     * 
     * @param container the grammar structure of the piece
     * @param monoStream the monophonic version of the piece
     * @return the constructed homophonic variation
     */
    public EventStream constructHomophony(GrammarContainer container, 
           EventStream monoStream) {
        assignFieldVariables(container);
        List<EventStream> candidates = new ArrayList<>();

       
            Map<Beat, Event> eventMap = RatingsGenerator.getInstance().createEventMap(container, monoStream);
            
           EventStream homoStream = homophonise(eventMap, monoStream.getLocalScale());
            
        

        return homoStream;
    }

    private EventStream homophonise(Map<Beat, Event> stream, Scale localScale) {

        //go through all the branches of the prolongational reducation and produce 
        //homophony based on whether it's right branching or left branching
       
        List<KeyLetterEnum> scaleCollection = new ArrayList<>();
        
        CadencedTimeSpanReductionBranch cbr = null;
        
        List<Branch> branches = getTopTBranch().getAllSubBranches();
        
        branches.add(0, getTopTBranch());
        
        for(Branch br : branches)
        {
            if(br.getClass() == CadencedTimeSpanReductionBranch.class)
            {
                cbr = (CadencedTimeSpanReductionBranch) br;
                break;
            }
        }
        
        if(cbr != null)
        {
            Beat startBeat = cbr.getCadenceStart();
            Beat endBeat = cbr.getCadenceEnd();
            
            AttackEvent startEv = (AttackEvent) stream.get(startBeat);
            AttackEvent endEv = (AttackEvent) stream.get(endBeat);
            
            Chord chrd = new Chord();
            chrd.addNote(startEv);
            
            Key chrdKey = IntervalConstructor.getInstance().getKeyAtIntervalDistance(startEv, IntervalEnum.PERFECT5TH, true);
            chrd.addNote(chrdKey);
            chrdKey.setPosition(KeyPositionEnum.getLastPosition(chrdKey.getPosition()));
            
            stream.put(startBeat, new AttackEventChord(chrd, startEv.getDurationEnum()));
            
            chrd = new Chord();
            chrd.addNote(endEv);
            
            chrdKey = IntervalConstructor.getInstance().getKeyAtIntervalDistance(endEv, IntervalEnum.PERFECT5TH, true);
            chrd.addNote(chrdKey);
            chrdKey.setPosition(KeyPositionEnum.getLastPosition(chrdKey.getPosition()));
            
            stream.put(endBeat, new AttackEventChord(chrd, endEv.getDurationEnum()));
                   
        }
        
       for(Beat b : getmContainer().getMetricalBeatsList())
        {
            if(stream.get(b) != null && stream.get(b).getClass() == AttackEvent.class)
            {
                stream.put(b, randomlyGenerateChord((AttackEvent) stream.get(b)));
            }
        }
        
        List<Event> streamList = new ArrayList<>();
        
        for(Beat b : getmContainer().getMetricalBeatsList())
        {
            if(stream.get(b) != null)
            streamList.add(stream.get(b));
        }
        
        
        return new EventStream(streamList, localScale);
        
        
    }
    
    private AttackEvent randomlyGenerateChord(AttackEvent ev)
    {
        Random rand = new Random();
        if(rand.nextBoolean())
        {
            return ev;
        }
        else{
            
            Chord chrd = new Chord();
            chrd.addNote(ev);
            
            int i = rand.nextInt(2);
            IntervalEnum interval;
            
            switch(i)
            {
                case 0: interval = IntervalEnum.MINOR3RD;
                    break;
                case 1: interval = IntervalEnum.MINOR6TH;
                    break;
                case 2: interval = IntervalEnum.PERFECT4TH;
                    break;
                default: interval = IntervalEnum.PERFECT5TH;
            }
            
            Key newKey = IntervalConstructor.getInstance().getKeyAtIntervalDistance(ev, interval, true);
            
            if(newKey.getPosition() != KeyPositionEnum.FIRST){
            newKey.setPosition(KeyPositionEnum.getLastPosition(newKey.getPosition()));
            }
            
            chrd.addNote(newKey);
            
            return new AttackEventChord(chrd, ev.getDurationEnum());
        }
    }
}


     
    

