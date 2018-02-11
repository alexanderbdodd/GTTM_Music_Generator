/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GrammarIllustrator;

import Grammar_Elements.MetricalStructure.Beat;
import Grammar_Elements.MetricalStructure.MetricalContainer;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**A class that is used to draw the metrical structure illustration.
 *
 * @author Alexander Dodd
 */
public class MetricalIllustrator {
    
    
    public static double METRICAL_EXPANSION_VALUE = 20;
        
    
    private static MetricalIllustrator instance = null;
    
    /**
     * 
     * @return the singleton instance of this class
     */
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
    
    /**
     * 
   * draws the metrical structure to the given Graphics object in the given position
     * @param g the Graphics object to draw the metrical structure illustration to
     * @param mContainer the metrical structure to follow
     * @param x the starting x position to draw the metrical structure
     * @param y the starting y position to draw the metrical structure
     */
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
