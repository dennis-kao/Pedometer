package de.j4velin.pedometer.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(
indices = @Index(value = {"month", "day", "year"}, unique = true)
)
public class StepLog {

    @PrimaryKey(autoGenerate = true)
    private int id;

    //  DATE
    private int month;
    private int day;
    private int year;

    private int steps; //   2,147,483,648 and a maximum value of 2,147,483,647

    public StepLog(int month, int day, int year, int steps) {
        this.month = month;
        this.day = day;
        this.year = year;
        this.steps = steps;
    }

    public void setSteps(int steps) {this.steps = steps;}

    public int getMonth() { return this.month;}

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public int getSteps() {
        return steps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
