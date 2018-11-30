package com.risk.common;

/**
 * thread sleep time
 */
public class SleepTime {

    private static int sleepTime = 500;

    /**
     * setter
     * @param time new time
     */
    public static void setSleepTime(int time){
        sleepTime = time;
    }

    /**
     * getter
     * @return time
     */
    public static int getSleepTime(){
        return sleepTime;
    }

}
