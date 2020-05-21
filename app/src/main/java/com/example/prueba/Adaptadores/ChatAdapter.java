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
    private FirebaseUser fuser;


    public ChatAdapter(Context context, ArrayList<Chat> chats,String imageurl) {
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




    }

    @Override
    public int getItemCount() {
        return listaChats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        Context context;
        TextView show_message;
        ImageView profile_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.msg_profile_image);

        }

    }

    @Override
    public int getItemViewType(int position) {

      if (listaChats.get(position).getSender().equals("1")){
          return MSG_TYPE_RIGHT;
      }
      else{
          return MSG_TYPE_LEFT;
        }
    }
}
