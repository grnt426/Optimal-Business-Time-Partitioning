package com.kurtzg.bizsim;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/*
 * File:        View.java
 *
 * Author:      Grant Kurtz
 *
 * Description: Acts as a View and Controller by setting up and handling all
 *              the necessary user actions in the same class.  Creates two
 *              windows, the first is the graphing window used for viewing the
 *              output of the algorithm, and the second is used for controlling
 *              the simulation and environment values.  The second window also
 *              contains Elite agent information and a history of events.
 */
public class View implements ActionListener, MouseListener, MouseMotionListener{

    // global instance variables
    // global GUI controls
    JButton start_stop = new JButton("Start/Stop");
    JButton reset = new JButton("Restart");
    JButton reconfigure = new JButton("Reconfigure");

    // environment control fields
    JTextField low_rate = new JTextField();
    JTextField med_rate = new JTextField();
    JTextField high_rate = new JTextField();
    JTextField low_sale = new JTextField();
    JTextField med_sale = new JTextField();
    JTextField high_sale = new JTextField();
    JTextField high_prod = new JTextField();
    JTextField med_prod = new JTextField();
    JTextField low_prod = new JTextField();
    JTextField high_sell = new JTextField();
    JTextField med_sell = new JTextField();
    JTextField low_sell = new JTextField();
    JTextField buy_roi = new JTextField();
    JTextField sell_roi = new JTextField();
    JTextField buy_roi_chance = new JTextField();
    JTextField sell_roi_chance = new JTextField();

    // GA specific control fields
    JTextField agent_count = new JTextField();
    JTextField generation_count = new JTextField();
    JTextField day_count = new JTextField();
    JTextField elite_percent = new JTextField();
    JTextField parent_percent = new JTextField();
    JTextField agent_performance = new JTextField();

    // elite display information
    JTextField cur_elite_total = new JTextField("0");
    JTextArea cur_elite_genome = new JTextArea(15, 4);

    // message window fields
    JTextArea message_history = new JTextArea(11, 33);
    JScrollPane message_history_scroll = new JScrollPane(message_history);
    JTabbedPane graphs = new JTabbedPane();
    JFrame graphWindow;

    // initialize paint classes
    Painter paint = new Painter();
    JScrollPane graph_history = new JScrollPane(paint);
    ElitePainter ep = new ElitePainter();
    GenerationPainter gp = new GenerationPainter();
    AgentPainter ap = new AgentPainter();

    // Our model class which contains all the classes to run and report
    // updates to our class
    private Model model;


