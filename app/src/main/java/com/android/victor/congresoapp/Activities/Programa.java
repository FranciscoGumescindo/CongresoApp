package com.android.victor.congresoapp.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.victor.congresoapp.Adapter.ViewHolderPrograma;
import com.android.victor.congresoapp.Login;
import com.android.victor.congresoapp.Model.ModelPrograma;
import com.android.victor.congresoapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.support.v7.app.ActionBarDrawerToggle;




public class Programa extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Declaracion de las variables respecto a la galeria...
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    /*Login*/
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programa);
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


        //Casteo de las variables para el uso de la galeria
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Crreacion de variable y referencia a base de datos en Firebase...
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef= mFirebaseDatabase.getReference("Programa");

        //Login
        mAuth = FirebaseAuth.getInstance();
        //boton de salida
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null) {
                    startActivity(new Intent(Programa.this, Login.class));
                }
            }
        };


    }

    //Bloqueo navegacion
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
            Intent searchIntent = new Intent(Programa.this, Expositores.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if(id == R.id.programa){
            Intent searchIntent = new Intent(Programa.this, Expositores.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.eventos){
            Intent searcIntent = new Intent(Programa.this,Eventos.class);
            startActivity(searcIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.talleres){
            Intent searcIntent = new Intent(Programa.this,Talleres.class);
            startActivity(searcIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.costos){
            Intent searcIntent = new Intent(Programa.this,Costos.class);
            startActivity(searcIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.ubicacion){
            Intent searcIntent = new Intent(Programa.this,MapsActivity.class);
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
    Codigo referente a firebase
     */

    @Override
    protected void onStart(){
        super.onStart();
        //Autenticacion--------
        mAuth.addAuthStateListener(mAuthListner);
        //----------------------
        FirebaseRecyclerAdapter<ModelPrograma, ViewHolderPrograma> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ModelPrograma, ViewHolderPrograma>(
                        ModelPrograma.class,
                        R.layout.rowprograma,
                        ViewHolderPrograma.class,
                        mRef
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderPrograma viewHolder, ModelPrograma model, int position) {

                        viewHolder.setDetails(getApplicationContext(),model.getFecha(),model.getHorario(),model.getDescripcion());


                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
