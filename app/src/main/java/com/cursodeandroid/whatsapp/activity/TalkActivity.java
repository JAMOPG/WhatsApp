package com.cursodeandroid.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cursodeandroid.whatsapp.R;
import com.cursodeandroid.whatsapp.adapter.MessageAdapter;
import com.cursodeandroid.whatsapp.config.FirebaseConfiguration;
import com.cursodeandroid.whatsapp.helper.Base64Custom;
import com.cursodeandroid.whatsapp.helper.Preferences;
import com.cursodeandroid.whatsapp.model.Message;
import com.cursodeandroid.whatsapp.model.Talk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TalkActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbar;

    //dados do destinatario
    private String destName;
    private String destId;
    private String senderId;
    private String senderName;

    private DatabaseReference firebase;
    private EditText editMessage;
    private ImageButton btSend;
    private ListView listView;
    private ArrayList<Message> messages;
    private ArrayAdapter<Message> adapter;
    private ValueEventListener valueEventListenerMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        toolbar = findViewById(R.id.tb_talk);
        editMessage = findViewById(R.id.editmessage);
        btSend = findViewById(R.id.btsend);
        listView = findViewById(R.id.lvtalks);

        //dados do usuario logado
        Preferences preferences = new Preferences(TalkActivity.this);
        senderId = preferences.getIdent();
        senderName= preferences.getName();

        Bundle  extra = getIntent().getExtras();

        if (extra != null){

            destName = extra.getString("name");
            String destEmail = extra.getString("email");
            destId = Base64Custom.encodeBase64(destEmail);
        }
        //Configura ToolBar

        toolbar.setTitle(destName);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Monta lista de mensagens
        messages = new ArrayList<>();
        //adapter = new ArrayAdapter(TalkActivity.this, android.R.layout.simple_list_item_1, messages);
        adapter = new MessageAdapter(TalkActivity.this, messages);
        listView.setAdapter(adapter);

        //Recuperar mensagens do firebase

        firebase = FirebaseConfiguration.getFirebase().child("messages").child(senderId).child(destId);

        //Listerner para mensagens

        valueEventListenerMessage = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Limpar Mensagens
                messages.clear();

                for (DataSnapshot dados:dataSnapshot.getChildren()){
                    Message message = dados.getValue(Message.class);
                    messages.add(message);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
                    };

        firebase.addValueEventListener(valueEventListenerMessage);

        //Envia Mensagem
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textMessage = editMessage.getText().toString();

                if (textMessage.isEmpty()){
                    Toast.makeText(TalkActivity.this,"Digite uma Mensagem", Toast.LENGTH_LONG).show();
                                    }
                else{

                    Message message = new Message();
                    message.setIduser(senderId);
                    message.setMessage(textMessage);

                    Boolean returnSenderMessage = saveMesssage (senderId,destId,message);
                    if (!returnSenderMessage){
                        Toast.makeText(TalkActivity.this,"Mensagem nao foi salva para o remetente",Toast.LENGTH_LONG).show();
                    }else {
                        Boolean returnDestMessage = saveMesssage(destId, senderId, message);
                        if (!returnDestMessage){
                            Toast.makeText(TalkActivity.this,"Mensagem nao foi salva para o destinatário",Toast.LENGTH_LONG).show();
                        }

                        //salvar Conversa do remetente
                        Talk talk = new Talk();
                        talk.setUserId(destId);
                        talk.setName(destName);
                        talk.setMessage(textMessage);

                        Boolean returnSenderTalk = saveTalk(senderId,destId,talk);
                        if(!returnSenderTalk){
                            Toast.makeText(TalkActivity.this,"Conversa nao foi salva para o remetente",Toast.LENGTH_LONG).show();
                        }else{
                            talk = new Talk();
                            talk.setUserId(senderId);
                            talk.setName(senderName);
                            talk.setMessage(textMessage);
                            saveTalk(destId,senderId,talk);
                         //Salva Conversa para o destinatário
                         Boolean returnDestTalk = saveTalk(destId,senderId,talk);
                         if(!returnDestTalk){
                         Toast.makeText(TalkActivity.this,"Conversa nao foi salva para o Destinatário",Toast.LENGTH_LONG).show();}

                        }


                                               editMessage.setText("");
                    }
                }
            }
        });

    }

    private boolean saveMesssage(String senderId, String destId, Message message){
        try
        {

            firebase = FirebaseConfiguration.getFirebase().child("messages");

            firebase.child(senderId).child(destId).push().setValue(message);

            return true;
        }catch (Exception e){

            e.printStackTrace();
            return false;
        }
    }
    private boolean saveTalk(String senderId, String destId, Talk talk){
        try{
            firebase =  FirebaseConfiguration.getFirebase().child("talks");
            firebase.child(senderId).child(destId).setValue(talk);
            return true;
        }catch (Exception e ) {
            e.printStackTrace();
            return false;
        }

        }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMessage);

    }
}
