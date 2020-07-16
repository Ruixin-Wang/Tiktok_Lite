package com.example.tiktoklite.database;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
public class DateConverter {
    @TypeConverter
    public Date fromTimeStamp(long ts) {
        return new Date(ts);
    }

    @TypeConverter
    public long toTimeStamp(Date date) {
        return date.getTime();
    }


}
