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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.Objetos.Chat;
import com.example.prueba.Objetos.Usuario2;
import com.example.prueba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private ArrayList<Chat> listaChats;
    private String imageurl;
    private String txt_visto;

    private FirebaseUser fuser;
    private FirebaseAuth auth;


    public ChatAdapter(Context context, ArrayList<Chat> chats,String imageurl) {
        auth=FirebaseAuth.getInstance();
        fuser=auth.getCurrentUser();
        this.mContext = context;
        this.listaChats = chats;
        this.imageurl=imageurl;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     if(viewType == MSG_TYPE_RIGHT){
         View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
         return new ChatAdapter.ViewHolder(view);
     }
     else{
         View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
         return new ChatAdapter.ViewHolder(view);
     }






    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        Chat chat = listaChats.get(position);
        holder.show_message.setText(chat.getMessage());

        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);

        }else{
            //Coger imagen
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }

/*
        //Esto es para que solo ponga el visto o el enviado solo en el ultimo mensaje segun he visto
        if(position == listaChats.size()-1){
            if(chat==visto){
                holder.txt_visto.setText("Visto");
            }
            else{
                holder.txt_visto.setText("Enviado");
            }
        }
        else {
            holder.txt_visto.setVisibility(View.GONE);
        }
*/



    }

    @Override
    public int getItemCount() {
        return listaChats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        Context context;
        TextView show_message;
        ImageView profile_image;
        TextView txt_visto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.msg_profile_image);
            txt_visto = itemView.findViewById(R.id.txt_seen);


        }

    }

    @Override
    public int getItemViewType(int position) {

      if (listaChats.get(position).getSender().equals(fuser.getUid())){
          return MSG_TYPE_RIGHT;
      }
      else{
          return MSG_TYPE_LEFT;
        }
    }
}
