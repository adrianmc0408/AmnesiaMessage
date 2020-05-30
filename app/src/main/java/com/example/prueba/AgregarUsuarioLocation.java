package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
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
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario3;
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


        info_location = findViewById(R.id.info_location);
        closeActivity = findViewById(R.id.close_activity);
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        seekBarDistancia = findViewById(R.id.seekbar);
        seekBarDistancia.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {


            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                String progresssToString = String.valueOf(progress);
                int value = Integer.parseInt(progresssToString);
                if (value == 0) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    info_location.setVisibility(View.VISIBLE);
                } else if (value == 10) {
                    ArrayList<Usuario3> lista = new ArrayList<>();
                    for (int i = 0; i < usuarioList.size(); i++) {
                        Location loc = new Location("A");
                        loc.setLatitude(usuarioList.get(i).getLatitud());
                        loc.setLongitude(usuarioList.get(i).getLongitud());

                        if (loc.distanceTo(location) <= 10000) {
                            lista.add(usuarioList.get(i));
                        }
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    info_location.setVisibility(View.INVISIBLE);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    userAdapterBusquedaLocation = new UserAdapterBusquedaLocation(lista, AgregarUsuarioLocation.this);
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
                    userAdapterBusquedaLocation = new UserAdapterBusquedaLocation(lista, AgregarUsuarioLocation.this);
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
                    userAdapterBusquedaLocation = new UserAdapterBusquedaLocation(lista, AgregarUsuarioLocation.this);
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
                    userAdapterBusquedaLocation = new UserAdapterBusquedaLocation(lista, AgregarUsuarioLocation.this);
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
                    userAdapterBusquedaLocation = new UserAdapterBusquedaLocation(lista, AgregarUsuarioLocation.this);
                    recyclerView.setAdapter(userAdapterBusquedaLocation);
                }


            }
        });
        base = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        referencia = base.getReference("Usuarios");
        user = auth.getCurrentUser();
        localizar();

    }

    public void localizar() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1000);
        } else {
            ubicacion_usuario = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = ubicacion_usuario.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            buscarUsuarios();
        }

    }

    public void buscarUsuarios() {
        referencia.addValueEventListener(listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarioList.clear();
                for (DataSnapshot datos : dataSnapshot.getChildren()) {
                    Usuario3 usuarioo = datos.getValue(Usuario3.class);
                    Log.i("usuario", usuarioo.getId());
                    if (usuarioo.getId().equals(user.getUid())) {

                        usuario = usuarioo;
                        usuario.setLongitud(location.getLongitude());
                        usuario.setLatitud(location.getLatitude());
                        usuarioo.setLocalizable(true);
                        referencia.child(datos.getKey()).setValue(usuario);


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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    } else {
                        ubicacion_usuario = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        location = ubicacion_usuario.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        buscarUsuarios();
                    }
                } else {

                }
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        referencia.removeEventListener(listener);
        this.finish();
    }
}


