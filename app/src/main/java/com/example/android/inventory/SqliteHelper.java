package com.example.android.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SqliteHelper extends SQLiteOpenHelper {
    String sqlCreate = "CREATE TABLE " + Contract.ContractN.TABLE_NAME + "(" + Contract.ContractN._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + Contract.ContractN.NAME + " TEXT," +
            Contract.ContractN.PRICE + " REAL," + Contract.ContractN.QUANTITY + " INTEGER," + Contract.ContractN.DESC + " TEXT," + Contract.ContractN.THUMBNAIL + " BLOB)";

    public SqliteHelper(@Nullable Context context) {
        super(context, Contract.ContractN.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.ContractN.TABLE_NAME);
        onCreate(db);
    }

    //insert & Read & Delete--------------------------------------------
    public Cursor read() {
        SQLiteDatabase sql = getReadableDatabase();
        String[] columns = {Contract.ContractN.NAME, Contract.ContractN.PRICE, Contract.ContractN.QUANTITY, Contract.ContractN.THUMBNAIL};
        return sql.query(Contract.ContractN.TABLE_NAME, columns, null, null, null, null, null);
    }

    public Cursor readALL() {
        SQLiteDatabase sql = getReadableDatabase();
        return sql.query(Contract.ContractN.TABLE_NAME, null, null, null, null, null, null);
    }
    public Cursor readAt(int position) {
        SQLiteDatabase sql = getReadableDatabase();
        return sql.query(Contract.ContractN.TABLE_NAME, null, Contract.ContractN._ID+""+" =?", new String[]{position+""}, null, null, null);
    }
    public boolean insert(String name, float price, int quantity, byte[] thumbnail, String description) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(Contract.ContractN.NAME, name);
        data.put(Contract.ContractN.PRICE, price);
        data.put(Contract.ContractN.QUANTITY, quantity);
        data.put(Contract.ContractN.THUMBNAIL, thumbnail);
        data.put(Contract.ContractN.DESC, description);
        long value = sql.insert(Contract.ContractN.TABLE_NAME, null, data);
        if (value == -1) return false;
        else return true;
    }

    public void update(int q, int rowID) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(Contract.ContractN.QUANTITY, q);
        sql.update(Contract.ContractN.TABLE_NAME, data, Contract.ContractN._ID + "" + "=?", new String[]{rowID + ""});
    }
    public void delete(int rowID){
        SQLiteDatabase sql=getReadableDatabase();
        sql.delete(Contract.ContractN.TABLE_NAME, Contract.ContractN._ID+""+"=?",new String[]{rowID+""});
    }
}
