/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.GrammarIllustrator;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.Beat;
import uk.ac.kent.computing.gttm.Grammar_Elements.MetricalStructure.MetricalContainer;


/**
 *
 * @author Alexander Dodd
 */
public class MetricalIllustrator {
    
    
    public static double METRICAL_EXPANSION_VALUE = 20;
        
    
    private static MetricalIllustrator instance = null;
    
    
    public static MetricalIllustrator getInstance()
    {
        if(instance == null)
        {
            instance = new MetricalIllustrator();
        }
        
        return instance;
    }
    
    private MetricalIllustrator()
    {
        
    }
    
    public void draw(Graphics g, MetricalContainer mContainer, double x, double y)
    {
      Graphics2D g2d = (Graphics2D) g;
      
    
      double yEx = 0;
      
      for(Beat b : mContainer.getMetricalBeatsList())
      {
          int strength = b.getBeatStrength();
          int pos = 0;
          while(pos < strength)
          {
              Shape s = new Ellipse2D.Double(x, y + yEx, 3, 3);
              g2d.draw(new Ellipse2D.Double(x, y + yEx, 3, 3));
              g2d.fill(s);
              pos++;
              yEx += 10;
          }
          
          x += METRICAL_EXPANSION_VALUE;
          yEx = 0;
          
      }
    }
    
  
    
}
