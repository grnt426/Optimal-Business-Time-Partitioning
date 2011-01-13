package com.kurtzg.bizsim;

public class Coordinate {

    private int x, y;
    private Object data;

    public Coordinate(){
        x = -1;
        y = -1;
        data = null;
    }

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
