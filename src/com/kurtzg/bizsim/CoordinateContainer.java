package com.kurtzg.bizsim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class CoordinateContainer {

    //vars
    private HashMap<String, Coordinate> coordinates;
    private int max_total_dist;

    public CoordinateContainer(){
        coordinates = new HashMap<String, Coordinate>();
        max_total_dist = 40;
    }

    public void clear(){
        coordinates.clear();
    }

    public void addPoint(int x, int y, Object data){
        coordinates.put(x+""+y, new Coordinate(x, y, data));
    }

    public Coordinate getPointAt(int x, int y){
        return coordinates.get(x+""+y);
    }

    public Collection<Coordinate> getAllCoordinates(){
        return coordinates.values();
    }

    public Object getDataAt(int x, int y){
        return getPointAt(x, y).getData();
    }

    public Coordinate getClosestPointAt(int x, int y){

        //vars
        double total_diff = -1, local_diff;
        int startx = x-5, starty = y-5, endx = x + 5, endy = y + 5;
        Coordinate coordinate = null;

        if(startx < 0)
                startx = 0;
        if(starty < 0)
                starty = 0;

        //search all coordinates
        for(int i = startx; i < endx; ++i){
            for(int k = starty; k < endy; ++k){

                //vars
                if(!coordinates.containsKey(i+""+k))
                    continue;

                //compute the total distance this coordinate is away
                local_diff = Math.sqrt(Math.pow(i-x, 2)+Math.pow(k-y, 2));
                if(local_diff < total_diff || total_diff == -1){
                    total_diff = local_diff;
                    coordinate = coordinates.get(i+""+k);
                }
                if(local_diff < 1)
                    return coordinate;
            }
        }
        return coordinate;
    }

    public Object getClosestDataAt(int x, int y){
        Coordinate c = getClosestPointAt(x, y);
        if(c == null)
            return null;
        return c.getData();
    }
}