    /*
     * Default Constructor Sets up both windows and populates with all the
     * necessary GUI components.  Handles assigning the ActionListeners et al,
     *  and updating various text-fields with the defaults as provided by the
     * model.
     *
     * Param:   m           the model that will be reporting all updates to the
     *                      simulation and species processing.
     *
     * Param:   numThreads  The number of individual threads (and separate
     *                      species) to run on this machine simultaneously
     */
    public View(Model m, int numThreads){

        model = m;

        // hand ourselves over to the model to listen for events
        model.addListeners(this);

        // grab the default Environment used by the model
        Environment e = model.getEnvironment();

         // configure our main window which will contain all the other elements
        JFrame control_window = new JFrame();
        control_window.setTitle("Control Suite");
        control_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        control_window.setSize(new Dimension(800, 480));
        control_window.setLayout(new BorderLayout());

        // species List
        /*
        JPanel species_control = new JPanel();
        JLabel species_list_l = new JLabel("Species Running");
        DefaultListModel species_list = new DefaultListModel();

        // create a selectable list of all the different species we have
        JList species_select_list = new JList(species_list);
        species_select_list.setSelectionMode(
                ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        species_select_list.setLayoutOrientation(JList.VERTICAL);
        species_select_list.setVisibleRowCount(3);
        */

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
                "Modify Environment Settings"));
        environment_controls.setLayout(new GridLayout(4, 1));

        // set the size of the button to just 3 characters
        high_rate.setColumns(2);
        high_sale.setColumns(3);
        med_rate.setColumns(2);
        med_sale.setColumns(3);
        low_rate.setColumns(2);
        low_sale.setColumns(3);
        buy_roi.setColumns(3);
        buy_roi_chance.setColumns(3);
        sell_roi.setColumns(3);
        sell_roi_chance.setColumns(3);

        // High Quality goods section
        JPanel high_controls = new JPanel();
        high_controls.setBorder(BorderFactory.createTitledBorder(
                "High Quality Goods"));
        high_controls.setLayout(new GridLayout(1, 8));
        high_controls.add(new JLabel("RMs:"));
        high_controls.add(high_rate);
        high_controls.add(new JLabel("Sell:"));
        high_controls.add(high_sale);
        high_controls.add(new JLabel("Make:"));
        high_controls.add(high_prod);
        high_controls.add(new JLabel("Buy:"));
        high_controls.add(high_sell);

        // Medium Quality Goods Section
        JPanel med_controls = new JPanel();
        med_controls.setBorder(BorderFactory.createTitledBorder(
                "Medium Quality Goods"));
        med_controls.setLayout(new GridLayout(1, 8));
        med_controls.add(new JLabel("RMs:"));
        med_controls.add(med_rate);
        med_controls.add(new JLabel("Sell:"));
        med_controls.add(med_sale);
        med_controls.add(new JLabel("Make:"));
        med_controls.add(med_prod);
        med_controls.add(new JLabel("Buy:"));
        med_controls.add(med_sell);

        // Low Quality Goods Section
        JPanel low_controls = new JPanel();
        low_controls.setBorder(BorderFactory.createTitledBorder(
                "Low Quality Goods"));
        low_controls.setLayout(new GridLayout(1, 8));
        low_controls.add(new JLabel("RMs:"));
        low_controls.add(low_rate);
        low_controls.add(new JLabel("Sell:"));
        low_controls.add(low_sale);
        low_controls.add(new JLabel("Make:"));
        low_controls.add(low_prod);
        low_controls.add(new JLabel("Buy:"));
        low_controls.add(low_sell);

        // Risk Section
        JPanel risk_control = new JPanel();
        risk_control.setBorder(BorderFactory.createTitledBorder(
                "Risk Payoffs"));
        risk_control.setLayout(new GridLayout(1, 6));
        risk_control.add(new JLabel("RMs:"));
        risk_control.add(buy_roi_chance);
        risk_control.add(new JLabel("ROI:"));
        risk_control.add(buy_roi);
        risk_control.add(new JLabel("FGs:"));
        risk_control.add(sell_roi_chance);
        risk_control.add(new JLabel("ROI:"));
        risk_control.add(sell_roi);

        // add each section to the environment_control panel
        environment_controls.add(high_controls);
        environment_controls.add(med_controls);
        environment_controls.add(low_controls);
        environment_controls.add(risk_control);

        // setup the default values for these text fields
        high_rate.setText(e.getHQRate()+"");
        high_sale.setText(e.getHQSale()+"");
        med_rate.setText(e.getMQRate()+"");
        med_sale.setText(e.getMQSale()+"");
        low_rate.setText(e.getLQRate()+"");
        low_sale.setText(e.getLQSale()+"");
        high_prod.setText(e.getMaxHQProducible()+"");
        med_prod.setText(e.getMaxMQProducible()+"");
        low_prod.setText(e.getMaxLQProducible()+"");
        high_sell.setText(e.getMaxHQSell()+"");
        med_sell.setText(e.getMaxMQSell()+"");
        low_sell.setText(e.getMaxLQSell()+"");
        buy_roi.setText(".25");
        buy_roi_chance.setText(".33");
        sell_roi.setText(".15");
        sell_roi_chance.setText(".33");

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
        simulation_controls.add(new JLabel("Day Count"));
        simulation_controls.add(day_count);
        day_count.setEnabled(false);

        // setup the default values for these text fields
        agent_count.setText(model.getMaxAgentCount() + "");
        generation_count.setText(model.getMaxGenCount()+"");
        elite_percent.setText(model.getElitePercent()+"");
        parent_percent.setText(model.getParentPercent()+"");
        agent_performance.setText(e.getIncomeRatioThreshold()+"");
        day_count.setText("100");

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

        // create a panel for the message history
        JPanel message_panel = new JPanel();
        message_panel.setBorder(BorderFactory.createTitledBorder(
                "Message History"
        ));
        message_panel.add(message_history_scroll);
        message_history.setText("Simulation Started!");

        // this panel encompasses the different panels for altering and showing
        // the current state of the simulation
        JPanel field_controls = new JPanel();
        field_controls.setLayout(new GridLayout(2, 2));
        field_controls.add(environment_controls);
        field_controls.add(simulation_controls);
        field_controls.add(elite_panel);
        field_controls.add(message_panel);


        // create a panel for all our flow control buttons
        JPanel control_flow_buttons = new JPanel();
        control_flow_buttons.setLayout(new GridLayout(1, 6));
        control_flow_buttons.add(start_stop);
        control_flow_buttons.add(reset);
        control_flow_buttons.add(reconfigure);
        cur_elite_genome.setWrapStyleWord(true);
        cur_elite_genome.setEditable(false);
        cur_elite_genome.setLineWrap(true);

        // add our components to the window
        //species_control.add(species_list_l);
        //species_control.add(species_select_list);
        //control_window.add(species_control, BorderLayout.WEST);
        control_window.add(field_controls, BorderLayout.CENTER);
        control_window.add(control_flow_buttons, BorderLayout.SOUTH);
        control_window.setLocation(630, 0);
        control_window.setVisible(true);

        // tell all our paint classes that we want to know about mouse events
        // from the user
        paint.addMouseListener(this);
        gp.addMouseListener(this);
        paint.addMouseMotionListener(this);
        gp.addMouseMotionListener(this);

        // Create a Tabbed panel for the different graphs
        graphWindow = new JFrame();
        graphs.addTab("Species Average", null, graph_history, "Graphical " +
                "Representation of All Species' Average Performance");
        graphs.addTab("Elite Performance", null, ep,
                "A 100-day Graph of the Elite Agent's Business Model");
        graphs.addTab("Generational Output", null, gp,
                "The Output of each agent in a given generation");
        graphs.addTab("Agent Performance", null, ap,
                "The Daily Workings of an Individual Agent");
        graphWindow.getContentPane().add(graphs);
        graphWindow.setTitle("Optimal Division of Tasks in a Business");
        graphWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        graphWindow.pack();
        graphWindow.setVisible(true);

        // Now, lets get this algorithm started!
        model.creteNewSpecies(numThreads);
    }

    /*
     * Adds a provided message to the history field by providing the previous
     * text, appending a new-line, and then adding our message
     *
     * Param:   msg         The string to be appended to the message window
     *                      with a preceding new-line
     */
    public void appendMessage(String msg){
        message_history.setText(message_history.getText() + "\n" + msg);
    }

    /*
     * Handles processing of all user-input as well as handling delegating
     * the information from an updated model to the respective paint classes.
     *
     * Param:   e               the object that alerted our class of a new
     *                          event
     */
    public void actionPerformed(ActionEvent e) {

        // vars
        Object src = e.getSource();
        String msg = e.getActionCommand();

        // we have received an event from our model
        if(src.equals(model)){

            if(msg.equals("generation_processed")){

               // need to update the generation graph
               paint.setHistory(model.getSpeciesData());
            }
            else if(msg.equals("new_elite")){

                // NOTE: THE BELOW IS HIGHLY UNSTABLE WITH MULTIPLE THREADS

                // vars
                ArrayList<Agent> elites = new ArrayList(model.getElites());

                // update the paint class
                ep.setElites(elites);

                if(elites != null && elites.size() != 0){

                    Agent top_elite = new Agent(elites.get(0));

                    if(top_elite != null){

                        // update the GUI window
                        cur_elite_total.setText("$" + top_elite.getMoney());
                        cur_elite_genome.setText(top_elite.toString());
                    }
                }

                model.donCopyingElites();
            }
            else if(msg.equals("generation_processing_done")){
                appendMessage("A Species has Finished Processing");
            }
        }
        else if(src instanceof Error){

            Error err = (Error) src;

            if(msg.equals("Error")){

                // update the error field
               appendMessage(err.getMsg());
            }
        }

        // event received from our GUI panel
        else{

            // TODO: should really disable the GUI buttons until the model
            // TODO: reports that all species have halted processing
            // TODO: successfully
            if(src == start_stop){
                appendMessage((model.toggleRunningState() ? "Running":"Stopping")
                        + " Simulator...");
            }

            // TODO: again, should freeze all GUI buttons until the model
            // TODO: reports it has reset all species
            else if(src == reset){

                model.resetSimulation();

                // clear the Elite Agent data
                cur_elite_genome.setText("");
                cur_elite_total.setText("");

                appendMessage("Simulation Reset...\nSimulation Running...");
            }

            // TODO: same as above, wait until previous action done
            else if(src == reconfigure){

                // create a new environment with the new parameters
                Environment environment = new Environment();

                // vars
                int agentCount = 0, dayCount = 0, genCount = 0;
                double elitePercent = 0, parentPercent = 0;

                try{

                    // override existing values
                    environment.setHQSale(Integer.parseInt(
                            high_sale.getText()));
                    environment.setHQRate(Integer.parseInt(
                            high_rate.getText()));
                    environment.setMQRate(Integer.parseInt(
                            med_rate.getText()));
                    environment.setMQSale(Integer.parseInt(
                            med_sale.getText()));
                    environment.setLQRate(Integer.parseInt(
                            low_rate.getText()));
                    environment.setLQSale(Integer.parseInt(
                            low_sale.getText()));
                    environment.setIncomeRatioThreshold(Double.parseDouble(
                            agent_performance.getText())
                    );

                    // grab the rest of the values interdependent of the
                    // environment
                    agentCount = Integer.parseInt(agent_count.getText());
                    dayCount = Integer.parseInt(day_count.getText());
                    elitePercent = Double.parseDouble(elite_percent.getText());
                    genCount = Integer.parseInt(generation_count.getText());
                    parentPercent = Double.parseDouble(
                            parent_percent.getText());
                }
                catch(NumberFormatException nfe){
                    appendMessage("Error: Use only numbers!");
                    return;
                }

                // make the call to tell the model to update everything
                if(model.reconfigureState(environment, agentCount, dayCount,
                        genCount, elitePercent, parentPercent)){
                    appendMessage("Species Reconfigured...");
                }
            }
        }
    }

    /*
     * Handles user actions requesting more information on clicked data-points.
     *
     * Param:   e               the object that is alerting us to a new event
     */
    public void mouseClicked(MouseEvent e) {

        // dump event data
        Object src = e.getSource();
        int x = e.getX(), y = e.getY();

        // if we got a click event from our paint class, we want a closer look
        // at some point in the graph
        if(src == paint){

            // grab the clicked generation and pass to the generational
            // painter, then change to that panel
            Generation gen = paint.getClickedGeneration(x, y);

            // ensure the user actually clicked somewhere with a generation!
            if(gen != null){
                gp.setGeneration(gen);
                graphs.setSelectedIndex(2);
            }
        }

        // if we got a click event from our GenerationalPainter class, then
        // we need to show the graph for a single agent
        else if(src == gp){
            Agent a = gp.getClickedAgent(x, y);

            // make sure the user clicked somewhere with an agent!
            if(a != null){
                ap.setAgent(a);
                graphs.setSelectedIndex(3);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    /*
     * Used to update currently focused graphs of the new location  to draw
     * a targeting reticule.
     */
    public void mouseMoved(MouseEvent e) {

        // mouse event data dump
        Object src = e.getSource();
        int x = e.getX(), y = e.getY();

        if(src == paint){
            paint.setHoveringOver(x, y);
        }
        else if(src == gp){
            gp.setHoveringOver(x, y);
        }
    }
}
