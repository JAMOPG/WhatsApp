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
import com.cursodeandroid.whatsapp.activity.MainActivity;
import com.cursodeandroid.whatsapp.activity.TalkActivity;
import com.cursodeandroid.whatsapp.adapter.ContactAdapter;
import com.cursodeandroid.whatsapp.config.FirebaseConfiguration;
import com.cursodeandroid.whatsapp.helper.Preferences;
import com.cursodeandroid.whatsapp.model.Contact;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contact> contacts;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;

    public ContactsFragment() {
        // Required empty public constructor
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contacts = new ArrayList<>();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        //contacts = new ArrayList<>();
        //contacts.add("Augusto");
        //contacts.add("Giseli");
        //contacts.add("Maria");

        listView =  view.findViewById(R.id.lv_contact);

        /*adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1,contacts
        );*/

        adapter = new ContactAdapter(getActivity(),contacts);
        listView.setAdapter(adapter);
        //recuperar lista de contatos
        Preferences preferences = new Preferences(getActivity());
        String identuserlogged = preferences.getIdent();
        firebase = FirebaseConfiguration.getFirebase().child("contatos").child(identuserlogged);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contacts.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Contact contact = dados.getValue(Contact.class);
                    contacts.add(contact);
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
                Intent intent = new Intent(getActivity(), TalkActivity.class);
                Contact contact = contacts.get(position);

                intent.putExtra("name", contact.getName());
                intent.putExtra("email", contact.getEmail());
                startActivity(intent);
            }
        });
        return  view;

    }


}
