package com.example.prueba.Adaptadores;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.Objetos.Amistad;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario2;
import com.example.prueba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SolicitudAmistadAdapter extends RecyclerView.Adapter<SolicitudAmistadAdapter.ViewHolder> {

    private ArrayList<Usuario2>listaPeticiones;
    private FirebaseDatabase base;
    private FirebaseAuth mAuth;
    private FirebaseUser usuario_app;
    private DatabaseReference referencia;
    private DatabaseReference referencia2;



    public SolicitudAmistadAdapter(ArrayList<Usuario2> usuarios ) {

        this.listaPeticiones = usuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_peticion_amistad, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        base=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        usuario_app=mAuth.getCurrentUser();
        referencia=base.getReference("Amigos");
        referencia2=base.getReference("Solicitudes");

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Usuario2 user = listaPeticiones.get(position);
        holder.username.setText(user.getNombre_usuario());
        //Aqui no se que coño poner asique pongo cualquier imagen
        holder.profile_image.setImageResource(R.drawable.profile);

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referencia2.child(listaPeticiones.get(position).getReferencia()).removeValue();
                removeAt(position);
            }
        });

        holder.aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amistad amistad =new Amistad(usuario_app.getUid(),listaPeticiones.get(position).getId());
                referencia.push().setValue(amistad);
                referencia2.child(listaPeticiones.get(position).getReferencia()).removeValue();
                removeAt(position);
            }
        });



    }



    @Override
    public int getItemCount() {
        return listaPeticiones.size();
    }

    public void removeAt(int position) {
        listaPeticiones.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listaPeticiones.size());
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView username;
        ImageView profile_image;
        Button aceptar;
        Button eliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            username = itemView.findViewById(R.id.username_peticion_amistad);
            profile_image = itemView.findViewById(R.id.peticion_amistad_image_profile);
            aceptar = itemView.findViewById(R.id.peticion_amistad_btn_aceptarSolicitud);
            eliminar = itemView.findViewById(R.id.peticion_amistad_btn_eliminar);


        }
    }

}


