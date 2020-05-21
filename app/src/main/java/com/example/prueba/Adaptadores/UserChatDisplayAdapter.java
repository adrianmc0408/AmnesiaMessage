package com.example.prueba.Adaptadores;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.ChatRoom;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
//implements Filterable
public class UserChatDisplayAdapter extends RecyclerView.Adapter<UserChatDisplayAdapter.ViewHolder>  {

    private Context mContext;
    private ArrayList<Usuario> listaUsuarios;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{

        void OnItemClick(int position);
    }


    public void setOnClickListener(OnItemClickListener listener){
        mListener=listener;
    }


    public UserChatDisplayAdapter(Context context, ArrayList<Usuario> usuarios) {
        this.mContext = context;
        this.listaUsuarios = usuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_chat_display, parent, false);
        ViewHolder viewHolder = new ViewHolder(view,mListener);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Usuario user = listaUsuarios.get(position);
        holder.username.setText(user.getNombre_usuario());
        holder.message_counter.setText("9");
        holder.last_message.setText("Lorem ipsum dolor sit amet, con..");
        holder.last_message_time.setText("9:45");

        holder.profile_image.setImageResource(R.mipmap.ic_launcher);




    }
    //Implementar mas tarde (busqueda en recyclers)

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }
/*
    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        //Run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<Usuario> filteredList = new ArrayList<>();
            if(charSequence.toString().isEmpty()){
                filteredList.addAll(listaUsuarios);
            } else{
                for( Usuario user : listaUsuarios){
                    if (user.getNombre_usuario().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(user);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;


            return filterResults;
        }
        //Run on a ui thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        listaUsuarios.clear();
        listaUsuarios.addAll((Collection<? extends Usuario>) filterResults.values);
        notifyDataSetChanged();
        }
          };*/
    public static class ViewHolder extends RecyclerView.ViewHolder{
        Context context;
        TextView username;
        TextView last_message;
        TextView last_message_time;
        TextView message_counter;

        ImageView profile_image;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            context = itemView.getContext();
            username = itemView.findViewById(R.id.chat_display_username);
            profile_image = itemView.findViewById(R.id.chat_display_profile_image);
            last_message = itemView.findViewById(R.id.chat_display_last_message);
            last_message_time = itemView.findViewById(R.id.chat_display_last_hour_message);
            message_counter = itemView.findViewById(R.id.chat_display_counter);


            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        //El adapter es el que sabe la posición absoluta dentro de la vista,
                        int position=getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            //Fijamos un listener para cada elemento de la lista
                            //Vamos al MainActivity...
                            listener.OnItemClick(position);
                        }
                    }
                }
            });

        }

    }


}
