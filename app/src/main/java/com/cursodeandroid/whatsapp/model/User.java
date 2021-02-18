package com.cursodeandroid.whatsapp.model;

import com.cursodeandroid.whatsapp.config.FirebaseConfiguration;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class User {

    private String id;
    private String name;
    private String password;
    private String email;




    public User(){


    }

    public void save(){

        DatabaseReference reference = FirebaseConfiguration.getFirebase();
        reference.child("users").child(getId()).setValue(this);
    }
    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}