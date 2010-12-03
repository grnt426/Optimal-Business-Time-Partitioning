public class MainTester{

	public static void main(String[] args){
		System.out.println("Testing Integrity of Agent Class");
		Agent a = new Agent();
		System.out.println(a+"\n");
		
		//add some money and basic hard-coded actions
		a.setMoney(50000);
		System.out.println(a);
		
		//testing default values
		System.out.println("Printing the current state of the Agent");
		System.out.println(a.printState());
		
		//adding agent
		Environment e = new Environment();
		e.addAgent(a);
		
		//Simulate a single business day for all agents
		for(int i = 0; i < 100;++i){
			e.simulateDay();
		}
		System.out.println("Printing the current state of the Agent");
		System.out.println(a.printState());
	}
}