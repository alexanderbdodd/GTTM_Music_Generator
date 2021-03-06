/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kent.computing.gttm.GrammarIllustrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;

import uk.ac.kent.computing.gttm.Grammar_Elements.GrammarContainer;

/**
 *
 * @author Alexander
 */
public class IllustratorFrame extends JFrame implements Runnable {

    private Thread thread;
    private GrammarContainer grammar;

    public IllustratorFrame(GrammarContainer grammar) {

        thread = new Thread(this);
        this.grammar = grammar;
        run();

    }

    private void initUI(GrammarContainer grammar) {

        JPanel topPanel = new JPanel();

        topPanel.setLayout(new BorderLayout());

        JPanel illustrator = new GrammarIllustrator(grammar);
        illustrator.setPreferredSize(new Dimension(500, 1000));
        illustrator.setAutoscrolls(true);

        JScrollPane slider = new JScrollPane(illustrator);
        slider.setPreferredSize(new Dimension(1000, 700));
        topPanel.add(slider, BorderLayout.CENTER);
        
        slider.getVerticalScrollBar().setUnitIncrement(16);

        JComponent newContentPane = topPanel;
        newContentPane.setOpaque(true); //content panes must be opaque
        setContentPane(newContentPane);

        pack();

        setTitle("Grammar Illustrator");

    }

    @Override
    public void run() {
        initUI(grammar);

    }

}
