package com.example.prueba.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.Adaptadores.UserAdapter;
import com.example.prueba.Adaptadores.UserChatDisplayAdapter;
import com.example.prueba.ChatRoom;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsDisplayFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserChatDisplayAdapter userChatDisplayAdapter;
    private ArrayList<Usuario> usuarioList;

    public ChatsDisplayFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chatdisplay, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_chat_display);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));

        usuarioList = new ArrayList<>();
        for (int i = 0 ; i<20 ; i++){
            usuarioList.add(new Usuario("","Usuario "+i,"","",""));
        }

        userChatDisplayAdapter = new UserChatDisplayAdapter(getContext(),usuarioList);

        DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.reycler_divider));
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(userChatDisplayAdapter);

        userChatDisplayAdapter.setOnClickListener(new UserChatDisplayAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(getContext(), ChatRoom.class);
                intent.putExtra("username",usuarioList.get(position).getNombre_usuario());
                startActivity(intent);

            }
        });


        return view;

    }





}
