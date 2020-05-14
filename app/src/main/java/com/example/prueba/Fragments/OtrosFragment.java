package com.example.prueba.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.prueba.AgregarUsuario;
import com.example.prueba.R;
import com.example.prueba.SolicitudAmistad;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtrosFragment extends Fragment {


    private RelativeLayout agregar_usuario;
    private RelativeLayout solicitud_amistad;

    public OtrosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_otros, container, false);

        agregar_usuario = view.findViewById(R.id.opcion_agregar_usuario);
        solicitud_amistad = view.findViewById(R.id.opcion_solicitudes_amistad);

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

        return view;

    }

}
