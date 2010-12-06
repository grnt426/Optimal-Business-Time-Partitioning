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
			
			Thread t = new Thread(new ProcessSimulator(agents));
			t.start();
		}
	}
	
	public class ProcessSimulator implements Runnable{
	
		//vars
		ArrayList<Agent> agents;
		Environment e;
		
		public ProcessSimulator(ArrayList<Agent> agents){
			this.agents = agents;
			e = new Environment();
			e.addAgents(this.agents);
		}
	
		public void run(){
		
			//Process 100-days
			for(int i = 0; i<100; i++){
				e.simulateDay();
			}
			
			//sort the results by money, descending
			Collections.sort(agents);
			Collections.reverse(agents);
			
			
			for(Agent a : agents)
				System.out.println(a.getMoney());
				
			//now we need to select agents for crossover, mutation, and elites
			
			
		}
	}
}