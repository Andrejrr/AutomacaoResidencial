package com.lspsoftware.automacaoresidencial;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Inicio extends AppCompatActivity {
    boolean back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        BuscarServicoLocal buscarServicoFragment = BuscarServicoLocal.newInstance();
        openFrag(buscarServicoFragment);
    }

    public void openFrag(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frmFragmentContainer, fragment);
        if(back)
            transaction.addToBackStack(null);
        back = true;
        transaction.commit();
    }
}
