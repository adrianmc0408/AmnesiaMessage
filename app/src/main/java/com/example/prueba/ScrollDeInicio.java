package com.example.prueba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.prueba.Adaptadores.OnboardingAdapter;
import com.example.prueba.Items.OnboardingItem;
import com.example.prueba.Objetos.ServicioNotificaciones;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ScrollDeInicio extends AppCompatActivity {
    //Declaramos las variables
    private LinearLayout layoutOnboardingIndicator;
    private OnboardingAdapter onboardingAdapter;
    private MaterialButton buttonOnBoardingAction;
    private FirebaseAuth mAuth;
    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolldeinicio);
        mAuth=FirebaseAuth.getInstance();
        layoutOnboardingIndicator = findViewById(R.id.layoutOnBoardingIndicator);
        buttonOnBoardingAction = findViewById(R.id.buttonOnBoardingAction);
        //Construimos el adaptador con todos los elementos
        setOnboardingItems();

        final ViewPager2 viewpager = findViewById(R.id.onboardingViewPager);
        //Asignamos el adaptador del viewpager
        viewpager.setAdapter(onboardingAdapter);
        setupOnBoardingIndicator();
        setCurrentOnBoardingIndicator(0);


        //Listener que nos situara
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnBoardingIndicator(position);
            }
        });

        buttonOnBoardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewpager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()){
                    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                }
                else{
                    startActivity(new Intent(getApplicationContext(), SelectorLoginRegistro.class));
                    finish();

                }
            }
        });

    }
    private void setOnboardingItems(){
        //Creamos una lista de OnBoardingItems que contendran la informacion de cada pantalla
        List<OnboardingItem> onboardingItemList = new ArrayList<>();
        //Declaramos e instanciamos el primer item
        OnboardingItem item1 = new OnboardingItem();
        //Le asignamos los atributos titulo, descripcion e imagen
        item1.setTitle("Bienvenido a Amnesia Message");
        item1.setDescription("Tu aplicacion de chat segura donde prevalece el anonimato de los usuarios y la seguridad de los chats ");
        item1.setImage(R.drawable.imagen1);
        //Declaramos e instanciamos el segundo item
        OnboardingItem item2 = new OnboardingItem();
        //Le asignamos los atributos titulo, descripcion e imagen
        item2.setTitle("Chats seguros");
        item2.setDescription("Podrás chatear de forma segura y totalmente anónima, ya que los mensajes se eliminan cada 24h y no se puede capturar la pantalla ni reenviar mensajes ");
        item2.setImage(R.drawable.imagen2);
        //Declaramos e instanciamos el utlimo item
        OnboardingItem item3 = new OnboardingItem();
        //Le asignamos los atributos titulo, descripcion e imagen
        item3.setTitle("Localización");
        item3.setDescription("Podrás agregar usuarios según la proximidad a tu ubicación, descubre gente nueva de una forma totalmente segura");
        item3.setImage(R.drawable.imagen3);

        //Añadimos todos los items a la lista
        onboardingItemList.add(item1);
        onboardingItemList.add(item2);
        onboardingItemList.add(item3);
        //Contruimos el adapter a partir de la lista que le pasamos por parametro
        onboardingAdapter = new OnboardingAdapter(onboardingItemList);


    }
    //Cambiamos el indicador de pagina no actual de la parte inferior izquierda
    private void setupOnBoardingIndicator(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);

        for (int i = 0 ; i < indicators.length;i++ ){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicator.addView(indicators[i]);
        }
    }
    //Cambiamos el indicador de pagina actual de la parte inferior izquierda
    private void setCurrentOnBoardingIndicator(int index){
        int childCount = layoutOnboardingIndicator.getChildCount();
        for (int i = 0 ; i < childCount;i++ ){
            ImageView imageView = (ImageView)layoutOnboardingIndicator.getChildAt(i);
            if(i == index){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_active)
                );
            }
            else{
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive)
                );
            }
        }
        //Cambiamos el texto del boton en funcion de la "pagina" en la que nos encontremos
        if(index == onboardingAdapter.getItemCount() - 1){
            buttonOnBoardingAction.setText("Empezar");
        }
        else{
            buttonOnBoardingAction.setText("Siguiente");
        }

    }
    //Metodo  que detecta si hay una sesion abierta
    protected void onResume(){
        super.onResume();
        FirebaseUser usuario_actual=mAuth.getCurrentUser();
        if(usuario_actual!=null){
            pasar();
        }

        startService(new Intent(getApplicationContext(), ServicioNotificaciones.class));
    }
    //Metodo que nos envia al Home si hay sesion abierta
    private void pasar(){
        Intent intent =new Intent(ScrollDeInicio.this, HomePrincipal.class);
        startActivity(intent);
        finish();
    }
}
