package com.example.prueba.Adaptadores;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prueba.Objetos.Amistad;
import com.example.prueba.Objetos.Solicitud;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario3;
import com.example.prueba.R;
import com.example.prueba.VisualizadorFotos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapterBusquedaLocation extends RecyclerView.Adapter<UserAdapterBusquedaLocation.ViewHolder> {

    //Declaramos las variables
    private ArrayList<Usuario3> listaUsuarios;
    private Context mContext;
    private Dialog myDialog;
    private FirebaseDatabase base;
    private DatabaseReference referencia;
    private DatabaseReference referencia2;
    private Usuario3 usuario;

    //Constructor del adaptador de Usuarios (presente en el activity AgregarUsuarioLocation)
    public UserAdapterBusquedaLocation( ArrayList<Usuario3> usuarios,Context mContext,Usuario3 usuario) {
        this.usuario = usuario;
        this.mContext = mContext;
        this.listaUsuarios = usuarios;
        //Obtenemos las referencias necesarias
        base = FirebaseDatabase.getInstance();
        referencia = base.getReference("Solicitudes");
        referencia2 = base.getReference("Amigos");
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

        viewHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView dialog_user= (TextView) myDialog.findViewById(R.id.dialog_user);
                final CircleImageView dialog_image_profile = (CircleImageView) myDialog.findViewById(R.id.dialog_img_profile);
                String url_destino = listaUsuarios.get(viewHolder.getAdapterPosition()).getUrl_imagen();
                if (url_destino.equals("default")) {
                    dialog_image_profile.setImageResource(R.drawable.profile);
                } else {
                    Glide.with(mContext).load(url_destino).into(dialog_image_profile);

                }
                dialog_user.setText(listaUsuarios.get(viewHolder.getAdapterPosition()).getNombre_usuario());
                Button enviarSolicitud = myDialog.findViewById(R.id.dialog_btn_enviarSolicitud);
                Button cancelar = myDialog.findViewById(R.id.dialog_btn_cancelar);
                //dialog_image_profile.setImageResource(R.id.profile);

                 /* Cuando clickamos sobre el usuario buscado se nos abrirá un dialogbox que nos dara la opcion de enviar una
                    solicitud de amistad al usuario o no */
                enviarSolicitud.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Construimos un objeto "Solicitud" y la subimos a la base de datos
                        comprobacionAmigoss(listaUsuarios.get(viewHolder.getAdapterPosition()).getId(), usuario.getId(), viewHolder.getAdapterPosition());
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.cancel();
                    }
                });

                myDialog.show();





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

        final Usuario3 user = listaUsuarios.get(position);


        holder.username.setText(user.getNombre_usuario());
        if(user.getUrl_imagen().equals("default")){
            holder.profile_image.setImageResource(R.drawable.profile);
        }
        else{
            Glide.with(mContext).load(user.getUrl_imagen()).into(holder.profile_image);

        }
        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VisualizadorFotos.class);
                intent.putExtra("url_image",user.getUrl_imagen());
                intent.putExtra("username",user.getNombre_usuario());
                mContext.startActivity(intent);
            }
        });





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
    public void comprobacionSolicitudes(final String IDdestino, final String IDorigen, final int position) {
        Boolean existente = false;
        referencia.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Solicitud> listaSolicitudes = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Solicitud solicitud = data.getValue(Solicitud.class);
                    listaSolicitudes.add(solicitud);
                }
                int existente = 0;
                for (Solicitud sol : listaSolicitudes) {
                    if (sol.getId_destino().equals(IDdestino) && sol.getId().equals(IDorigen))
                        //1=ya has mandado una solcitud
                        existente = 1;
                    if (sol.getId_destino().equals(IDorigen) && sol.getId().equals(IDdestino))
                        //2=ya te han mandado una solicitud
                        existente = 2;
                }
                if (existente==0) {
                    String id_destino = listaUsuarios.get(position).getId();
                    String id = usuario.getId();
                    String nombre_usuario = usuario.getNombre_usuario();
                    String email = usuario.getEmail();
                    String telefono = usuario.getTelefono();

                    String url_imagen = usuario.getUrl_imagen();
                    Solicitud solicitud = new Solicitud(id_destino, id, nombre_usuario, email, telefono, url_imagen);
                    referencia.push().setValue(solicitud);

                    Toast.makeText(mContext, "Solicitud enviada", Toast.LENGTH_SHORT).show();
                    myDialog.cancel();
                }
                if (existente==1) {
                    Toast.makeText(mContext, "Ya has enviado una solicitud a este usuario", Toast.LENGTH_SHORT).show();
                }
                if (existente==1) {
                    Toast.makeText(mContext, "Tienes una solicitud pendiente de este usuario", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    public void comprobacionAmigoss(final String IDdestino, final String IDorigen, final int position) {
        Boolean existente = false;
        referencia2.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Amistad> listaAmigos = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Amistad amigo = data.getValue(Amistad.class);
                    listaAmigos.add(amigo);
                }
                boolean amistadEncontrada=false;
                int contador = 0;
                for (Amistad am : listaAmigos){
                    if(    (am.getId1().equals(IDdestino)&& am.getId2().equals(IDorigen) ) || (am.getId1().equals(IDorigen)&& am.getId2().equals(IDdestino) ) ){
                        amistadEncontrada=true;
                    }
                }
                if(amistadEncontrada==true){
                    Toast.makeText(mContext, "Este usuario ya es tu amigo", Toast.LENGTH_SHORT).show();
                }
                else{
                    comprobacionSolicitudes(IDdestino,IDorigen,position);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

}

