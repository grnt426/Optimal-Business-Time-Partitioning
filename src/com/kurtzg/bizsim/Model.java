package com.kurtzg.bizsim;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Model implements ActionListener{

    //instance variables
    private List<ActionListener> listeners = new ArrayList<ActionListener>();
    private List<Species> species = new ArrayList<Species>();
    private int max_gen_count, species_counter;
    Environment e = new Environment();

    /*
     * Default constructor, nothing fancy
     */
    public Model(){

        //set up our default values
        max_gen_count = 100;

    }

    /*
     * Creates an arbitrary number of species to be added to the total pool
     */
    public void creteNewSpecies(int num){

        //vars
        Environment e;

        //create a new thread, environment, etc for each new species
        for(int i = 0; i < num; ++i){
            e = new Environment();
            Species s = new Species(null, max_gen_count, species.size(),
                    e, this);

            //create the thread, and run
            Thread t = new Thread(s);
            t.setPriority(8);
            t.start();

            species.add(s);
        }
    }

    /*
     * Adds an object that wants updates on changes to the Model
     */
    public void addListeners(ActionListener al){
        listeners.add(al);
    }

    /*
     * Handles informing all listeners of a change in the model
     */
    public void processEvent(ActionEvent e){
        for(ActionListener a : listeners){
            a.actionPerformed(e);
        }
    }

    public void actionPerformed(ActionEvent e) {

        //vars
        Object src = e.getSource();
        String msg = e.getActionCommand();

        if(msg.equals("day_processed")){

            //alert the controller that another day has been processed

        }
    }
}
