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
	final static int MAX_CHROMOSOME_LENGTH = 48;
	final static int MAX_GENE_LENGTH = 3;
	
	/*
	* Creates an empty order queue
	*/
	public Agent(){
		chromosome = new ArrayList<Boolean>();
		for(int i = 0; i < MAX_CHROMOSOME_LENGTH; ++i){
			chromosome.add(false);
		}
	}
	
	public Agent(ArrayList<Boolean> chromosome){
		this.chromosome = chromosome;
	}

	public ArrayList<Boolean> getChrome(){
		return chromosome;
	}
	
	public String toString(){
		String s;
		int i = 1;
		
		s = "{";
		for(Boolean b : chromosome){
			s += b ? "1":"0";
			if(i % MAX_GENE_LENGTH == 0){
				i = 0;
				s += ", ";
			}
			i++;
		}
		s = s.substring(0, s.length()-2)+"}";
		return s;
	}
}