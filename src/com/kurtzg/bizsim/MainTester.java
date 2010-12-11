package com.kurtzg.bizsim;

import java.util.ArrayList;
import java.util.List;

public class MainTester{

	public static void main(String[] args){
		System.out.println("Testing Integrity of com.kurtzg.bizsim.Agent Class");
		Agent a = new Agent();
		System.out.println(a+"\n");
		
		//add some money and basic hard-coded actions
		a.setMoney(50000);
		System.out.println(a);
		
		//testing default values
		System.out.println("Printing the current state of the com.kurtzg.bizsim.Agent");
		System.out.println(a.printState());
		
		//adding agent
		Environment e = new Environment();
		e.addAgent(a);
		e.addAgent(a);
		
		//Simulate a business cycle for all agents
		for(int i = 0; i < 100;++i){
			e.simulateDay();
		}
		System.out.println("Printing the current state of the com.kurtzg.bizsim.Agent");
		System.out.println(a.printState());
		
		//create com.kurtzg.bizsim.Evolution chamber
		Evolution chamber = new Evolution();
		ArrayList<Agent> first_gen = new ArrayList<Agent>();
		first_gen.add(a);
		first_gen.add(new Agent());
		System.out.println("Parents:");
		for(Agent f : first_gen)
			System.out.println(f);
		List<Agent> children = chamber.performCrossover(first_gen);
		System.out.println("Children:");
		for(Agent c : children)
			System.out.println(c);
		System.out.println("Mutated Children:");
		children = chamber.performMutation(children);
		for(Agent m : children)
			System.out.println(m);
	}
}