import java.util.ArrayList;

public class Environment{

	//instance variables
	int RM_COST, LQ_RATE, MQ_RATE, HQ_RATE, LQ_SALE, MQ_SALE, HQ_SALE,
				MAX_RMS_PER_AC, MAX_LQ_PER_AC, MAX_MQ_PER_AC, MAX_HQ_PER_AC, 				MAX_SELL_PER_AC;
	int ACTIONS_TOTAL;
	ArrayList<Agent> agents;
	
	/*
	* Initializes with hard-coded defaults
	*
	*/
	public Environment(){
		agents = new ArrayList<Agent>();
		ACTIONS_TOTAL = 16;
		RM_COST = 5;
		LQ_RATE = 7;
		MQ_RATE = 9;
		HQ_RATE = 12;
		LQ_SALE = 9;
		MQ_SALE = 14;
		HQ_SALE = 20;
		MAX_RMS_PER_AC = 2500;
		MAX_LQ_PER_AC = 1000;
		MAX_MQ_PER_AC = 800;
		MAX_HQ_PER_AC = 500;
		MAX_LQSELL = 1500;
		MAX_MQSELL = 1300;
		MAX_HQSELL = 900;
	}

	/*
	*
	*/
	public void addAgent(Agent a){
		agents.add(a);
	}
	
	/*
	* 
	*/
	public void addAgents(ArrayList<Agent> a){
		agents.addAll(a);
	}
	
	public void simulateDay(){
		for(Agent a : agents){
			ArrayList<Boolean> actions;
			actions = a.getChrome();
			int money, rms, lqfg, mqfg, hqfg;
			int temp_rms, temp_fgs, temp_sale;
			
			//process all the actions for this agent
			for(int i = 0; i<a.size();i=i+4){
				String action = actions.get(i);
				action += actions.get(i+1);
				action += actions.get(i+2);
				action += actions.get(i+3);
				
				//Search for Raw Materials
				if(action == "0000"){
					money = agent.getMoney();
					temp_rms = money/RM_COST;
					temp_rms = temp_rms > MAX_RMS_PER_AC ? 
									MAX_RMS_PER_AC : temp_rms;
					money = money - (temp_rms * RM_COST);
					agent.setMoney(money);
					agent.addRawMaterials(temp_rms);
					agent.setCurrentlyStoring(false);
				}
				
				//Risky search for Raw Materials
				else if(action == "0001"){
					//do nothing, stub action
				}
				
				//Produce Low-Quality Finished Goods
				else if(action == "0010"){
					temp_rms = agent.getRawMaterials();
					temp_fgs = temp_rms/LQ_RATE;
					temp_fgs = temp_fgs > MAX_LQ_PER_AC ?
									MAX_LQ_PER_AC : temp_fgs;
					temp_rms = temp_rms - temp_fgs*LQ_RATE;
					agent.setRawMaterials(temp_rms);
					agent.addProducedLQ(temp_fgs);
					agent.setCurrentlyStoring(false);
				}
				
				//Produce Medium-Quality Finished Goods
				else if(action == "0011"){
					temp_rms = agent.getRawMaterials();
					temp_fgs = temp_rms/MQ_RATE;
					temp_fgs = temp_fgs > MAX_MQ_PER_AC ?
									MAX_MQ_PER_AC : temp_fgs;
					temp_rms = temp_rms - temp_fgs*MQ_RATE;
					agent.setRawMaterials(temp_rms);
					agent.addProducedMQ(temp_fgs);
					agent.setCurrentlyStoring(false);
				}
				
				//Produce High-Quality Finished Goods
				else if(action == "0100"){
					temp_rms = agent.getRawMaterials();
					temp_fgs = temp_rms/HQ_RATE;
					temp_fgs = temp_fgs > MAX_HQ_PER_AC ?
									MAX_HQ_PER_AC : temp_fgs;
					temp_rms = temp_rms - temp_fgs*HQ_RATE;
					agent.setRawMaterials(temp_rms);
					agent.addProducedHQ(temp_fgs);
					agent.setCurrentlyStoring(false);
				}
				
				//Sell All Goods
				else if(action == "0101"){
					
					//sell demand's worth of Low-Quality goods
					temp_fgs = agent.getProducedLQ();
					temp_fgs = (temp_fgs > MAX_LQSELL ? MAX_LQSELL : temp_fgs);
					agent.addProducedLQ(-temp_fgs);
					money = temp_fgs * LQ_SALE;
					
					//sell demand's worth of Medium-Quality goods
					temp_fgs = agent.getProducedMQ();
					temp_fgs = (temp_fgs > MAX_MQSELL ? MAX_MQSELL : temp_fgs);
					agent.addProducedMQ(-temp_fgs);
					money += temp_fgs * MQ_SALE;
					
					//sell demand's worth of High-Quality goods
					temp_fgs = agent.getProducedHQ();
					temp_fgs = (temp_fgs > MAX_HQSELL ? MAX_HQSELL : temp_fgs);
					agent.addProducedHQ(-temp_fgs);
					money += temp_fgs * HQ_SALE;
					agent.adjustMoney(money);
					agent.setCurrentlyStoring(false);
				}
				
				//sell all risky goods
				else if(action == "0110"){
					//stub action
				}
				
				//store all RMs and FGs
				else if(action == "0111"){
					agent.setCurrentlyStoring(true);
				}
			}
		}
	}
}