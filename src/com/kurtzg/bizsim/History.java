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
        if(generations.size()<=index || index < 0)
            return null;
        return generations.get(index);
    }

    public void clearHistory(){
        generations.clear();
    }

    /*
     * Convenience method
     */
    public Agent getAgent(int genID, int agentID){
        return getGeneration(genID).getAgent(agentID);
    }

    public Generation getLastGeneration(){
        if(generations.isEmpty())
            return null;
        return generations.get(generations.size()-1);
    }
}
