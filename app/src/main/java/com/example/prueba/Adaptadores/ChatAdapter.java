package com.example.prueba.Adaptadores;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prueba.MiPerfil;
import com.example.prueba.Objetos.Chat;
import com.example.prueba.Objetos.Usuario2;
import com.example.prueba.R;
import com.example.prueba.VisualizadorFotos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    //Declaramos e inicializamos las variables

    //Variables que serviran para establecer un layout u otro a la burbuja de chat en función del emisor del mensaje
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private ArrayList<Chat> listaChats;
    private String imageurl;
    private String txt_visto;
    private String txt_hora;
    //1Izquierda 2Derecha
    private int mensajeType;
    private FirebaseUser fuser;
    private FirebaseAuth auth;

    //Constructor del adaptador del chat
    public ChatAdapter(Context context, ArrayList<Chat> chats,String imageurl) {
        auth=FirebaseAuth.getInstance();
        fuser=auth.getCurrentUser();
        this.mContext = context;
        this.listaChats = chats;
        this.imageurl=imageurl;
    }



    @NonNull
    @Override
    /*
        Creamos un ViewHolder, el RecyclerView lo inflará con las filas visibles
        - Asignamos un layout u otro a la vista en función del emisor del mensaje
     */
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            mensajeType=2;
            return new ChatAdapter.ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            mensajeType=1;
            return new ChatAdapter.ViewHolder(view);
        }
    }

    @Override
    /*
    El RecyclerView llama a este método para reutilizar los ViewHolder creados,
    , es decir los infla con las nuevas filas visibles según el usuario desliza la lista.
    (Se establece una foto de perfil predeterminada en caso de que no tenga una el usuario)
     */
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, final int position) {
        final Chat chat = listaChats.get(position);
        String tipo = chat.getType();

        String hora="";
        int horas=listaChats.get(position).getFecha().getHours();
        int minutos=listaChats.get(position).getFecha().getMinutes();
        if(horas<10){
            hora="0"+horas+":";
        }
        else{
            hora=horas+":";
        }
        if(minutos<10){
            hora=hora+"0"+minutos;
        }
        else{
            hora=hora+minutos;
        }

        holder.txt_hora.setText(hora);

        if(tipo.equals("img")){
            holder.show_image.setVisibility(View.VISIBLE);
            holder.show_message.setVisibility(View.GONE);
            Glide.with(mContext).load(chat.getUrl_imagen()).into(holder.show_image);
            holder.show_image.setPadding(12,12,12,12);
            holder.show_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VisualizadorFotos.class);
                    intent.putExtra("url_image",chat.getUrl_imagen());
                    intent.putExtra("username","null");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
            if(mensajeType==1) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.txt_hora.getLayoutParams();
                params.addRule(RelativeLayout.BELOW, R.id.show_image);
                holder.txt_hora.setLayoutParams(params);
            }

        }
        else{
            holder.show_message.setText(chat.getMessage());
        }



        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.drawable.profile);
        }else{
            //Coger imagen
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }



        //En este if solo entraremos en el caso de que sea el ultimo mensaje del array
        if(position == listaChats.size()-1){
            /*
            Si el mensaje ha sido leido, mostraremos la etiqueta "Visto" y desplazaremos la etiqueta de hora de
            envio de mensaje hacia la izquierda
             */
            if(chat.isLeido()==true){
                holder.txt_visto.setText("Visto");
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.txt_hora.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_END,0);
                params.addRule(RelativeLayout.LEFT_OF, R.id.txt_seen);


                holder.txt_hora.setLayoutParams(params);
                if( getItemViewType(position)==MSG_TYPE_RIGHT){
                    holder.txt_hora.setText(holder.txt_hora.getText()+" - ");
                }

            }
            //Si el mensaje no ha sido leido ocultamos la etiqueta de "Visto"
            else {
                holder.txt_visto.setVisibility(View.GONE);
            }
        }
        //Si no es el ultimo mensaje ocultamos la etiqueta "Visto"
        else {
            holder.txt_visto.setVisibility(View.GONE);
        }



    }

    @Override
    //Obtenemos el numero de elementos de nuestro Arraylist listaChats
    public int getItemCount() {
        return listaChats.size();
    }

    //Clase estática por recomendación del API
    public static class ViewHolder extends RecyclerView.ViewHolder{
        Context context;
        TextView show_message;
        CircleImageView profile_image;
        TextView txt_visto;
        TextView txt_hora;
        ImageView show_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.msg_profile_image);
            txt_visto = itemView.findViewById(R.id.txt_seen);
            txt_hora = itemView.findViewById(R.id.txt_hour);
            show_image = itemView.findViewById(R.id.show_image);


        }

    }
    /*
    Con este metodo filtraremos si el mensaje obtenido lo hemos enviado nosotros o no
     */
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