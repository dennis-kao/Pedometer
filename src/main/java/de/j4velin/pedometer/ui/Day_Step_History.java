package de.j4velin.pedometer.ui;

/**
 * Created by averyspeller on 2018-03-15.
 */

public class Day_Step_History {

    private int _step;
    private int _day;
    private long startDate = 1521143691;
    private long endDate = 1521143692;
    private int totalStep = 100;
    private int avgStep = 100;
    private long bestDay = 1521143691;


    public String getStartDate(){


        return "1521143691";
    }
    public String getEndDate(){

        return "1521143692";
    }
    public String getTotalStep(){

        return "100";
    }
    public String getAvgStep(){

        return "100";
    }
    public String getBestDay(){
        return "1521143691";
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
