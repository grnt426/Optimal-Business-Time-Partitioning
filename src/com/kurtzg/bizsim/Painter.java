package com.kurtzg.bizsim;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Painter extends JPanel{

	//instance vars
    List<Integer> average_history = new ArrayList<Integer>();
	int generation_count;
	double max_y_val = 250000, max_y_real = 480.0;
	
	public void paint(Graphics g){

        super.paintComponent(g);

        setBackground(Color.white);

        if(average_history.size() == 0)
            return;

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

        //the color of our data point
        g.setColor(Color.green);
        int avg = 0;

        //draw the current history of all agents
		for(int i = 0; i < average_history.size(); ++i){
            avg = average_history.get(i);
            g.fillOval(i, (int)((1-avg/max_y_val)*max_y_real+30), 4, 4);
        }

		//just some data to print out
		//System.out.println("Average Gen #"+generation_count+" $"+avg+" VALUE:"+(1-avg/max_y_val)*max_y_real);

		generation_count++;
	}
	
	public synchronized void addAgents(List<Agent> agents){

        //vars
        int average = 0;

        //compute average performance of generation
		for(Agent a : agents){
			if(!a.isAgentIneffective())
				average+=a.getMoney();
            if(a.getName().equals("My Agent")){
                System.out.println("MY AGENT GOT:" + a.getMoney());
                System.out.println(a.getTotalHistory());
                System.out.println(a.getChrome());
            }
        }
		average = average/agents.size();
        average_history.add(average);

        //finally, perform a repaint
        repaint();
	}

    public void clearHistory(){
        average_history.clear();
        generation_count = 0;
    }

    public void saveHistory(){

    }

    public void setEliteAgentData(){

    }
	
	public Dimension getPreferredSize(){
		return new Dimension(601, 481);
	}
}