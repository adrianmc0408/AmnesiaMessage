package com.example.prueba.Adaptadores;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.prueba.Fragments.*;

//La clase heredaa de FragementPagerAdapter
public class PagerAdapter extends FragmentPagerAdapter {

    //Declaracion de variables
    private int tabsNumber;

    //Contructor del pageAdapter
    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs) {
        super(fm, behavior);
        this.tabsNumber=tabs;
    }

    //Este método abstracto controla el click sobre el tab, estableciendo un fragment u otro en funcion de nuestra pulsacion
    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new ChatsDisplayFragment();
            case 1:
                return new FriendsDisplayFragment();
            case 2:
                return new OtrosFragment();
            default:
                return null;
        }

    }
    //Metodo abstracto que devuelve el numero de tabs del pagerAdapters
    @Override
    public int getCount() {
        return tabsNumber;
    }
}
