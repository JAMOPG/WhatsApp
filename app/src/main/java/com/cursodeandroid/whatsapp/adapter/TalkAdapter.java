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
import com.cursodeandroid.whatsapp.model.Talk;

import java.util.ArrayList;
import java.util.List;

public class TalkAdapter extends ArrayAdapter<Talk> {


    private ArrayList<Talk> talks;
    private Context context;
    public TalkAdapter(@NonNull Context c,  @NonNull ArrayList<Talk> objects) {
        super(c, 0, objects);
        this.context = c;
        this.talks = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (talks != null){
            //inicializa o objeto
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //monta a lista
            view = inflater.inflate(R.layout.listalk, parent, false);

            //recupera lista
            TextView talkName = view.findViewById(R.id.tvname);
            TextView talkMessage = view.findViewById(R.id.tvmessage);
            Talk talk = talks.get(position);
            talkName.setText(talk.getName());
            talkMessage.setText(talk.getMessage());
        }
        return view;
    }
}
