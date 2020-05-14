package com.example.prueba.Adaptadores;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapterBusqueda extends RecyclerView.Adapter<UserAdapterBusqueda.ViewHolder> {

    private ArrayList<Usuario> listaUsuarios;
    private Context mContext;
    private Dialog myDialog;



    public UserAdapterBusqueda( ArrayList<Usuario> usuarios,Context mContext) {
        this.mContext= mContext;
        this.listaUsuarios = usuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_agregar_usuario, parent, false);
       final ViewHolder viewHolder = new ViewHolder(view);

        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.popup_mandar_solicitud);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        viewHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               TextView dialog_user= (TextView) myDialog.findViewById(R.id.dialog_user);
                CircleImageView dialog_image_profile = (CircleImageView) myDialog.findViewById(R.id.dialog_img_profile);
                dialog_user.setText(listaUsuarios.get(viewHolder.getAdapterPosition()).getNombre_usuario());
                Button enviarSolicitud = myDialog.findViewById(R.id.dialog_btn_enviarSolicitud);
                Button cancelar = myDialog.findViewById(R.id.dialog_btn_cancelar);
                //dialog_image_profile.setImageResource(R.id.profile);

                enviarSolicitud.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "SolicitudEnviada", Toast.LENGTH_SHORT).show();
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       myDialog.cancel();
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


