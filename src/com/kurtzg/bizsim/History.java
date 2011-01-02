package com.kurtzg.bizsim;

import java.util.ArrayList;
import java.util.List;


public class History {

    //instance variables
    private List<Generation> generations = new ArrayList<Generation>();
    int species_id;

    public History(int species_id){
        this.species_id = species_id;
    }

    public void addGeneration(List<Agent> agents){
        generations.add(new Generation(agents, species_id));
    }

    public List<Generation> getGenerations(){
        return generations;
    }

    public int getSpeciesId(){
        return species_id;
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

    public Generation getLastGeneration(){
        return generations.get(generations.size());
    }
}
