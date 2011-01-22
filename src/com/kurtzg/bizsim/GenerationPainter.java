package com.kurtzg.bizsim;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

public class GenerationPainter extends JPanel{

    //instance vars
    private Generation generation;
    private CoordinateContainer coordinates;
    private Coordinate last_agent;
    private double max_income, min_income;
    private int agent_count, max_x = 600, max_y = 480, hover_x = -1,
            hover_y = -1;

    // marks if we are hovering over a new point, but not necessarily a new
    // coordinate
    private boolean changed;

    public GenerationPainter(){
        generation = null;
        coordinates = new CoordinateContainer();
        changed = false;
    }

    public void paint(Graphics g){

        // override the previously painted image, and provide a background
        super.paintComponent(g);
        setBackground(Color.WHITE);

        if(generation == null)
            return;

        // grid boundaries, give a 10px buffer
        int agent_distance = max_x/agent_count;

        g.setColor(Color.gray);

        // draw the grid, x-axis
        for(int i = 0; i < agent_count*.2; ++i){

            //draw a line divider for every 20th agent
            int x = agent_distance * i * (int)(agent_count*.2);
            g.drawLine(x, 0, x, max_y);
        }

        // y-axis separators
        for(int i = 0; i < 8; ++i){
            int y = max_y/8*i;
            g.drawLine(0, y, max_x, y);
            g.drawString("$"+(max_income/8*(8-i)), 0, y-4);
        }

        // clear out our coordinate history to blow away any changes
        // TODO: in the future, simply correct/replace changes
        coordinates.clear();

        // draw the relative incomes of all agents in the generation
        g.setColor(Color.red);
        for(int i = 0; i < agent_count; ++i){
            Agent a = generation.getAgent(i);

            // define the x and circumference of the dot
            int x = i * agent_distance+2;
            int circumference = 4;

            // we need agents with the most money at the top
            int y = max_y - (int)(a.getMoney() / max_income * max_y);

            // store this point for use later
            coordinates.addPoint(x, y, a);

            // draw the actual dot
            g.fillOval(x, y, circumference, circumference);
        }

        if(hover_x != -1){

            // grab the closest coordinate
            Coordinate coord = last_agent;

            // don't re-search if we haven't moved
            if(changed){
                coord = coordinates.getClosestPointAt(hover_x, hover_y);
                last_agent = coord;
            }

            if(coord != null){

                // vars
                Agent agent = (Agent)coord.getData();
                int x = coord.getX(), y = coord.getY();
                double avg = agent.getMoney();
                g.setColor(Color.black);

                // ->
                g.drawLine(0, y+1, x-4, y+1);

                // <-
                g.drawLine(max_x, y+1, x+7, y+1);

                // \/
                g.drawLine(x+1, 0, x+1, y-3);

                // ^
                g.drawLine(x+1, max_y, x+1, y+6);

                // draw "targeting" box
                g.drawRect(x-1, y-1, 5, 5);

                // draw the hovering box to indicate income
                // but first, we must determine where the box will be relative
                // to the mouse based on overlapping with the edge of the window
                int text_len = (avg+"$").length()*7;
                x = x+10;
                int total = x + text_len;
                if(total > max_x)
                    x -= text_len + 16;

                g.setColor(Color.BLACK);
                g.fillRect(x, y-13, text_len, 13);
                g.setColor(Color.GREEN);
                g.drawString("$" + avg, x, y-3);
            }
        }
    }

    public void setGeneration(Generation g){

        // give the graph some breathing room
        max_income = (int)(g.getHighestIncome() * 1.2);
        min_income = (int)(g.getLowestIncome() * .8);

        // store everything else
        agent_count = g.getAgents().size();
        generation = g;

        repaint();
    }

    public Agent getClickedAgent(int x, int y){

        Agent a = null;

        a = (Agent) coordinates.getClosestDataAt(x, y);

        return a;
    }

    public void setHoveringOver(int x, int y){

        if(hover_x != x && hover_y != y)
            changed = true;
        else
            changed = false;
        hover_x = x;
        hover_y = y;
        repaint();
    }

    public Dimension getPreferredSize(){
        return new Dimension(max_x+1, max_y+1);
    }
}
