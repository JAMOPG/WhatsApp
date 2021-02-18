package com.cursodeandroid.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cursodeandroid.whatsapp.R;
import com.cursodeandroid.whatsapp.config.FirebaseConfiguration;
import com.cursodeandroid.whatsapp.helper.Base64Custom;
import com.cursodeandroid.whatsapp.helper.Permissao;
import com.cursodeandroid.whatsapp.helper.Preferences;
import com.cursodeandroid.whatsapp.model.User;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

  private EditText email;
  private EditText password;
  private Button btlogin;
  private User user;
  private FirebaseAuth authentication;
  private ValueEventListener valueEventListenerUser;
  private DatabaseReference firebase;
  private String loggeduserident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verifyUserlogged();

        email = findViewById(R.id.emailid);
        password = findViewById(R.id.passwordid);
        btlogin = findViewById(R.id.btlogin);

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               user = new User();
               user.setEmail(email.getText().toString());
               user.setPassword(password.getText().toString());
               validateLogin();

            }
        });


    }

    public void verifyUserlogged () {
        authentication = FirebaseConfiguration.getAuthentication();

        if (authentication.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }else{

        }

    }
    public void validateLogin(){

    authentication = FirebaseConfiguration.getAuthentication();
    authentication.signInWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

            if (task.isSuccessful()){


                loggeduserident = Base64Custom.encodeBase64(user.getEmail());
                firebase = FirebaseConfiguration.getFirebase().child("users ").child(loggeduserident);

                valueEventListenerUser = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        User recovereduser =  dataSnapshot.getValue(User.class);
                        Preferences preferences = new Preferences(LoginActivity.this);
                        preferences.salvarDados(loggeduserident,recovereduser.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                firebase.addListenerForSingleValueEvent(valueEventListenerUser);




                abrirTelaPrincipal();
                Toast.makeText(LoginActivity.this, "Sucessful",Toast.LENGTH_LONG).show();
            }else
            {
                String erroExcessao = "";
                try {

                    throw task.getException();
                }catch (FirebaseAuthWeakPasswordException e){
                    erroExcessao = "Digite uma senha com os requisitos mínimos";
                }
                catch (FirebaseAuthInvalidCredentialsException e){
                    erroExcessao = "Digite um email Válido";
                }
                catch (FirebaseAuthUserCollisionException e){
                    erroExcessao = "Usuario ja Existe";
                }
                catch (Exception e){
                    erroExcessao = "Erro genérico";
                    e.printStackTrace();
                }
                Toast.makeText(LoginActivity.this, "Fail:" + erroExcessao,Toast.LENGTH_LONG).show();
            }
        }
    });

    }

    public void abrirTelaPrincipal(){

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

            }


    public void abrirCadastroUsuario (View view)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);

        startActivity(intent);
    }
}
