package com.cursodeandroid.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class FirebaseConfiguration {

    private static DatabaseReference referenceFirebase ;
    private static FirebaseAuth authentication ;


    public static DatabaseReference getFirebase() {

    if (referenceFirebase == null) {
        referenceFirebase = FirebaseDatabase.getInstance().getReference();

        }

    return referenceFirebase;

    }


    public static FirebaseAuth getAuthentication(){
        if (authentication == null){
            authentication = FirebaseAuth.getInstance();
        }

        return authentication;
}


}