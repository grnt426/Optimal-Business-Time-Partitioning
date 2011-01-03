package com.kurtzg.bizsim;

import java.util.ArrayList;
import java.util.List;


public class Generation {

    //instance variables
    private List<Agent> agents = new ArrayList<Agent>();
    private double average = -1;
    private int species_id;

    public Generation(List<Agent> agents, int species_id){
        this.species_id = species_id;
        this.agents = agents;
    }

    public List<Agent> getAgents(){
        return agents;
    }

    public Agent getAgent(int index){
        return agents.get(index);
    }

    public int getSpeciesId(){
        return species_id;
    }

    public double getAverage(){

        //avoid recomputing the average until necessary
        if(average == -1)
            computeAverage();

        return average;
    }

    public void computeAverage(){

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
