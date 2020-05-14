package com.example.prueba.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.Objetos.Usuario;
import com.example.prueba.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Usuario> listaUsuarios;

    public UserAdapter(Context context, ArrayList<Usuario> usuarios) {
        this.context = context;
        this.listaUsuarios = usuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_user_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Usuario user = listaUsuarios.get(position);
        holder.username.setText(user.getNombre_usuario());
        //Aqui no se que coño poner asique pongo cualquier imagen
        holder.profile_image.setImageResource(R.mipmap.ic_launcher);

        holder.setOnClickListeners();

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Context context;
         TextView username;
         ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            username = itemView.findViewById(R.id.username_friends);
            profile_image = itemView.findViewById(R.id.image_profile);
        }
        void setOnClickListeners(){ profile_image.setOnClickListener(this);}

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "FUNCIONA", Toast.LENGTH_SHORT).show();

        }
    }


}
