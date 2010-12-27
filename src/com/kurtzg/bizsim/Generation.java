package com.kurtzg.bizsim;

import java.util.ArrayList;
import java.util.List;


public class Generation {

    //instance variables
    private List<Agent> agents = new ArrayList<Agent>();

    public Generation(List<Agent> agents){
        this.agents = agents;
    }

    public List<Agent> getAgents(){
        return agents;
    }

    public Agent getAgent(int index){
        return agents.get(index);
    }

}
