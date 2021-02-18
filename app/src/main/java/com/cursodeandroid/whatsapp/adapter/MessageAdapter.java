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
import com.cursodeandroid.whatsapp.helper.Preferences;
import com.cursodeandroid.whatsapp.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private Context context;
    private ArrayList<Message> messages;

    public MessageAdapter(@NonNull Context c, ArrayList<Message> objects) {
        super(c, 0, objects);
    this.context = c;
    this.messages = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        //Verifica Mensagens
        if (messages != null){

            //Recupera Identificador
            Preferences preferences = new Preferences(context);
            String senderId = preferences.getIdent();


            //Inicializao  oObjeto montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Recupera Mensagem
            Message message = messages.get(position);


            if (senderId.equals(message.getIduser())) {


                //montaegem
                view = inflater.inflate(R.layout.right_item_message, parent, false);
            }
            else{
                view = inflater.inflate(R.layout.left_item_message, parent, false);
            }
            //Recupera Elementos para Mensagem

            TextView textMessage = (TextView) view.findViewById(R.id.tvmessages);
            textMessage.setText(message.getMessage());


        }




        return view;

    }
}
