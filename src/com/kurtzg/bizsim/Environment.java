package com.kurtzg.bizsim; /**
* File:			com.kurtzg.bizsim.Environment.java
*
* Author:		Grant Kurtz
*
* Description:	Contains all information about the environment the agents will
*				participate in.  This class is also able to simulate a business
*				day given a list of agents.
*/

import java.util.ArrayList;
import java.util.List;

public class Environment{

	//instance variables
	private int RM_COST, LQ_RATE, MQ_RATE, HQ_RATE, LQ_SALE, MQ_SALE, HQ_SALE,
				MAX_RMS_PER_AC, MAX_LQ_PER_AC, MAX_MQ_PER_AC, MAX_HQ_PER_AC,
                MAX_LQSELL, MAX_MQSELL, MAX_HQSELL;
	private int ACTIONS_TOTAL, current_day;
    private double income_ratio_threshold;
	private ArrayList<Agent> agents;
	
	/*
	* Initializes with hard-coded defaults
	*
	*/
	public Environment(){
		agents = new ArrayList<Agent>();
		ACTIONS_TOTAL = 16;
		RM_COST = 5;
		LQ_RATE = 3;
		MQ_RATE = 5;
		HQ_RATE = 7;
		LQ_SALE = 19;
		MQ_SALE = 29;
		HQ_SALE = 39;
		MAX_RMS_PER_AC = 2500;
		MAX_LQ_PER_AC = 1000;
		MAX_MQ_PER_AC = 800;
		MAX_HQ_PER_AC = 500;
		MAX_LQSELL = 1500;
		MAX_MQSELL = 1300;
		MAX_HQSELL = 900;
		current_day = 0;
        income_ratio_threshold = 1.1;
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
	public void addAgents(List<Agent> a){
		agents.addAll(a);
	}
	
	public void purgeAgents(){
		agents.clear();
	}

    /*
    * Look out below, giant list of get/set variables!
    *
     */
    public void setHQRate(int rate){
        this.HQ_RATE = rate;
    }

    public void setMQRate(int rate){
        this.MQ_RATE = rate;
    }

    public void setLQRate(int rate){
        this.LQ_RATE = rate;
    }

    public void setHQSale(int rate){
        this.HQ_SALE = rate;
    }

    public void setMQSale(int rate){
        this.MQ_SALE = rate;
    }

    public void setLQSale(int rate){
        this.LQ_SALE = rate;
    }

    public int getHQRate(){
        return HQ_RATE;
    }

    public int getHQSale(){
        return HQ_SALE;
    }

    public int getMQRate(){
        return MQ_RATE;
    }

    public int getMQSale(){
        return MQ_SALE;
    }

    public int getLQSale(){
        return LQ_SALE;
    }

    public int getLQRate(){
        return LQ_RATE;
    }

    public void setIncomeRatioThreshold(double threshold){
        this.income_ratio_threshold = threshold;
    }

    public double getIncomeRatioThreshold(){
        return income_ratio_threshold;
    }
	
	/*
	* Processes a single business day for all agents, grabbing each agent's
	* action during that time-slot, and forcing the agent to dump any unsaved
	* RMs & FGs once the day is finished processed.
	*/
	public void simulateDay(){
		for(Agent agent : agents){
			ArrayList<Boolean> actions;
			actions = agent.getChrome();
			int money, rms, lqfg, mqfg, hqfg;
			int temp_rms, temp_fgs, temp_sale;
			
			//process all the actions for this agent
			for(int i = 0; i<actions.size();i=i+3){
				String action = actions.get(i) ? "1":"0";
				action += actions.get(i+1) ? "1" : "0";
				action += actions.get(i+2) ? "1" : "0";
				//action += actions.get(i+3) ? "1" : "0";

				//Search for Raw Materials
				if(action.equals("000")){
					money = agent.getMoney();
					temp_rms = money/RM_COST;
					temp_rms = temp_rms > MAX_RMS_PER_AC ? 
									MAX_RMS_PER_AC : temp_rms;
					money = money - (temp_rms * RM_COST);
					agent.setMoney(money);
					agent.adjustRawMaterials(temp_rms);
					agent.setCurrentlyStoring(false);
				}
				
				//Risky search for Raw Materials
				else if(action.equals("001")){
					money = agent.getMoney();
					temp_rms = money/RM_COST;
					temp_rms = temp_rms > MAX_RMS_PER_AC ? 
									MAX_RMS_PER_AC : temp_rms;
					money = money - (temp_rms * RM_COST);
					agent.setMoney(money);
					agent.adjustRawMaterials(temp_rms);
					agent.setCurrentlyStoring(false);
				}
				
				//Produce Low-Quality Finished Goods
				else if(action.equals("010")){
					temp_rms = agent.getRawMaterials();
					temp_fgs = temp_rms/LQ_RATE;
					temp_fgs = temp_fgs > MAX_LQ_PER_AC ?
									MAX_LQ_PER_AC : temp_fgs;
					temp_rms = temp_rms - temp_fgs*LQ_RATE;
					agent.setRawMaterials(temp_rms);
					agent.adjustProducedLQ(temp_fgs);
					agent.setCurrentlyStoring(false);
				}
				
				//Produce Medium-Quality Finished Goods
				else if(action.equals("011")){
					temp_rms = agent.getRawMaterials();
					temp_fgs = temp_rms/MQ_RATE;
					temp_fgs = temp_fgs > MAX_MQ_PER_AC ?
									MAX_MQ_PER_AC : temp_fgs;
					temp_rms = temp_rms - temp_fgs*MQ_RATE;
					agent.setRawMaterials(temp_rms);
					agent.adjustProducedMQ(temp_fgs);
					agent.setCurrentlyStoring(false);
				}
				
				//Produce High-Quality Finished Goods
				else if(action.equals("100")){
					temp_rms = agent.getRawMaterials();
					temp_fgs = temp_rms/HQ_RATE;
					temp_fgs = temp_fgs > MAX_HQ_PER_AC ?
									MAX_HQ_PER_AC : temp_fgs;
					temp_rms = temp_rms - temp_fgs*HQ_RATE;
					agent.setRawMaterials(temp_rms);
					agent.adjustProducedHQ(temp_fgs);
					agent.setCurrentlyStoring(false);
				}
				
				//Sell All Goods
				else if(action.equals("101")){
					
					//sell demand's worth of Low-Quality goods
					temp_fgs = agent.getProducedLQ();
					temp_fgs = temp_fgs > MAX_LQSELL ? MAX_LQSELL : temp_fgs;
					agent.adjustProducedLQ(-temp_fgs);
					money = temp_fgs * LQ_SALE;
					
					//sell demand's worth of Medium-Quality goods
					temp_fgs = agent.getProducedMQ();
					temp_fgs = temp_fgs > MAX_MQSELL ? MAX_MQSELL : temp_fgs;
					agent.adjustProducedMQ(-temp_fgs);
					money += temp_fgs * MQ_SALE;
					
					//sell demand's worth of High-Quality goods
					temp_fgs = agent.getProducedHQ();
					temp_fgs = temp_fgs > MAX_HQSELL ? MAX_HQSELL : temp_fgs;
					agent.adjustProducedHQ(-temp_fgs);
					money += temp_fgs * HQ_SALE;
					agent.adjustMoney(money);
					agent.setCurrentlyStoring(false);
				}
				
				//sell all risky goods
				else if(action.equals("110")){
					//stub action
					//sell demand's worth of Low-Quality goods
					temp_fgs = agent.getProducedLQ();
					temp_fgs = temp_fgs > MAX_LQSELL ? MAX_LQSELL : temp_fgs;
					agent.adjustProducedLQ(-temp_fgs);
					money = temp_fgs * LQ_SALE;
					
					//sell demand's worth of Medium-Quality goods
					temp_fgs = agent.getProducedMQ();
					temp_fgs = temp_fgs > MAX_MQSELL ? MAX_MQSELL : temp_fgs;
					agent.adjustProducedMQ(-temp_fgs);
					money += temp_fgs * MQ_SALE;
					
					//sell demand's worth of High-Quality goods
					temp_fgs = agent.getProducedHQ();
					temp_fgs = temp_fgs > MAX_HQSELL ? MAX_HQSELL : temp_fgs;
					agent.adjustProducedHQ(-temp_fgs);
					money += temp_fgs * HQ_SALE;
					agent.adjustMoney(money);
					agent.setCurrentlyStoring(false);
				}
				
				//store all RMs and FGs
				else if(action.equals("111")){
					agent.setCurrentlyStoring(true);
				}
			}
			
			//throw away unsaved RMs/FGs
			if(!agent.storingExcess())
				agent.dumpGoods();

            //compute the agent's current progress
            agent.tabulateIncomeHistory();
            if(agent.getIncomeRatio() < income_ratio_threshold && current_day < 15){
                if(!agent.getName().equals("My Agent")){
                    agent.markAgentIneffective();
                    continue;
                }
            }
		}
		
		current_day++;
	}
}