package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prueba.Adaptadores.UserAdapter;
import com.example.prueba.Adaptadores.UserAdapterBusqueda;
import com.example.prueba.Adaptadores.UserAdapterBusquedaLocation;
import com.example.prueba.Objetos.ServicioNotificaciones;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario3;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.Locale;

public class AgregarUsuarioLocation extends AppCompatActivity {
    //Declaramos los atributos
    private TextView tituloActivity;
    private ImageView closeActivity;
    private BubbleSeekBar seekBarDistancia;
    private TextView info_location;

    private RecyclerView recyclerView;
    private UserAdapterBusquedaLocation userAdapterBusquedaLocation;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Usuario3> usuarioList;
    private LocationManager ubicacion_usuario;
    private Usuario3 usuario;
    private FirebaseDatabase base;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference referencia;
    private Location location;

    ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario_location);

        usuarioList = new ArrayList<>();
        tituloActivity = findViewById(R.id.titulo_activity);

        tituloActivity.setText("Agregar usuario por localización");

        recyclerView = findViewById(R.id.recyclerview_agregar_usuarios_por_localizacion);
        recyclerView.setHasFixedSize(true);

        //Enlazamos las vistas con los atributos
        info_location = findViewById(R.id.info_location);
        closeActivity = findViewById(R.id.close_activity);
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referencia.removeEventListener(listener);
                finish();
            }
        });
        seekBarDistancia = findViewById(R.id.seekbar);
        //Listener que dota de funcionalidad  al objeto BubbleSeekBar , el cual tiene varias posiciones comprendidas
        //entre 10 y 50
        seekBarDistancia.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {


            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
               //Solo funciona si la localización del usuario esta disponible
                if(location!=null){
                String progresssToString = String.valueOf(progress);
                int value = Integer.parseInt(progresssToString);
                if (value == 0) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    info_location.setVisibility(View.VISIBLE);
                    //Según el valor mostraremos unos usuarios u otros para ser agregados.
                } else if (value == 10) {
                    ArrayList<Usuario3> lista = new ArrayList<>();
                    //Comprobamos cuales de todos los usuarios estan a los Km dados mediante el metodo
                    //distanceTo , el cual nos devuelve la distancia entre dos localizaciones en metros
                    for (int i = 0; i < usuarioList.size(); i++) {
                        Location loc = new Location("A");
                        loc.setLatitude(usuarioList.get(i).getLatitud());
                        loc.setLongitude(usuarioList.get(i).getLongitud());

                        if (loc.distanceTo(location) <= 10000) {
                            lista.add(usuarioList.get(i));
                        }
                    }
                    //Creamos un nuevo layout y un nuevo adapter con los usuarios que han pasado el filtro
                    recyclerView.setVisibility(View.VISIBLE);
                    info_location.setVisibility(View.INVISIBLE);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    userAdapterBusquedaLocation = new UserAdapterBusquedaLocation(lista, AgregarUsuarioLocation.this,usuario);
                    recyclerView.setAdapter(userAdapterBusquedaLocation);
                } else if (value == 20) {
                    ArrayList<Usuario3> lista = new ArrayList<>();
                    for (int i = 0; i < usuarioList.size(); i++) {
                        Location loc = new Location("A");
                        loc.setLatitude(usuarioList.get(i).getLatitud());
                        loc.setLongitude(usuarioList.get(i).getLongitud());

                        if (loc.distanceTo(location) <= 20000) {
                            lista.add(usuarioList.get(i));
                        }
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    info_location.setVisibility(View.INVISIBLE);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    userAdapterBusquedaLocation = new UserAdapterBusquedaLocation(lista, AgregarUsuarioLocation.this,usuario);
                    recyclerView.setAdapter(userAdapterBusquedaLocation);
                } else if (value == 30) {
                    ArrayList<Usuario3> lista = new ArrayList<>();
                    for (int i = 0; i < usuarioList.size(); i++) {
                        Location loc = new Location("A");
                        loc.setLatitude(usuarioList.get(i).getLatitud());
                        loc.setLongitude(usuarioList.get(i).getLongitud());

                        if (loc.distanceTo(location) <= 30000) {
                            lista.add(usuarioList.get(i));
                        }
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    info_location.setVisibility(View.INVISIBLE);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    userAdapterBusquedaLocation = new UserAdapterBusquedaLocation(lista, AgregarUsuarioLocation.this,usuario);
                    recyclerView.setAdapter(userAdapterBusquedaLocation);
                } else if (value == 40) {
                    ArrayList<Usuario3> lista = new ArrayList<>();
                    for (int i = 0; i < usuarioList.size(); i++) {
                        Location loc = new Location("A");
                        loc.setLatitude(usuarioList.get(i).getLatitud());
                        loc.setLongitude(usuarioList.get(i).getLongitud());

                        if (loc.distanceTo(location) <= 40000) {
                            lista.add(usuarioList.get(i));
                        }
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    info_location.setVisibility(View.INVISIBLE);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    userAdapterBusquedaLocation = new UserAdapterBusquedaLocation(lista, AgregarUsuarioLocation.this,usuario);
                    recyclerView.setAdapter(userAdapterBusquedaLocation);
                } else if (value == 50) {
                    ArrayList<Usuario3> lista = new ArrayList<>();
                    for (int i = 0; i < usuarioList.size(); i++) {
                        Location loc = new Location("A");
                        loc.setLatitude(usuarioList.get(i).getLatitud());
                        loc.setLongitude(usuarioList.get(i).getLongitud());

                        if (loc.distanceTo(location) <= 50000) {
                            lista.add(usuarioList.get(i));
                        }
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    info_location.setVisibility(View.INVISIBLE);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    userAdapterBusquedaLocation = new UserAdapterBusquedaLocation(lista, AgregarUsuarioLocation.this,usuario);
                    recyclerView.setAdapter(userAdapterBusquedaLocation);
                }


            }
            else{
                Toast.makeText(getApplicationContext(),"Ubicación no disponible",Toast.LENGTH_SHORT).show();
            }
        }
        });
        //Intanciamos los objetos de Firebase necesarios para trabajar
        base = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        referencia = base.getReference("Usuarios");
        user = auth.getCurrentUser();
        //Llamamos al método que dota de funcionalidad de GPS a esta activity
        localizar();

    }
    //Este método comprueba si tenemos permisos de GPS y si los tienes obtiene nuestra ubicación actual
    public void localizar() {
        //Si los permisos están denegados, se los requerimos al usuario
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1000);
        } else {
            //Sino obtenemos la localizacion
            LocationManager  ubicacion_usuario = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = ubicacion_usuario.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //Buscamos todos los usuarios
            buscarUsuarios();
        }

    }
