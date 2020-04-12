package com.example.prueba.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.Adaptadores.UserAdapter;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsDisplayFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Usuario> usuarioList;
    public FriendsDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));

        usuarioList = new ArrayList<>();
        for (int i = 0 ; i<20 ; i++){
            usuarioList.add(new Usuario("usuario "+i,"",""));
        }

        userAdapter = new UserAdapter(getContext(),usuarioList);


        recyclerView.setAdapter(userAdapter);

        return view;
    }

}
