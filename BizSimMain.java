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

public class BizSimMain{

	public static void main(String[] args){

		new BizSimMain();
		
	}
	
	public BizSimMain(){
	
		for(int k = 0; k < 100; ++k){
		
			//create some test agents
			ArrayList<Agent> agents = new ArrayList<Agent>();
			for(int i = 0; i < 1000; ++i){
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
		
			//Process 100-days, print the best (or sets of best)
			for(int i = 0; i<100; i++){
				e.simulateDay();
			}
			
			int mostMoney = 0;
			ArrayList<Agent> winners = new ArrayList<Agent>();
			for(Agent a : agents){
				if(a.getMoney() > mostMoney && a.getMoney() != 50000
						&& a.getMoney() > 1000){
					mostMoney = a.getMoney();
					winners.clear();
					winners.add(a);
				}
				else if(a.getMoney() == mostMoney && mostMoney != 0)
					winners.add(a);
			}
			
			for(Agent a : winners)
				System.out.println(a.getMoney());
		}
	}
}