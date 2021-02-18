package com.cursodeandroid.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cursodeandroid.whatsapp.R;
import com.cursodeandroid.whatsapp.activity.TalkActivity;
import com.cursodeandroid.whatsapp.adapter.ContactAdapter;
import com.cursodeandroid.whatsapp.adapter.TalkAdapter;
import com.cursodeandroid.whatsapp.config.FirebaseConfiguration;
import com.cursodeandroid.whatsapp.helper.Base64Custom;
import com.cursodeandroid.whatsapp.helper.Preferences;
import com.cursodeandroid.whatsapp.model.Contact;
import com.cursodeandroid.whatsapp.model.Talk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TalkFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<Talk> adapter;
    private ArrayList<Talk> talks;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;

    public TalkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        talks = new ArrayList<>();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_talk, container, false);
        //contacts = new ArrayList<>();
        //contacts.add("Augusto");
        //contacts.add("Giseli");
        //contacts.add("Maria");

        listView =  view.findViewById(R.id.lv_talk);

        /*adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1,contacts
        );*/

        adapter = new TalkAdapter(getActivity(),talks);
        listView.setAdapter(adapter);
        //recuperar lista de contatos
        Preferences preferences = new Preferences(getActivity());
        String identuserlogged = preferences.getIdent();
        firebase = FirebaseConfiguration.getFirebase().child("talks").child(identuserlogged);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                talks.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Talk talk = dados.getValue(Talk.class);
                    talks.add(talk);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Talk talk = talks.get(position);
                Intent intent = new Intent(getActivity(), TalkActivity.class);
                intent.putExtra("name",talk.getName());
                String email = Base64Custom.decodeBase64(talk.getUserId());
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });
        return  view;

    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }
}



