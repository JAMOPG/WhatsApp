package com.cursodeandroid.whatsapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes) {

        if (Build.VERSION.SDK_INT >= 23) {

            List<String> listapermissoes = new ArrayList<String>();

            for (String permissao : permissoes) {

                Boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;

                if (!validaPermissao) listapermissoes.add(permissao);
            }
                String [] novasPermissoes = new String[listapermissoes.size()];
                 listapermissoes.toArray(novasPermissoes);

if(listapermissoes.isEmpty()) return true;
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);
        }
    return true;

    }

}

