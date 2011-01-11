package com.kurtzg.bizsim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Painter extends JPanel{

	//instance vars
    private List<Species> species;
    private List<Color> s_colors = new ArrayList<Color>();
    private int hover_x, hover_y, max_x = 600, max_y = 480, highest_money,
        lowest_money;

    public Painter(){

        //setup some basic colors for our species
        s_colors.add(Color.red);
        s_colors.add(Color.green);
        s_colors.add(Color.blue);
        s_colors.add(Color.black);
        s_colors.add(Color.yellow);
        s_colors.add(Color.magenta);
        s_colors.add(Color.orange);
        s_colors.add(Color.pink);

        //setup defaults
        hover_x = -1;
        highest_money = 60000;
        lowest_money = 0;
    }
	
	public void paint(Graphics g){

        super.paintComponent(g);

        setBackground(Color.white);

        //draw the income line separators
        g.setColor(Color.gray);
        for(int i = 4; i > 0; --i){
            int y =  max_y - (max_y/4*i)+10;
            g.drawString("$"+i*highest_money/4, 140, y);
            g.drawLine(0, y, max_x, y);
        }

        //draw the generational count separators
        for(int i = 1; i < 6; ++i){
            int x = i*100;
            g.drawString("Gen: "+(i*100), x+1, max_y-2);
            g.drawLine(x, 0, x, max_y);
        }

        if(species == null)
            return;

        //draws all the generations based on income
        for(int i = 0; i < species.size(); ++i){
            g.setColor(s_colors.get(i));
            Species s = species.get(i);
            History h = s.getHistory();
            List<Generation> gens = h.getGenerations();
            for(int k = 0; k < gens.size(); ++k){
                double avg = gens.get(k).getAverage();
                int y = max_y - (int)(avg / highest_money * max_y);
                g.fillOval(k, y, 3, 3);
            }
        }


        //used for drawing the targeting box
        if(hover_x != -1){
            Species s = species.get(0);
            History h = s.getHistory();

            // TODO: Use the hover_y value to give better accuracy for the
            // TODO: agent
            Generation gen = h.getGeneration(hover_x-1);

            if(gen != null){

                double avg = gen.getAverage();
                int y = max_y - (int)(avg / highest_money * max_y);
                g.setColor(Color.black);

                // ->
                g.drawLine(0, y, hover_x-5, y);

                // <-
                g.drawLine(max_x, y, hover_x+5, y);

                // \/
                g.drawLine(hover_x, 0, hover_x, y-3);

                // ^
                g.drawLine(hover_x, max_y, hover_x, y+4);

                // draw "targeting" box
                g.drawRect(hover_x-2, y-1, 5, 5);
            }
        }
    }

    public synchronized void setHistory(List<Species> s){
        species = s;

        //determine the boundary for highest money
        for(Species spec : species){
            Generation gen = spec.getHistory().getLastGeneration();

            // we could be at the start of our program, where there may be no
            // starting generations
            if(gen == null)
                continue;


            double avg = gen.getAverage();
            if(avg > (highest_money*.9))
                highest_money = (int)(avg*1.2);
            if(avg < (lowest_money*.9))
                lowest_money = (int)(avg*.8);
        }

        repaint();
    }

    public Generation getClickedGeneration(int x, int y){

        // we should only need the x-coordinate to determine where the agent is
        // in a single species generation
        // TODO: need to allow for selecting generations from an arbitrary
        // TODO: number of species
        return species.get(0).getHistory().getGeneration(x);
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