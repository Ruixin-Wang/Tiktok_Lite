package com.example.tiktoklite.database;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "video")
public class VideoEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pos")
    private Long mPos;

    @ColumnInfo(name = "id")
    private String mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "imgUrl")
    private String mImgUrl;

    @ColumnInfo(name = "videoUrl")
    private String mVideoUrl;



    public VideoEntity(String mId, String mName, String mImgUrl, String mVideoUrl, Long mPos) {
        this.mId = mId;
        this.mName = mName;
        this.mImgUrl = mImgUrl;
        this.mVideoUrl = mVideoUrl;
        this.mPos = mPos;
    }

    public Long getPos() {
        return mPos;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }



}
