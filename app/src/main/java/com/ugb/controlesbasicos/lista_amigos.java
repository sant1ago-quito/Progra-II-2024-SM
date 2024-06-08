package com.ugb.controlesbasicos;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import kotlin.contracts.Returns;

public class lista_amigos extends AppCompatActivity {
    TextView tempVal;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    Bundle paramatros = new Bundle();
    DB db;
    ListView lts;
    Cursor cAmigos;
    final ArrayList<amigos> alAmigos = new ArrayList<amigos>();
    final ArrayList<amigos> alAmigosCopy = new ArrayList<amigos>();
    amigos datosAmigos;
    FloatingActionButton btn,btn_logout;
    JSONArray datosJSON; //para los datos que vienen del servidor.
    JSONObject jsonObject;
    obtenerDatosServidor datosServidor;
    detectarInternet di;
    int posicion = 0;
    DatabaseReference databaseReference;
    String miToken="";
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_amigos);
        tempVal = findViewById(R.id.lblSensorLuz);
        activarSensorLuz();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        lts = findViewById(R.id.ltsAmigos);
        db = new DB(getApplicationContext(),"", null, 1);
        btn = findViewById(R.id.fabAgregarAmigos);
        btn_logout = findViewById(R.id.fabCerrarSesion);

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        }
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paramatros.putString("accion","nuevo");
                abrirActividad(paramatros);
            }
        });
        di = new detectarInternet(getApplicationContext());
        if( di.hayConexionInternet() ){//online
            obtenerDatosAmigosServidor();
            //sincronizar();
        }else{//offline
            mostrarMsg("No hay conexion, datos en local");
            obtenerAmigos();
        }
        buscarAmigos();
        mostrarChats();
    }
    //inicio sesor
    @Override
    protected void onResume() {
        iniciar();
        super.onResume();
    }
    @Override
    protected void onPause() {
        detener();
        super.onPause();
    }
    private void activarSensorLuz(){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(sensor==null){
            tempVal.setText("Tu telefono NO tiene sensor de Luz");
            finish();
        }
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                double valor = sensorEvent.values[0];
                tempVal.setText("Luz: "+ valor);
                if( valor<=20 ){
                    getWindow().getDecorView().setBackgroundColor(Color.parseColor("#8f7193"));
                } else if (valor<=50) {
                    getWindow().getDecorView().setBackgroundColor(Color.parseColor("#c0a0c3"));
                }else{
                    getWindow().getDecorView().setBackgroundColor(Color.parseColor("#e5dde6"));
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }
    private void iniciar(){
        sensorManager.registerListener(sensorEventListener, sensor, 2000*1000);
    }
    private void detener(){
        sensorManager.unregisterListener(sensorEventListener);
    }
    //fin sensor
    private void mostrarChats(){
        lts.setOnItemClickListener((parent, view, position, id) -> {
            try{
                Bundle bundle = new Bundle();
                bundle.putString("nombre", datosJSON.getJSONObject(position).getString("nombre") );
                bundle.putString("to", datosJSON.getJSONObject(position).getString("to") );
                bundle.putString("from", datosJSON.getJSONObject(position).getString("from") );
                bundle.putString("urlCompletaFoto", datosJSON.getJSONObject(position).getString("urlCompletaFoto") );
                bundle.putString("urlFotoAmigoFirestore", datosJSON.getJSONObject(position).getString("urlFotoAmigoFirestore") );

                Intent intent = new Intent(getApplicationContext(), chats.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }catch (Exception ex){
                mostrarMsg(ex.getMessage());
            }
        });
    }
    private void sincronizar(){
        try{
            cAmigos = db.pendientesActualizar();
            if( cAmigos.moveToFirst() ){//hay registros pendientes de sincronizar con el servidor
                mostrarMsg("Sincronizando...");
                jsonObject = new JSONObject();

                do{
                    if( cAmigos.getString(0).length()>0 && cAmigos.getString(1).length()>0 ){
                        jsonObject.put("_id", cAmigos.getString(0));
                        jsonObject.put("_rev", cAmigos.getString(1));
                    }
                    jsonObject.put("idNota", cAmigos.getString(2));
                    jsonObject.put("nombre", cAmigos.getString(3));
                    jsonObject.put("titulo", cAmigos.getString(4));
                    jsonObject.put("emocion", cAmigos.getString(5));
                    jsonObject.put("contenido", cAmigos.getString(6));
                    jsonObject.put("urlCompletaFoto", cAmigos.getString(7));
                    jsonObject.put("actualizado", "si");

                    enviarDatosServidor objGuardarDatosServidor = new enviarDatosServidor(getApplicationContext());
                    String respuesta = objGuardarDatosServidor.execute(jsonObject.toString()).get();

                    JSONObject respuestaJSONObject = new JSONObject(respuesta);
                    if (respuestaJSONObject.getBoolean("ok")) {
                        DB db = new DB(getApplicationContext(), "",null, 1);
                        String[] datos = new String[]{
                                respuestaJSONObject.getString("id"),
                                respuestaJSONObject.getString("rev"),
                                jsonObject.getString("idNota"),
                                jsonObject.getString("nombre"),
                                jsonObject.getString("titulo"),
                                jsonObject.getString("emocion"),
                                jsonObject.getString("contenido"),
                                jsonObject.getString("urlCompletaFoto"),
                                jsonObject.getString("actualizado")
                        };
                        respuesta = db.administrar_amigos("modificar", datos);
                        if(!respuesta.equals("ok")){
                            mostrarMsg("Error al guardar la actualizacion en local "+ respuesta);
                        }
                    } else {
                        mostrarMsg("Error al sincronizar datos en el servidor "+ respuesta);
                    }
                }while (cAmigos.moveToNext());
                mostrarMsg("Sincronizacion completa.");
            }
        }catch (Exception e){
            mostrarMsg("Error al sincronizar "+ e.getMessage());
        }
    }
    private void obtenerDatosAmigosServidor(){
        try{
            databaseReference = FirebaseDatabase.getInstance().getReference("notas");
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(tarea->{
                if(!tarea.isSuccessful()) return;
                miToken = tarea.getResult();
                if( miToken!=null && miToken.length()>1 ){
                    databaseReference.orderByChild("token").equalTo(miToken).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try{
                                if( snapshot.getChildrenCount()<=0 ){
                                    mostrarMsg("Agrega una nota para ver el chat");
                                    paramatros.putString("accion", "nuevo");
                                    abrirActividad(paramatros);
                                }
                            }catch (Exception e){
                                mostrarMsg("Error al buscar nuestro registro: "+ e.getMessage());
                                paramatros.putString("accion", "nuevo");
                                abrirActividad(paramatros);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        datosJSON = new JSONArray();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            amigos amigo = dataSnapshot.getValue(amigos.class);
                            jsonObject = new JSONObject();
                            jsonObject.put("idNota", amigo.getIdNota());
                            jsonObject.put("nombre", amigo.getNombre());
                            jsonObject.put("titulo", amigo.getTitulo());
                            jsonObject.put("emocion", amigo.getEmocion());
                            jsonObject.put("contenido", amigo.getContenido());
                            jsonObject.put("urlCompletaFoto", amigo.getUrlFotoAmigo());
                            jsonObject.put("urlFotoAmigoFirestore", amigo.getUrlFotoAmigoFirestore());
                            jsonObject.put("to", amigo.getToken());
                            jsonObject.put("from", miToken);
                            datosJSON.put(jsonObject);
                        }
                        mostrarDatosAmigos();
                    }catch (Exception e){
                        mostrarMsg("Error al obtener los datos: "+ e.getMessage());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            mostrarMsg("Error al obtener datos notas del server: "+ e.getMessage());
        }
    }
    private void mostrarDatosAmigos(){
        try{
            if( datosJSON.length()>0 ){
                alAmigos.clear();
                alAmigosCopy.clear();

                JSONObject misDatosJSONObject;
                for (int i=0; i<datosJSON.length(); i++){
                    misDatosJSONObject = datosJSON.getJSONObject(i);
                    datosAmigos = new amigos(
                            misDatosJSONObject.getString("idNota"),
                            misDatosJSONObject.getString("nombre"),
                            misDatosJSONObject.getString("titulo"),
                            misDatosJSONObject.getString("emocion"),
                            misDatosJSONObject.getString("contenido"),
                            misDatosJSONObject.getString("urlCompletaFoto"),
                            misDatosJSONObject.getString("urlFotoAmigoFirestore"),
                            misDatosJSONObject.getString("to")
                    );
                    alAmigos.add(datosAmigos);
                }
                alAmigosCopy.addAll(alAmigos);

                adaptadorimagenes adImagenes = new adaptadorimagenes(getApplicationContext(), alAmigos);
                lts.setAdapter(adImagenes);

                registerForContextMenu(lts);
            }else{
                mostrarMsg("No hay datos que mostrar");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar datos: "+ e.getMessage());
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            menu.setHeaderTitle("Que deseas hacer con " + datosJSON.getJSONObject(posicion).getJSONObject("value").getString("nombre"));
        }catch (Exception e){
            mostrarMsg("Error al mostrar el menu: "+ e.getMessage());
        }
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try{
            switch (item.getItemId()){
                case R.id.mnxAgregar:
                    paramatros.putString("accion", "nuevo");
                    abrirActividad(paramatros);
                    break;
                case R.id.mnxModificar:
                    paramatros.putString("accion", "modificar");
                    paramatros.putString("amigos", datosJSON.getJSONObject(posicion).toString());
                    abrirActividad(paramatros);
                    break;
                case R.id.mnxEliminar:
                    eliminarAmigo();
                    break;
            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error al seleccionar el item: "+ e.getMessage());
            return super.onContextItemSelected(item);
        }
    }
    private void eliminarAmigo(){
        try {
            AlertDialog.Builder confirmar = new AlertDialog.Builder(lista_amigos.this);
            confirmar.setTitle("Esta seguro de eliminar la nota: ");
            confirmar.setMessage(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("nombre"));
            confirmar.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        String respuesta = db.administrar_amigos("eliminar", new String[]{"", "", datosJSON.getJSONObject(posicion).getJSONObject("value").getString("idNota")});
                        if (respuesta.equals("ok")) {
                            mostrarMsg("Amigo eliminado con exito");
                            obtenerAmigos();
                        } else {
                            mostrarMsg("Error al eliminar el amigo: " + respuesta);
                        }
                    }catch (Exception e){
                        mostrarMsg("Error al eliminar datos: "+ e.getMessage());
                    }
                }
            });
            confirmar.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            confirmar.create().show();
        }catch (Exception e){
            mostrarMsg("Error al eliminar: "+ e.getMessage());
        }
    }
    private void buscarAmigos(){
        TextView tempVal = findViewById(R.id.txtBuscarAmigos);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    alAmigos.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if( valor.length()<=0 ){
                        alAmigos.addAll(alAmigosCopy);
                    }else{
                        for (amigos amigo : alAmigosCopy){
                            String nombre = amigo.getNombre();
                            String titulo = amigo.getTitulo();
                            String emocion = amigo.getEmocion();
                            if( nombre.trim().toLowerCase().contains(valor) ||
                                    titulo.trim().toLowerCase().contains(valor) ||
                                    emocion.trim().contains(valor)){
                                alAmigos.add(amigo);
                            }
                        }
                        adaptadorimagenes adImagenes=new adaptadorimagenes(getApplicationContext(), alAmigos);
                        lts.setAdapter(adImagenes);
                    }
                }catch (Exception e){
                    mostrarMsg("Error al buscar: "+ e.getMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void abrirActividad(Bundle parametros){
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        abrirActividad.putExtras(parametros);
        startActivity(abrirActividad);
    }
    private void obtenerAmigos(){ //offline
        try{
            cAmigos = db.obtener_amigos();
            if( cAmigos.moveToFirst() ){
                datosJSON = new JSONArray();
                do{
                    jsonObject = new JSONObject();
                    JSONObject jsonObjectValue = new JSONObject();

                    jsonObject.put("_id", cAmigos.getString(0));
                    jsonObject.put("_rev", cAmigos.getString(1));
                    jsonObject.put("idNota", cAmigos.getString(2));
                    jsonObject.put("nombre", cAmigos.getString(3));
                    jsonObject.put("titulo", cAmigos.getString(4));
                    jsonObject.put("emocion", cAmigos.getString(5));
                    jsonObject.put("contenido", cAmigos.getString(6));
                    jsonObject.put("urlCompletaFoto", cAmigos.getString(7));
                    jsonObjectValue.put("value", jsonObject);

                    datosJSON.put(jsonObjectValue);
                }while (cAmigos.moveToNext());
                mostrarDatosAmigos();
            }else {
                paramatros.putString("accion", "nuevo");
                abrirActividad(paramatros);
                mostrarMsg("No hay Datos de amigos.");
            }
        }catch (Exception e){
            mostrarMsg("Error al obtener los amigos : "+ e.getMessage());
        }
    }
    private void mostrarMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}