package GrammarIllustrator;

import GrammarIllustrator.MetricalIllustrator;
import Grammar_Elements.GrammarContainer;
import Grammar_Elements.MetricalStructure.*;
import Grammar_Elements.ReductionBranches.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javafx.scene.shape.Ellipse;

public class TimeSpanIllustrator {

    private static TimeSpanIllustrator instance = null;

    /**
     * 
     * @return the singleton instance of the class
     */
    public static TimeSpanIllustrator getInstance() {
        if (instance == null) {
            instance = new TimeSpanIllustrator();
        }

        return instance;
    }

    private TimeSpanIllustrator() {

    }

      /**
     * draws the  time-span reduction structure to the given Graphics object in the given position
     * @param g the Graphics object to draw the  time-span reduction reduction structure illustration to
     * @param grammar the grammar structure to follow
     * @param x the starting x position to draw the  time-span reduction reduction structure
     * @param y the starting y position to draw the  time-span reduction reduction structure
     * @param branchX the starting position of the top  time-span reduction branch.
     * @param branchY the starting position of the top  time-span reduction branch.
     */
    public void draw(Graphics g, GrammarContainer grammar, double x, double y,
            double branchX, double branchY) {
        Graphics2D g2d = (Graphics2D) g;
        
        double origX = x;
        double origY = y;
        
       

        TimeSpanReductionBranch topTBranch = grammar.getTopTimeSpanReductionBranch();
        Beat firstBeat = grammar.getMetricalStructure().getBeatAtPosition(0);

        while (firstBeat != topTBranch.getAssociatedBeat()) {
            x += MetricalIllustrator.METRICAL_EXPANSION_VALUE;
            firstBeat = firstBeat.getNextBeat();
        }

        Line2D topLine = new Line2D.Double(branchX, branchY, x, (y - 5));

        x = origX;
        
        Map<Beat, Line2D> cadenceLines = new HashMap<>();

        if (topTBranch.getClass() == CadencedTimeSpanReductionBranch.class) {
            CadencedTimeSpanReductionBranch cBr = (CadencedTimeSpanReductionBranch) topTBranch;

            firstBeat = grammar.getMetricalStructure().getBeatAtPosition(0);
            
            while (firstBeat != cBr.getCadenceStart()) {
                x += MetricalIllustrator.METRICAL_EXPANSION_VALUE;
                firstBeat = firstBeat.getNextBeat();
            }

            Line2D cadenceStart = new Line2D.Double(branchX, branchY, x, (y - 5));
            g2d.draw(cadenceStart);
            //drawCadenceSymbol(cadenceStart, topLine, g2d);
            
            cadenceLines.put(cBr.getCadenceStart(), cadenceStart);
            cadenceLines.put(cBr.getCadenceEnd(), topLine);
            
            drawCadenceSymbol(cadenceStart, topLine, g2d);
        }

        g2d.draw(topLine);

        
        

        Map<Branch, Line2D> lineMap = new HashMap();

        int highestLevel = grammar.getTopProlongationalReductionBranch().getLevel();
        
        for(Branch br : grammar.getTopTimeSpanReductionBranch().getAllSubBranches())
        {
            if(br.getLevel() > highestLevel)
            {
                highestLevel = br.getLevel();
            }
        }
        recursivelyDrawSubBranches(highestLevel, topLine.getY1(), topLine.getY2(), topLine, grammar.getTopTimeSpanReductionBranch(),
                g2d, lineMap, grammar.getMetricalStructure(), cadenceLines, origX);

        /* drawSubBranches(g2d, topLine, grammar.getTopProlongationalReductionBranch(),
         topLine.getX1(), grammar.getMetricalStructure(), lineMap);
        
         Branch lastBranch = grammar.getTopProlongationalReductionBranch();
        
         while(!branches.isEmpty())
         {
            
         }*/
    }

