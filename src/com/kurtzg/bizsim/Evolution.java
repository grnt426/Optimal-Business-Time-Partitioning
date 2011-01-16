package com.kurtzg.bizsim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
* File:			Evolution.java
*
* Author:		Grant Kurtz
*
* Description:	Performs the actual crossover and mutation of a provided
*				list of agents.
*
*/
public class Evolution{

	// instance vars
	Random generator = new Random();

	/*
	* Given a list of agents, the algorithm will take the mother/father
	* and produce 2 new children by altering the genome of the mother and
	* father.
	*
	* Param:    agents      the list of parents by which the children will have
	*                       their genetic blue prints copied from
	*
	* Returns:              a list of children with a mixed genome from their
	*                       parents
	*/
	public List<Agent> performCrossover(List<Agent> agents){
	
		// vars
		int mid = agents.size()/2, rand, gene_count;
		Agent father, mother;
		ArrayList<Agent> children = new ArrayList<Agent>();
        Random gen = new Random();
        //Collections.shuffle(agents);

        // grab agents from the start (highest income) and pair with agents
        // at the middle, moving down the list
		for(int i = 0; i < agents.size()-1; i+=2){
			
			// grab the parents
			father = agents.get(i);
			mother = agents.get(1+i);

			// grab their respective chromosomes
			ArrayList<Boolean> father_chromes = father.getChrome();
			ArrayList<Boolean> mother_chromes = mother.getChrome();
			
			// grab the size of the chromosome
			// TODO: Need to replace the constant 3 with a dynamic value
			gene_count = father_chromes.size();

            // grab a random point to mix father/mother genes
            rand = gen.nextInt(48);

            // create a new child with the father's genes first
			ArrayList<Boolean> child = new ArrayList<Boolean>();
            child.addAll(father_chromes.subList(0, rand));
			child.addAll(mother_chromes.subList(rand, gene_count));

			// physically create two new agents, and imprint them with the new
			// chromosomal sequences just generated
			children.add(new Agent(child));
			child = new ArrayList<Boolean>();
			
			// create a child with an opposite copy property of the above
            // child ie. mother's genes first
			child.addAll(mother_chromes.subList(0, rand));
			child.addAll(father_chromes.subList(rand, gene_count));
			
			// add our new child to the list of next-gen children
			children.add(new Agent(child));

		}
		
		return children;
	}

    /*
     * Randomly performs max_mutations on the entire chromosome of all agents
     *
     * Param:   agents      the list of agents to augment
     *
     * Returns:             a list of mutated children
     */
	public List<Agent> performMutation(List<Agent> agents){
		
		// vars
		int max_mutations = 6, rand,
				chrome_size = agents.get(0).getChrome().size();

        // grab all agents
		for(Agent a : agents){
			ArrayList<Boolean> mutated = a.getChrome();

            // perform a mutation randomly on any bit
			for(int i = 0; i < max_mutations;++i){
				rand = generator.nextInt(chrome_size);
				mutated.set(rand, !mutated.get(rand));
			}
			a.setChrome(mutated);
		}
		
		return agents;
	}
}