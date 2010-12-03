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
	final static int MAX_CHROMOSOME_LENGTH = 64;
	final static int MAX_GENE_LENGTH = 4;
	int money, rms, lqfg, mqfg, hqfg;
	boolean currently_storing;
	
	/*
	* Creates an empty order queue
	*/
	public Agent(){
		chromosome = new ArrayList<Boolean>();
		for(int i = 0; i < MAX_CHROMOSOME_LENGTH; ++i){
			chromosome.add(false);
		}
		currently_storing = false;
	}
	
	public Agent(ArrayList<Boolean> chromosome){
		this.chromosome = chromosome;
		currently_storing = false;
	}

	public ArrayList<Boolean> getChrome(){
		return chromosome;
	}
	
	public void setChrome(String chrome){
		chromosome = new ArrayList<Boolean>();
		for(Character c : chrome.toCharArray()){
			if(c == ' ')
				continue;
			chromosome.add(c == '0' ? false : true);
		}
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
	
	public String printState(){
		return "Money: " + getMoney() + "\nRMs: " + getRawMaterials() 
			+ "\nLow-Quality FGs: " + getProducedLQ() 
			+ "\nMedium-Quality FGs: " + getProducedMQ() 
			+ "\nHigh-Quality FGs: " + getProducedHQ() + "\nStoring Excess: "
			+ storingExcess();
	}
}