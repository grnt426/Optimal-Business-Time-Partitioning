package com.kurtzg.bizsim;

/*
 * File:            Error.java
 *
 * Author:          Grant Kurtz
 *
 * Description:     A simple class for throwing around error messages like an
 *                  exception but contained within the Event Driven
 *                  architecture
 */
public class Error {

    //vars
    String msg;

    public Error(String msg){
        this.msg = msg;
    }

    public String getMsg(){
        return msg;
    }

    public String toString(){
        return msg;
    }
}
