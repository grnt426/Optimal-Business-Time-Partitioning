package com.kurtzg.bizsim;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class View implements ActionListener{

    // global instance variables
    // global GUI controls
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
    JTextField cur_elite_total = new JTextField("0");
    JTextArea cur_elite_genome = new JTextArea(15, 4);
    JLabel error_label = new JLabel();

    //initialize paint classes
    Painter paint = new Painter();
    ElitePainter ep = new ElitePainter();

    private Model model;
    private JFrame graphWindow;

    public View(Model m, int numThreads){

        model = m;

        //hand ourselves over to the model to listen for events
        model.addListeners(this);

         // configure our main window which will contain all the other elements
        JFrame control_window = new JFrame();
        control_window.setTitle("Control Suite");
        control_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        control_window.setSize(new Dimension(800, 480));
        //control_window.setAlwaysOnTop(true);
        control_window.setLayout(new BorderLayout());

        //species List
        JPanel species_control = new JPanel();
        JLabel species_list_l = new JLabel("Species Running");
        DefaultListModel species_list = new DefaultListModel();

        // create a selectable list of all the different species we have
        JList species_select_list = new JList(species_list);
        species_select_list.setSelectionMode(
                ListSelectionModel.SINGLE_INTERVAL_SELECTION);
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
        environment_controls.setBorder(BorderFactory.createTitledBorder(
                "Modify Finished Good Values"));
        environment_controls.setLayout(new GridLayout(3, 4));
        environment_controls.add(new JLabel("High Rate:"));
        environment_controls.add(high_rate);
        environment_controls.add(new JLabel("High Sale:"));
        environment_controls.add(high_sale);
        environment_controls.add(new JLabel("Med Rate:"));
        environment_controls.add(med_rate);
        environment_controls.add(new JLabel("Med Sale:"));
        environment_controls.add(med_sale);
        environment_controls.add(new JLabel("Low Rate:"));
        environment_controls.add(low_rate);
        environment_controls.add(new JLabel("Low Sale:"));
        environment_controls.add(low_sale);

        // create a panel for all our simulation variables
        JPanel simulation_controls = new JPanel();
        simulation_controls.setBorder(BorderFactory.createTitledBorder(
                "Modify Simulation Controls"));
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

        // create a panel for displaying information about the current elite
        // agent
        JPanel elite_panel = new JPanel();
        elite_panel.setBorder(BorderFactory.createTitledBorder(
                "Current Elite Agent Performance"
        ));
        elite_panel.setLayout(new GridLayout(2, 2));
        elite_panel.add(new JLabel("Total: "));
        elite_panel.add(cur_elite_total);
        elite_panel.add(new JLabel("Genome: "));
        JScrollPane genome_scroll = new JScrollPane(cur_elite_genome);
        elite_panel.add(genome_scroll);

        // this panel encompasses the different panels for altering and showing
        // the current state of the simulation
        JPanel field_controls = new JPanel();
        field_controls.setLayout(new GridLayout(2, 2));
        field_controls.add(environment_controls);
        field_controls.add(simulation_controls);
        field_controls.add(elite_panel);

        //set some attributes on misc. GUI stuff
        cur_elite_genome.setWrapStyleWord(true);
        cur_elite_genome.setEditable(false);
        cur_elite_genome.setLineWrap(true);

        // set the size of the button to just 3 characters
        high_rate.setColumns(3);
        high_sale.setColumns(3);
        med_rate.setColumns(3);
        med_sale.setColumns(3);
        low_rate.setColumns(3);
        low_sale.setColumns(3);

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


         //Create a Tabbed panel for the different graphs
        graphWindow = new JFrame();
        JTabbedPane graphs = new JTabbedPane();
        graphs.addTab("Species Average", null, paint, "Graphical " +
                "Representation of All Species' Average Performance");
        graphs.addTab("Elite Performance", null, ep,
                "A 100-day Graph of the Elite Agent's Business Model");
        graphWindow.getContentPane().add(graphs);
        graphWindow.setTitle("Optimal Division of Tasks in a Business");
        graphWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        graphWindow.pack();
        graphWindow.setVisible(true);

        //Now, create our first species
        model.creteNewSpecies(numThreads);

    }

    public void actionPerformed(ActionEvent e) {

        //vars
        Object src = e.getSource();
        String msg = e.getActionCommand();

        //we have received an event from our model
        if(src.equals(model)){

            if(msg.equals("generation_processed")){

               //need to update the generation graph
               paint.setHistory(model.getSpeciesData());
            }
        }

        //event received from our GUI panel
        else{

            if(src == start_stop){
                model.toggleRunningState();
                System.out.println("Started/Stopped Simulator...");
            }
            else if(src == reset){

                model.resetSimulation();

                //clear the Elite Agent data
                cur_elite_genome.setText("");
                cur_elite_total.setText("");

                System.out.println("Simulation Reset...");
            }
            else if(src == reconfigure){

                //create a new environment with the new parameters
                Environment environment = new Environment();

                //override existing values
                environment.setHQSale(Integer.parseInt(high_sale.getText()));
                environment.setHQRate(Integer.parseInt(high_rate.getText()));
                environment.setMQRate(Integer.parseInt(med_rate.getText()));
                environment.setMQSale(Integer.parseInt(med_sale.getText()));
                environment.setLQRate(Integer.parseInt(low_rate.getText()));
                environment.setLQSale(Integer.parseInt(low_sale.getText()));
                environment.setIncomeRatioThreshold(Double.parseDouble(
                        agent_performance.getText())
                );

                //grab the rest of the values interdependent of the environment
                int agentCount = Integer.parseInt(agent_count.getText());
                int dayCount = Integer.parseInt(day_count.getText());
                double elitePercent = Double.parseDouble(elite_percent.getText());
                int genCount = Integer.parseInt(generation_count.getText());
                double parentPercent = Double.parseDouble(parent_percent.getText());

                model.reconfigureState(environment, agentCount, dayCount,
                        genCount, elitePercent, parentPercent);

                System.out.println("Species Reconfigured...");
            }
            else if(msg == "elite_total"){

                // one of the threads has a new elite
                // TODO: Later, we are going to want to differentiate between the
                // TODO: different threads
                // TODO: THE TODO ABOVE IS WORTHLESS, THE MODEL WILL HANDLE THIS
                // TODO: EASILY
                //current_elite = ((ProcessSimulator) src).getCurrentElite();

                //update our GUI
                //cur_elite_total.setText("$" + current_elite.getMoney());
                //cur_elite_genome.setText(current_elite.toString());
            }
        }
    }
}
