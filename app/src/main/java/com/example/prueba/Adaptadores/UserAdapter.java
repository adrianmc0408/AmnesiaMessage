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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Usuario> listaUsuarios;
    private Dialog myDialog;

    public UserAdapter(Context context, ArrayList<Usuario> usuarios) {
        this.mContext = context;
        this.listaUsuarios = usuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_user_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.popup_opciones_usuario);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        viewHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView dialog_user= (TextView) myDialog.findViewById(R.id.dialog_usuario_username);
                CircleImageView dialog_image_profile = (CircleImageView) myDialog.findViewById(R.id.dialog_usuario_foto_perfil);
                dialog_user.setText(listaUsuarios.get(viewHolder.getAdapterPosition()).getNombre_usuario());
                Button perfil = myDialog.findViewById(R.id.dialog_usuario_btn_perfil);
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
                        Toast.makeText(mContext, "boton eliminar", Toast.LENGTH_SHORT).show();
                    }
                });

                myDialog.show();

                //EVENTO DE ENVIAR SOLICITUD AQUI



            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Usuario user = listaUsuarios.get(position);
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
        RelativeLayout item_contact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            username = itemView.findViewById(R.id.username_friends);
            profile_image = itemView.findViewById(R.id.image_profile);
            item_contact = itemView.findViewById(R.id.relative_contact_item);
        }

    }


}
