package com.kurtzg.bizsim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Generation {

    //instance variables
    private List<Agent> agents = new ArrayList<Agent>();
    private double average = -1;
    private int species_id, gen_id, highest_income, lowest_income;
    private static int id = 0;

    public Generation(List<Agent> agents, int species_id){
        this.species_id = species_id;
        this.agents = agents;
        gen_id = id;
        id++;

        //sort the agents
        Collections.sort(agents);
        highest_income = agents.get(agents.size()-1).getMoney();
        lowest_income = agents.get(0).getMoney();
    }

    public List<Agent> getAgents(){
        return agents;
    }

    public Agent getAgent(int index){
        if(agents.size()<=index || index<0)
            return null;
        return agents.get(index);
    }

    public int getSpeciesId(){
        return species_id;
    }

    public int getID(){
        return gen_id;
    }

    public int getHighestIncome(){
        return highest_income;
    }

    public int getLowestIncome(){
        return lowest_income;
    }

    public double getAverage(){

        //avoid recomputing the average until necessary
        if(average == -1)
            computeAverage();

        return average;
    }

    private void computeAverage(){

        //vars
        double average = 0.0;
        int total = 0;

        //compute average performance of generation
		for(Agent a : agents){
			if(!a.isAgentIneffective()){
				average+=a.getMoney();
                total++;
            }
        }

        //set our global average to our newly computed value
		this.average = average/total;
    }

}
