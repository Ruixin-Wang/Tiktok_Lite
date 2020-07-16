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
public interface LoginDao {
    @Query("SELECT * FROM login")
    List<LoginEntity> loadAll();

    @Query("SELECT name FROM login WHERE id = :userId")
    String select_id(String userId);

    @Insert
    long addInfo(LoginEntity entity);

    @Query("DELETE FROM login")
    void deleteAll();

}

