package com.kurtzg.bizsim; /**
* File:			com.kurtzg.bizsim.Evolution.java
*
* Author:		Grant Kurtz
*
* Description:	Performs the actual crossover and mutation of a provided
*				list of agents.
*
*/


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Evolution{

	//instance vars
	Random generator = new Random();

	/*
	* Given a list of agents, the algorithm will take the mother/father
	* and produce 2 new children by altering the genome of the mother and
	* father.
	*
	*/
	public List<Agent> performCrossover(List<Agent> agents){
	
		//vars
		int mid = agents.size()/2, rand, gene_count;
		Agent father, mother;
		ArrayList<Agent> children = new ArrayList<Agent>();
		
		for(int i = 0; i < mid; ++i){
			
			//grab the parents
			father = agents.get(i);
			mother = agents.get(mid+i);
			
			//grab their respective chromosomes
			ArrayList<Boolean> father_chromes = father.getChrome();
			ArrayList<Boolean> mother_chromes = mother.getChrome();
			
			//grab the size of the chromosome
			//TODO: Need to replace the constant 3 with a dynamic value
			gene_count = father_chromes.size();
			
			ArrayList<Boolean> child = new ArrayList<Boolean>();
			child.addAll(father_chromes.subList(0,gene_count/2));
			child.addAll(mother_chromes.subList(gene_count/2,gene_count));
			
			//physically create two new agents, and imprint them with the new
			//chromosomal sequences just generated
			children.add(new Agent(child));
			child = new ArrayList<Boolean>();
			
			//create a child with an opposite copy property of the above child
			child.addAll(mother_chromes.subList(0, gene_count/2));
			child.addAll(father_chromes.subList(gene_count/2, gene_count));
			
			//again, imprint the child with the new chromosomal sequence
			children.add(new Agent(child));

		}
		
		return children;
	}
	
	public List<Agent> performMutation(List<Agent> agents){
		
		//vars
		int max_mutations = 16, rand,
				chrome_size = agents.get(0).getChrome().size();
		
		for(Agent a : agents){
			ArrayList<Boolean> mutated = a.getChrome();
			for(int i = 0; i < max_mutations;++i){
				rand = generator.nextInt(chrome_size);
				mutated.set(rand, !mutated.get(rand));
			}
			a.setChrome(mutated);
		}
		
		return agents;
	}
}