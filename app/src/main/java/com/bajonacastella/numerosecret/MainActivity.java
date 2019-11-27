package com.bajonacastella.numerosecret;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int numeroSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Generem un número aleatori entre 0 i 1000 amb el Random que ens dona Java.
        Random ns = new Random();
        numeroSecret = ns.nextInt(1000);


    }

    public void botoEndevinar(View v) {
        Toast.makeText(this,"El número és: " + numeroSecret, Toast.LENGTH_LONG).show();
    }
}
