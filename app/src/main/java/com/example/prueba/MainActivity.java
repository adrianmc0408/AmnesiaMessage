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

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {
    private LinearLayout layoutOnboardingIndicator;
    private OnboardingAdapter onboardingAdapter;
    private MaterialButton buttonOnBoardingAction;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        layoutOnboardingIndicator = findViewById(R.id.layoutOnBoardingIndicator);
        buttonOnBoardingAction = findViewById(R.id.buttonOnBoardingAction);
        setOnboardingItems();
        final ViewPager2 viewpager = findViewById(R.id.onboardingViewPager);
        viewpager.setAdapter(onboardingAdapter);
        setupOnBoardingIndicator();
        setCurrentOnBoardingIndicator(0);



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
                    startActivity(new Intent(getApplicationContext(), pantallaInicio.class));
                    finish();

                }
            }
        });

    }
    private void setOnboardingItems(){
        List<OnboardingItem> onboardingItemList = new ArrayList<>();
        OnboardingItem itemPayOnline = new OnboardingItem();
        itemPayOnline.setTitle("Lorem  ipsum");
        itemPayOnline.setDescription("Lorem  ipsum  dolor sit  amet,  consectetur adipisicing  elit, sed  do eiusmod tempor incididunt  ut labore  et dolore magna aliqua. ");
        itemPayOnline.setImage(R.drawable.imagen1);

        OnboardingItem itemOnTheWay = new OnboardingItem();
        itemOnTheWay.setTitle("Lorem  ipsum");
        itemOnTheWay.setDescription("Lorem  ipsum  dolor sit  amet,  consectetur adipisicing  elit, sed  do eiusmod tempor incididunt  ut labore  et dolore magna aliqua. ");
        itemOnTheWay.setImage(R.drawable.imagen2);

        OnboardingItem itemOn = new OnboardingItem();
        itemOn.setTitle("Lorem  ipsum");
        itemOn.setDescription("Lorem  ipsum  dolor sit  amet,  consectetur adipisicing  elit, sed  do eiusmod tempor incididunt  ut labore  et dolore magna aliqua. ");
        itemOn.setImage(R.drawable.imagen3);

        onboardingItemList.add(itemPayOnline);
        onboardingItemList.add(itemOnTheWay);
        onboardingItemList.add(itemOn);

        onboardingAdapter = new OnboardingAdapter(onboardingItemList);


    }
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
        if(index == onboardingAdapter.getItemCount() - 1){
            buttonOnBoardingAction.setText("Start");
        }
        else{
            buttonOnBoardingAction.setText("Next");
        }

    }
    protected void onResume(){
        super.onResume();
        FirebaseUser usuario_actual=mAuth.getCurrentUser();
        if(usuario_actual!=null){
            pasar();
        }
    }
    private void pasar(){

        Intent intent =new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
