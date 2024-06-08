package com.ugb.controlesbasicos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class adaptadorimagenes extends BaseAdapter {
    Context context;
    ArrayList<amigos> datosAmigosArrayList;
    amigos datosAmigos;
    LayoutInflater layoutInflater;
    public adaptadorimagenes(Context context, ArrayList<amigos> datosProductosArrayList) {
        this.context = context;
        this.datosAmigosArrayList = datosProductosArrayList;
    }
    @Override
    public int getCount() {
        return datosAmigosArrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return datosAmigosArrayList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;//Long.parseLong(datosProductosArrayList.get(i).getIdproductos());
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.listview_imagenes, viewGroup, false);
        try{
            datosAmigos = datosAmigosArrayList.get(i);

            TextView tempVal = itemView.findViewById(R.id.lblcodigo);
            tempVal.setText(datosAmigos.getCodigo());

            tempVal = itemView.findViewById(R.id.lbldescripcion);
            tempVal.setText(datosAmigos.getDescripcion());

            tempVal = itemView.findViewById(R.id.lblprecio);
            tempVal.setText(datosAmigos.getPrecio());

            tempVal = itemView.findViewById(R.id.lblpresentacion);
            tempVal.setText(datosAmigos.getPresentacion());

            tempVal = itemView.findViewById(R.id.lblmarca);
            tempVal.setText(datosAmigos.getMarca());

            Bitmap imageBitmap = BitmapFactory.decodeFile(datosAmigos.getUrlFotoProdu());
            ImageView img = itemView.findViewById(R.id.imgFoto);
            img.setImageBitmap(imageBitmap);
        }catch (Exception e){
            Toast.makeText(context, "Error al mostrar los datos: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }
}