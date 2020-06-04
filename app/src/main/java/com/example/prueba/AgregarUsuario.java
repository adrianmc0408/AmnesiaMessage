package com.example.prueba;

import android.app.Dialog;
import android.os.Bundle;


import com.example.prueba.Adaptadores.UserAdapterBusqueda;
import com.example.prueba.Objetos.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AgregarUsuario extends AppCompatActivity implements View.OnKeyListener {

    //Declaramos los atributos
    private RecyclerView recyclerView;
    private UserAdapterBusqueda userAdapterBusqueda;
    private RecyclerView.LayoutManager layoutManager;
    private TextView user_follow;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private ArrayList<Usuario> usuarioList;
    private Usuario usuario_actual;
    private String cadena;
    private EditText busqueda;
    private TextView tituloActivity;
    private ImageView closeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Enlazamos las vistas con los atributos
        setContentView(R.layout.activity_agregar_usuario);
        busqueda=findViewById(R.id.search_input);
        busqueda.setOnKeyListener(this);

        tituloActivity=findViewById(R.id.titulo_activity);
        tituloActivity.setText("Agregar usuario");

        closeActivity=findViewById(R.id.close_activity);
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


         usuarioList = new ArrayList<>();
         //Obtenemos las instancias de Firebase necesarias
        mAuth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Usuarios");
        recyclerView = findViewById(R.id.recyclerview_agregar_usuarios);
        recyclerView.setHasFixedSize(true);
        cadena="";
        //Llamada al método que nos permite leer los usuarios
        leerUsuarios();








    }
    //Método encargado de la lectura de los usuarios, es sensible a los cambios en la base de datos,
    //agregando al adapter todos aquellos distintos al usuario actual
    public void leerUsuarios(){
        //Añadimos el listener, el cual será el encargado de controlar los cambios producidos en nuestra base de datos,
        //En este caso los cambios de el nodo Usuarios.
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            //Detecta cuando los datos de ese nodo cambian
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Limpiamos la lista
                usuarioList.clear();
                //Recorremos el objeto DataSnapchot obteniendo los nodos hijos y serializandolos a un objeto de
                //tipo usuario
                for(DataSnapshot data:dataSnapshot.getChildren()){
                   Usuario usuario= data.getValue(Usuario.class);
                   //Obtenemos nuestro usuario actual
                    FirebaseUser user=mAuth.getCurrentUser();
                   if(usuario!=null ){
                       //Si el usuario no es el actual comprobamos que la cadena de busqueda del EditText superior no
                       //esté vacia, si está vacia lo añadimos directamente ya que significa que queremos ver todos los usuarios
                       //mientras que si la cadena contiene texto deberemos de buscar aquellos usuarios cuyo  nombre de usuario
                       //contenga la cadena a buscar
                       if(!usuario.getId().equals(user.getUid())) {
                           if(!cadena.equals("")){
                               if(usuario.getNombre_usuario().contains(cadena)){
                                   usuarioList.add(usuario);
                               }
                           }
                           else{
                               usuarioList.add(usuario);
                           }
                       }
                       //Si el ID del usuario coincide con el nuestro lo almacenamos en una variable aparte que contiene
                       //los datos  del usuario que actualmente controla la app
                       else{
                           usuario_actual=usuario;
                       }
                   }
                }
                //Creamos un nuevo layout y un nuevo adapter cada vez que escribimos en el TextEdit o los datos de la base de datos
                //cambian (es decir cuando un nuevo usuario es creado)
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                userAdapterBusqueda = new UserAdapterBusqueda( usuarioList,AgregarUsuario.this,usuario_actual);

                recyclerView.setAdapter(userAdapterBusqueda);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Método utilizado para controlar el evento de tecla presionada, lo que nos permitirá buscar dentro de nuestra
    //BD la cadena escrita.
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        cadena=busqueda.getText().toString();
        leerUsuarios();
        return false;
    }

}
