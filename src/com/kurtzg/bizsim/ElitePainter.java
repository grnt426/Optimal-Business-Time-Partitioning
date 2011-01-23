package com.kurtzg.bizsim;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/*
 * File:            ElitePainter.java
 *
 * Author:          Grant Kurtz
 *
 * Description:     Paints information pertinent to the elite agent of each
 *                  species
 */
public class ElitePainter extends JPanel{

    // instance vars
    private List<Agent> elites = new ArrayList<Agent>();
    private final double MAX_X = 600.0, MAX_Y = 480.0;
    private int MAX_MONEY = 1800000;
    private List<Color> s_colors = new ArrayList<Color>();

    /*
     * Default Constructor sets up some unique colors in preparation for
     * an unknown number of threads
     */
    public ElitePainter(){

        // setup some basic colors for our species
        s_colors.add(new Color(255, 0, 0));
        s_colors.add(new Color(0, 255, 0));
        s_colors.add(new Color(0, 0, 255));
        s_colors.add(new Color(255, 255, 0));
        s_colors.add(new Color(0, 255, 255));
        s_colors.add(new Color(255, 0, 255));
    }

    /*
     * Is called on any repaint() call to this class (or its parents) and
     * handles the drawing necessary to render our graph
     */
    public void paint(Graphics g){

        // let the parent JPanel class know how we are painting
        super.paintComponent(g);

        setBackground(Color.WHITE);

        // draw our x- and y-axis
        for(int i = 0; i < 11; ++i){

            // y-axis
            g.drawLine((55*i), 0, (55*i), (int)MAX_Y);

            // x-axis
            g.drawString((i*10) + "", (55*i)+1, (int)MAX_Y-2);
            g.drawLine(0, (int)(MAX_Y/10*i), (int)MAX_X, (int)(MAX_Y/10*i));
            if(i != 0)
                g.drawString("$"+(i*(MAX_MONEY/10)), 0, (int)(MAX_Y/10*(10-i)));
        }

        // draw each elite agent from all species
        for(int i = 0; i < elites.size(); ++i){

            // grab this species' color
            g.setColor(s_colors.get(i));
            Agent a = elites.get(i);

            // make sure we got an agent
            if(a == null)
                continue;

            // grab the agent's income history
            List<Integer> income_history = a.getTotalHistory();

            // draw a point for each day in this agent's income history
            for(int k = 0; k < income_history.size(); ++k){
                double income = income_history.get(k);
                g.fillOval((int)(k*5.5), (int)((1.0-income/MAX_MONEY)*MAX_Y), 3, 3);
            }
        }
    }

    /*
     * Used to let our paint method which agents are the elites to draw
     *
     * Param:   elites      a list of elite agents to draw
     */
    public void setElites(List<Agent> elites){

        if(elites.isEmpty())
                return;
        // setup our y-axis boundary
        for(Agent a : elites){
            if(a == null)
                continue;
            if(a.getMoney() > MAX_MONEY*.8)
                MAX_MONEY = (int)(a.getMoney()*1.15);
        }

        this.elites = elites;
        repaint();
    }

     /*
     * Tells any containing frame/panel the size we want to be held in for
     * optimal presentation
     *
     * Returns              the preferred x/y dimensions for this panel
     */
    public Dimension getPreferredSize(){
        return new Dimension((int)MAX_X+1, (int)MAX_Y+1);
    }
}
