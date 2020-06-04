package com.example.prueba.Adaptadores;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.example.prueba.MiPerfil;
import com.example.prueba.Objetos.Amistad;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario2;
import com.example.prueba.R;
import com.example.prueba.SolicitudAmistad;
import com.example.prueba.VisualizadorFotos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SolicitudAmistadAdapter extends RecyclerView.Adapter<SolicitudAmistadAdapter.ViewHolder> {
    //Declaramos e inicializamos las variables
    private Context mContext;
    private ArrayList<Usuario2>listaPeticiones;
    private FirebaseDatabase base;
    private FirebaseAuth mAuth;
    private FirebaseUser usuario_app;
    private DatabaseReference referencia;
    private DatabaseReference referencia2;


    //Constructor del adaptador de solcitudes de amistad
    public SolicitudAmistadAdapter(ArrayList<Usuario2> usuarios,Context mContext ) {
        this.listaPeticiones = usuarios;
        this.mContext = mContext;
    }

    @NonNull
    @Override
     /*
        Creamos un ViewHolder, el RecyclerView lo inflará con las filas visibles
        -Obtenemos las referencias necesarias
     */
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
      /*
    El RecyclerView llama a este método para reutilizar los ViewHolder creados,
    , es decir los infla con las nuevas filas visibles según el usuario desliza la lista.
    (Se establece una foto de perfil predeterminada en caso de que no tenga una el usuario)
     */
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Cogemos el usuario actual
        final Usuario2 user = listaPeticiones.get(position);
        //Rellenamos los campos de la vista
        holder.username.setText(user.getNombre_usuario());
        if(user.getUrl_imagen().equals("default")){
            holder.profile_image.setImageResource(R.drawable.profile);
        }
        else{
            Glide.with(mContext).load(user.getUrl_imagen()).into(holder.profile_image);
        }


        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VisualizadorFotos.class);
                intent.putExtra("url_image",user.getUrl_imagen());
                intent.putExtra("username",user.getNombre_usuario());
                mContext.startActivity(intent);
            }
        });


        //En caso de que denegemos la solicitud de amistad la borraremos de Firebase y del recyclerview
        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referencia2.child(listaPeticiones.get(position).getReferencia()).removeValue();
                removeAt(position);
            }
        });
        /*En el caso de que aceptemos la peticion de amistad se creara un objeto amistad que contendra el
        id de ambos amigos y se almacenara dicha amistad en la base de datos y eliminaremos dicha solicitud de amistad
        del recycler*/
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
    //Obtenemos el numero de elementos de nuestro Arraylist listaPeticiones

    public int getItemCount() {
        return listaPeticiones.size();
    }
    /*Metodo que utilizamos para eliminar una solicitud de amistad del recyclerview
    (pasandole por parametro la posicion dentro del arraylist que queremos eliminar)
     */
    public void removeAt(int position) {
        listaPeticiones.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listaPeticiones.size());
    }
    //Clase estática por recomendación del API
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


