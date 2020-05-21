package com.example.prueba.Adaptadores;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.example.prueba.ChatRoom;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario2;
import com.example.prueba.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Usuario2> listaUsuarios;
    private Dialog myDialog;
    private FirebaseDatabase base;
    private DatabaseReference referencia;

    public UserAdapter(Context context, ArrayList<Usuario2> usuarios) {
        this.mContext = context;
        this.listaUsuarios = usuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_friend, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        base=FirebaseDatabase.getInstance();
        referencia=base.getReference("Amigos");

        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.popup_opciones_usuario);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        viewHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView dialog_user= (TextView) myDialog.findViewById(R.id.dialog_usuario_username);
                CircleImageView dialog_image_profile = (CircleImageView) myDialog.findViewById(R.id.dialog_usuario_foto_perfil);
                dialog_user.setText(listaUsuarios.get(viewHolder.getAdapterPosition()).getNombre_usuario());
                final int position = viewHolder.getAdapterPosition();
                Button perfil = myDialog.findViewById(R.id.dialog_usuario_btn_perfil);
                Button chat = myDialog.findViewById(R.id.dialog_usuario_btn_chat);
                Button eliminar = myDialog.findViewById(R.id.dialog_usuario_btn_eliminar);
                //dialog_image_profile.setImageResource(R.id.profile);

                perfil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "boton perfil", Toast.LENGTH_SHORT).show();
                    }
                });
                eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    referencia.child(listaUsuarios.get(position).getReferencia()).removeValue();
                        removeAt(position);
                        myDialog.cancel();
                    }
                });
                chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ChatRoom.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("usuario",listaUsuarios.get(position));
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });

                myDialog.show();

                //EVENTO DE ENVIAR SOLICITUD AQUI



            }
        });


        return viewHolder;
    }
    public final void removeAt(int position) {
        listaUsuarios.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listaUsuarios.size());
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Usuario2 user = listaUsuarios.get(position);
        holder.username.setText(user.getNombre_usuario());
        //Aqui no se que coño poner asique pongo cualquier imagen
        holder.profile_image.setImageResource(R.mipmap.ic_launcher);



    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
         Context context;
         TextView username;
         ImageView profile_image;
         CardView item_contact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            username = itemView.findViewById(R.id.friend_username);
            profile_image = itemView.findViewById(R.id.friend_image_profile);
            item_contact = itemView.findViewById(R.id.friend_cardview);
        }

    }


}
