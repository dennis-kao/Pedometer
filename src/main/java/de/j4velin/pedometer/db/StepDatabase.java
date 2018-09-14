package de.j4velin.pedometer.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {StepLog.class}, version = 1, exportSchema = false)
public abstract class StepDatabase extends RoomDatabase{

    public static final String DATABASE_NAME = "Steps.db";
    private static StepDatabase sInstance;

    public abstract StepDao dao();

    public StepDatabase getInstance (final Context c) {

        /* Implements singleton pattern for database instance */

        if (sInstance == null) {
            synchronized (StepDatabase.class) {
                sInstance = Room.databaseBuilder(c, StepDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
            }
        }

        return sInstance;
    }
}
