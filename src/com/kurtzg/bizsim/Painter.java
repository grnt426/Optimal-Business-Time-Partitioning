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
    private List<Integer> average_history = new ArrayList<Integer>();
	private int generation_count;
	private double max_y_val = 250000, max_y_real = 480.0;
    private List<Species> species;
    private List<Color> s_colors = new ArrayList<Color>();

    public Painter(){

        //setup some basic colors for our species
        s_colors.add(new Color(255, 0, 0));
        s_colors.add(new Color(0, 255, 0));
        s_colors.add(new Color(0, 0, 255));
        s_colors.add(new Color(255, 255, 0));
        s_colors.add(new Color(0, 255, 255));
        s_colors.add(new Color(255, 0, 255));
    }
	
	public void paint(Graphics g){

        super.paintComponent(g);

        setBackground(Color.white);

        //draw the income line separators
        g.setColor(Color.gray);
        for(int i = 4; i > 0; --i){
            int y =  480 - (480/4*i)+10;
            g.drawString("$"+i*max_y_val/4, 140, y);
            g.drawLine(0, y, 1280, y);
        }

        //draw the generational count separators
        for(int i = 1; i < 6; ++i){
            int x = i*100;
            g.drawString("Gen: "+(i*100), x+1, 478);
            g.drawLine(x, 0, x, 480);
        }

        if(species == null)
            return;

        for(int i = 0; i < species.size(); ++i){
            g.setColor(s_colors.get(i));
            Species s = species.get(i);
            History h = s.getHistory();
            List<Generation> gens = h.getGenerations();
            for(int k = 0; k < gens.size(); ++k){
                double avg = gens.get(k).getAverage();
                g.fillOval(k, (int)((1-avg/max_y_val)*max_y_real+30), 4, 4);
            }
        }
    }

    public synchronized void setHistory(List<Species> s){
        species = s;
        repaint();
    }
	
	public Dimension getPreferredSize(){
		return new Dimension(601, 481);
	}

}