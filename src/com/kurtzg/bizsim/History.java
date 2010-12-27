package com.kurtzg.bizsim;

import java.util.ArrayList;
import java.util.List;


public class History {

    //instance variables
    private List<Generation> generations = new ArrayList<Generation>();

    public History(){

    }

    public void addGeneration(List<Agent> agents){
        generations.add(new Generation(agents));
    }

    public List<Generation> getGenerations(){
        return generations;
    }

    public Generation getGeneration(int index){
        return generations.get(index);
    }

    /*
     * Convenience method
     */
    public Agent getAgent(int genID, int agentID){
        return getGeneration(genID).getAgent(agentID);
    }
}
