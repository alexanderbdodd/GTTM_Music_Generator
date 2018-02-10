/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.GrammarIllustrator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.*;

import uk.ac.kent.computing.gttm.Grammar_Elements.GrammarContainer;
import uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure.BaseGroup;
import uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure.Group;
import uk.ac.kent.computing.gttm.Grammar_Elements.GroupingStructure.HighLevelGroup;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;

/**
 *
 * @author Alexander Dodd
 */
public class GroupingStructureIllustrator {

    private static GroupingStructureIllustrator instance = null;

    public static GroupingStructureIllustrator getInstance() {
        if (instance == null) {
            instance = new GroupingStructureIllustrator();
        }
        return instance;
    }

    private GroupingStructureIllustrator() {

    }

    public void draw(Graphics g, GrammarContainer grammar, double mStartX, double mStartY) {

        int highestStrength = grammar.getMetricalStructure().findHighestMetricalStrength();

        int height = highestStrength * 10;
        height += 2;

        mStartY += height;

        Map<Integer, List<Group>> map = orderGroups(grammar.getTopLevelGroup());

        List<Integer> levels = new ArrayList<>();
        map.keySet().stream().forEach((level) -> {
            levels.add(level);
        });

        //Collections.sort(levels);
        Collections.reverse(levels);

        for (Integer level : levels) {
            drawGroupLevel(mStartX, mStartY, map.get(level), grammar, (Graphics2D) g);
            mStartY += 10;
            mStartY += 2;
        }

    }

    private void drawGroupLevel(double startX, double y, List<Group> groups, GrammarContainer grammar, Graphics2D g2d) {

       
            groups = orderGroupByMetricalStructure(groups);

            for (Group gr : groups) {
                Beat firstBeat = grammar.getMetricalStructure().getBeatAtPosition(0);
                double x = startX;

                while (firstBeat != gr.getMetricalBeatSpan().get(0)) {
                    x += MetricalIllustrator.METRICAL_EXPANSION_VALUE;
                    firstBeat = firstBeat.getNextBeat();
                }

                Line2D startLine = new Line2D.Double((x - 5), y, (x - 5), y + 10);
                g2d.draw(startLine);

                y += 10;

                double endX = x;

                x -= 5;

                firstBeat = gr.getMetricalBeatSpan().get(0);
                while (firstBeat != gr.getMetricalBeatSpan().get(gr.getMetricalBeatSpan().size() - 1)) {
                    endX += MetricalIllustrator.METRICAL_EXPANSION_VALUE;
                    firstBeat = firstBeat.getNextBeat();
                }

                endX += 5;

                Line2D acrossLine = new Line2D.Double(x, y, endX, y);

                g2d.draw(acrossLine);
                
                y -= 10;

                startLine = new Line2D.Double(endX, y + 10, endX, y);

                g2d.draw(startLine);
           
        }

    }

    private List<Group> orderGroupByMetricalStructure(List<Group> groups) {
        Map<Integer, Group> groupMap = new HashMap<>();
        List<Group> arrangedGroup = new ArrayList<>();

        for (Group gr : groups) {
            groupMap.put(gr.getMetricalBeatSpan().get(0).getPosition(), gr);
        }

        List<Integer> orders = new ArrayList<>();

        for (Integer level : groupMap.keySet()) {
            orders.add(level);
        }

        Collections.sort(orders);

        for (Integer order : orders) {
            arrangedGroup.add(groupMap.get(order));
        }

        return arrangedGroup;

    }

    private Map<Integer, List<Group>> orderGroups(HighLevelGroup gr) {
        Map<Integer, List<Group>> map = new HashMap<>();

        List<Group> groups = new ArrayList<>();
        groups.add(gr);
        map.put(0, groups);

        int level = 1;
        List<Group> baseGroups = new ArrayList<>();

        while (!groups.isEmpty()) {
            List<Group> lastGroups = groups;
            groups = new ArrayList<>();

            for (Group gr2 : lastGroups) {
                if (gr2.getClass() == BaseGroup.class && !baseGroups.contains(gr2)) {
                    baseGroups.add(gr2);
                } else {
                    HighLevelGroup hg = (HighLevelGroup) gr2;

                    for (Group bgroup : hg.getSubGroups()) {
                        if(bgroup.getClass() == HighLevelGroup.class){
                        groups.add(bgroup);
                        }
                        else{
                            baseGroups.add(bgroup);
                        }
                    }
                }
            }

            map.put(level, groups);
            level++;
        }

        map.put(level, baseGroups);

        return map;

    }

}
