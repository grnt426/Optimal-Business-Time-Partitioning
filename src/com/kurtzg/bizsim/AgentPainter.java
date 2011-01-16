package com.kurtzg.bizsim;


import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/*
 * File:            AgentPainter.java
 *
 * Author:          Grant Kurtz
 *
 * Description:     Paints all the information of an agent's holdings of
 *                  resources over time.
 *
 * TODO:            Needs to draw info of other resources aside from just money
 */
public class AgentPainter extends JPanel{

    // instance vars
    private Agent agent;
    private int max_x = 600, max_y = 480;
    private double MAX_MONEY = 2000000;

    /*
     * Handles drawing everything to our panel
     */
    public void paint(Graphics g){

        // pass our JPanel super what we are using to draw
        super.paintComponent(g);
        setBackground(Color.WHITE);

        // draw our x and y-axis
        for(int i = 0; i < 11; ++i){
            g.drawLine((55*i), 0, (55*i), (int)max_y);
            g.drawString((i*10) + "", (55*i)+1, (int)max_y-2);
            g.drawLine(0, (int)(max_y/10*i), (int)max_x, (int)(max_y/10*i));
            if(i != 0)
                g.drawString("$"+(i*200000), 0, (int)(max_y/10*(10-i)));
        }

        // don't try drawing unless we have an agent
        if(agent == null)
            return;

        // grab the agent's income history
        List<Integer> income_history = agent.getTotalHistory();

        // print out points for the amount of money held after each day
        for(int k = 0; k < income_history.size(); ++k){
            int income = income_history.get(k);
            g.fillOval((int)(k*5.5), (int)((1-income/MAX_MONEY)*max_y), 3, 3);
        }
    }

    /*
     * Used to tell this class which agent we will be drawing all the data
     * points for
     *
     * Param:   a           the agent to graphically represent
     */
    public void setAgent(Agent a){
        agent = a;
        repaint();
    }

    /*
     * Tells any containing frame/panel the size we want to be held in for
     * optimal presentation
     *
     * Returns              the preferred x/y dimensions for this panel
     */
    public Dimension getPreferredSize(){
        return new Dimension(max_x+1, max_y+1);
    }
}
