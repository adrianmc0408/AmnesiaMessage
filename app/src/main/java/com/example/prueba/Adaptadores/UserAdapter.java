package com.example.prueba.Adaptadores;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.prueba.ChatRoom;
import com.example.prueba.Objetos.Chat;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario2;
import com.example.prueba.R;
import com.example.prueba.SelectorLoginRegistro;
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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    //Declaramos las variables
    private Context mContext;
    private ArrayList<Usuario2> listaUsuarios;
    private Dialog myDialog;
    private FirebaseDatabase base;
    private DatabaseReference referencia;
    private DatabaseReference referencia2;
    private FirebaseAuth auth;
    private FirebaseUser user;

    //Constructor del adaptador de Usuarios (presente en el fragment friends)
    public UserAdapter(Context context, ArrayList<Usuario2> usuarios) {
        this.mContext = context;
        this.listaUsuarios = usuarios;
        //Obtenemos las referencias necesarias
        base=FirebaseDatabase.getInstance();
        referencia=base.getReference("Amigos");
        referencia2=base.getReference("Chats");
        auth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    //Creamos un ViewHolder, el RecyclerView lo inflará con las filas visibles
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_friend, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setIsRecyclable(false);

        //Construimos un dialogo para cada elemento que se abrirá al clickar sobre cada usuario desplegado en el recycler
        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.popup_opciones_usuario);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        /*
        Cuando clickamos sobre un usuario desplegaremos un dialog con dos opciones:
                -Chat: abrimos un chat con ese usuario
                -Eliminar: nos dará la posibilidad de eliminar a ese usuario de nuestros amigos
                          (Despliega antes un dialogbox de confirmacion)
         */
        viewHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView dialog_user= (TextView) myDialog.findViewById(R.id.dialog_usuario_username);
                final CircleImageView dialog_image_profile = (CircleImageView) myDialog.findViewById(R.id.dialog_usuario_foto_perfil);
                dialog_user.setText(listaUsuarios.get(viewHolder.getAdapterPosition()).getNombre_usuario());
                final int position = viewHolder.getAdapterPosition();
                String url_destino = listaUsuarios.get(viewHolder.getAdapterPosition()).getUrl_imagen();
                if(url_destino.equals("default")){
                    dialog_image_profile.setImageResource(R.drawable.profile);
                }
                else{
                    Glide.with(mContext).load(url_destino).into(dialog_image_profile);

                }
                Button chat = myDialog.findViewById(R.id.dialog_usuario_btn_chat);
                Button eliminar = myDialog.findViewById(R.id.dialog_usuario_btn_eliminar);
                //dialog_image_profile.setImageResource(R.id.profile);


                eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog_conf = new Dialog(mContext);
                        dialog_conf.setContentView(R.layout.popup_confirmacion);

                        TextView titulo = (TextView) dialog_conf.findViewById(R.id.dialog_conf_titulo);
                        Button btn_si = (Button) dialog_conf.findViewById(R.id.dialog_conf_si);
                        Button btn_no =(Button) dialog_conf.findViewById(R.id.dialog_conf_no);
                        titulo.setText("¿Desea eliminar a este usuario de su lista de amigos?");
                        btn_si.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                borrarChats(position);
                                referencia.child(listaUsuarios.get(position).getReferencia()).removeValue();
                                myDialog.cancel();
                                dialog_conf.cancel();


                            }
                        });
                        btn_no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_conf.cancel();
                            }
                        });

                        dialog_conf.show();

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
                        myDialog.cancel();
                    }
                });
                //Mostramos el dialog box
                myDialog.show();





            }
        });


        return viewHolder;
    }
    /*Metodo que utilizamos para eliminar una solicitud de amistad del recyclerview
    (pasandole por parametro la posicion dentro del arraylist que queremos eliminar) */
    public final void removeAt(int position) {
        listaUsuarios.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listaUsuarios.size());
    }
    @Override
     /*
    El RecyclerView llama a este método para reutilizar los ViewHolder creados,
    , es decir los infla con las nuevas filas visibles según el usuario desliza la lista.
    (Se establece una foto de perfil predeterminada en caso de que no tenga una el usuario)
     */
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Usuario2 user = listaUsuarios.get(position);
        holder.username.setText(user.getNombre_usuario());
        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VisualizadorFotos.class);
                intent.putExtra("url_image",user.getUrl_imagen());
                intent.putExtra("username",user.getNombre_usuario());
                mContext.startActivity(intent);
            }
        });
        if(user.getUrl_imagen().equals("default")){
            holder.profile_image.setImageResource(R.drawable.profile);
        }
        else{
            Glide.with(mContext).load(user.getUrl_imagen()).into(holder.profile_image);

        }



    }

    @Override
    //Obtenemos el numero de elementos de nuestro Arraylist listaChats
    public int getItemCount() {
        return listaUsuarios.size();
    }
    //Clase estática por recomendación del API
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
    //Este método recibe la posición del usuario a por parámetro, y borra todos los mensajes que haya intercambiado con el usuario
    //
public void borrarChats(final int posicion){
        //Consulta a la base de datos Real Time
    referencia2.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Chat chat;
            user=auth.getCurrentUser();
            ArrayList<String> borrar=new ArrayList<>();
            //Recorremos los nodos , serializandolos en objetos de tipo Chat
            for(DataSnapshot datos:dataSnapshot.getChildren()){
                chat=datos.getValue(Chat.class);
                //Si el mensaje es nuestro hacia el usuario a borrar o vicerversa añadimos su clave a el array
                if (((chat.getReceiver().equals(user.getUid())) && (chat.getSender().equals(listaUsuarios.get(posicion).getId()))) || ((chat.getReceiver().equals(listaUsuarios.get(posicion).getId())) && (chat.getSender().equals(user.getUid())))) {
                    borrar.add(datos.getKey());
                }
            }
            //Borramos los mensajes
            for(int i=0;i<borrar.size();i++){
                referencia2.child(borrar.get(i)).removeValue();
            }
            //Borramos la posición del usuario en cuestión
            removeAt(posicion);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}

}
