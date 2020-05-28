package com.example.prueba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.usage.NetworkStatsManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prueba.Adaptadores.UserAdapter;
import com.example.prueba.Adaptadores.UserAdapterBusqueda;
import com.example.prueba.Adaptadores.UserAdapterBusquedaLocation;
import com.example.prueba.Objetos.Usuario;
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

    private ArrayList<Usuario> usuarioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario_location);



        usuarioList = new ArrayList<>();
        tituloActivity = findViewById(R.id.titulo_activity);

        tituloActivity.setText("Agregar usuario por localización");

        for (int i = 0 ; i<20 ; i++){
            usuarioList.add(new Usuario("","Usuario "+i , "","",""));
        }
        recyclerView=findViewById(R.id.recyclerview_agregar_usuarios_por_localizacion);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        userAdapterBusquedaLocation = new UserAdapterBusquedaLocation( usuarioList,AgregarUsuarioLocation.this);

        recyclerView.setAdapter(userAdapterBusquedaLocation);




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
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                String progresssToString = String.valueOf(progress);
                int value = Integer.parseInt(progresssToString);
                if(value==0){
                    recyclerView.setVisibility(View.INVISIBLE);
                    info_location.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    info_location.setVisibility(View.INVISIBLE);
                }



            }
        });

    }
}
