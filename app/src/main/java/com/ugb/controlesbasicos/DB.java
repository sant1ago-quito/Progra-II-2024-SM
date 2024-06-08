package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String dbname = "notas";
    private static final int v =1;
    private static final String SQLdb = "CREATE TABLE notas(id text, rev text, idNota text, " +
            "nombre text, titulo text, emocion text, contenido text, foto text, actualizado text)";
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, v);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQLdb);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //cambiar estructura de la BD
    }
    public String administrar_amigos(String accion, String[] datos){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "";
            if( accion.equals("nuevo") ){
                sql = "INSERT INTO notas(id,rev,idNota,nombre,titulo,emocion,contenido,foto,actualizado) VALUES('"+ datos[0] +"','"+ datos[1] +"','"+ datos[2] +"', '"+
                        datos[3] +"', '"+ datos[4] +"','"+ datos[5] +"','"+ datos[6] +"', '"+ datos[7] +"', '"+ datos[8] +"')";
            } else if (accion.equals("modificar")) {
                sql = "UPDATE notas SET id='"+ datos[0] +"',rev='"+ datos[1] +"',nombre='"+ datos[3] +"', titulo='"+ datos[4] +"', emocion='"+ datos[5] +"', contenido=" +
                        "'"+ datos[6] +"', foto='"+ datos[7] +"', actualizado='"+ datos[8] +"' WHERE idNota='"+ datos[2] +"'";
            } else if (accion.equals("eliminar")) {
                sql = "DELETE FROM notas WHERE idNota='"+ datos[2] +"'";
            }
            db.execSQL(sql);
            return "ok";
        }catch (Exception e){
            return e.getMessage();
        }
    }
    public Cursor obtener_amigos(){
        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM notas ORDER BY nombre", null);
        return cursor;
    }
    public Cursor pendientesActualizar(){
        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM notas WHERE actualizado='no'", null);
        return cursor;
    }
}