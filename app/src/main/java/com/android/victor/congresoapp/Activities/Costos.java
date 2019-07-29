package com.android.victor.congresoapp.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.victor.congresoapp.Login;
import com.android.victor.congresoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;

import org.w3c.dom.Text;

public class Costos extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //declaracion var
    private TextView primerPrecio, segundoPrecio, primerExterno, segundoExterno;

    //-----------------------------
    //Referencia a Base de DatosFirebase.....
    //-----------------------------
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mensaje1 = ref.child("precio1ITSM");
    DatabaseReference mensaje2 = ref.child("precio2ITSM");
    DatabaseReference mensaje3 = ref.child("precio1Foraneo");
    DatabaseReference mensaje4 = ref.child("precio2Foraneo");

    /*Login*/
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costos);

        //Declaracion de la orientacion de la pantalla
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Bloque de codigo referente  al DrawerLayout, para cada una de las ventanas..
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toogle);
        toogle.syncState();
        //Casteo de Var. Drawer Layout..
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //-----------------------------
        //Casteo.....
        //-----------------------------
        primerPrecio = (TextView)findViewById(R.id.primerPrecio);
        segundoPrecio = (TextView)findViewById(R.id.segundoPrecio);
        primerExterno = (TextView)findViewById(R.id.primerExterno);
        segundoExterno = (TextView)findViewById(R.id.segundoExterno);

        //Login
        mAuth = FirebaseAuth.getInstance();
        //boton de salida
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null) {
                    startActivity(new Intent(Costos.this, Login.class));
                }
            }
        };


    }
    //Bloqueo Navegacion
    @Override
    public void onBackPressed(){
    }

    //....
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    //Declaracion de la accion del menu lateral, para cada seccion..
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.expositores){
            Intent searchIntent = new Intent(Costos.this, Expositores.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if(id == R.id.programa){
            Intent searchIntent = new Intent(Costos.this, Programa.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.eventos){
            Intent searcIntent = new Intent(Costos.this,Eventos.class);
            startActivity(searcIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.talleres){
            Intent searcIntent = new Intent(Costos.this,Talleres.class);
            startActivity(searcIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.costos){
            Intent searcIntent = new Intent(Costos.this,Costos.class);
            startActivity(searcIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.ubicacion){
            Intent searcIntent = new Intent(Costos.this,MapsActivity.class);
            startActivity(searcIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id== R.id.salirc){
            mAuth.signOut();
        }



        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
    Bloque de codigo para referir a cada nodo de la BD
     */
    @Override
    protected void onStart() {
        super.onStart();

        //Autenticacion--------
        mAuth.addAuthStateListener(mAuthListner);
        //----------------------

        //Bloque de codigo para hacer refrencia al el texto a Firebase....
        mensaje1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                primerPrecio.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mensaje2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                segundoPrecio.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mensaje3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                primerExterno.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mensaje4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                segundoExterno.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }




}
