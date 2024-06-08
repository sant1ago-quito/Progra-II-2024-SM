package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String dbname = "carros";
    private static final int v =1;
    private static final String SQLdb = "CREATE TABLE carros(id text, rev text, idCarro text, " +
            "marca text, motor text, chasis text, VIN text, combustion text, foto text)";
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
                sql = "INSERT INTO carros(id,rev,idCarro,marca,motor,chasis,VIN,combustion,foto) VALUES('"+ datos[0] +"','"+ datos[1] +"','"+ datos[2] +"', '"+
                        datos[3] +"', '"+ datos[4] +"','"+ datos[5] +"','"+ datos[6] +"', '"+ datos[7] +"', '"+ datos[8] +"' )";
            } else if (accion.equals("modificar")) {
                sql = "UPDATE carros SET id='"+ datos[0] +"',rev='"+ datos[1] +"', marca='"+ datos[3] +"', motor='"+ datos[4] +"', chasis='"+ datos[5] +"', VIN=" +
                        "'"+ datos[6] +"', combustion='"+ datos[7] +"', foto='"+ datos[8] +"' WHERE idCarro='"+ datos[2] +"'";
            } else if (accion.equals("eliminar")) {
                sql = "DELETE FROM carros WHERE idCarro='"+ datos[2] +"'";
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
        cursor = db.rawQuery("SELECT * FROM carros ORDER BY marca", null);
        return cursor;
    }
}