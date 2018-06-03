package com.example.emre.sestenyazya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.HashMap;

public class YazilarSQL extends SQLiteOpenHelper {
    public static String baslik,icerik,dosyayolu;


    public YazilarSQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Kayıtlı Notları Listelemek için Kullanılan Fonksiyon
    public void dataSorgu(String sql){
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(sql);
    }

    //Yeni Yazılar Eklemek için Kullanılan Fonksiyon
    public void veriEkle(String baslik,String icerik,String tarih,int arkaplan,int notResim,String dosyayolu){
        SQLiteDatabase database=getWritableDatabase();
        String sql="INSERT INTO NOTLAR VALUES (NULL,?,?,?,?,?,?)";

        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,baslik);
        statement.bindString(2,icerik);
        statement.bindString(3,tarih);
        statement.bindDouble(4,(double) arkaplan);
        statement.bindDouble(5,(double)notResim);
        statement.bindString(6,dosyayolu);

        statement.executeInsert();
    }


    //Yazıları Dünlenlemek için...
    public void veriGuncelle(String baslik,String icerik,String tarih,String dosyayolu,int id){
        SQLiteDatabase database=getWritableDatabase();
        //String sql="UPDATE NOTLAR SET baslik=?,icerik=?,tarih=? WHERE id=?";
        String sql="UPDATE NOTLAR SET baslik=?,icerik=?,tarih=?,dosyayolu=? WHERE id=?";
        SQLiteStatement statement=database.compileStatement(sql);

        statement.bindString(1,baslik);
        statement.bindString(2,icerik);
        statement.bindString(3,tarih);
        statement.bindString(4,dosyayolu);
        statement.bindDouble(5,(double) id);

        statement.execute();
        database.close();
    }

    //Silme işlemi Yaparken...
    public void verileriSil(int id){
        SQLiteDatabase database=getWritableDatabase();
        String sql="DELETE FROM NOTLAR WHERE id=?";
        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();

        statement.bindDouble(1,(double)id);
        statement.execute();
        database.close();
    }

    public Cursor verileriCek(String sql){
        SQLiteDatabase database=getReadableDatabase();
        return database.rawQuery(sql,null);
    }


    public void YazıCek(int id){
        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT baslik,icerik,dosyayolu FROM NOTLAR WHERE id="+id,null);
        cursor.moveToFirst();
        StringBuilder builder=new StringBuilder("");
        do{
            baslik=cursor.getString(0);
            icerik=cursor.getString(1);
            dosyayolu=cursor.getString(2);
        }while (cursor.moveToNext());

        cursor.close();
        database.close();
    }

        @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
