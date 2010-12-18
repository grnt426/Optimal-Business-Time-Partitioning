package com.kurtzg.bizsim;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grnt426
 * Date: 12/15/10
 * Time: 12:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class ElitePainter extends JPanel{

    private Agent elite;
    protected static final double MAX_X = 600.0, MAX_Y = 480.0, MAX_MONEY = 2000000.0;

    public void paint(Graphics g){

        super.paintComponent(g);

        setBackground(Color.WHITE);

        //don't paint if the agent was never set
        if(elite == null){
            return;
        }

        //draw our x- and y-axis
        for(int i = 0; i < 11; ++i){
            g.drawLine((int)(55*i), 0, (int)(55*i), (int)MAX_Y);
            g.drawString((i*10) + "", (int)(55*i)+1, (int)MAX_Y-2);
            g.drawLine(0, (int)(MAX_Y/10*i), (int)MAX_X, (int)(MAX_Y/10*i));
            if(i != 0)
                g.drawString("$"+(i*200000), 0, (int)(MAX_Y/10*(10-i)));
        }

        //draw the agent's performance over time
        List<Integer> income_history = elite.getTotalHistory();
        g.setColor(Color.GREEN);
        for(int i = 0; i < income_history.size(); ++i){
            int income = income_history.get(i);
            g.fillOval((int)(i*5.5), (int)((1-income/MAX_MONEY)*MAX_Y), 3, 3);
        }
    }

    public void setNewElite(Agent e){
        elite = e;
    }

    public Dimension getPreferSize(){
        return new Dimension((int)MAX_X+1, (int)MAX_Y+1);
    }
}
