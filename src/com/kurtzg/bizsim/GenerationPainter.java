package com.kurtzg.bizsim;

import javax.swing.*;
import java.awt.*;

public class GenerationPainter extends JPanel{

    //instance vars
    private Generation generation;
    private double max_income, min_income;
    private int agent_count, max_x = 600, max_y = 480, hover_x = -1,
            hover_y = -1;

    public GenerationPainter(){
        generation = null;
    }

    public void paint(Graphics g){

        //override the previously painted image, and provide a background
        super.paintComponent(g);
        setBackground(Color.WHITE);

        if(generation == null)
            return;

        //grid boundaries, give a 10px buffer
        int agent_distance = max_x/agent_count;

        g.setColor(Color.gray);

        //draw the grid
        for(int i = 0; i < agent_count*.2; ++i){

            //draw a line divider for every 20th agent
            int x = agent_distance * i * (int)(agent_count*.2);
            g.drawLine(x, 0, x, max_y);
        }

        for(int i = 0; i < 8; ++i){
            int y = max_y/8*i;
            g.drawLine(0, y, max_x, y);
            g.drawString("$"+(max_income/8*(8-i)), 0, y-4);
        }

        //draw the relative incomes of all agents in the generation
        g.setColor(Color.red);
        for(int i = 0; i < agent_count; ++i){
            Agent a = generation.getAgent(i);

            //define the x and circumference of the dot
            int x = i * agent_distance+2;
            int circumference = 4;

            //we need agents with the most money at the top
            int y = max_y - (int)(a.getMoney() / max_income * max_y)-10;

            //draw the actual dot
            g.fillOval(x, y, circumference, circumference);
        }

        if(hover_x != -1){

            // TODO: Use the hover_y value to give better accuracy for the
            // TODO: agent
            Agent agent = generation.getAgent(hover_x-1);

            if(agent != null){

                double avg = agent.getMoney();
                //int y = max_y - (int)(a.getMoney() / max_income * max_y)-10;
                g.setColor(Color.black);

                // ->


                // <-


                // \/


                // ^


                // draw "targeting" box
               // g.drawRect(hover_x-1, (int)((1-(avg/max_y_val))*max_y_real)-1,
                        //4, 4);
            }
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

    public Agent getClickedAgent(int x, int y){
        return generation.getAgent(10);
    }

    public void setHoveringOver(int x, int y){
        hover_x = x;
        hover_y = y;
        repaint();
    }

    public Dimension getPreferredSize(){
        return new Dimension(max_x+1, max_y+1);
    }
}
