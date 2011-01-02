package com.kurtzg.bizsim;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View implements ActionListener{

    private Model model;
    private JFrame graphWindow;

    public View(){

        //hand ourselves over to the model to listen for events
        model.addListeners(this);

         //Create a Tabbed panel for the different graphs
        JTabbedPane graphs = new JTabbedPane();
        graphs.addTab("Species Average", null, paint, "Graphical " +
                "Representation of All Species' Average Performance");
        graphs.addTab("Elite Performance", null, ep,
                "A 100-day Graph of the Elite Agent's Business Model");
        graphWindow.getContentPane().add(graphs);

        //give our paint class a copy of the Generational History
        //paint.setHistory(history);

         // construct our window
        // TODO: consolidate each thread's results into a single window for
        // TODO: ease of human comparison
        graphWindow.setTitle("Optimal Division of Tasks in a Business");
        graphWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //window.getContentPane().add(paint);
        running = true;
        graphWindow.pack();
        graphWindow.setVisible(true);

    }

    public void updateGenerationProgressGraph(){

    }

    public void actionPerformed(ActionEvent e) {

        //vars
        Object src = e.getSource();
        String msg = e.getActionCommand();

        //we have received an event from our model
        if(src.equals(model)){

            if(msg.equals("generation_processed")){

                //tell the view to repaint the generation progress graph
                view.updateGenerationProgressGraph();
            }
        }
    }
}
