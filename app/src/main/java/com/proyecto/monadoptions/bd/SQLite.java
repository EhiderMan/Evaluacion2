package com.proyecto.monadoptions.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.util.Log;

import java.util.ArrayList;

public class SQLite {

    private sql sql;
    private SQLiteDatabase db;

    public SQLite (Context context){
        sql=new sql(context);
    }

    public void abrir(){
        Log.i("SQLite","Se abre conexión con BD"+ sql.getDatabaseName());
        db=sql.getWritableDatabase();
    }

    public void cerrar(){
        Log.i("SQLite","Se cierra conexión con BD"+ sql.getDatabaseName());
        sql.close();
    }

    public boolean addRegistroAnimalzoo(
             String especie, String habt, String name, String sex, String date, String edoGen,
            String weight, String image
    ){
        String status="ACTIVO";
        ContentValues cv = new ContentValues();
        cv.put("ESPECIE",especie);
        cv.put("HABITAT",habt);
        cv.put("NOMBRE",name);
        cv.put("SEXO",sex);
        cv.put("FECHA_INGRESO",date);
        cv.put("EDO_GENERAL",edoGen);
        cv.put("PESO",weight);
        cv.put("STATUS",status);
        cv.put("IMAGEN",image);

        return (db.insert("ANIMALESZOO", null, cv)!= -1) ? true:false;
    }


    public Cursor getRegistro(){
        return db.rawQuery("SELECT * FROM ANIMALESZOO", null);
    }

    public Cursor getRegistroActivo(){
        return db.rawQuery("SELECT * FROM ANIMALESZOO WHERE STATUS='ACTIVO'",null);
    }
    public Cursor getRegistroINactivo(){
        return db.rawQuery("SELECT * FROM ANIMALESZOO WHERE STATUS='INACTIVO'", null);
    }

    public ArrayList<String> getAnimales(Cursor cursor){

        ArrayList<String> ListData = new ArrayList<>();
        String item ="";
        if (cursor.moveToFirst()){
            do {
                item += "ID: ["+ cursor.getString(0)+"]\r\n";
                item += "Especie: ["+ cursor.getString(1)+"]\r\n";
                item += "Habitat: ["+ cursor.getString(2)+"]\r\n";
                item += "Nombre: ["+ cursor.getString(3)+"]\r\n";
                item += "Sexo: ["+ cursor.getString(4)+"]\r\n";
                item += "Fecha de ingreso: ["+ cursor.getString(5)+"]\r\n";
                item += "Estado General: ["+ cursor.getString(6)+"]\r\n";
                item += "Peso: ["+ cursor.getString(7)+"]\r\n";
                item += "Status: ["+ cursor.getString(8)+"]\r\n";
                ListData.add(item);
                item="";
            }while (cursor.moveToNext());
        }
        return ListData;
    }
    public ArrayList<String> getIDall(Cursor cursor){

        ArrayList<String> ListData = new ArrayList<>();
        String item ="";
        if (cursor.moveToFirst()){
            do {
                item +=cursor.getString(0);
                ListData.add(item);
                item="";
            }while (cursor.moveToNext());
        }
        return ListData;
    }


    public ArrayList<String> getImagenes(Cursor cursor){

        ArrayList<String> ListData = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                ListData.add(cursor.getString(9));
            }while (cursor.moveToNext());
        }
        return ListData;
    }

    public ArrayList<String> getID(Cursor cursor){

        ArrayList<String> ListData = new ArrayList<>();
        String item ="";
        if (cursor.moveToFirst()){
            do {
                item += "ID: ["+ cursor.getString(0)+"]\r\n";
                ListData.add(item);
                item="";
            }while (cursor.moveToNext());
        }
        return ListData;
    }

    public String updateRegistroAnimal(
            int id, String especie, String habt, String name, String sex, String date, String edoGen,
            String weight,String status, String image
    ){
        ContentValues cv = new ContentValues();
        cv.put("ID",id);
        cv.put("ESPECIE",especie);
        cv.put("HABITAT",habt);
        cv.put("NOMBRE",name);
        cv.put("SEXO",sex);
        cv.put("FECHA_INGRESO",date);
        cv.put("EDO_GENERAL",edoGen);
        cv.put("PESO",weight);
        cv.put("STATUS",status);
        cv.put("IMAGEN",image);

        int valor=db.update("ANIMALESZOO",cv,"ID = "+id,null);
        if (valor==1){
            return "Animal actualizado";
        }else {
            return "Error en la actualización";
        }
    }
    public String StatusActivo(int id){
        String status="ACTIVO";
        ContentValues cv = new ContentValues();
        cv.put("STATUS",status);

        int valor=db.update("ANIMALESZOO",cv,"ID= "+id,null);
        if (valor==1){
            return "Animal Activado";
        }else {
            return "Error en la Activación";
        }
    }




    public Cursor getValorActivos(int id){
        return db.rawQuery("SELECT * FROM ANIMALESZOO WHERE ID="+ id+" AND STATUS='ACTIVO'",null);
    }
    public Cursor getValorall(int id){
        return db.rawQuery("SELECT * FROM ANIMALESZOO WHERE ID="+ id,null);
    }

    public int EliminarPermanente(Editable id){
        return db.delete("ANIMALESZOO", "ID= "+id,null);
    }
    public int EliminacionLog(Editable id){
        String status="INACTIVO";
        ContentValues cv = new ContentValues();
        cv.put("STATUS",status);
        return db.update("ANIMALESZOO", cv,"ID= "+id,null);
    }

}
