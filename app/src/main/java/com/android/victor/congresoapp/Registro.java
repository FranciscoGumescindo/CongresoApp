package com.android.victor.congresoapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.victor.congresoapp.Activities.Expositores;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registro extends AppCompatActivity {

    //declaracion de variables
    private EditText name, email_id, passwordcheck;
    private FirebaseAuth mAuth;
    private static final String TAG = "";
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Declaracion de la orientacion de la pantalla
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        TextView btnSignUp = (TextView) findViewById(R.id.login_page);

        //evento para cuando regrese de. layout registro a login
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro.this, Login.class);
                startActivity(intent);
            }
        });


        mAuth = FirebaseAuth.getInstance();

        //casteo elementos
        email_id = (EditText) findViewById(R.id.input_email);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        passwordcheck = (EditText) findViewById(R.id.input_password);
        Button ahsignup = (Button) findViewById(R.id.btn_signup);


        ahsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_id.getText().toString();
                String password = passwordcheck.getText().toString();
                //Si no hay datos manda este mensaje
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter Eamil Id", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Si no hay datos manda este mensaje
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                //muestra progressa bar antes de iniciar
                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Registro.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    // Iniciar sesión correctamente, actualizar la interfaz de usuario con la información del usuario registrado
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(Registro.this, Expositores.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Si falla el inicio de sesión, muestre un mensaje al usuario.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Registro.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                            }


                        });
            }
        });
    }
    //bloqueo de botones de navegacion
    @Override
    public void onBackPressed(){
    }
}
