package com.example.prueba.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.prueba.AgregarUsuario;
import com.example.prueba.AgregarUsuarioLocation;
import com.example.prueba.AgregarUsuarioQR;
import com.example.prueba.HomePrincipal;
import com.example.prueba.Login;
import com.example.prueba.MiPerfil;
import com.example.prueba.R;
import com.example.prueba.SelectorLoginRegistro;
import com.example.prueba.SolicitudAmistad;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtrosFragment extends Fragment {


    private RelativeLayout agregar_usuario;
    private RelativeLayout agregar_usuario_qr;
    private RelativeLayout agregar_usuario_location;
    private RelativeLayout solicitud_amistad;
    private RelativeLayout miperfil;
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
        agregar_usuario_qr = view.findViewById(R.id.opcion_agregar_por_qr);
        agregar_usuario_location = view.findViewById(R.id.opcion_agregar_por_localizacion);
        solicitud_amistad = view.findViewById(R.id.opcion_solicitudes_amistad);
        cerrar_sesion=view.findViewById(R.id.cerrar_sesion);
        miperfil=view.findViewById(R.id.opcion_mi_perfil);
        mAuth=FirebaseAuth.getInstance();

        agregar_usuario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), AgregarUsuario.class));

            }
        });
        agregar_usuario_qr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), AgregarUsuarioQR.class));

            }
        });
        agregar_usuario_location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), AgregarUsuarioLocation.class));

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
               final Dialog dialog_conf = new Dialog(getContext());
                dialog_conf.setContentView(R.layout.popup_confirmacion);
                dialog_conf.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView titulo = (TextView) dialog_conf.findViewById(R.id.dialog_conf_titulo);
                Button btn_si = (Button) dialog_conf.findViewById(R.id.dialog_conf_si);
                Button btn_no =(Button) dialog_conf.findViewById(R.id.dialog_conf_no);

                btn_si.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuth.signOut();
                        startActivity(new Intent(getContext(), SelectorLoginRegistro.class));
                        getActivity().finish();

                    }
                });
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_conf.cancel();
                    }
                });

                dialog_conf.show();




            }
        });

        miperfil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), MiPerfil.class));

            }
        });


        return view;

    }


}
