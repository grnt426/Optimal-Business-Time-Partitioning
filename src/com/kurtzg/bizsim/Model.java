package com.kurtzg.bizsim;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Model {

    //instance variables
    List<ActionListener> listeners = new ArrayList<ActionListener>();

    public Model(){

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

}
