package com.kurtzg.bizsim;
/**
* File:			com.kurtzg.bizsim.BizSimMain.java
*
* Author:		Grant Kurtz
*
* Description:	Handles the initialization of the program, asks for user input
*				on environment and simulation values, and then begins separate
*				threads to begin processing.
*
*/


// imports
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import javax.swing.*;

public class BizSimMain implements ActionListener{

    // global instance variables
    JButton start_stop = new JButton("Start/Stop");
    JButton reset = new JButton("Restart");
    JButton reconfigure = new JButton("Reconfigure");
    JTextField low_rate = new JTextField();
    JTextField med_rate = new JTextField();
    JTextField high_rate = new JTextField();
    JTextField low_sale = new JTextField();
    JTextField med_sale = new JTextField();
    JTextField high_sale = new JTextField();
    JTextField agent_count = new JTextField();
    JTextField generation_count = new JTextField();
    JTextField day_count = new JTextField();
    JTextField elite_percent = new JTextField();
    JTextField parent_percent = new JTextField();
    JTextField agent_performance = new JTextField();
    JLabel error_label = new JLabel();
    List<ProcessSimulator> species = new ArrayList<ProcessSimulator>();

	public static void main(String[] args){
		
		// process arguments
		if(args.length != 3){
			System.err.println("WTF? I need the correct number of arguments");
			System.exit(0);
		}
		
		// vars
		int numThreads = 0, numAgents = 0, numGenerations = 0;

        // attempt to parse the input
		try{
			numThreads = Integer.parseInt(args[0]);
			numAgents = Integer.parseInt(args[1]);
			numGenerations = Integer.parseInt(args[2]);
		}
		catch(NumberFormatException nfe){
			System.err.println(nfe);
			System.exit(0);
		}
		
		// Initialize our Main Class's simulator
		new BizSimMain(numThreads, numAgents, numGenerations);
		
	}
	
