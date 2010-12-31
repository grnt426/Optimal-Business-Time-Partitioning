package com.kurtzg.bizsim;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener{

    //instance variables
    Model m = new Model();

    public Controller(){

        //hand the model ourself to listen for changes
        m.addListeners(this);
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        String msg = e.getActionCommand();

        //we have received an event from our model
        if(src.equals(m)){

        }
    }
}
