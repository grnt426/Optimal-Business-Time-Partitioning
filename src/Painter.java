import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Painter extends JPanel{

	//instance vars
    List<Integer> average_history = new ArrayList<Integer>();
	int generation_count;
	double max_y_val = 20000.0, max_y_real = 640.0;
	
	public void paint(Graphics g){

        super.paintComponent(g);

        setBackground(Color.white);

        if(average_history.size() == 0)
            return;

        //the color of our data point
        g.setColor(Color.green);
        int avg = 0;

        //draw the current history of all agents
		for(int i = 0; i < average_history.size(); ++i){
            avg = average_history.get(i);
            g.fillOval(i, (int)((1-avg/max_y_val)*max_y_real), 4, 4);
        }

		//just some data to print out
		System.out.println("Average Gen #"+generation_count+" $"+avg+" VALUE:"+(1-avg/max_y_val)*max_y_real);

		generation_count++;
	}
	
	public synchronized void addAgents(List<Agent> agents){

        //vars
        int average = 0;

        //compute average performance of generation
		for(Agent a : agents)
			if(!a.isAgentIneffective())
				average+=a.getMoney();
		average = average/agents.size();
        average_history.add(average);

        //finally, perform a repaint
        repaint();
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(1281, 481);
	}
}