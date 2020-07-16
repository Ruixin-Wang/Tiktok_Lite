package com.example.tiktoklite.database;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "login")
public class LoginEntity {

    @ColumnInfo(name = "id")
    private String mId;

    @ColumnInfo(name = "name")
    private String mName;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "avatar")
    private  int mAvatar;



    public LoginEntity(String mId, String mName, int mAvatar) {
        this.mId = mId;
        this.mName = mName;
        this.mAvatar = mAvatar;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getAvatar() {
        return mAvatar;
    }



}
