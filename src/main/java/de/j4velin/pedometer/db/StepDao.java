package de.j4velin.pedometer.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StepDao {

    @Insert
    void insertSingleDay(StepLog sl);

    @Insert
    void insertMultipleDays(List<StepLog> slList);

    @Query("SELECT * FROM StepLog WHERE day = :day AND month = :month AND year = :year")
    StepLog getStepsOnThisDay (int day, int month, int year);

    @Update
    void updateDay(StepLog sl);

    @Delete
    void deleteDay(StepLog sl);

    @Query("SELECT * FROM StepLog WHERE id >= :start AND id <= :stop")
    StepLog[] getRange (int start, int stop);

    @Query("SELECT id FROM StepLog WHERE day = :day AND month = :month AND year = :year")
    int getID (int day, int month, int year);

}
