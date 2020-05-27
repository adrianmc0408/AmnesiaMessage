package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MiPerfil extends AppCompatActivity {

    private String username_mod;
    private String telefono_mod;

    private TextView tituloActivity;
    private ImageView closeActivity;

    private ImageView btn_qr;
    private ImageView btn_ubi;

    private Button btn_modificar;
    private String funcion_mod="mod";
    private Button btn_contrasena;


    private TextView username_superior;
    private CircleImageView foto_perfil;

    private EditText username;
    private EditText telefono;
    private EditText correo;
    private EditText password;
    private Dialog qr_dialog;

    private FirebaseDatabase base;
    private DatabaseReference referencia;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Usuario2 usuario2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        qr_dialog = new Dialog(this);
        qr_dialog.setContentView(R.layout.popup_qr);
        qr_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btn_qr=findViewById(R.id.miperfil_btn_qr);
        btn_ubi=findViewById(R.id.miperfil_btn_ubi);

        btn_modificar=findViewById(R.id.miperfil_btn_modificar);
        btn_contrasena=findViewById(R.id.miperfil_btn_password);


        username_superior=findViewById(R.id.miperfil_usuario);
        foto_perfil=findViewById(R.id.miperfil_profile_image);

        username=findViewById(R.id.miperfil_edit_nombreusuario);
        telefono=findViewById(R.id.miperfil_edit_telefono);
        correo=findViewById(R.id.miperfil_edit_correo);




        tituloActivity=findViewById(R.id.titulo_activity);
        tituloActivity.setText("Mi Perfil");

        closeActivity=findViewById(R.id.close_activity);
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        base=FirebaseDatabase.getInstance();
        referencia=base.getReference("Usuarios");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();



    btn_qr.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            ImageView image= (ImageView) qr_dialog.findViewById(R.id.qr_image);
            image.setImageResource(R.drawable.qr_example);
            qr_dialog.show();
        }
    });

        btn_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),CambioContrasena.class));
            }
        });
        btn_ubi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_ubi.getTag().equals("1")){
                    btn_ubi.setTag("2");
                    btn_ubi.setImageResource(R.drawable.ic_location_off);
                }
                else{
                    btn_ubi.setTag("1");
                    btn_ubi.setImageResource(R.drawable.ic_location_on);
                }
            }
        });
        btn_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(funcion_mod.equals("mod")){

                    telefono_mod=telefono.getText().toString();


                    btn_contrasena.setVisibility(View.INVISIBLE);

                    btn_modificar.setBackgroundColor(Color.rgb(10,210,149));


                    btn_modificar.setText("Confirmar cambios");



                    telefono.setEnabled(true);
                    funcion_mod="conf";
                }
                else{
                    boolean salida;
                    //Si no se han realizado cambios
                    if( telefono.getText().toString().equals(telefono_mod)){
                        salida=true;
                        Toast.makeText(MiPerfil.this, "No se ha realizado ningun cambio", Toast.LENGTH_SHORT).show();
                    }
                    else {
                      if (telefono.getText().equals("")){
                          Toast.makeText(MiPerfil.this, "Campo incompleto", Toast.LENGTH_SHORT).show();
                          salida=false;
                      }
                      else{
                          if( validCellPhone(telefono.getText().toString()) && telefono.getText().toString().length()==9 ){
                              salida=true;
                          }
                          else{
                              Toast.makeText(MiPerfil.this, "Campo no valido", Toast.LENGTH_SHORT).show();
                              salida=false;
                          }
                      }
                    }
                    if(salida==true){
                        usuario2.setTelefono(telefono.getText().toString());
                        referencia.child(usuario2.getReferencia()).setValue(usuario2);
                        btn_contrasena.setVisibility(View.VISIBLE);

                        btn_modificar.setBackgroundColor(Color.rgb(33,150,244));

                        btn_modificar.setText("MODIFICAR MIS DATOS");

                        telefono.setEnabled(false);
                        funcion_mod="mod";
                    }



                }
            }

            public boolean validCellPhone(String number) { return android.util.Patterns.PHONE.matcher(number).matches(); }
        });
        leerDatos();
    }
    public void leerDatos(){
        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Usuario usuario = data.getValue(Usuario.class);
                    if (usuario.getId().equals(user.getUid())) {
                        usuario2 = new Usuario2(data.getKey(), usuario.getId(), usuario.getNombre_usuario(), usuario.getEmail(), usuario.getTelefono(), usuario.getUrl_imagen());
                    }
                }
                username_superior.setText(usuario2.getNombre_usuario());
                username.setText(usuario2.getNombre_usuario());
                telefono.setText(usuario2.getTelefono());
                correo.setText(usuario2.getEmail());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
