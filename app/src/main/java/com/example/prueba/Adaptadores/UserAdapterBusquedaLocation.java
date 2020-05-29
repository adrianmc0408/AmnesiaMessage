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

import com.example.prueba.Objetos.Solicitud;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario3;
import com.example.prueba.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapterBusquedaLocation extends RecyclerView.Adapter<UserAdapterBusquedaLocation.ViewHolder> {

    private ArrayList<Usuario3> listaUsuarios;
    private Context mContext;



    public UserAdapterBusquedaLocation(ArrayList<Usuario3> usuarios, Context mContext) {


        this.mContext= mContext;
        this.listaUsuarios = usuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_agregar_usuario, parent, false);

        ViewHolder viewHolder1 = new ViewHolder(view);


        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Usuario3 user = listaUsuarios.get(position);


            holder.username.setText(user.getNombre_usuario());
            //Aqui no se que coño poner asique pongo cualquier imagen
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);






    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView username;
        ImageView profile_image;
        RelativeLayout item_contact;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_contact = itemView.findViewById(R.id.searched_user);
            context = itemView.getContext();
            username = itemView.findViewById(R.id.username_busqueda);
            profile_image = itemView.findViewById(R.id.image_profile_busqueda);


        }

    }
}


