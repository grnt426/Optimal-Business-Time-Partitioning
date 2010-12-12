package com.kurtzg.bizsim; /**
* File:			com.kurtzg.bizsim.BizSimMain.java
*
* Author:		Grant Kurtz
*
* Description:	Handles the initialization of the program, asks for user input
*				on environment and simulation values, and then begins separate
*				threads to begin processing.
*
*/


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import javax.swing.*;

public class BizSimMain implements ActionListener{

    //global instance variables
    JButton start_stop = new JButton("Start/Stop");
    List<ProcessSimulator> species = new ArrayList<ProcessSimulator>();

	public static void main(String[] args){
		
		//process arguments
		if(args.length != 3){
			System.err.println("WTF? I need the correct number of arguments");
			System.exit(0);
		}
		
		//vars
		int numThreads = 0, numAgents = 0, numGenerations = 0;
		
		try{
			numThreads = Integer.parseInt(args[0]);
			numAgents = Integer.parseInt(args[1]);
			numGenerations = Integer.parseInt(args[2]);
		}
		catch(NumberFormatException nfe){
			System.err.println(nfe);
			System.exit(0);
		}
		
		//Initialize our Main Class's simulator
		new BizSimMain(numThreads, numAgents, numGenerations);
		
	}
	
	public BizSimMain(int numThreads, int numAgents, int numGenerations){

        JFrame control_window = new JFrame();
        control_window.setTitle("Control Suite");
        control_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        control_window.setSize(new Dimension(640, 480));
        control_window.setAlwaysOnTop(true);
        control_window.setLayout(new BorderLayout());

        JPanel species_control = new JPanel();
        JLabel species_list_l = new JLabel("Species Running");
        DefaultListModel species_list = new DefaultListModel();

        //create our Environment
        Environment e = new Environment();

        //create all our threads
		for(int kittehsex = 0; kittehsex < numThreads; ++kittehsex){
		
			//create some test agents
			ArrayList<Agent> agents = new ArrayList<Agent>();
			for(int i = 0; i < numAgents; ++i){
				agents.add(new Agent());
			}

            //create our new thread and pass in some control variables
            ProcessSimulator ps = new ProcessSimulator(agents, numGenerations, kittehsex, e);
			Thread t = new Thread(ps);
            species.add(ps);

            //update our current species list
            species_list.addElement("Species #"+kittehsex);

            //set the thread to a high priority, and then start it
			t.setPriority(9);
			t.start();
		}

        JList species_select_list = new JList(species_list);
        species_select_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        species_select_list.setLayoutOrientation(JList.VERTICAL);
        species_select_list.setVisibleRowCount(3);

        //create a button to control the running state of all threads
        start_stop.addActionListener(this);
        control_window.add(start_stop, BorderLayout.SOUTH);

        //add our components to the window
        species_control.add(species_list_l);
        species_control.add(species_select_list);
        control_window.add(species_control, BorderLayout.WEST);
        control_window.setVisible(true);
	}

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == start_stop){

            //tell all threads to continue/resume operation
            for(ProcessSimulator ps : species)
                if(ps.toggleRunning())
                    synchronized (ps){
                        ps.notify();
                    }
        }
    }

    public class ProcessSimulator implements Runnable{
	
		//vars
		private Environment e;
		private List<Agent> children = new ArrayList<Agent>();
		//List<List<com.kurtzg.bizsim.Agent>> history = new ArrayList<List<com.kurtzg.bizsim.Agent>>();
		private int generations, cur_gen, total_agents, id;
		private JFrame window = new JFrame();
		private Painter paint = new Painter();
		private Evolution evo = new Evolution();
        private boolean running;
		
		public ProcessSimulator(ArrayList<Agent> agents, int generations, int id, Environment e){
            this.e = e;
            this.id = id;
			children = agents;
            total_agents = agents.size();
			this.generations = generations;
			cur_gen = 0;
			window.setTitle("Average Performance of Each Generation #" + id);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.getContentPane().add(paint);
			window.pack();
			window.setVisible(true);
            running = true;
		}
	
        public int getID(){
            return id;
        }

        public boolean toggleRunning(){
            running = !running;
            return running;
        }

        public void replaceEnvironment(Environment e){
            this.e = e;
        }

		public void run(){

            while(true){
                if(!running){
                    synchronized (this){
                        try{
                            wait();
                        }
                        catch(InterruptedException ie){

                        }
                    }
                }

                //run for the number of generations specified
                while(cur_gen < generations && running){

                    //create our list of agents
                    e.purgeAgents();
                    e.addAgents(children);

                    //Process 100-days
                    for(int i = 0; i<100; i++){
                        e.simulateDay();
                    }

                    //sort the results by money, descending
                    Collections.sort(children);
                    Collections.reverse(children);

                    paint.addAgents(children);

                    //history.add(children);

                    //print out the last generation
                    if(cur_gen == generations)
                        for(Agent a : children.subList(0, 41))
                            System.out.println(a.getMoney());

                    //now we need to select agents for crossover, mutation, and elites
                    //for elites, keep the first 5%
                    List<Agent> elites = children.subList(0, (int)(children.size()*.05));

                    //for parents to keep, simply choose the top 80% (including the elites)
                    List<Agent> parents = children.subList(0,
                                            (int)(children.size()*.8));

                    //create some children, and perform mutations
                    children = evo.performCrossover(parents);
                    children = evo.performMutation(children);

                    //add the elite agents into the pool
                    children.addAll(elites);

                    System.out.println(elites.get(0) + ": " + elites.get(0).getMoney());

                    //reset the elites for the next generation
                    for(Agent elitea : elites)
                        elitea.reset();

                    //refill the agent pool with random agents
                    while(children.size() < total_agents){
                        children.add(new Agent());
                    }

                    cur_gen++;
                }
            }
		}
	}
}