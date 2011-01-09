package com.kurtzg.bizsim;


import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AgentPainter extends JPanel{

    //instance vars
    private Agent agent;
    private int max_x = 600, max_y = 480;
    private double MAX_MONEY = 2000000;

    public void paint(Graphics g){

        super.paintComponent(g);
        setBackground(Color.WHITE);

        //draw our x- and y-axis
        for(int i = 0; i < 11; ++i){
            g.drawLine((55*i), 0, (55*i), (int)max_y);
            g.drawString((i*10) + "", (55*i)+1, (int)max_y-2);
            g.drawLine(0, (int)(max_y/10*i), (int)max_x, (int)(max_y/10*i));
            if(i != 0)
                g.drawString("$"+(i*200000), 0, (int)(max_y/10*(10-i)));
        }

        if(agent == null)
            return;

        List<Integer> income_history = agent.getTotalHistory();
            for(int k = 0; k < income_history.size(); ++k){
                int income = income_history.get(k);
                g.fillOval((int)(k*5.5), (int)((1-income/MAX_MONEY)*max_y), 3, 3);
            }
    }

    public void setAgent(Agent a){
        agent = a;
        repaint();
    }

    public Dimension getPreferredSize(){
        return new Dimension(max_x+1, max_y+1);
    }
}
