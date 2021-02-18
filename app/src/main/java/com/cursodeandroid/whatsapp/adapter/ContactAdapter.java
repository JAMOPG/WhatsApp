package com.cursodeandroid.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cursodeandroid.whatsapp.R;
import com.cursodeandroid.whatsapp.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {


    private ArrayList<Contact> contacts;
    private Context context;
    public ContactAdapter(@NonNull Context c,  @NonNull ArrayList<Contact> objects) {
        super(c, 0, objects);
        this.contacts = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (contacts != null){
            //inicializa o objeto
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //monta a lista
            view = inflater.inflate(R.layout.listcontact, parent, false);

            //recupera lista
            TextView contactName = view.findViewById(R.id.tvnome);
            TextView contactEmail = view.findViewById(R.id.tvemail);
            Contact contact = contacts.get(position);
            contactName.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
        }
        return view;
    }
}
