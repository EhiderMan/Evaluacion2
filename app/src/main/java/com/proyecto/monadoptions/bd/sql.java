package com.proyecto.monadoptions.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class sql extends SQLiteOpenHelper {
    private static final String database="animaleszoo";
    private static final int VERSION=1;

    private final String tAnimaleszoo="CREATE TABLE ANIMALESZOO(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
            "ESPECIE TEXT NOT NULL, "+
            "HABITAT TEXT NOT NULL, "+
            "NOMBRE TEXT NOT NULL, "+
            "SEXO TEXT NOT NULL, "+
            "FECHA_INGRESO TEXT NOT NULL, "+
            "EDO_GENERAL TEXT NOT NULL, "+
            "PESO TEXT NOT NULL, "+
            "STATUS TEXT NOT NULL, "+
            "IMAGEN TEXT NOT NULL );";

    public sql(Context context){
        super(context,database,null,VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tAnimaleszoo);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion>oldVersion){
            db.execSQL("DROP TABLE IF EXISTS ANIMALESZOO");
            db.execSQL(tAnimaleszoo);
        }
    }
}
