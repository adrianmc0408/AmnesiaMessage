package com.example.prueba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
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
    private Button btn_contraseña;
    private Button btn_cerrar_sesion;

    private TextView username_superior;
    private CircleImageView foto_perfil;

    private EditText username;
    private EditText telefono;
    private EditText correo;
    private EditText password;
    private Dialog qr_dialog;

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
        btn_contraseña=findViewById(R.id.miperfil_btn_password);
        btn_cerrar_sesion=findViewById(R.id.miperfil_btn_cerrar_sesion);

        username_superior=findViewById(R.id.miperfil_usuario);
        foto_perfil=findViewById(R.id.miperfil_profile_image);

        username=findViewById(R.id.miperfil_edit_nombreusuario);
        telefono=findViewById(R.id.miperfil_edit_telefono);
        correo=findViewById(R.id.miperfil_edit_correo);
        password=findViewById(R.id.miperfil_edit_contraseña);



        tituloActivity=findViewById(R.id.titulo_activity);
        tituloActivity.setText("Mi Perfil");

        closeActivity=findViewById(R.id.close_activity);
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    btn_qr.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            ImageView image= (ImageView) qr_dialog.findViewById(R.id.qr_image);
            image.setImageResource(R.drawable.qr_example);
            qr_dialog.show();
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


                    btn_contraseña.setVisibility(View.INVISIBLE);
                    btn_cerrar_sesion.setVisibility(View.INVISIBLE);
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
                        btn_contraseña.setVisibility(View.VISIBLE);
                        btn_cerrar_sesion.setVisibility(View.VISIBLE);
                        btn_modificar.setBackgroundColor(Color.rgb(33,150,244));

                        btn_modificar.setText("MODIFICAR MIS DATOS");

                        telefono.setEnabled(false);
                        funcion_mod="mod";
                    }



                }
            }

            public boolean validCellPhone(String number) { return android.util.Patterns.PHONE.matcher(number).matches(); }
        });


    }
}
