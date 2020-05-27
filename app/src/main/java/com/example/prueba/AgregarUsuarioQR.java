package com.example.prueba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgregarUsuarioQR extends AppCompatActivity {

    private TextView tituloActivity;
    private ImageView closeActivity;

    private Button mostrar_pop;
    private Dialog dialog_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_q_r);

        dialog_confirm = new Dialog(this);
        dialog_confirm.setContentView(R.layout.popup_confirmacion_qr);
        dialog_confirm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mostrar_pop = findViewById(R.id.btn_prueba);


        mostrar_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CircleImageView image= (CircleImageView) dialog_confirm.findViewById(R.id.dialog_qr_img_profile);
                TextView username = (TextView) dialog_confirm.findViewById(R.id.dialog_qr_username);
                Button confirmar = (Button) dialog_confirm.findViewById(R.id.dialog_qr_btn_confirmar);
                image.setImageResource(R.drawable.profile);
                username.setText("usuario");

                confirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });




                dialog_confirm.show();
            }
        });


        tituloActivity=findViewById(R.id.titulo_activity);
        tituloActivity.setText("Agregar por código QR");

        closeActivity=findViewById(R.id.close_activity);
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }





}
