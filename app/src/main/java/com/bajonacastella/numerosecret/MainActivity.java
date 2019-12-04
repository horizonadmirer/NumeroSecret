package com.bajonacastella.numerosecret;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int numeroSecret;
    private EditText edNumUsuari;
    private boolean numeroEndevinat = false;
    private Button btnJugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edNumUsuari = findViewById(R.id.edNumUsuari);
        btnJugar = findViewById(R.id.btnJugar);

        //Generem un número aleatori entre 0 i 1000 amb el Random que ens dona Java.
        Random ns = new Random();
        numeroSecret = ns.nextInt(1000);


    }

    public void botoEndevinar(View v) {
        int numUsuari = Integer.parseInt(edNumUsuari.getText().toString());
        if(numUsuari < numeroSecret) {
            Toast.makeText(this,"El número que has introduït és més petit", Toast.LENGTH_LONG).show();
        }
        else if (numUsuari > numeroSecret) {
            Toast.makeText(this,"El número que has introduït és més gran", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"El número que has introduït és el NÚMERO SECRET!", Toast.LENGTH_LONG).show();
            numeroEndevinat = true;

            btnJugar.setEnabled(false);
            edNumUsuari.setEnabled(false);
        }

    }
}
