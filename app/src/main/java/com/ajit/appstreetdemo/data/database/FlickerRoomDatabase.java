package com.ajit.appstreetdemo.data.database;

import android.content.Context;

import com.ajit.appstreetdemo.data.models.FlickerPhotos;
import com.ajit.appstreetdemo.data.models.FlickerPhotosPhoto;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FlickerPhotos.class, FlickerPhotosPhoto.class}, version = 1, exportSchema = false)
public abstract class FlickerRoomDatabase extends RoomDatabase {
    public abstract PhotoDao flickerDao();

    private static volatile FlickerRoomDatabase INSTANCE;

    public static FlickerRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FlickerRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FlickerRoomDatabase.class, "flicker_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
