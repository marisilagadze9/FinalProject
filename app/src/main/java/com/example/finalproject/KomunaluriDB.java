package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KomunaluriDB extends SQLiteOpenHelper {


    final String DB_NAME="komunaluriDB";
    final int DB_VERSION=1;

    final String TABLE="komunaluri";

    final String COL_ID="id";
    final String COL_NAME="name";
    String COL_AMOUNT="amount";
    String COL_PAID="paid";
    String COL_DATE="date";

    public KomunaluriDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE " + TABLE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_AMOUNT + " REAL, " +
                COL_PAID + " INTEGER, " +
                COL_DATE + " TEXT" +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
    }

    public void add(Komunaluri k){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL_NAME,k.getName());
        cv.put(COL_AMOUNT,k.getAmount());
        cv.put(COL_PAID,k.isPaid() ? 1 : 0);
        cv.put(COL_DATE,k.getDate());
        db.insert(TABLE,null,cv);
        db.close();
    }

    public void update(Komunaluri k){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL_NAME,k.getName());
        cv.put(COL_AMOUNT,k.getAmount());
        cv.put(COL_PAID,k.isPaid() ? 1 : 0);
        cv.put(COL_DATE,k.getDate());
        db.update(TABLE,cv,COL_ID+"=?",new String[]{String.valueOf(k.getId())});
        db.close();
    }

    public List<Komunaluri> getall(){
        List<Komunaluri> list=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        Cursor cur=db.rawQuery("select * from "+TABLE,null);

        while(cur.moveToNext()){
            int id=cur.getInt(cur.getColumnIndexOrThrow(COL_ID));
            String name=cur.getString(cur.getColumnIndexOrThrow(COL_NAME));
            double amount=cur.getDouble(cur.getColumnIndexOrThrow(COL_AMOUNT));
            boolean paid=cur.getInt(cur.getColumnIndexOrThrow(COL_PAID))==1;
            String date=cur.getString(cur.getColumnIndexOrThrow(COL_DATE));

            list.add(new Komunaluri(id,name,amount,paid,date));
        }
        cur.close();
        return list;
    }
}
