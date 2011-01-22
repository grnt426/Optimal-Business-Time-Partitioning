package com.kurtzg.bizsim;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
* File:			Environment.java
*
* Author:		Grant Kurtz
*
* Description:	Contains all information about the environment the agents will
*				participate in.  This class is also able to simulate a business
*				day given a list of agents.
*/
public class Environment{

	// instance variables
	private int RM_COST, LQ_RATE, MQ_RATE, HQ_RATE, LQ_SALE, MQ_SALE, HQ_SALE,
				MAX_RMS_PER_AC, MAX_LQ_PER_AC, MAX_MQ_PER_AC, MAX_HQ_PER_AC,
                MAX_LQSELL, MAX_MQSELL, MAX_HQSELL;
	private int ACTIONS_TOTAL, current_day, total_days;
    private double income_ratio_threshold, RISK_BUY_ROI, RISK_SELL_ROI;
	private ArrayList<Agent> agents;
    private List<Boolean> riskBuyDays;
    private List<Boolean> riskSellDays;
	
	/*
	* Initializes with hard-coded defaults
	*/
	public Environment(){

        // setup some default values
		agents = new ArrayList<Agent>();
        riskBuyDays = new ArrayList<Boolean>();
        riskSellDays = new ArrayList<Boolean>();
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
        total_days = 100;
        income_ratio_threshold = 1.2;
        RISK_BUY_ROI = .25;
        RISK_SELL_ROI = .15;

        // randomly populate the buy/sell risk days such that 1/3 of the days
        // give a ROI
        int rand;
        Random generator = new Random();
        for(int i = 0; i < total_days; ++i){
            rand = generator.nextInt(100);
            riskBuyDays.add(rand < 33);
            rand = generator.nextInt(100);
            riskSellDays.add(rand < 33);
        }

	}

    /*
     * Copy-Constructor
     */
    public Environment(Environment e){

        this();

        if(e == null)
            return;

        RM_COST = e.RM_COST;
        LQ_RATE = e.LQ_RATE;
        MQ_RATE = e.MQ_RATE;
        HQ_RATE = e.HQ_RATE;
        LQ_SALE = e.LQ_SALE;
        MQ_SALE = e.MQ_SALE;
        HQ_SALE = e.HQ_SALE;
        income_ratio_threshold = e.income_ratio_threshold;
        riskBuyDays = new ArrayList<Boolean>(e.riskBuyDays);
        riskSellDays = new ArrayList<Boolean>(e.riskSellDays);
    }

	public void addAgent(Agent a){
		agents.add(a);
	}

	public void addAgents(List<Agent> a){
		agents.addAll(a);
	}

    /*
     * Clears the environment of agents and resets the environment so that
     * another generation can be processed
     */
	public void purgeAgents(){
        if(agents != null)
		    agents.clear();
        current_day = 0;
	}

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

        // loop through all given agents
		for(Agent agent : agents){

            // grab the agent's actions
			ArrayList<Boolean> actions;
			actions = agent.getChrome();
			int money, rms, lqfg, mqfg, hqfg;
			int temp_rms, temp_fgs, temp_sale;

			// process all the actions for this agent
			for(int i = 0; i<actions.size();i=i+3){

                // break apart the chromosome into 3-bit sized genes
				String action = actions.get(i) ? "1":"0";
				action += actions.get(i+1) ? "1" : "0";
				action += actions.get(i+2) ? "1" : "0";
                boolean riskSale = riskSellDays.get(current_day);
                boolean riskBuy = riskBuyDays.get(current_day);

				// Search for Raw Materials
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
				
				// Risky search for Raw Materials
                // TODO: TEST THIS
				else if(action.equals("001")){

                    // vars
					money = agent.getMoney();
                    double risk_payoff = riskBuy ? 1+RISK_BUY_ROI : 1-RISK_BUY_ROI;

                    // compute new rms/money
                    temp_rms = money/(int)(RM_COST*risk_payoff);
                    temp_rms = temp_rms > (MAX_RMS_PER_AC*risk_payoff) ?
                                    (int)(MAX_RMS_PER_AC*risk_payoff) : temp_rms;
                    money = money - (temp_rms * (int)(RM_COST*risk_payoff));

                    // update our agent
					agent.setMoney(money);
					agent.adjustRawMaterials(temp_rms);
					agent.setCurrentlyStoring(false);
				}
				
				// Produce Low-Quality Finished Goods
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
				
				// Produce Medium-Quality Finished Goods
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
				
				// Produce High-Quality Finished Goods
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
				
				// Sell All Goods
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
				
				// sell all risky goods
                // TODO: TEST THIS
				else if(action.equals("110")){

                    // vars
                    double risk_payoff = riskSale ? 1+RISK_SELL_ROI : 1-RISK_SELL_ROI;
                    int new_max;

					// sell demand's worth of Low-Quality goods
					temp_fgs = agent.getProducedLQ();
                    new_max = (int)(MAX_LQSELL * risk_payoff);
					temp_fgs = temp_fgs > new_max ? new_max : temp_fgs;
					agent.adjustProducedLQ(-temp_fgs);
					money = temp_fgs * (int)(LQ_SALE * risk_payoff);
					
					// sell demand's worth of Medium-Quality goods
					temp_fgs = agent.getProducedMQ();
                    new_max = (int)(MAX_MQSELL * risk_payoff);
					temp_fgs = temp_fgs > new_max ? new_max : temp_fgs;
					agent.adjustProducedMQ(-temp_fgs);
					money += temp_fgs * (int)(MQ_SALE*risk_payoff);
					
					// sell demand's worth of High-Quality goods
					temp_fgs = agent.getProducedHQ();
                    new_max = (int)(MAX_HQSELL * risk_payoff);
					temp_fgs = temp_fgs > new_max ? new_max : temp_fgs;
					agent.adjustProducedHQ(-temp_fgs);
					money += temp_fgs * (int)(HQ_SALE*risk_payoff);

                    // update the agent
					agent.adjustMoney(money);
					agent.setCurrentlyStoring(false);
				}
				
				// store all RMs and FGs
				else if(action.equals("111")){
					agent.setCurrentlyStoring(true);
				}
			}
			
			// throw away unsaved RMs/FGs
			if(!agent.storingExcess())
				agent.dumpGoods();

            // compute the agent's current progress
            // TODO: getIncomeRatio() function does not work as intended,
            // TODO: so no agents are ever marked as ineffective
            agent.tabulateIncomeHistory();
            if(agent.getIncomeRatio() < income_ratio_threshold && current_day < 0){
                agent.markAgentIneffective();
                continue;
            }
		}
        current_day++;
	}
}