package com.android.victor.congresoapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;


import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.victor.congresoapp.Login;
import com.android.victor.congresoapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;

public class MapsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Declaracion de variables del Mapa...
    ArrayList<OverlayItem> puntos = new ArrayList<>();
    private MapView myOpenMapView;
    private MapController myMapController;
    private GeoPoint posicionActual;
    //Mensaje desde firebase
    private TextView msgMapa;

    //-----------------------------
    //Referencia a Base de DatosFirebase.....
    //-----------------------------
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mensaje1 = ref.child("msgMapa");

    /*Login*/
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //Si hay permisos de escritura..
        if (tengoPermisoEscritura()) {
            cargarMapas();
        }

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
        msgMapa = (TextView)findViewById(R.id.msgMapa);

        //Login
        mAuth = FirebaseAuth.getInstance();
        //boton de salida
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null) {
                    startActivity(new Intent(MapsActivity.this, Login.class));
                }
            }
        };
    }

    //Declaracionn del codigo para el Mapa en el layout
    private void cargarMapas() {
        //Declaracion de la ubicacion a mostrar....
        GeoPoint madrid = new GeoPoint(19.950547, -96.843977);
        GeoPoint franche = new GeoPoint(19.930170, -96.848762);
        //Casteo de las variables respecto al mapa
        myOpenMapView = (MapView) findViewById(R.id.openmapview);
        myOpenMapView.setBuiltInZoomControls(true);
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setCenter(madrid);
        myMapController.setZoom(16);
        myOpenMapView.setMultiTouchControls(true);

        ///////////////////////////////////
        //Centrar en la posición actual
        final MyLocationNewOverlay myLocationoverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), myOpenMapView);
        myOpenMapView.getOverlays().add(myLocationoverlay); //No añadir si no quieres una marca
        myLocationoverlay.enableMyLocation();
        myLocationoverlay.runOnFirstFix(new Runnable() {
            public void run() {
                myMapController.animateTo(myLocationoverlay.getMyLocation());
            }
        });

        /////////////////////////////////////////
        // Añadir un punto en el mapa
        puntos.add(new OverlayItem("Instituto Tecnologico Superior de Misantla", "Misantla Veracruz", madrid));
        refrescaPuntos();
        puntos.add(new OverlayItem("Salon De Eventos Franche, El Zotuco","Misantla Veracruz",franche));
        refrescaPuntos();

        /////////////////////////////////////////
        // Detectar cambios de ubicación mediante un listener (OSMUpdateLocation)
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        OSMUpdateLocation detectaPosicion = new OSMUpdateLocation(this);
        if (tengoPermisoUbicacion()) {
            Location ultimaPosicionConocida = null;
            for (String provider : locationManager.getProviders(true)) {
                if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    ultimaPosicionConocida = locationManager.getLastKnownLocation(provider);
                if (ultimaPosicionConocida != null) {
                    actualizaPosicionActual(ultimaPosicionConocida);
                }
                //Pedir nuevas ubicaciones
                locationManager.requestLocationUpdates(provider, 0, 0, detectaPosicion);
                break;
            }
        } else {
            // No tengo permiso de ubicación
        }
    }

    //Dclaracion de codigo para soliciitar el permiso de utilizar las ubicaciones para el mapa....
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setClass(this, this.getClass());
            startActivity(intent);
            finish();
        } else {
            // El usuario no ha dado permiso
        }
    }
    //Si el ussuario a dado permisos de escritura y el SDK es mayor 23 entra aqui/.....
    public boolean tengoPermisoEscritura() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }
    //Declaracion de los permisos de la ubicacion para ser implementado....
    public boolean tengoPermisoUbicacion() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return false;
            }
        } else {
            return true;
        }
    }

    public void actualizaPosicionActual(Location location) {
        posicionActual = new GeoPoint(location.getLatitude(), location.getLongitude());
        myMapController.setCenter(posicionActual);
        if (puntos.size() > 1)
            puntos.remove(1);
        OverlayItem marcador = new OverlayItem("Estás aquí", "Posicion actual", posicionActual);
        marcador.setMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.center, null));
        puntos.add(marcador);
        refrescaPuntos();
    }

    private void refrescaPuntos() {
        myOpenMapView.getOverlays().clear();
        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> tap = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemLongPress(int arg0, OverlayItem arg1) {
                return false;
            }
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }
        };
        ItemizedOverlayWithFocus<OverlayItem> capa = new ItemizedOverlayWithFocus<>(this, puntos, tap);
        capa.setFocusItemsOnTap(true);
        myOpenMapView.getOverlays().add(capa);
    }

    //-------------------------------------------
    //Bloque de codigo correspondiente a drawer layout
    //-------------------------------------------
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
            Intent searchIntent = new Intent(MapsActivity.this, Expositores.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if(id == R.id.programa){
            Intent searchIntent = new Intent(MapsActivity.this, Programa.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.eventos){
            Intent searcIntent = new Intent(MapsActivity.this,Eventos.class);
            startActivity(searcIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.talleres){
            Intent searcIntent = new Intent(MapsActivity.this,Talleres.class);
            startActivity(searcIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.costos){
            Intent searcIntent = new Intent(MapsActivity.this,Costos.class);
            startActivity(searcIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.ubicacion){
            Intent searcIntent = new Intent(MapsActivity.this,MapsActivity.class);
            startActivity(searcIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id== R.id.salirc){
            mAuth.signOut();
        }



        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //-----------------------------
    //Referencia a firebase.....
    //-----------------------------

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
                msgMapa.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
