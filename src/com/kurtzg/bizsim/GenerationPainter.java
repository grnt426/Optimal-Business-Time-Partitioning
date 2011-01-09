package com.kurtzg.bizsim;

import javax.swing.*;
import java.awt.*;

public class GenerationPainter extends JPanel{

    //instance vars
    private Generation generation;
    private int max_income, min_income, agent_count, max_x = 640, max_y = 480;

    public GenerationPainter(){
        generation = null;
    }

    public void paint(Graphics g){

        //override the previously painted image, and provide a background
        super.paintComponent(g);
        setBackground(Color.WHITE);

        //grid boundaries, give a 10px buffer
        int agent_distance = (max_x-10)/agent_count;

        //draw the grid
        for(int i = 0; i < agent_count*.2; ++i){

            //draw a line divider for every 20th agent
            int x = agent_distance * i * (int)(agent_count*.2);
            g.drawLine(x, 0, x, max_y);
        }

        if(generation == null)
            return;

        //draw the relative incomes of all agents in the generation
        for(int i = 0; i < agent_count; ++i){
            Agent a = generation.getAgent(i);

            //define the x and circumference of the dot
            int x = i * agent_distance;
            int circ = 3;

            //we need agents with the most money at the top
            int y = max_y - (a.getMoney() / max_income * max_y);

            //draw the actual dot
            g.fillOval(x, y, circ, circ);
        }
    }

    public void setGeneration(Generation g){

        //give the graph some breathing room
        max_income = (int)(g.getHighestIncome() * 1.2);
        min_income = (int)(g.getLowestIncome() * .8);

        //store everything else
        agent_count = g.getAgents().size();
        generation = g;

        repaint();
    }

    public Dimension getPreferredSize(){
        return new Dimension(max_x+1, max_y+1);
    }
}
