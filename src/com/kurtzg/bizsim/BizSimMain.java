package com.kurtzg.bizsim;

import sun.misc.JavaLangAccess;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import javax.swing.*;
import javax.xml.bind.Marshaller;

/**
* File:			BizSimMain.java
*
* Author:		Grant Kurtz
*
* Description:	Handles the initialization of the program, asks for user input
*				on environment and simulation values, and then creates the
*               model/view to start the program
*
*/
public class BizSimMain{

	public static void main(String[] args){
		
		// vars
		int numThreads = 1, numAgents = 100, numGenerations = 600;

        // attempt to parse the input
		try{
			//numThreads = Integer.parseInt(args[0]);
			//numAgents = Integer.parseInt(args[1]);
			//numGenerations = Integer.parseInt(args[2]);
		}
		catch(NumberFormatException nfe){
			System.err.println(nfe);
			System.exit(0);
		}

        // create our Model/View and start the program
        Model m = new Model();
        View v = new View(m, numThreads);
	}
}