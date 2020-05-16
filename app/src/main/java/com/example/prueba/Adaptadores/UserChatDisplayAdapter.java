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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.Objetos.Usuario;
import com.example.prueba.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserChatDisplayAdapter extends RecyclerView.Adapter<UserChatDisplayAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Usuario> listaUsuarios;


    public UserChatDisplayAdapter(Context context, ArrayList<Usuario> usuarios) {
        this.mContext = context;
        this.listaUsuarios = usuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_chat_display, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Usuario user = listaUsuarios.get(position);
        holder.username.setText(user.getNombre_usuario());
        holder.message_counter.setText("9");
        holder.last_message.setText("Lorem ipsum dolor sit amet, con..");
        holder.last_message_time.setText("9:45");

        holder.profile_image.setImageResource(R.mipmap.ic_launcher);



    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        Context context;
        TextView username;
        TextView last_message;
        TextView last_message_time;
        TextView message_counter;
        CardView chat;
        ImageView profile_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            username = itemView.findViewById(R.id.chat_display_username);
            profile_image = itemView.findViewById(R.id.chat_display_profile_image);
            last_message = itemView.findViewById(R.id.chat_display_last_message);
            last_message_time = itemView.findViewById(R.id.chat_display_last_hour_message);
            message_counter = itemView.findViewById(R.id.chat_display_counter);
            chat = itemView.findViewById(R.id.cardview_chat_display);
        }

    }


}