    private void drawSubBranchesByLevel(int highestLevel, double highestY, double lowestY, Branch topBranch,
            Graphics2D g2d, Map<Branch, Line2D> lineMap, Line2D topLine, MetricalContainer mContainer, Map<Beat, Line2D> cadenceMap,
            double x) {
        double diff = Math.abs(highestY - lowestY);
        double subsections = diff / (highestLevel + 1);

        for (Integer i : topBranch.getOrderedLevels()) {
            
            

            Branch br = topBranch.getChildBranches().get(i);
            
            
              if(topBranch.getClass() == CadencedTimeSpanReductionBranch.class)
        {
            CadencedTimeSpanReductionBranch cBr = (CadencedTimeSpanReductionBranch) topBranch;
            
            if(br.getAssociatedBeat().getPosition() < topBranch.getAssociatedBeat().getPosition())
            {
                topLine = cadenceMap.get(cBr.getCadenceStart());
            }
            else{
                topLine = cadenceMap.get(cBr.getCadenceEnd());
            }
        }
              
                 int level = br.getLevel();

            double startY = level * subsections;
            startY += highestY;

            double startX =  MetricalIllustrator.METRICAL_EXPANSION_VALUE * mContainer.getMetricalBeatsList().size();
            Line2D.Double tempLine = new Line2D.Double(startX, startY, 0, startY);

            while (tempLine.intersectsLine(topLine)) {
                startX -= 0.1;
                tempLine = new Line2D.Double(startX, startY, 0, startY);
            }
            
              
           

         
            double origX = x;

            double xEnd = x;

            Beat firstBeat = mContainer.getBeatAtPosition(0);

            while (firstBeat != br.getAssociatedBeat()) {
                xEnd += MetricalIllustrator.METRICAL_EXPANSION_VALUE;
                firstBeat = firstBeat.getNextBeat();
            }

            Line2D newLine = new Line2D.Double(startX, startY, xEnd, lowestY);

            g2d.draw(newLine);

            TimeSpanReductionBranch tBr = (TimeSpanReductionBranch) br;

                 if (br.getClass() == CadencedTimeSpanReductionBranch.class) {
            CadencedTimeSpanReductionBranch cBr = (CadencedTimeSpanReductionBranch) br;

           firstBeat = mContainer.getBeatAtPosition(0);
           
           double newX = x;
            
            while (firstBeat != cBr.getCadenceStart()) {
                origX += MetricalIllustrator.METRICAL_EXPANSION_VALUE;
                firstBeat = firstBeat.getNextBeat();
            }

            Line2D cadenceStart = new Line2D.Double(startX, startY, origX, startY);
            g2d.draw(cadenceStart);
            //drawCadenceSymbol(cadenceStart, topLine, g2d);
            
            cadenceMap.put(cBr.getCadenceStart(), cadenceStart);
            cadenceMap.put(cBr.getCadenceEnd(), topLine);
            
            drawCadenceSymbol(cadenceStart, newLine, g2d);
        }
            
            lineMap.put(br, newLine);

        }

    }

    private void recursivelyDrawSubBranches(int highestLevel, double highestY, double lowestY,
            Line2D topLine, Branch topBranch, Graphics2D g2d, Map<Branch, Line2D> lineMap,
            MetricalContainer mContainer, 
            Map<Beat, Line2D> cadenceMap, double x) {
        
        
        drawSubBranchesByLevel(highestLevel, highestY, lowestY, topBranch, g2d, lineMap, topLine, mContainer, cadenceMap, x);
        for (Branch br : topBranch.getChildBranches().values()) {
      
            recursivelyDrawSubBranches(highestLevel, highestY, lowestY, lineMap.get(br), br, g2d, lineMap, mContainer,
                    cadenceMap, x);
        }
    }

    private void drawCadenceSymbol(Line2D cadenceStart, Line2D cadenceEnd, Graphics2D g2d) {
        
        Line2D middleLine = new Line2D.Double(cadenceStart.getX1(), cadenceStart.getY1(),
        ((cadenceEnd.getX2() - cadenceStart.getX2())/2) + cadenceStart.getX2(), 
        cadenceEnd.getY2());
        
        double halfX = (middleLine.getX2() - middleLine.getX1());
        halfX /= 4;
        halfX += middleLine.getX1();
        double halfY = (middleLine.getY2() - middleLine.getY1());
        halfY /= 4;
        halfY += middleLine.getY1();
        
        double width = cadenceEnd.getX2() - cadenceEnd.getX1();
        width /= 4;
        width += cadenceEnd.getX1();
        
        double start =  cadenceStart.getX2() - cadenceStart.getX1();
        start /= 4;
        start += cadenceStart.getX1();
        
        width = width - start;
        
        
        Ellipse2D el = new Ellipse2D.Double(halfX - (width / 2), halfY - 2.5, width, 5);
        
        g2d.draw(el);
      
    }
}
