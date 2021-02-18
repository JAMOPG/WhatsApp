package com.cursodeandroid.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cursodeandroid.whatsapp.R;
import com.cursodeandroid.whatsapp.config.FirebaseConfiguration;
import com.cursodeandroid.whatsapp.helper.Base64Custom;
import com.cursodeandroid.whatsapp.helper.Preferences;
import com.cursodeandroid.whatsapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private Button register;
    private User user;
    private FirebaseAuth authentication;



        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        name = findViewById(R.id.nameid);
        email = findViewById(R.id.emailid);
        password= findViewById(R.id.passwordid);
        register = findViewById(R.id.btregister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = new User();
                user.setName(name.getText().toString() );
                user.setPassword(password.getText().toString() );
                user.setEmail(email.getText().toString() );

                registerUser();


            }
        });

    }


    private void registerUser () {

    authentication = FirebaseConfiguration.getAuthentication();
    authentication.createUserWithEmailAndPassword(
            user.getEmail(),
            user.getPassword()
        ).addOnCompleteListener(RegisterUserActivity.this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if ( task.isSuccessful()){
                String loggeduserident = Base64Custom.encodeBase64(user.getEmail());
                Toast.makeText(RegisterUserActivity.this, "Sucessful",Toast.LENGTH_LONG).show();

                FirebaseUser userFirebase = task.getResult().getUser();

                String identuser = Base64Custom.encodeBase64(user.getEmail());

                user.setId(loggeduserident);
                user.save();



                Preferences preferences = new Preferences(RegisterUserActivity.this);
                preferences.salvarDados(loggeduserident,user.getName());
                openUserLogin();
                //authentication.signOut();
                finish();

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
                Toast.makeText(RegisterUserActivity.this, "Fail:" + erroExcessao,Toast.LENGTH_LONG).show();

            }
        }
    });

    }

    public void openUserLogin(){
        Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }


}
