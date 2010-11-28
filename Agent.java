/**
* File: 		Agent.java
*
* Author: 		Grant Kurtz
*
* Description:	A simple container class meant to hold all information about
*				an agent, represented by a bit-string to store all of the
*				agent's actions through out a single business day.
*
*/

import java.util.ArrayList;


public class Agent{

	//instance variables
	ArrayList<Boolean> chromosome;

	/*
	* Creates an empty order queue
	*/
	public Agent(){
		for(int i = 0; i < chromosome.size(); ++i){
			chromosome.set(i, false);
		}
	}
	
	public Agent(ArrayList<Boolean> chromosome){
		this.chromosome = chromosome;
	}

	public ArrayList<Boolean> getChrome(){
		return chromosome;
	}

}