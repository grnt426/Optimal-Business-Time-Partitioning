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

	// instance vars
    private List<Species> species;
    private CoordinateContainer coordinates;
    private List<Color> s_colors = new ArrayList<Color>();
    private int hover_x, hover_y, max_x = 600, max_y = 480, highest_money,
        lowest_money;
    private Coordinate last_hovered_gen;
    private boolean changed = false;
    private Dimension size;

    public Painter(){

        // setup some basic colors for our species
        s_colors.add(Color.red);
        s_colors.add(Color.green);
        s_colors.add(Color.blue);
        s_colors.add(Color.black);
        s_colors.add(Color.yellow);
        s_colors.add(Color.magenta);
        s_colors.add(Color.orange);
        s_colors.add(Color.pink);

        // setup defaults
        hover_x = -1;
        highest_money = 60000;
        lowest_money = -1;
        size = new Dimension(max_x, max_y);

        // initialize some clases
        coordinates = new CoordinateContainer();
    }

	public void paint(Graphics g){

        super.paintComponent(g);

        setBackground(Color.white);

        // draw the income line separators
        g.setColor(Color.gray);
        CoordinateContainer income_seps = new CoordinateContainer();
        for(int i = 4; i >= 0; --i){
            int y =  max_y - (max_y/4*i);
            String money = "$"+i*(highest_money)/4;
            if(i != 0){
                g.drawString(money, 5, y+15);
                income_seps.addPoint(5, y+15, money);
            }
            g.drawLine(0, y, getSize().width, y);
        }

        // draw the generational count separators
        for(int i = 0; i < this.getSize().width/100+1; ++i){
            int x = i*100;

            if(i!=0)
                g.drawString("Gen: "+(i*100), x+1, max_y-2);

            // redraw the income separators
            if(i % 5 == 0 && i != 0){
                for(Coordinate c : income_seps.getAllCoordinates())
                    g.drawString((String)c.getData(), c.getX()+(i*100), c.getY());
            }
            g.drawLine(x, 0, x, max_y);
        }

        if(species == null)
            return;

        // clear the coordinate array for the next data
        // TODO: should probably have some way of caching the data instead of
        // blowing it away each time...could be very difficult/CPU intensive
        coordinates.clear();

        // draws all the generations based on income
        for(int i = 0; i < species.size(); ++i){
            g.setColor(s_colors.get(i));
            Species s = species.get(i);
            History h = s.getHistory();
            List<Generation> gens = h.getGenerations();

            // make sure our window has enough size for the whole history
            if(size.getWidth() < gens.size())
                size.setSize(gens.size(), size.getHeight());

            for(int k = 0; k < gens.size(); ++k){
                double avg = gens.get(k).getAverage();
                int y = max_y - (int)((avg) / highest_money * max_y);
                g.fillOval(k, y, 3, 3);

                // cache this data for later use
                coordinates.addPoint(k, y, gens.get(k));
            }
        }


        // used for drawing the targeting box
        if(hover_x != -1){

            // find the closest point
            Coordinate gen = last_hovered_gen;
            if(changed){
                gen = coordinates.getClosestPointAt(hover_x, hover_y);
                last_hovered_gen = gen;
            }

            if(gen != null){

                // vars
                int y = gen.getY(), x = gen.getX();
                double avg = ((Generation)gen.getData()).getAverage();
                g.setColor(Color.black);

                // ->
                g.drawLine(0, y+1, x-4, y+1);

                // <-
                g.drawLine(getSize().width, y+1, x+7, y+1);

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
                if(total > getSize().width)
                    x -= text_len + 16;

                g.setColor(Color.BLACK);
                g.fillRect(x, y-13, text_len, 13);
                g.setColor(Color.GREEN);
                g.drawString("$" + avg, x, y-3);
            }
        }
    }

    public synchronized void setHistory(List<Species> s){
        species = s;

        // determine the boundary for highest money
        for(Species spec : species){
            Generation gen = spec.getHistory().getLastGeneration();

            // we could be at the start of our program, where there may be no
            // starting generations
            if(gen == null)
                continue;

            double avg = gen.getAverage();

            // only increase the y-axis once a generation reaches 108% of the
            // previously highest generation average
            if(avg > (highest_money*.9))
                highest_money = (int)(avg*1.2);

            // same as above, except for the lower bound must fall below 72%
            // of the previously lowest generation
            if(avg < (lowest_money*.9) || lowest_money == -1)
                lowest_money = (int)(avg*.8);
        }

        repaint();
    }

    public Generation getClickedGeneration(int x, int y){

        //vars
        Generation clicked = null;

        clicked = (Generation) coordinates.getClosestDataAt(x, y);

        return clicked;
    }

    public void setHoveringOver(int x, int y){

        // in case we need to repaint, but haven't changed
        if(hover_x != x || hover_y != y)
            changed = true;
        else
            changed = false;

        // store for use, then call ourselves to update the graph
        hover_x = x;
        hover_y = y;
        repaint();
    }

	public Dimension getPreferredSize(){
        return size;
    }

}