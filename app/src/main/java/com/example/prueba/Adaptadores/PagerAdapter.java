package com.example.prueba.Adaptadores;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.prueba.Fragments.*;


public class PagerAdapter extends FragmentPagerAdapter {

    private int tabsNumber;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs) {
        super(fm, behavior);
        this.tabsNumber=tabs;
    }

    //Controla el click sobre el tab
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

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
