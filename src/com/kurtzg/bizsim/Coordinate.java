package com.kurtzg.bizsim;

/*
 * File:        Coordinate.java
 *
 * Author:      Grant Kurtz
 *
 * Description: A simple class used to hold an arbitrary piece of data that is
 *              represented by the given coordinates of some graph
 *
 * TODO:        Switch over to generics
 */
public class Coordinate {

    // instance vars
    private int x, y;
    private Object data;

    /*
     * Default Constructor, sets up null values
     */
    public Coordinate(){
        x = -1;
        y = -1;
        data = null;
    }

    /*
     * Sets up this coordinates x/y position and its corresponding data object
     *
     * Param:   x           the x-coordinate
     * Param:   y           the y-coordinate
     * Param:   o           the object that is represented by the given x/y
     *                      points
     */
    public Coordinate(int x, int y, Object o){
        this.x = x;
        this.y = y;
        data = o;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Object getData(){
        return data;
    }
}
