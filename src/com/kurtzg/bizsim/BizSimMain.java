package com.kurtzg.bizsim;
/**
* File:			com.kurtzg.bizsim.BizSimMain.java
*
* Author:		Grant Kurtz
*
* Description:	Handles the initialization of the program, asks for user input
*				on environment and simulation values, and then creates the
*               model/view to start the program
*
*/


// imports
import sun.misc.JavaLangAccess;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import javax.swing.*;
import javax.xml.bind.Marshaller;

public class BizSimMain{

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

        //create our Model/View and start the program
        Model m = new Model();
        View v = new View(m, numThreads);
	}
}