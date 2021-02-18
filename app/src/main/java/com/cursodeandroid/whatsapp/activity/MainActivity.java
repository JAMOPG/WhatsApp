package com.cursodeandroid.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cursodeandroid.whatsapp.R;
import com.cursodeandroid.whatsapp.adapter.TabAdapter;
import com.cursodeandroid.whatsapp.config.FirebaseConfiguration;
import com.cursodeandroid.whatsapp.helper.Base64Custom;
import com.cursodeandroid.whatsapp.helper.Preferences;
import com.cursodeandroid.whatsapp.helper.SlidingTabLayout;
import com.cursodeandroid.whatsapp.model.Contact;
import com.cursodeandroid.whatsapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //private Button btlogout;
    private Toolbar toolbar;
    private FirebaseAuth authentication;
    private DatabaseReference firebase;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    private String contactident;
    private DatabaseReference referenceFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        authentication = FirebaseConfiguration.getAuthentication();
        firebase = FirebaseConfiguration.getFirebase();

        slidingTabLayout = findViewById(R.id.stl_tabs);
        viewPager = findViewById(R.id.vp_page);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorAccent));

        //Configura TabAdapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter( tabAdapter);

        slidingTabLayout.setViewPager(viewPager);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        item.getItemId();

        switch (item.getItemId()){

            case R.id.sair:
                logofUser();
                return true;
            case R.id.action_settings:
                return true;
            case R.id.addperson:
                openUserRegistered();
                return true;
                default:
                    return super.onOptionsItemSelected(item);

        }


    }
    private  void openUserRegistered(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //configura DIalog
        alertDialog.setTitle("Novo Contato");
        alertDialog.setMessage("Email do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

        //Configura BOtoes
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String emailContato = editText.getText().toString();

                if (emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this,"Preencha o Campo",Toast.LENGTH_LONG).show();
                  }else {
                    contactident = Base64Custom.encodeBase64(emailContato);

                referenceFirebase = FirebaseConfiguration.getFirebase().child("users").child(contactident);

                referenceFirebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() != null){

                            User userContact = dataSnapshot.getValue(User.class);
                            Preferences preferences = new Preferences(MainActivity.this);
                            String ident = preferences.getIdent();

                            firebase = FirebaseConfiguration.getFirebase();
                            firebase = firebase.child("contatos").child(ident).child(contactident);

                            Contact contact = new Contact();
                            contact.setUserIdent(contactident);
                            contact.setName(userContact.getName());
                            contact.setEmail(userContact.getEmail());


                            firebase.setValue(contact);
                            //authentication.getCurrentUser().getEmail();


                        }else{
                            Toast.makeText(MainActivity.this,"Usuario não possui cadastro",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alertDialog.create();
        alertDialog.show();
    }


    private void logofUser(){

        authentication.signOut();
        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}


