package com.example.tiktoklite.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {LoginEntity.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class LoginDatabase extends RoomDatabase {
    private static volatile LoginDatabase INSTANCE;
    public abstract LoginDao loginDao();
    public LoginDatabase() {

    }

    public static LoginDatabase inst(Context context) {
        if (INSTANCE == null) {
            synchronized (LoginDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), LoginDatabase.class, "login.db").build();
                }
            }
        }
        return INSTANCE;
    }
}
