package com.cursodeandroid.whatsapp.helper;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferences {

    private Context contexto ;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "whatapp.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    public final String KEY_IDENT = "identlogged";
    public final String KEY_NAME = "nameUser";

    public Preferences (Context contextoParametro) {

        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE );
        editor = preferences.edit();

    }

    public void salvarDados(String userIdent, String nameUser){
       editor.putString(KEY_IDENT, userIdent);
       editor.putString(KEY_NAME, nameUser);
       editor.commit();
    }



    public String getIdent (){
        return preferences.getString(KEY_IDENT, null);
    }

    public String getName (){
        return preferences.getString(KEY_NAME, null);
    }
}
