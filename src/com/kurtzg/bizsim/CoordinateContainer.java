package com.kurtzg.bizsim;

import java.util.ArrayList;
import java.util.List;

public class CoordinateContainer {

    //vars
    private List<Coordinate> coordinates;
    private int max_total_dist;

    public CoordinateContainer(){
        coordinates = new ArrayList<Coordinate>();
        max_total_dist = 40;
    }

    public void clear(){
        coordinates.clear();
    }

    public void addPoint(int x, int y, Object data){
        coordinates.add(new Coordinate(x, y, data));
    }

    public Coordinate getPointAt(int x, int y){
        for(Coordinate c : coordinates){
            if(c.getX() == x && c.getY() == y)
                return c;
        }
        return null;
    }

    public Object getDataAt(int x, int y){
        return getPointAt(x, y).getData();
    }

    public Coordinate getClosestPointAt(int x, int y){

        //vars
        double total_diff = -1;
        Coordinate coordinate = null;

        //search all coordinates
        for(Coordinate c : coordinates){

            //vars
            int dx = c.getX(), dy = c.getY();
            double local_diff;

            //compute the total distance this coordinate is away
            local_diff = Math.sqrt(Math.pow(dx-x, 2)+Math.pow(dy-y, 2));
            if((local_diff < total_diff || total_diff == -1)
                    && local_diff <= max_total_dist){
                total_diff = local_diff;
                coordinate = c;
            }
        }
        return coordinate;
    }

    public Object getClosestDataAt(int x, int y){
        return getClosestPointAt(x, y).getData();
    }
}
