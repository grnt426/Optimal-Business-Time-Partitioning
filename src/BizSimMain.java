/**
* File:			BizSimMain.java
*
* Author:		Grant Kurtz
*
* Description:	Handles the initialization of the program, asks for user input
*				on environment and simulation values, and then begins separate
*				threads to begin processing.
*
*/

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import javax.swing.*;

public class BizSimMain{

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
	
		for(int k = 0; k < numThreads; ++k){
		
			//create some test agents
			ArrayList<Agent> agents = new ArrayList<Agent>();
			for(int i = 0; i < numAgents; ++i){
				agents.add(new Agent());
			}
			
			Thread t = new Thread(new ProcessSimulator(agents, numGenerations));
			t.setPriority(9);
			t.start();
		}
	}
	
	public class ProcessSimulator implements Runnable{
	
		//vars
		Environment e;
		List<Agent> children = new ArrayList<Agent>();
		//List<List<Agent>> history = new ArrayList<List<Agent>>();
		int generations, cur_gen, total_agents;
		JFrame window = new JFrame();
		Painter paint = new Painter();
		Evolution evo = new Evolution();
		
		public ProcessSimulator(ArrayList<Agent> agents, int generations){
			children = agents;
            total_agents = agents.size();
			e = new Environment();
			this.generations = generations;
			cur_gen = 0;
			window.setTitle("Average Ability of Each Generation");
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.getContentPane().add(paint);
			window.pack();
			window.setVisible(true);

		}
	
		public void run(){
			
			//run for the number of generations specified
			while(cur_gen < generations){
				
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