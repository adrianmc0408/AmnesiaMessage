package com.example.prueba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.prueba.Fragments.OtrosFragment;
import com.github.chrisbanes.photoview.PhotoView;

public class VisualizadorFotos extends AppCompatActivity {

    private PhotoView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizador_fotos);
        String url_image = getIntent().getStringExtra("url_image");
        String username = getIntent().getStringExtra("username");
        imageView = findViewById(R.id.photo_view);


        if(url_image.equals("default")) {
            imageView.setImageResource(R.drawable.profile);
        }
        else{
            Glide.with(this).load(url_image).into(imageView);
        }




    }
}
