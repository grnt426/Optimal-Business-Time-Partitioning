package com.kurtzg.bizsim;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ElitePainter extends JPanel{

    private List<Agent> elites = new ArrayList<Agent>();
    protected static final double MAX_X = 600.0, MAX_Y = 480.0,
            MAX_MONEY = 2000000.0;
    private List<Color> s_colors = new ArrayList<Color>();

    public ElitePainter(){

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

        setBackground(Color.WHITE);

        //draw our x- and y-axis
        for(int i = 0; i < 11; ++i){
            g.drawLine((int)(55*i), 0, (int)(55*i), (int)MAX_Y);
            g.drawString((i*10) + "", (int)(55*i)+1, (int)MAX_Y-2);
            g.drawLine(0, (int)(MAX_Y/10*i), (int)MAX_X, (int)(MAX_Y/10*i));
            if(i != 0)
                g.drawString("$"+(i*200000), 0, (int)(MAX_Y/10*(10-i)));
        }

        //draw each elite agent from all species
        for(int i = 0; i < elites.size(); ++i){
            g.setColor(s_colors.get(i));
            Agent a = elites.get(i);
            if(a == null)
                continue;
            List<Integer> income_history = a.getTotalHistory();
            for(int k = 0; k < income_history.size(); ++k){
                int income = income_history.get(k);
                g.fillOval((int)(k*5.5), (int)((1-income/MAX_MONEY)*MAX_Y), 3, 3);
            }
        }
    }

    public void setElites(List<Agent> elites){
        this.elites = elites;
        repaint();
    }

    public Dimension getPreferredSize(){
        return new Dimension((int)MAX_X+1, (int)MAX_Y+1);
    }
}
