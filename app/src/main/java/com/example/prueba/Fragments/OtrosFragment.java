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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.prueba.AgregarUsuario;
import com.example.prueba.AgregarUsuarioLocation;
import com.example.prueba.AgregarUsuarioQR;
import com.example.prueba.HomePrincipal;
import com.example.prueba.Login;
import com.example.prueba.MiPerfil;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario3;
import com.example.prueba.R;
import com.example.prueba.SelectorLoginRegistro;
import com.example.prueba.SolicitudAmistad;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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
    private FirebaseDatabase base;
    private DatabaseReference referencia;
    private FirebaseUser user;
    private ImageView foto_perfil;

    public OtrosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_otros, container, false);
        foto_perfil=view.findViewById(R.id.profile_otros);
        agregar_usuario = view.findViewById(R.id.opcion_agregar_usuario);
        agregar_usuario_qr = view.findViewById(R.id.opcion_agregar_por_qr);
        agregar_usuario_location = view.findViewById(R.id.opcion_agregar_por_localizacion);
        solicitud_amistad = view.findViewById(R.id.opcion_solicitudes_amistad);
        cerrar_sesion=view.findViewById(R.id.cerrar_sesion);
        miperfil=view.findViewById(R.id.opcion_mi_perfil);
        mAuth=FirebaseAuth.getInstance();
        base=FirebaseDatabase.getInstance();
        referencia=base.getReference("Usuarios");
        user=mAuth.getCurrentUser();

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

        obtenerUsuario();
        return view;

    }
 public void obtenerUsuario(){
        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot datos:dataSnapshot.getChildren()){
                    Usuario3 usuario=datos.getValue(Usuario3.class);
                    if(user.getUid().equals(usuario.getId())){
                        if(!usuario.getUrl_imagen().equals("default")){
                            Glide.with(OtrosFragment.this).load(usuario.getUrl_imagen()).into(foto_perfil);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
 }

}