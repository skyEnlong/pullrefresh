package com.eileng.app.library.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enlong on 2017/1/22.
 */

public abstract class BaseDBHelper extends SQLiteOpenHelper {
  public Context mContext;

    public BaseDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public List<String> getTableNames(){
        List<String> tables = null;
        SQLiteDatabase db = getReadableDatabase();
        if(null != db) {
            Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
            if(null != cursor){
                tables = new ArrayList<>();
                while (cursor.moveToNext()) {
                    String table_name =cursor.getString(0);

                    tables.add(table_name);
                }
            }
        }
        return tables;
    }



    /* 判断列名是否存在 */
    public boolean isColumnExist(SQLiteDatabase db, String tableName,
                                 String columnName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }

        try {
            Cursor cursor = null;
            String sql = "select count(1) as c from sqlite_master where type ='table' and name ='"
                    + tableName.trim()
                    + "' and sql like '%"
                    + columnName.trim() + "%'";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

            cursor.close();
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 判断某张表是否存在
     * <p/>
     * 表名
     *
     * @return
     */
    public boolean isTableExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }

        try {
            Cursor cursor = null;
            String sql = "select count(1) as c from sqlite_master where type ='table' and name ='"
                    + tableName.trim() + "'";
            cursor = getWritableDatabase().rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

            cursor.close();
        } catch (Exception e) {
        }
        return result;
    }

}