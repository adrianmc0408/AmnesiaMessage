package com.example.prueba.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.prueba.AgregarUsuario;
import com.example.prueba.Login;
import com.example.prueba.R;
import com.example.prueba.SolicitudAmistad;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtrosFragment extends Fragment {


    private RelativeLayout agregar_usuario;
    private RelativeLayout solicitud_amistad;
    private Button cerrar_sesion;
    private FirebaseAuth mAuth;

    public OtrosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_otros, container, false);

        agregar_usuario = view.findViewById(R.id.opcion_agregar_usuario);
        solicitud_amistad = view.findViewById(R.id.opcion_solicitudes_amistad);
        cerrar_sesion=view.findViewById(R.id.cerrar_sesion);
        mAuth=FirebaseAuth.getInstance();

        agregar_usuario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), AgregarUsuario.class));

            }
        });

        solicitud_amistad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), SolicitudAmistad.class));

            }
        });

        cerrar_sesion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            mAuth.signOut();
            startActivity(new Intent(getContext(), Login.class));


            }
        });


        return view;

    }

}
