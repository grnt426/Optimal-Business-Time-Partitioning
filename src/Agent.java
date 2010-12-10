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
import java.util.List;
import java.util.Random;

public class Agent implements Comparable<Agent>{

	//instance variables
	private ArrayList<Boolean> chromosome;
	private final static int MAX_CHROMOSOME_LENGTH = 48;
	private final static int MAX_GENE_LENGTH = 3;
	private int money, rms, lqfg, mqfg, hqfg, max_income_history;
    private List<Integer> income_history = new ArrayList<Integer>();
	private boolean currently_storing, ineffective;
	private Random gen = new Random();
	
	/*
	* Creates an empty order queue
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
	
	public Agent(ArrayList<Boolean> chromosome){
		this.chromosome = chromosome;
		currently_storing = false;
	}

	public ArrayList<Boolean> getChrome(){
		return chromosome;
	}
	
	public void setChrome(ArrayList<Boolean> chrome){
		chromosome = chrome;
	}
	
	public void setChrome(String chrome){
		chromosome = new ArrayList<Boolean>();
		for(Character c : chrome.toCharArray()){
			if(c == ' ')
				continue;
			chromosome.add(c == '0' ? false : true);
		}
	}
	
	public void reset(){
		money = 50000;
		currently_storing = false;
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

    public double getIncomeRatio(){

        //if we do not have a complete history, simply return 0
        if(max_income_history != income_history.size())
            return 1;

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
	
	public void dumpGoods(){
		rms = 0;
		lqfg = 0;
		mqfg = 0;
		hqfg = 0;
	}

    public void tabulateIncomeHistory(){
        income_history.add(money);
        if(income_history.size() > max_income_history)
            income_history.remove(0);
    }

    public void markAgentIneffective(){
        ineffective = true;
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
	
	public int compareTo(Agent a){
		a = (Agent) a;
		if(money < a.getMoney())
			return -1;
		if(money > a.getMoney())
			return 1;
		return 0;
	}
	
	public String printState(){
		return "Money: " + getMoney() + "\nRMs: " + getRawMaterials() 
			+ "\nLow-Quality FGs: " + getProducedLQ() 
			+ "\nMedium-Quality FGs: " + getProducedMQ() 
			+ "\nHigh-Quality FGs: " + getProducedHQ() + "\nStoring Excess: "
			+ storingExcess();
	}
}