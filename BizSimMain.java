/**
* File:			BizSimMain.java
*
* Author:		Grant Kurtz
*
* Description:	Handles the initialization of the program, asks for user input
*				on environment and simulation values, and then begins separate
*				threads to begin processing.
*
*/


public class BizSimMain{

	public static void main(String[] args){

		//create some test agents
		Agent a = new Agent();
		
		
	}
	
	public class ProcessSimulator implements Runnable{
	
		//vars
		Agent a;
		
		public ProcessSimulator(Agent a){
			this.a = a;
		}
	
		public void run(){
		
			//Process a single day
			
		}
	}
}