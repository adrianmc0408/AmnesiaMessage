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

import com.example.prueba.Objetos.Usuario;
import com.example.prueba.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SolicitudAmistadAdapter extends RecyclerView.Adapter<SolicitudAmistadAdapter.ViewHolder> {

    private ArrayList<Usuario> listaPeticiones;


    public SolicitudAmistadAdapter(ArrayList<Usuario> usuarios ) {

        this.listaPeticiones = usuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_peticion_amistad, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Usuario user = listaPeticiones.get(position);
        holder.username.setText(user.getNombre_usuario());
        //Aqui no se que coño poner asique pongo cualquier imagen
        holder.profile_image.setImageResource(R.drawable.profile);

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(position);
            }
        });

        holder.aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