//Método usado para buscar a los usuarios de la BD que se van a mostrar
    public void buscarUsuarios() {
        referencia.addValueEventListener(listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarioList.clear();
                //Recorremos el objeto DataSnapchot obteniendo los nodos hijos y serializandolos a un objeto de
                //tipo usuario
                for (DataSnapshot datos : dataSnapshot.getChildren()) {
                    Usuario3 usuarioo = datos.getValue(Usuario3.class);
                    Log.i("usuario", usuarioo.getId());
                    //Si es nuestro usuario, le asignamos la localización obtenida anteriormente y lo marcamos como
                    //localizable
                    if (usuarioo.getId().equals(user.getUid())) {

                        usuario = usuarioo;
                        if(location!=null) {
                            usuario.setLongitud(location.getLongitude());
                            usuario.setLatitud(location.getLatitude());
                            usuarioo.setLocalizable(true);
                            referencia.child(datos.getKey()).setValue(usuario);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Ubicación no disponible",Toast.LENGTH_SHORT).show();
                        }
                        //Para los demas usuarios solo lo mostraremos si desea ser localizado (es decir si el atributo
                        // localizable es igual a true)
                    } else if (usuarioo.isLocalizable()) {
                        usuarioList.add(usuarioo);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Método que espera a la respuesta de el usuario en referencia a los permisos y actua en consecuencia
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //Codigo 1000 correspondiente a nuestra solicitud de permisos de ubicación
            case 1000: {
                if (grantResults.length > 0) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    } else {
                        //Una vez concedidos los permisos localizamos al usuario y llamamos al método de busqueda
                        LocationManager  ubicacion_usuario = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        location = ubicacion_usuario.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        buscarUsuarios();
                    }
                } else {

                }
            }

        }
    }

    //Método que reconoce cuando pulsamos la tecla atrás, finalizando la misma y eliminando el listener de la BD,
    //ya que no nos interesa que corra en segundo plano ya que lleva a conflicto con otras activities
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        referencia.removeEventListener(listener);
        this.finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(this, ServicioNotificaciones.class));
    }
}


