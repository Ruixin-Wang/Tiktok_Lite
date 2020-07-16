package com.example.tiktoklite.database;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
@Dao
public interface VideoDao {
    @Query("SELECT * FROM video")
    List<VideoEntity> loadAll();

    @Insert
    long addVideo(VideoEntity entity);

    @Query("DELETE FROM video")
    void deleteAll();

    @Delete
    void deleteVideo(VideoEntity entity);
}

