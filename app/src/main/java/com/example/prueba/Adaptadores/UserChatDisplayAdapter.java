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
import com.example.prueba.Objetos.Chat;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario2;
import com.example.prueba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
//implements Filterable
public class UserChatDisplayAdapter extends RecyclerView.Adapter<UserChatDisplayAdapter.ViewHolder>  {

    private Context mContext;
    private ArrayList<Usuario2> listaUsuarios;
    private OnItemClickListener mListener;
    private FirebaseAuth auth;
    private FirebaseUser firabase_user;
    private FirebaseDatabase base;
    private DatabaseReference reference;
    ArrayList<String> datos;
    ArrayList<Chat> conversacion;


    public interface OnItemClickListener{

        void OnItemClick(int position);
    }


    public void setOnClickListener(OnItemClickListener listener){
        mListener=listener;
    }


    public UserChatDisplayAdapter(Context context, ArrayList<Usuario2> usuarios) {
        datos=new ArrayList<>();
        conversacion=new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        firabase_user=auth.getCurrentUser();
        base=FirebaseDatabase.getInstance();
        reference=base.getReference("Chats");
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

        Usuario2 user = listaUsuarios.get(position);
        ultimoMensaje(user,holder);

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
    public void ultimoMensaje(final Usuario2 usuario,final ViewHolder holder){


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datos.clear();
                conversacion.clear();
                int cont_leidos=0;
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Chat chat=data.getValue(Chat.class);
                    if(((chat.getSender().equals(firabase_user.getUid()))&&(chat.getReceiver().equals(usuario.getId())))||((chat.getSender().equals(usuario.getId()))&&(chat.getReceiver().equals(firabase_user.getUid())))){
                        conversacion.add(chat);
                        if((chat.getReceiver().equals(firabase_user.getUid()))&&(chat.isLeido()==false)){
                            cont_leidos++;
                        }
                    }
                }
                String ultimo_mensaje="";
                if(conversacion.get(conversacion.size()-1).getSender().equals(firabase_user.getUid())){
                    if(conversacion.get(conversacion.size()-1).getMessage().length()<=31) {
                        String mensaje = "Tú: " + conversacion.get(conversacion.size() - 1).getMessage();
                        conversacion.get(conversacion.size() - 1).setMessage(mensaje);
                    }
                    else{
                        String mensaje =conversacion.get(conversacion.size() - 1).getMessage().substring(0,27);
                        mensaje="Tú: "+mensaje+"...";
                        conversacion.get(conversacion.size() - 1).setMessage(mensaje);
                    }
                }
                else
                {
                    if(conversacion.get(conversacion.size()-1).getMessage().length()>=35) {
                        String mensaje =conversacion.get(conversacion.size() - 1).getMessage().substring(0,31);
                        mensaje=mensaje+"...";
                        conversacion.get(conversacion.size() - 1).setMessage(mensaje);
                    }
                }
                datos.add(conversacion.get(conversacion.size()-1).getMessage());
                int hora=conversacion.get(conversacion.size()-1).getFecha().getHours();
                String hora_t;
                if(hora<10){
                   hora_t="0"+hora;
                }
                else{
                   hora_t=hora+"";
                }

                int minuto=conversacion.get(conversacion.size()-1).getFecha().getMinutes();
                String minuto_t;
                if(minuto<10){
                    minuto_t="0"+minuto;
                }
                else{
                    minuto_t=minuto+"";
                }

                String hora_total=hora_t+":"+minuto_t;
                datos.add(hora_total);
                datos.add(cont_leidos+"");
                holder.username.setText(usuario.getNombre_usuario());
                if(cont_leidos!=0) {
                    holder.message_counter.setText(datos.get(2));
                }
                else{
                    holder.message_counter.setVisibility(View.INVISIBLE);
                }
                holder.last_message.setText(datos.get(0));
                holder.last_message_time.setText(datos.get(1));

                holder.profile_image.setImageResource(R.mipmap.ic_launcher);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
