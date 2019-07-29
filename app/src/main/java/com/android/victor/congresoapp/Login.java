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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.victor.congresoapp.Activities.Expositores;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private static final String TAG = "";
    private EditText inputEmail, inputPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Button btnExit;

    FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        //comprobar el usuario actual
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, Expositores.class));
            finish();
        }

        //Declaracion de la orientacion de la pantalla
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Casteo de variables con xml
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        Button ahlogin = (Button) findViewById(R.id.ah_login);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView btnSignIn = (TextView) findViewById(R.id.sign_in_button);
        btnExit = (Button) findViewById(R.id.btnExit);


        //evento pasar a registro de un nuevo ususario
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registro.class));
            }
        });


        mAuth = FirebaseAuth.getInstance();

        // Checking si email y password estan ingresados en Editext
        ahlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                //authenticate user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    //Cuando la autenticacion es correcta
                                    Log.d(TAG, "signInWithEmail:success");
                                    Intent intent = new Intent(Login.this, Expositores.class);
                                    startActivity(intent);
                                    finish();
                                    //cuando la authenticacion es fallida
                                } else {
                                    Log.d(TAG, "singInWithEmail:Fail");
                                    Toast.makeText(Login.this, getString(R.string.failed), Toast.LENGTH_LONG).show();
                                }
                            }

                        });
            }

        });


        //evento para accion de inicio de sesion permiendo asi ingresar a home
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(Login.this, Expositores.class));
                }

            }
        };

        //evento de Btn de salida de la App..
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.exit(0);
                finish();
                inputEmail.setText("");
                inputPassword.setText("");
            }
        });

    }
    @Override
    public void onBackPressed(){
    }
}
