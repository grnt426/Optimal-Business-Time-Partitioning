package com.kurtzg.bizsim;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Species implements Runnable{

        //vars
		private Environment e;
        private ActionListener listener;
		private List<Agent> children = new ArrayList<Agent>();
		private int generations, cur_gen, total_agents, id, day_count,
                    prev_elite_total;
        private double elite_percent, parent_percent;
		private Evolution evo = new Evolution();
        private boolean running;
        private Agent current_elite;
        private History history;

		public Species(ArrayList<Agent> agents, int generations,
                                int id, Environment e, ActionListener l){

            //store parameters as instance variables
            this.e = e;
            this.id = id;
            this.listener = l;
            this.generations = generations;
			children = agents;

            //setup all other data
            total_agents = agents.size();
            history = new History(id);
			cur_gen = 0;
            day_count = 100;
            elite_percent = .05;
            parent_percent = .8;
            current_elite = null;
            prev_elite_total = 0;
		}

        /*
        * Completely erases the current state of the Simulation
         */
        public void reset(){

            //reset classes
            e.purgeAgents();
            children.clear();
            fillWithAgents();

            //reset vars
            cur_gen = 0;
            current_elite = null;
            prev_elite_total = 0;
            running = true;
        }

        public int getID(){
            return id;
        }

        public boolean toggleRunning(){
            running = !running;
            return running;
        }

        public boolean isRunning(){
            return running;
        }

        public void replaceEnvironment(Environment e){
            this.e = e;
        }

        /*
         * Populates the children array with agents of random creation
         */
        public void fillWithAgents(){
            while(children.size() < total_agents){
				children.add(new Agent());
			}
        }

        public void setGenerationCount(int gen_count){
            generations = gen_count;
        }

        public int getGenerationCount(){
            return generations;
        }

        public void setAgentCount(int agent_count){
            total_agents = agent_count;
        }

        public int getAgentCount(){
            return total_agents;
        }

        public void setDayCount(int day_count){
            this.day_count = day_count;
        }

        public int getDayCount(){
            return day_count;
        }

        public void setElitePercent(double elite_percent){
            this.elite_percent = elite_percent;
        }

        public double getElitePercent(){
            return elite_percent;
        }

        public void setParentPercent(double parent_percent){
            this.parent_percent = parent_percent;
        }

        public double getParentPercent(){
            return parent_percent;
        }

        public Agent getCurrentElite(){
            return current_elite;
        }

        public Generation getLatestGeneration(){
            return history.getLastGeneration();
        }

		public void run(){

            //keep the thread alive
            while(true){

                //wait for a signal to re-continue processing agents
                if(!running){
                    synchronized (this){
                        try{
                            wait();
                        }
                        catch(InterruptedException ie){
                            System.err.println("ERROR: wait() call failed"
                                    + ie
                            );
                        }
                    }
                }

                //run for the number of generations specified
                while(cur_gen < generations && running){

                    //create our list of agents
                    e.purgeAgents();
                    e.addAgents(children);

                    //Process 100-days
                    for(int i = 0; i < day_count; i++){
                        e.simulateDay();
                    }

                    //record this generation
                    history.addGeneration(children);

                    //alert the model
                    listener.actionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_PERFORMED,
                            "generation_processed"));

                    //sort the results by money, descending
                    Collections.sort(children);
                    Collections.reverse(children);

                    //if the elite total changed, alert the main class
                    int elite_most_money = children.get(0).getMoney();
                    if(elite_most_money > prev_elite_total){

                        //cache the new elite total
                        prev_elite_total = elite_most_money;
                        current_elite = children.get(0);

                        //fire an event to the model
                        listener.actionPerformed(new ActionEvent(this,
                                ActionEvent.ACTION_PERFORMED, "elite_total"));
                    }

                    //now we need to select agents for crossover, mutation,
                    // and elites for elites, keep the first x%
                    List<Agent> elites = children.subList(
                            0, (int)(children.size()*elite_percent)
                    );

                    //for parents to keep, simply choose the top y%
                    // (including the elites)
                    List<Agent> parents = children.subList(0,
                                            (int)(children.size()
                                                    *parent_percent));

                    //create some children, and perform mutations
                    children = evo.performCrossover(parents);
                    children = evo.performMutation(children);

                    //add the elite agents into the pool
                    children.addAll(elites);

                    //System.out.println(elites.get(0) + ": " + elites.get(0).getMoney());

                    //reset the elites for the next generation
                    for(Agent elitea : elites)
                        elitea.reset();

                    //repopulate the next generation with new Agents
                    fillWithAgents();

                    cur_gen++;
                }
                running = false;
            }
		}

}
