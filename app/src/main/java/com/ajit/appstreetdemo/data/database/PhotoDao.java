package com.ajit.appstreetdemo.data.database;

import com.ajit.appstreetdemo.data.models.FlickerPhotos;
import com.ajit.appstreetdemo.data.models.FlickerPhotosPhoto;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PhotoDao {
    @Insert
    void insertFlickerPhotos(FlickerPhotos flickerPhotos);

    @Update
    void updateFlickerPhotos(FlickerPhotos flickerPhotos);

    @Query("SELECT * from flicker_photos_table ")
    FlickerPhotos getPhotos();

    @Query("SELECT * from flicker_photos_table where search_text IN(:searchText) ")
    FlickerPhotos getPhotos(String searchText);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllFlickerPhotosPhoto(List<FlickerPhotosPhoto> flickerPhotosPhotoList);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFlickerPhotosPhoto(FlickerPhotosPhoto flickerPhotosPhotoList);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAllFlickerPhotosPhoto(FlickerPhotosPhoto flickerPhotosPhotoList);

    @Query("SELECT * from flicker_photos_photo_table where search_text IN(:searchText) ")
    List<FlickerPhotosPhoto> getAllPhotosPhotoList(String searchText);


    @Query("SELECT * from flicker_photos_photo_table where search_text IN (:searchText) and localId > :localId order by localId limit :perpage")
    List<FlickerPhotosPhoto> getPhotosPhotoList(int perpage, int localId, String searchText);


    @Query("SELECT * from flicker_photos_photo_table where id IN(:imageId) and search_text IN(:searchText) ")
    boolean isPhotoAvaliable(String imageId, String searchText);

    @Query("SELECT * from flicker_photos_photo_table where localId IN(:imageID) ")
    FlickerPhotosPhoto getSignlePhotosPhoto(int imageID);
}
