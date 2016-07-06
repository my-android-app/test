package com.dixiang.framework.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.support.annotation.NonNull;
import java.util.List;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by dx on 15/4/18.
 */
public abstract class BaseSQLiteOpenHelper extends SQLiteOpenHelper {

    public BaseSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        for (Class clz : getRegisters()) {
            cupboard().register(clz);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // this will ensure that all tables are created
        cupboard().withDatabase(db).createTables();
        // add indexes and other database tweaks
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this will upgrade tables, adding columns and new tables.
        // Note that existing columns will not be converted
        cupboard().withDatabase(db).upgradeTables();
        // do migration work
    }

    public long putObject(Object obj){
        //存储单个对象
        SQLiteDatabase db = getWritableDatabase();
        return cupboard().withDatabase(db).put(obj);
    }
    public void pubObject(Message msg){
        SQLiteDatabase db =getWritableDatabase();
        cupboard().withDatabase(db).put(msg);
    }
    public void putList(List<?> obj){
        //存储一组对象
        SQLiteDatabase db = getWritableDatabase();
        cupboard().withDatabase(db).put(obj);
    }
    public int updateById(Class cls,long id,ContentValues values){
        SQLiteDatabase db = getWritableDatabase();
        return cupboard().withDatabase(db).update(cls,values,"_id=?",id+"");
    }


    public int update(Class cls, ContentValues values, String selection, String... selectionArgs){
        SQLiteDatabase db =getWritableDatabase();
        return cupboard().withDatabase(db).update(cls, values, selection, selectionArgs);
    }

    public Cursor querySQL(String sql, String... args){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, args);
    }

    public void execUpdate(String sql, String... args){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql, args);
    }

    public Object getObject(Class cls, long id){
        SQLiteDatabase db = getReadableDatabase();
        return cupboard().withDatabase(db).get(cls, id);
    }
    public boolean delete(Class cls ,long id){
        SQLiteDatabase db = getWritableDatabase();
        return cupboard().withDatabase(db).delete(cls,id);
    }

    public Cursor getCursor(Class cls){
        SQLiteDatabase db =getReadableDatabase();
        return cupboard().withDatabase(db).query(cls).getCursor();
    }

//    public void iterator(Class cls){
//        SQLiteDatabase db =getReadableDatabase();
//        cls = (Class) cupboard().withDatabase(db).query(cls).get();
//        QueryResultIterable<Object> itr = cupboard().withDatabase(db).query(cls)
//    }

    protected abstract @NonNull Class[] getRegisters();

}
