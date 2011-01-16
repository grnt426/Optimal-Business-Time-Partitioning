package com.kurtzg.bizsim;

/**
* File: 		Agent.java
*
* Author: 		Grant Kurtz
*
* Description:	A simple container class meant to hold all information about
*				an agent, represented by a bit-string to store all of the
*				agent's actions through out a single business day.
*
* TODO:         Need to create a gene class for easy creation of individual
* TODO:         actions
*/

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Agent implements Comparable<Agent>, Cloneable{

	//instance variables
	private ArrayList<Boolean> chromosome;
	private final static int MAX_CHROMOSOME_LENGTH = 48;
	private final static int MAX_GENE_LENGTH = 3;
	private int money, rms, lqfg, mqfg, hqfg, max_income_history;
    private List<Integer> income_history = new ArrayList<Integer>();
	private boolean currently_storing, ineffective;
	private Random gen = new Random();
    private List<Integer> total_history = new ArrayList<Integer>();
    private String name = "Generated";
	
	/*
	* Creates a random order queue for our agent
	*/
	public Agent(){
		chromosome = new ArrayList<Boolean>();
		for(int i = 0; i < MAX_CHROMOSOME_LENGTH; ++i){
			chromosome.add(gen.nextBoolean());
		}
		currently_storing = false;
		money = 50000;
        max_income_history = 5;
	}

    // Copy-Constructor
    public Agent(Agent agent){

        if(agent == null)
               return;

        // copy all the arrays first
        chromosome = new ArrayList<Boolean>(agent.getChrome());
        income_history = new ArrayList<Integer>(agent.income_history);

        // copy over the primitives next
        name = agent.name;
        currently_storing = agent.currently_storing;
        ineffective = agent.ineffective;
        money = agent.money;
        rms = agent.rms;
        lqfg = agent.lqfg;
        mqfg = agent.mqfg;
        hqfg = agent.hqfg;
        max_income_history = agent.max_income_history;

    }

    public Agent clone(){
        return new Agent(this);
    }

    public void setName(String n){
        name = n;
    }

    public String getName(){
        return name;
    }
	
	public Agent(ArrayList<Boolean> chromosome){
		this.chromosome = chromosome;
		currently_storing = false;
	}

	public ArrayList<Boolean> getChrome(){
		return chromosome;
	}

    /*
     * Nothing special, just makes sure the agent has all the needed bits
     * necessary to represent a complete day's actions
     *
     * Returns                  true if the agent has the correct number of
     *                          bits, otherwise false
     */
    public boolean hasWellFormedGenome(){
        return chromosome.size() == MAX_CHROMOSOME_LENGTH;
    }
	
	public void setChrome(ArrayList<Boolean> chrome){
		chromosome = chrome;
	}

    /*
     * This is a convenience method so that a human can quickly create a test
     * agent.  All spaces are ignored (so that strings can be created like
     * "000 010 111"), the rest are translated to the appropriate true/false
     * values
     *
     * TODO:                    Consider throwing an exception if the genome
     * TODO:                    is malformed
     *
     * Param    chrome          A string that represents the agent's genome
     *
     * Returns                  true if the given genome is well formed
     */
	public boolean setChrome(String chrome){
		chromosome = new ArrayList<Boolean>();
		for(Character c : chrome.toCharArray()){
			if(c == ' ')
				continue;
			chromosome.add(c == '0' ? false : true);
		}
        return hasWellFormedGenome();
	}

    /*
     * Will reset the agent to default values as if the simulation were never
     * run (however, the complete history is still preserved)
     */
	public void reset(){
		money = 50000;
		currently_storing = false;
        income_history.clear();
		dumpGoods();
	}
	
	public int getMoney(){
		return money;
	}
	
	public int getRawMaterials(){
		return rms;
	}
	
	public int getProducedLQ(){
		return lqfg;
	}
	
	public int getProducedMQ(){
		return mqfg;
	}
	
	public int getProducedHQ(){
		return hqfg;
	}

    /*
     * Huge issues with this method, but it is supposed to take a moving and
     * then total average over 5 days to see if this agent is meeting a
     * threshold of income growth.
     *
     * Returns                  the average percentage of growth from each day
     *                          to the next.
     */
    public double getIncomeRatio(){

        //if we do not have a complete history, simply return 1
        if(max_income_history != income_history.size())
            return 2;

        //don't bother calculating it again if we are already marked useless
        if(isAgentIneffective())
            return 0;

        //otherwise process an average of income growth
        double average = 0.0;
        for(int i =0; i < income_history.size()-1; ++i){
            if(income_history.get(i+1) == 0 || income_history.get(i) == 0)
                continue;
            average += income_history.get(i+1) / income_history.get(i);
        }
        return average / income_history.size();
    }
	
	public void setMoney(int money){
		this.money = money;
	}
	
	public void adjustMoney(int money){
		this.money += money;
	}
	
	public void setRawMaterials(int rms){
		this.rms = rms;
	}
	
	public void adjustRawMaterials(int rms){
		this.rms += rms;
	}
	
	public void setProducedLQ(int lqfg){
		this.lqfg = lqfg;
	}
	
	public void setProducedMQ(int mqfg){
		this.mqfg = mqfg;
	}
	
	public void setProducedHQ(int hqfg){
		this.hqfg = hqfg;
	}
	
	public void adjustProducedLQ(int lqfg){
		this.lqfg += lqfg;
	}
	
	public void adjustProducedMQ(int mqfg){
		this.mqfg += mqfg;
	}
	
	public void adjustProducedHQ(int hqfg){
		this.hqfg += hqfg;
	}
	
	public void setCurrentlyStoring(boolean store){
		currently_storing = store;
	}
	
	public boolean storingExcess(){
		return currently_storing;
	}

    public boolean isAgentIneffective(){
        return ineffective;
    }

    public List<Integer> getTotalHistory(){
        return total_history;
    }

    /*
     * Resets held Raw Materials and Finished Goods, usually called when the
     * agent did not store their goods at the end of the day
     */
	public void dumpGoods(){
		rms = 0;
		lqfg = 0;
		mqfg = 0;
		hqfg = 0;
	}

    /*
     * Stores a running history of the agent's income over time, as well as
     * caching the most recent income history.
     */
    public void tabulateIncomeHistory(){
        income_history.add(money);
        if(total_history.size()!=100)
            total_history.add(money);
        if(income_history.size() > max_income_history)
            income_history.remove(0);
    }

    /*
     * Calling this method will disable this agent from most tasks, averages
     * method calls, etc.  Only call this method if the agent will never
     * be used outside of just retaining for history's sake
     */
    public void markAgentIneffective(){
        ineffective = true;
    }

    /*
     * Prints out a representation of the agent's actions as sequence of
     * grouped binary numbers, ie. {000, 010, 111}
     *
     * Returns                  a formatted string representation of the agent
     */
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

    /*
     * Compares this agent to another agent based on total standing money
     *
     * Param    a               the agent to compare against
     *
     * Returns                  -1 if this agent has less money than the given,
     *                          0 if we have the same amount of money, and 1
     *                          if this agent has more money
     */
	public int compareTo(Agent a){
		a = (Agent) a;
		if(money < a.getMoney())
			return -1;
		if(money > a.getMoney())
			return 1;
		return 0;
	}

    /*
     * Prints out the Agent's current inventory
     */
	public String printState(){
		return "Money: " + getMoney() + "\nRMs: " + getRawMaterials() 
			+ "\nLow-Quality FGs: " + getProducedLQ() 
			+ "\nMedium-Quality FGs: " + getProducedMQ() 
			+ "\nHigh-Quality FGs: " + getProducedHQ() + "\nStoring Excess: "
			+ storingExcess();
	}
}