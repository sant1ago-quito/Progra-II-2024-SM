package com.ugb.controlesbasicos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    EditText userregis, usercontra;
    Button btn_login, btn_pasa;
    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        //Comprobar si el usuario ha iniciado sesión (no nula) y actualizar la interfaz de un usuario en consecuencia
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(getApplicationContext(), lista_amigos.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        userregis = findViewById(R.id.txtUsuario);
        usercontra = findViewById(R.id.txtContrasena);
        btn_login = findViewById(R.id.btnGuardarUsuario);
        btn_pasa = findViewById(R.id.btnCrearUsuario);
        btn_pasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),registrar.class);
                startActivity(intent);
                finish();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(userregis.getText());
                password = String.valueOf(usercontra.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(login.this, "Ingrea el correo",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(login.this, "Ingrea la contraseña",Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(login.this, "Inicio de Sesión completo",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(login.this, "No se pudo iniciar sesiòn revise las credenciales o crea una una cuenta",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}