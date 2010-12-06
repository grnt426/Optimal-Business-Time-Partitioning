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
			t.start();
		}
	}
	
	public class ProcessSimulator implements Runnable{
	
		//vars
		Environment e;
		List<Agent> children = new ArrayList<Agent>();
		int generations, cur_gen;
		
		public ProcessSimulator(ArrayList<Agent> agents, int generations){
			children = agents;
			e = new Environment();
			this.generations = generations;
			cur_gen = 0;
		}
	
		public void run(){
			
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
				
				if(cur_gen == generations - 1)
					for(Agent a : children.subList(0, 21))
						System.out.println(a.getMoney());
				
				//now we need to select agents for crossover, mutation, and elites
				//for elites, keep the first one
				Agent elite = children.get(0);
				elite.reset();
				
				//for parents to keep, simply choose the top 20% (excluding the elite)
				List<Agent> parents = children.subList(1, (int)(children.size()*.2));
				
				//Create an instance of the Evolution class
				Evolution evo = new Evolution();
				
				//create some children, and perform mutations
				children = evo.performCrossover(parents);
				children = evo.performMutation(children);
				
				//add the elite agent into the pool
				children.add(elite);
				
				//refill the agent pool with random agents
				for(int i = 0; i < 79; ++i){
					children.add(new Agent());
				}
				
				cur_gen++;
			}
		}
	}
}