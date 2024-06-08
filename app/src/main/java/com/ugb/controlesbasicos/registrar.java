package com.ugb.controlesbasicos;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registrar extends AppCompatActivity {

    EditText userregis, usercontra;
    Button btn_registra;
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
        setContentView(R.layout.activity_registrar);
        mAuth = FirebaseAuth.getInstance();
        userregis = findViewById(R.id.txtReUsuario);
        usercontra = findViewById(R.id.txtReContrasena);

        btn_registra = findViewById(R.id.btnRegistraUsuario);
        btn_registra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email, pasword;
                email = String.valueOf(userregis.getText());
                pasword = String.valueOf(usercontra.getText());
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(registrar.this, "Ingresa el correo",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(pasword)){
                    Toast.makeText(registrar.this, "Ingrea la contraseña",Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, pasword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(registrar.this, "Cuenta Creada",
                                                Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), login.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(registrar.this, "Error al crear cuenta, revise las credenciales o inicie sesiòn",
                                                Toast.LENGTH_SHORT).show();
                                    }


                            }
                        });

            }
        });
    }
}