	public BizSimMain(int numThreads, int numAgents, int numGenerations){

        // configure our main window which will contain all the other elements
        JFrame control_window = new JFrame();
        control_window.setTitle("Control Suite");
        control_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        control_window.setSize(new Dimension(640, 480));
        control_window.setAlwaysOnTop(true);
        control_window.setLayout(new BorderLayout());

        JPanel species_control = new JPanel();
        JLabel species_list_l = new JLabel("Species Running");
        DefaultListModel species_list = new DefaultListModel();

        // create our Environment
        Environment e = new Environment();

        // create all our threads
		for(int kittehsex = 0; kittehsex < numThreads; ++kittehsex){
		
			// create some test agents
			ArrayList<Agent> agents = new ArrayList<Agent>();
			for(int i = 0; i < numAgents; ++i){
				agents.add(new Agent());
			}

            // create our new thread and pass in some control variables
            ProcessSimulator ps = new ProcessSimulator(agents, numGenerations, kittehsex, e);
			Thread t = new Thread(ps);
            species.add(ps);

            // update our current species list
            species_list.addElement("Species #"+kittehsex);

            // set the thread to a high priority, and then start it
			t.setPriority(9);
			t.start();
		}

        // create a selectable list of all the different species we have
        JList species_select_list = new JList(species_list);
        species_select_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        species_select_list.setLayoutOrientation(JList.VERTICAL);
        species_select_list.setVisibleRowCount(3);

        // start listening to all buttons
        start_stop.addActionListener(this);
        reset.addActionListener(this);
        reconfigure.addActionListener(this);

        // add mnemonics to some buttons
        start_stop.setMnemonic('s');
        reset.setMnemonic('r');
        reconfigure.setMnemonic('e');

        // create a panel for all our environment variables
        JPanel environment_controls = new JPanel();
        environment_controls.setBorder(BorderFactory.createTitledBorder("Modify Finished Good Values"));
        environment_controls.setLayout(new GridLayout(3, 4));
        environment_controls.add(new JLabel("High Rate:"));
        environment_controls.add(high_rate);
        environment_controls.add(new JLabel("High Sale:"));
        environment_controls.add(high_sale);
        environment_controls.add(new JLabel("Medium Rate:"));
        environment_controls.add(med_rate);
        environment_controls.add(new JLabel("Medium Sale:"));
        environment_controls.add(med_sale);
        environment_controls.add(new JLabel("Low Rate:"));
        environment_controls.add(low_rate);
        environment_controls.add(new JLabel("Low Sale:"));
        environment_controls.add(low_sale);

        // create a panel for all our simulation variables
        JPanel simulation_controls = new JPanel();
        simulation_controls.setBorder(BorderFactory.createTitledBorder("Modify Simulation Controls"));
        simulation_controls.setLayout(new GridLayout(6, 2));
        simulation_controls.add(new JLabel("Agent Count:"));
        simulation_controls.add(agent_count);
        simulation_controls.add(new JLabel("Generation Count:"));
        simulation_controls.add(generation_count);
        simulation_controls.add(new JLabel("Elite Percent"));
        simulation_controls.add(elite_percent);
        simulation_controls.add(new JLabel("Parent Percent"));
        simulation_controls.add(parent_percent);
        simulation_controls.add(new JLabel("Agent Performance"));
        simulation_controls.add(agent_performance);

        // this panel encompasses the different panels for altering the state
        // of the simulation
        JPanel field_controls = new JPanel();
        field_controls.setLayout(new GridLayout(2, 2));
        field_controls.add(environment_controls);
        field_controls.add(simulation_controls);

        // set the size of the button to just 3 characters
        high_rate.setColumns(3);
        high_sale.setColumns(3);
        med_rate.setColumns(3);
        med_sale.setColumns(3);
        low_rate.setColumns(3);
        low_sale.setColumns(3);

        // set the current value of our fields to the current environment value
        high_rate.setText(e.getHQRate() + "");
        high_sale.setText(e.getHQSale() + "");
        med_rate.setText(e.getMQRate() + "");
        med_sale.setText(e.getMQSale() + "");
        low_rate.setText(e.getLQRate() + "");
        low_sale.setText(e.getLQSale() + "");

        // set the default value of our fields to the current simulation values
        day_count.setText("100");
        generation_count.setText(numGenerations + "");
        elite_percent.setText(".05");
        parent_percent.setText(".8");
        agent_count.setText(numAgents + "");
        agent_performance.setText(e.getIncomeRatioThreshold() + "");

        // create a panel for all our flow control buttons
        JPanel control_flow_buttons = new JPanel();
        control_flow_buttons.setLayout(new GridLayout(1, 6));
        control_flow_buttons.add(start_stop);
        control_flow_buttons.add(reset);
        control_flow_buttons.add(reconfigure);

        // add our components to the window
        species_control.add(species_list_l);
        species_control.add(species_select_list);
        control_window.add(species_control, BorderLayout.WEST);
        control_window.add(field_controls, BorderLayout.CENTER);
        control_window.add(control_flow_buttons, BorderLayout.SOUTH);
        control_window.setLocation(620, 0);
        control_window.setVisible(true);
	}

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == start_stop){

            // tell all threads to continue/resume operation
            for(ProcessSimulator ps : species)
                if(ps.toggleRunning())
                    synchronized (ps){
                        ps.notify();
                    }
        }
        else if(e.getSource() == reset){
            for(ProcessSimulator ps : species){
                if(!ps.isRunning()){
                    ps.reset();
                    synchronized (ps){
                        ps.notify();
                    }
                }
            }
        }
        else if(e.getSource() == reconfigure){

            //create a new environment with the new parameters
            Environment environment = new Environment();

            //override existing values
            environment.setHQSale(Integer.parseInt(high_sale.getText()));
            environment.setHQRate(Integer.parseInt(high_rate.getText()));
            environment.setMQRate(Integer.parseInt(med_rate.getText()));
            environment.setMQSale(Integer.parseInt(med_sale.getText()));
            environment.setLQRate(Integer.parseInt(low_rate.getText()));
            environment.setLQSale(Integer.parseInt(low_sale.getText()));
            environment.setIncomeRatioThreshold(Double.parseDouble(agent_performance.getText()));

            for(ProcessSimulator ps : species){
                if(!ps.isRunning()){
                    ps.replaceEnvironment(environment);

                    //replace simulation values
                    ps.setAgentCount(Integer.parseInt(agent_count.getText()));
                    ps.setDayCount(Integer.parseInt(day_count.getText()));
                    ps.setElitePercent(Double.parseDouble(elite_percent.getText()));
                    ps.setGenerationCount(Integer.parseInt(generation_count.getText()));
                    ps.setParentPercent(Double.parseDouble(parent_percent.getText()));
                }
            }
        }
    }

    public class ProcessSimulator implements Runnable{
	
		//vars
		private Environment e;
		private List<Agent> children = new ArrayList<Agent>();
		//List<List<com.kurtzg.bizsim.Agent>> history = new ArrayList<List<com.kurtzg.bizsim.Agent>>();
		private int generations, cur_gen, total_agents, id, day_count;
        private double elite_percent, parent_percent;
		private JFrame window = new JFrame();
		private Painter paint = new Painter();
		private Evolution evo = new Evolution();
        private boolean running;
		
		public ProcessSimulator(ArrayList<Agent> agents, int generations, int id, Environment e){
            this.e = e;
            this.id = id;
			children = agents;
            total_agents = agents.size();
			this.generations = generations;
			cur_gen = 0;
            day_count = 100;
            elite_percent = .05;
            parent_percent = .8;
			window.setTitle("Average Performance of Each Generation #" + id);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.getContentPane().add(paint);
			window.pack();
			window.setVisible(true);
            running = true;
		}

        public void reset(){
            e.purgeAgents();
            paint.clearHistory();
            children.clear();
            fillWithAgents();
            cur_gen = 0;
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

		public void run(){

            while(true){
                if(!running){
                    synchronized (this){
                        try{
                            wait();
                        }
                        catch(InterruptedException ie){

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

                    //sort the results by money, descending
                    Collections.sort(children);
                    Collections.reverse(children);

                    paint.addAgents(children);

                    //history.add(children);

                    //print out the last generation
                    if(cur_gen == generations)
                        for(Agent a : children.subList(0, 41))
                            System.out.println(a.getMoney());

                    //now we need to select agents for crossover, mutation, and elites
                    //for elites, keep the first 5%
                    List<Agent> elites = children.subList(0, (int)(children.size()*elite_percent));

                    //for parents to keep, simply choose the top 80% (including the elites)
                    List<Agent> parents = children.subList(0,
                                            (int)(children.size()*parent_percent));

                    //create some children, and perform mutations
                    children = evo.performCrossover(parents);
                    children = evo.performMutation(children);

                    //add the elite agents into the pool
                    children.addAll(elites);

                    System.out.println(elites.get(0) + ": " + elites.get(0).getMoney());

                    //reset the elites for the next generation
                    for(Agent elitea : elites)
                        elitea.reset();

                    fillWithAgents();

                    cur_gen++;
                }
                running = false;
            }
		}
	}
}