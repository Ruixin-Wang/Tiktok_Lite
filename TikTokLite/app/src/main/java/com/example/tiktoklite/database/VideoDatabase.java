package com.example.tiktoklite.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {VideoEntity.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class VideoDatabase extends RoomDatabase {
    private static volatile VideoDatabase INSTANCE;
    public abstract VideoDao videoDao();
    public VideoDatabase() {

    }

    public static VideoDatabase inst(Context context, String userid, String Type) {
        if (INSTANCE == null) {
            synchronized (VideoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VideoDatabase.class, userid+Type+".db").build();
                }
            }
        }
        return INSTANCE;
    }
}
