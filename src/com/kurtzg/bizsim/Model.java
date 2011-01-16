package com.kurtzg.bizsim;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Model implements ActionListener{

    //instance variables
    private List<ActionListener> listeners = new ArrayList<ActionListener>();
    private List<Species> species = new ArrayList<Species>();
    private Vector<Agent> elites = new Vector<Agent>();
    private int max_gen_count, max_agent_count, max_day_count, total_running;
    private double max_elite_percent, max_parent_percent;
    private boolean running, copyingElites;
    private Environment environment;

    /*
     * Default constructor, nothing fancy
     */
    public Model(){

        // set up our default values
        max_gen_count = 100;
        max_agent_count = 100;
        max_day_count = 100;
        max_elite_percent = .25;
        max_parent_percent = .7;
        total_running = 0;

        // initialize some classes
        environment = new Environment();

        // global state values
        running = true;
        copyingElites = false;
    }

    public int getMaxGenCount(){
        return max_gen_count;
    }

    public void setMaxGenCount(int gen_count){
        max_gen_count = gen_count;
        for(Species s : species)
            s.setGenerationCount(max_gen_count);
    }

    public int getTotalSpecies(){
        return species.size();
    }

    public int getMaxAgentCount(){
        return max_agent_count;
    }

    public void setMaxAgentCount(int agent_count){
        max_agent_count = agent_count;
        for(Species s : species){
            s.setAgentCount(max_agent_count);
        }
    }

    public int getDayCount(){
        return max_day_count;
    }

    public void setMaxDayCount(int day_count){
        max_day_count = day_count;
        for(Species s : species)
            s.setDayCount(max_day_count);
    }

    public void setElitePercent(double elite_percent){
        max_elite_percent = elite_percent;
        for(Species s : species)
            s.setElitePercent(max_elite_percent);
    }

    public double getElitePercent(){
        return max_elite_percent;
    }

    public void setParentPercent(double parent_percent){
        max_parent_percent = parent_percent;
        for(Species s : species){
            s.setParentPercent(max_parent_percent);
        }
    }

    public double getParentPercent(){
        return max_parent_percent;
    }

    public Environment getEnvironment(){
        return environment;
    }

    public List<Agent> getElites(){
        copyingElites = true;
        return elites;
    }

    public void donCopyingElites(){
        copyingElites = false;
        synchronized (this){
            notifyAll();
        }
    }

    public boolean reconfigureState(Environment e, int agent_count, int day_count,
                                 int gen_count, double elite_percent,
                                 double parent_percent){

        //for now, don't allow modification unless we aren't running
        if(running){
            processError("Must Pause Simulation Before Reconfiguring!");
            return false;
        }

        //override old values
        replaceEnvironment(e);
        setElitePercent(elite_percent);
        setParentPercent(parent_percent);
        setMaxAgentCount(agent_count);
        setMaxDayCount(day_count);
        setMaxGenCount(gen_count);
        return true;
    }


    /*
     * Creates an arbitrary number of species to be added to the total pool
     */
    public void creteNewSpecies(int num){

        // vars
        Environment e;//TODO: this should copy the current environment

        // create a new thread, environment, etc for each new species
        for(int i = 0; i < num; ++i){
            e = new Environment();
            Species s = new Species(max_agent_count, max_gen_count,
                    species.size(), e, this);
            s.fillWithAgents();

            // setup our simulation constraints
            s.setDayCount(max_day_count);
            s.setElitePercent(max_elite_percent);
            s.setGenerationCount(max_gen_count);
            s.setParentPercent(max_parent_percent);
            s.setAgentCount(max_agent_count);

            incrementRunning();

            // create the thread and run
            Thread t = new Thread(s);
            t.setPriority(8);
            t.start();
            species.add(s);
        }

        // fill the elite class
        while(elites.size() < species.size())
                    elites.add(null);
    }

    public synchronized void incrementRunning(){
        total_running++;
    }

    public synchronized void decrementRunning(){
        total_running--;
        checkStillRunning();
    }

    public void checkStillRunning(){
        if(total_running == 0)
            running = false;
    }

    /*
     * Adds an object that wants updates on changes to the Model
     */
    public void addListeners(ActionListener al){
        listeners.add(al);
    }

    /*
     * Handles informing all listeners of a change in the model
     */
    private void processEvent(ActionEvent e){
        for(ActionListener a : listeners){
            a.actionPerformed(e);
        }
    }

    public List<Species> getSpeciesData(){
        return species;
    }

    public synchronized boolean toggleRunningState(){

        // toggle all species
        for(Species s : species){
            if(s.toggleRunning()){
                synchronized (s) {
                    s.notify();
                }
            }
        }
        running = !running;
        return running;
    }

    public synchronized void resetSimulation(){

        // don't reset an already running simulation
        if(running){
           processError("Must Pause Simulation Before Resetting!");
           return;
        }

        // reset all the species
        for(Species s : species){
            s.reset();
            synchronized (s){
                s.notify();
            }
        }

        running = true;
        total_running = species.size();
    }

    private void replaceEnvironment(Environment e){

        // TODO: this should pause the thread before replacing the environment
        // TODO: otherwise it may produce interesting results if it doesn't
        for(Species s : species){
            s.replaceEnvironment(new Environment(e));
        }

        // store for later
        environment = e;
    }

    public void processError(String errMsg){
        processEvent(new ActionEvent(new Error(errMsg),
                ActionEvent.ACTION_PERFORMED, "Error"));
    }

    public void actionPerformed(ActionEvent e) {

        // vars
        Object src = e.getSource();
        String msg = e.getActionCommand();

        if(msg.equals("generation_processed")){

            // alert the controller that another day has been processed
            processEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                    "generation_processed"));
        }
        else if(msg.equals("generation_processing_done")){

            // alert the controller that a species finished processing
            // completely
            // TODO: much later, need some way of keeping a list of all species
            // TODO: that finished processing
            decrementRunning();
            processEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                    "generation_processing_done"));
        }
        else if(msg.equals("new_elite")){

            synchronized (this){
                try{
                    while(copyingElites){
                        wait();
                    }
                }
                catch(InterruptedException ie){
                    
                }
            }

            Species s = (Species) src;

            // replace the previous species' elite with the new one
            elites.set(species.indexOf(s), s.getCurrentElite());

            // alert the controller
            processEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                    "new_elite"));
        }
    }
}
