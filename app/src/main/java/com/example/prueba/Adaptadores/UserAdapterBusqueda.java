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

import com.bumptech.glide.Glide;
import com.example.prueba.Objetos.Solicitud;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapterBusqueda extends RecyclerView.Adapter<UserAdapterBusqueda.ViewHolder> {
    //Declaramos las variables

    private ArrayList<Usuario> listaUsuarios;
    private Context mContext;
    private Dialog myDialog;
    private FirebaseDatabase base;
    private DatabaseReference referencia;
    private Usuario usuario;

    //Constructor del adaptador de Usuarios (presente en el activity AgregarUsuario)
    public UserAdapterBusqueda( ArrayList<Usuario> usuarios,Context mContext,Usuario usuario) {
        this.usuario=usuario;
        this.mContext= mContext;
        this.listaUsuarios = usuarios;
    }

    @NonNull
    @Override
    //Creamos un ViewHolder, el RecyclerView lo inflará con las filas visibles
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_agregar_usuario, parent, false);
       final ViewHolder viewHolder = new ViewHolder(view);
        //Obtenemos las referencias necesarias
       base=FirebaseDatabase.getInstance();
       referencia=base.getReference("Solicitudes");
        //Construimos un dialogo para cada elemento que se abrirá al clickar sobre cada usuario desplegado en el recycler

        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.popup_mandar_solicitud);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /*
        Cuando clickamos sobre el usuario buscado se nos abrirá un dialogbox que nos dara la opcion de enviar una
        solicitud de amistad al usuario o no
         */
        viewHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               TextView dialog_user= (TextView) myDialog.findViewById(R.id.dialog_user);
                CircleImageView dialog_image_profile = (CircleImageView) myDialog.findViewById(R.id.dialog_img_profile);
                String url_destino = listaUsuarios.get(viewHolder.getAdapterPosition()).getUrl_imagen();
                if(url_destino.equals("default")){
                    dialog_image_profile.setImageResource(R.drawable.profile);
                }
                else{
                    Glide.with(mContext).load(url_destino).into(dialog_image_profile);

                }
                dialog_user.setText(listaUsuarios.get(viewHolder.getAdapterPosition()).getNombre_usuario());
                Button enviarSolicitud = myDialog.findViewById(R.id.dialog_btn_enviarSolicitud);
                Button cancelar = myDialog.findViewById(R.id.dialog_btn_cancelar);
                //dialog_image_profile.setImageResource(R.id.profile);

                enviarSolicitud.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Construimos un objeto "Solicitud" y la subimos a la base de datos
                        String id_destino=listaUsuarios.get(viewHolder.getAdapterPosition()).getId();
                        String id=usuario.getId();
                        String nombre_usuario=usuario.getNombre_usuario();
                        String email=usuario.getEmail();
                        String telefono=usuario.getTelefono();

                        String url_imagen=usuario.getUrl_imagen();
                        Solicitud solicitud=new Solicitud( id_destino,id,nombre_usuario,email, telefono, url_imagen);
                        referencia.push().setValue(solicitud);

                        Toast.makeText(mContext, "Solicitud enviada", Toast.LENGTH_SHORT).show();
                        myDialog.cancel();
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
     /*
    El RecyclerView llama a este método para reutilizar los ViewHolder creados,
    , es decir los infla con las nuevas filas visibles según el usuario desliza la lista.
    (Se establece una foto de perfil predeterminada en caso de que no tenga una el usuario)
     */
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Usuario user = listaUsuarios.get(position);


            holder.username.setText(user.getNombre_usuario());
        if(user.getUrl_imagen().equals("default")){
            holder.profile_image.setImageResource(R.drawable.profile);
        }
        else{
            Glide.with(mContext).load(user.getUrl_imagen()).into(holder.profile_image);

        }





    }

    @Override
    //Metodo con el que obtenemos la longitud del Arraylist de listaUsuarios
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


