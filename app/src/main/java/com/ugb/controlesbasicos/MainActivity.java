package com.ugb.controlesbasicos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView tempVal;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    Button btn;
    FloatingActionButton fab;
    String accion = "nuevo";
    String id="", rev="", idNota="";
    String urlCompletaFoto;
    String getUrlCompletaFotoFirestore;
    Intent tomarFotoIntent;
    ImageView img;
    utilidades utls;
    detectarInternet di;
    String miToken = "";
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempVal = findViewById(R.id.lblSensorLuz);
        activarSensorLuz();
        di = new detectarInternet(getApplicationContext());
        utls = new utilidades();
        fab = findViewById(R.id.fabListarAmigos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividad();
            }
        });
        btn = findViewById(R.id.btnGuardarAgendaAmigos);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirFotoFirestore();
            }
        });
        img = findViewById(R.id.btnImgAmigo);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFotoAmigo();
            }
        });
        obtenerToken();
        mostrarDatosAmigos();
    }
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
    //inicio del sensor
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
    //fin del sensor
    private void subirFotoFirestore(){
        mostrarMsg("Subiendo Foto...");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        Uri file = Uri.fromFile(new File(urlCompletaFoto));
        final StorageReference reference = storageReference.child("foto/"+file.getLastPathSegment());

        final UploadTask tareaSubir = reference.putFile(file);
        tareaSubir.addOnFailureListener(e->{
            mostrarMsg("Error al subir la foto: "+ e.getMessage());
        });
        tareaSubir.addOnSuccessListener(tareaInstantanea->{
            mostrarMsg("Foto subida con exito.");
            Task<Uri> descargarUri = tareaSubir.continueWithTask(tarea->reference.getDownloadUrl()).addOnCompleteListener(tarea->{
                if( tarea.isSuccessful() ){
                    getUrlCompletaFotoFirestore = tarea.getResult().toString();
                    guardarAmigo();
                }else{
                    mostrarMsg("Error al descargar la ruta de la imagen");
                }
            });
        });
    }
    private void obtenerToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if( !task.isSuccessful() ){
                return;
            }
            miToken = task.getResult();
        });
    }
    private void guardarAmigo(){
        try {
            tempVal = findViewById(R.id.txtnombre);
            String nombre = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtTitulo);
            String titulo = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtEmocion);
            String emocion = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtContenido);
            String contenido = tempVal.getText().toString();

            databaseReference = FirebaseDatabase.getInstance().getReference("notas");
            String key = databaseReference.push().getKey();

            if(miToken.equals("") || miToken==null){
                obtenerToken();
            }
            if( miToken!=null && miToken!="" ){
                amigos amigo = new amigos(idNota,nombre,titulo,emocion,contenido,urlCompletaFoto,getUrlCompletaFotoFirestore,miToken);
                if(key!=null){
                    databaseReference.child(key).setValue(amigo).addOnSuccessListener(aVoid->{
                        mostrarMsg("Nota registrada con exito.");
                        abrirActividad();
                    });
                }else{
                    mostrarMsg("Error nose pudo guardar en la base de datos");
                }
            }else {
                mostrarMsg("Tu dispositivo no soporta la aplicacion");
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void tomarFotoAmigo(){
        tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fotoAmigo = null;
        try{
            fotoAmigo = crearImagenAmigo();
            if( fotoAmigo!=null ){
                Uri uriFotoamigo = FileProvider.getUriForFile(MainActivity.this,
                        "com.ugb.controlesbasicos.fileprovider", fotoAmigo);
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoamigo);
                startActivityForResult(tomarFotoIntent, 1);
            }else{
                mostrarMsg("No se pudo crear la foto");
            }
        }catch (Exception e){
            mostrarMsg("Error al abrir la camara: "+ e.getMessage());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode==1 && resultCode==RESULT_OK){
                Bitmap imageBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageBitmap(imageBitmap);
            }else{
                mostrarMsg("El usuario cancelo la toma de la foto");
            }
        }catch (Exception e){
            mostrarMsg("Error al obtener la foto de la camara");
        }
    }
    private File crearImagenAmigo() throws Exception{
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileName = "imagen_"+ fechaHoraMs +"_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if( dirAlmacenamiento.exists()==false ){
            dirAlmacenamiento.mkdirs();
        }
        File imagen = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaFoto = imagen.getAbsolutePath();
        return imagen;
    }
    private void mostrarDatosAmigos(){
        try{
            Bundle parametros = getIntent().getExtras();//Recibir los parametros...
            accion = parametros.getString("accion");

            if(accion.equals("modificar")){
                JSONObject jsonObject = new JSONObject(parametros.getString("notas")).getJSONObject("value");
                id = jsonObject.getString("_id");
                rev = jsonObject.getString("_rev");
                idNota = jsonObject.getString("idNota");

                tempVal = findViewById(R.id.txtnombre);
                tempVal.setText(jsonObject.getString("nombre"));

                tempVal = findViewById(R.id.txtTitulo);
                tempVal.setText(jsonObject.getString("titulo"));

                tempVal = findViewById(R.id.txtEmocion);
                tempVal.setText(jsonObject.getString("emocion"));

                tempVal = findViewById(R.id.txtContenido);
                tempVal.setText(jsonObject.getString("contenido"));

                urlCompletaFoto = jsonObject.getString("urlCompletaFoto");
                Bitmap imageBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageBitmap(imageBitmap);
            }else{//nuevo registro
                idNota = utls.generarIdUnico();
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar datos: "+ e.getMessage());
        }
    }
    private void mostrarMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    private void abrirActividad(){
        Intent abrirActividad = new Intent(getApplicationContext(), lista_amigos.class);
        startActivity(abrirActividad);
    }

}