/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GrammarIllustrator;

import Grammar_Elements.GrammarContainer;
import java.awt.*;
import javax.swing.JPanel;

/**
 *Extends JPanel to create a custom panel that draws grammar illustrations.
 * @author Alexander Dodd
 */
public class GrammarIllustrator extends JPanel {

    GrammarContainer grammar = null;

    public GrammarIllustrator() {
        
         setBackground(Color.white);

    }

    /**
     * Constructs a GrammarIllustrator object with a grammar structure
     * @param grammar the grammar structure associated with the GrammarIllustrator object
     */
    public GrammarIllustrator(GrammarContainer grammar) {

        this.grammar = grammar;
        setBackground(Color.white);
        

    }
    
    /**
     * Sets a new grammar to be associated with the GrammarIllustrator
     * @param newGrammar the new grammar structure to be associated with the GrammarIllustrator
     */
    public void setNewGrammar(GrammarContainer newGrammar)
    {
        grammar = newGrammar;
        repaint();
                
    }

    private void doDrawing(Graphics g) {

        MetricalIllustrator.getInstance().draw(g, grammar.getMetricalStructure(), 30, 200);
        TimeSpanIllustrator.getInstance().draw(g, grammar, 30, 500, 100, 350);
        MetricalIllustrator.getInstance().draw(g, grammar.getMetricalStructure(), 30, 500);
        ProlongationalIllustrator.getInstance().draw(g, grammar, 30, 200, 100, 50);

        GroupingStructureIllustrator.getInstance().draw(g, grammar, 30, 200);
        GroupingStructureIllustrator.getInstance().draw(g, grammar, 30, 500);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (grammar != null) {
            super.paintComponent(g);
            doDrawing(g);
        }
    }

}
