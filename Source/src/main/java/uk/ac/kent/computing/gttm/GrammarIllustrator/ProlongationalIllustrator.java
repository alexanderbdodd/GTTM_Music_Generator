/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.GrammarIllustrator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.kent.computing.gttm.Grammar_Elements.GrammarContainer;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.MetricalContainer;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.Branch;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.ProlongationalBranch;
import uk.ac.kent.computing.gttm.Grammar_Elements.ReductionBranches.ProlongationalTypeEnum;

/**
 *
 * @author Alexander Dodd
 */
public class ProlongationalIllustrator {

    private static ProlongationalIllustrator instance = null;

    public static ProlongationalIllustrator getInstance() {
        if (instance == null) {
            instance = new ProlongationalIllustrator();
        }

        return instance;
    }

    private ProlongationalIllustrator() {

    }

    public void draw(Graphics g, GrammarContainer grammar, double x, double y, double branchX, double branchY) {
        Graphics2D g2d = (Graphics2D) g;

       double origX = x;

        ProlongationalBranch topPBranch = grammar.getTopProlongationalReductionBranch();
        Beat firstBeat = grammar.getMetricalStructure().getBeatAtPosition(0);

        List<Branch> branches = topPBranch.getAllSubBranches();

        while (firstBeat != topPBranch.getAssociatedBeat()) {
            x += MetricalIllustrator.METRICAL_EXPANSION_VALUE;
            firstBeat = firstBeat.getNextBeat();
        }

        Line2D topLine = new Line2D.Double(branchX, branchY, x, y - 5);

        g2d.draw(topLine);

        Map<Branch, Line2D> lineMap = new HashMap();
        
        
        
        int highestLevel = grammar.getTopProlongationalReductionBranch().getLevel();
        
        for(Branch br : grammar.getTopProlongationalReductionBranch().getAllSubBranches())
        {
            if(br.getLevel() > highestLevel)
            {
                highestLevel = br.getLevel();
            }
        }
        
        
        recursivelyDrawSubBranches(highestLevel, topLine.getY1(), topLine.getY2(), topLine, grammar.getTopProlongationalReductionBranch(),
                g2d, lineMap, grammar.getMetricalStructure(), origX);

       

    }
    
    
    
    private void drawSubBranchesByLevel(int highestLevel, double highestY, double lowestY, Branch topBranch,
    Graphics2D g2d, Map<Branch, Line2D> lineMap, Line2D topLine, MetricalContainer mContainer, double x)
    {
        double diff = Math.abs(highestY - lowestY);
        double subsections = diff / (highestLevel + 1);
        
         for (Integer i : topBranch.getOrderedLevels()) {
                       
            Branch br = topBranch.getChildBranches().get(i);
            
            int level = br.getLevel();
            
            double startY = level * subsections;
            startY += highestY;
            
            double startX = MetricalIllustrator.METRICAL_EXPANSION_VALUE * mContainer.getMetricalBeatsList().size();
            Line2D.Double tempLine = new Line2D.Double(startX, startY, 0, startY);
            
            while(tempLine.intersectsLine(topLine))
            {
                startX -= 0.1;
                tempLine = new Line2D.Double(startX, startY, 0, startY);
            }
            
            double xEnd = x;
       

            Beat firstBeat = mContainer.getBeatAtPosition(0);

            while (firstBeat != br.getAssociatedBeat()) {
                xEnd += MetricalIllustrator.METRICAL_EXPANSION_VALUE;
                firstBeat = firstBeat.getNextBeat();
            }

            Line2D newLine = new Line2D.Double(startX, startY, xEnd, lowestY);

            g2d.draw(newLine);

            ProlongationalBranch pBr = (ProlongationalBranch) br;

            if (pBr.getProlongationType() == ProlongationalTypeEnum.WEAK_PROLONGATION) {
                g2d.draw(new Ellipse2D.Double(newLine.getX1() - 2.5, newLine.getY1() - 2.5, 5, 5));
            }
            if (pBr.getProlongationType() == ProlongationalTypeEnum.STRONG_PROLONGATION) {
                Shape sh = new Ellipse2D.Double(newLine.getX1() - 2.5, newLine.getY1() - 2.5, 5, 5);
                g2d.draw(sh);
                g2d.fill(sh);
            }

            lineMap.put(br, newLine);
            
            
            
         }
        
    }   
    
    
    private void recursivelyDrawSubBranches(int highestLevel, double highestY, double lowestY,
            Line2D topLine, Branch topBranch, Graphics2D g2d, Map<Branch, Line2D> lineMap,
            MetricalContainer mContainer, double x)
    {
        drawSubBranchesByLevel(highestLevel, highestY, lowestY, topBranch, g2d, lineMap, topLine, mContainer, x);
        for(Branch br : topBranch.getChildBranches().values())
        {
            recursivelyDrawSubBranches(highestLevel, highestY, lowestY, lineMap.get(br), br, g2d, lineMap, mContainer, x);
        }
    }

   
}
