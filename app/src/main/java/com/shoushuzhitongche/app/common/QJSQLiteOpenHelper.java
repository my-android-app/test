package com.shoushuzhitongche.app.common;

import com.dixiang.framework.common.BaseSQLiteOpenHelper;
import com.shoushuzhitongche.app.view.doctor.bean.DeptSubCatEntity;
import com.shoushuzhitongche.app.view.localdata.bean.CommonEntity;
import com.shoushuzhitongche.app.view.localdata.bean.SubCityEntity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by dx on 15-4-18.
 */
public class QJSQLiteOpenHelper extends BaseSQLiteOpenHelper {
    public static final String TAG = "QJSQLiteOpenHelper";
    private static final int DATABASE_VERSION = 3;

    public QJSQLiteOpenHelper(Context context, String dbName) {
        super(context,dbName, new SQLiteDatabase.CursorFactory() {
            @Override
            public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
                Log.d(TAG, "Query: "+sqLiteQuery);
                return new SQLiteCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            }
        }, DATABASE_VERSION);
    }

    @NonNull
    @Override
    protected Class[] getRegisters() {
        return new Class[]{
        		SubCityEntity.class,
        		CommonEntity.class,
        		DeptSubCatEntity.class
        };
    }
}
