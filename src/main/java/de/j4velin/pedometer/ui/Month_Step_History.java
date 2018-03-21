package de.j4velin.pedometer.ui;

/**
 * Created by kiranbir on 2018-03-21.
 */

public class Month_Step_History {

    private int _step;
    private int _day;
    private long startDate = 1521143691;
    private long endDate = 1521143692;
    private int totalStep = 100;
    private int avgStep = 100;
    private long bestDay = 1521143691;


    public String getStartDate(){


        return "20180301";
    }
    public String getEndDate(){

        return "20180331";
    }

    public String getAvgStep(){

        return "1000";
    }
    public String getBestDay(){
        return "20180319";
    }


    public int get_step() {
        return _step;
    }

    public int get_day() {
        return _day;
    }

    public void set_step(int _step) {
        this._step = _step;
    }

    public void set_day(int _day) {
        this._day = _day;
    }
}
