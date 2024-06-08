package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String dbname = "pelis";
    private static final int v =1;
    private static final String SQLdb = "CREATE TABLE pelis(id text, rev text, idPelis text, " +
            "titulo text, sinopsis text, duracion text, actor text, foto text)";
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
                sql = "INSERT INTO pelis(id,rev,idPelis,titulo,sinopsis,duracion,actor,foto) VALUES('"+ datos[0] +"','"+ datos[1] +"','"+ datos[2] +"', '"+
                        datos[3] +"', '"+ datos[4] +"','"+ datos[5] +"','"+ datos[6] +"', '"+ datos[7] +"')";
            } else if (accion.equals("modificar")) {
                sql = "UPDATE pelis SET id='"+ datos[0] +"',rev='"+ datos[1] +"', titulo='"+ datos[3] +"', sinopsis='"+ datos[4] +"', duracion='"+ datos[5] +"', actor=" +
                        "'"+ datos[6] +"', foto='"+ datos[8] +"' WHERE idPelis='"+ datos[2] +"'";
            } else if (accion.equals("eliminar")) {
                sql = "DELETE FROM pelis WHERE idPelis='"+ datos[2] +"'";
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
        cursor = db.rawQuery("SELECT * FROM pelis ORDER BY titulo", null);
        return cursor;
    }
}