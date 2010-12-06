import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Painter extends JPanel{

	//instance vars
	List<Agent> agents = new ArrayList<Agent>();
	int generation_count;
	double max_y_val = 12000.0, max_y_real = 640.0;
	
	public void paint(Graphics g){
		
		//draw the x-/y-axis
		if(generation_count == 0){
			setBackground(Color.white);
			g.setColor(Color.black);
			g.drawLine(0, 0, 0, 480);
			g.drawLine(0, 480, 640, 480);
		}
		
		//compute average of generation
		if(agents.size() == 0)
			return;
		
		int average = 0;
		for(Agent a : agents)
			if(a.getMoney() > 100)
				average+=a.getMoney();
		average = average/agents.size();
		
		//compute the y-location of the agent
		double y = (1-average/max_y_val) * max_y_real;
		
		System.out.println("Average Gen #"+generation_count+" $"+average+" VALUE:"+(1-average/max_y_val)*max_y_real);
		
		//actually draw the dot
		g.setColor(Color.green);
		g.fillOval(generation_count, (int)y, 4, 4);
		generation_count++;
	}
	
	public void addAgents(List<Agent> a){
		agents.clear();
		System.out.println(a.size());
		agents = a;
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(641, 481);
	}
}