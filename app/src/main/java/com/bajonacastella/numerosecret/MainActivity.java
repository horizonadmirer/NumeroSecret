package com.bajonacastella.numerosecret;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Random;

/**
 * MAIN ACTIVITY - Classe principal de l'aplicació Número Secret.
 * Programat per Marc Bajona i Ester Castellà.
 * M09-UF2 - Programació multithread en Android
 */
public class MainActivity extends AppCompatActivity {

    // Declaració de tots els elements gràfics que utilitzarem i modificarem i també altres variables.
    private int numeroSecret;
    private EditText edNumUsuari;
    private Button btnJugar;
    private NumberPicker npTemps;
    private Button btnEstablirTemps;
    private TextView txtFeedbackUsuari, txtTempsRestant;
    private static String[] feedback = {"El número secret és més petit","El número secret és més gran", "Has ENCERTAT el número secret!"};
    private Random ns;
    TascaTemps tt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referenciem les variables de cada component gràfic.
        edNumUsuari = findViewById(R.id.edNumUsuari);
        btnJugar = findViewById(R.id.btnJugar);
        npTemps = findViewById(R.id.npTemps);
        npTemps.setMinValue(10); npTemps.setMaxValue(60); // Posem el NumberPicker entre 10 i 60.
        btnEstablirTemps = findViewById(R.id.btnEstablirTemps);
        txtFeedbackUsuari = findViewById(R.id.txtFeedback);
        txtTempsRestant = findViewById(R.id.txtTempsR);

        // Generem un número aleatori entre 0 i 1000 amb el Random que ens dona Java.
        ns = new Random();
        numeroSecret = ns.nextInt(1000);
        Log.e("NÚMERO SECRET", ""+numeroSecret); // Ensenyem per consola com si fos un error, el número secret per fer proves amb l'app.

        // Deshabilitem el botó de jugar i el edittext.
        btnJugar.setEnabled(false);
        edNumUsuari.setEnabled(false);
    }

    /**
     * Mètode que s'execurarà cada cop que l'usuari vulgui provar sort en endivinar el número secret.
     * Si l'usuari no introdueïx cap número, es posarà un 0 automàticament.
     * @param v
     */
    public void botoEndevinar(View v) {

        if(edNumUsuari.getText().toString().isEmpty()) {
            edNumUsuari.setText("0");
        }

        int numUsuari = Integer.parseInt(edNumUsuari.getText().toString());

        // Si el número secret és més gran - L'usuari no guanya, ha de seguir jugant.
        if(numUsuari < numeroSecret) {
            //Toast.makeText(this,"El número que has introduït és més petit", Toast.LENGTH_LONG).show();
            txtFeedbackUsuari.setText(feedback[1]);
            txtFeedbackUsuari.setTextColor(Color.RED);
        }
        // Si el número secret és més petit - L'usuari no guanya, ha de seguir jugant.
        else if (numUsuari > numeroSecret) {
            //Toast.makeText(this,"El número que has introduït és més gran", Toast.LENGTH_LONG).show();
            txtFeedbackUsuari.setText(feedback[0]);
            txtFeedbackUsuari.setTextColor(Color.RED);
        }
        // Si el número secret és correcte - L'usuari guanya, el joc s'ha acabat.
        else {
            //Toast.makeText(this,"El número que has introduït és el NÚMERO SECRET!", Toast.LENGTH_LONG).show();
            txtFeedbackUsuari.setText(feedback[2]);
            txtFeedbackUsuari.setTextColor(Color.rgb(0,133,119));

            // Deshabilitem els botons i edittexts.
            btnJugar.setEnabled(false);
            edNumUsuari.setEnabled(false);
            txtTempsRestant.setEnabled(false);

            // Parem el thread i deixem el temps restant que ens quedava visible.
            tt.cancel(true);

            // Tornem a habilitar el botó per tornar a establir el temps per jugar.
            npTemps.setEnabled(true);
            btnEstablirTemps.setEnabled(true);

            // Generem un número aleatori entre 0 i 1000 amb el Random que ens dona Java.
            numeroSecret = ns.nextInt(1000);
            Log.e("NÚMERO SECRET", ""+numeroSecret);
        }

    }

    /**
     * Mètode per el botó d'Establir Temps que farà que el joc s'iniciï
     * i també engegarà un thread per el compte enrere corresponent.
     * @param v
     */
    public void establirTemps (View v) {
        tt = new TascaTemps();
        // Passarem com a paràmetre els segons que l'usuari ha introduït en el NumberPicker.
        tt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,npTemps.getValue());
    }


    /**
     * Classe interna per a la gestió d'un thread independent per al càlcul del compte enrera del joc.
     */
    class TascaTemps extends AsyncTask<Integer, Integer, Long> {

        @Override
        protected void onPreExecute() {
            // Deshabilitem el NumberPicker i el botó d'establir temps i habilitem el edittext i el botó per jugar.
            btnEstablirTemps.setEnabled(false);
            npTemps.setEnabled(false);
            btnJugar.setEnabled(true);
            edNumUsuari.setEnabled(true);

        }

        /* VERSIÓ PER FER MÉS PRECÍS EL COMPTE ENRERE, EN SEGONS, DÈCIMES i CENTÈSIMES DE SEGON.
            ÉS MÉS INTERESSANT, PERÒ DESPRÉS PER CONTROLAR ELS COLORS DEL TEXTVIEW DEL COMPTADOR
            NO TROBEM LA MANERA DE SABER SI ÉS MENYS DEL 50% o 25% DEL TEMPS, AL HAVER LES MULTIPLICACIONS
            DE 10 i 100 (per dècimes i centèsimes).
        @Override
        protected Long doInBackground(Integer... integers) {
            int segons = integers[0];
            int seg50, seg25;
            seg50 = segons / 2;
            seg25 = segons / 4;
            boolean normal = true;
            boolean mig = false, quart = false;
            if (normal) {
                for (int i = segons; i >= seg50; i--) {
                    SystemClock.sleep(1000);
                    publishProgress(i);
                }
                normal = false;
                mig = true;
            }
            if (mig) {
                for (int i = seg50*10; i >= seg25*10; i--) {
                    SystemClock.sleep(100);
                    publishProgress(i);
                }
                normal = false;
                mig = false;
                quart = true;
            }
            if (quart) {
                for (int i = seg25*100; i >= 0; i = i - 10) {
                    SystemClock.sleep(100);
                    publishProgress(i);
                }
                normal = false;
                mig = false;
                quart = false;
            }

            return null;
        }
        */

        @Override
        protected Long doInBackground(Integer... integers) {
            int segons = integers[0];
            for (int i = segons; i >= 0; i--) {
                    SystemClock.sleep(1000);
                    publishProgress(i); // Anem enviant cada segon, els segons restants en el compte enrere.
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int segonsRestants = values[0];
            int segonsInicials = npTemps.getValue();

            // Si els segons que queden són més d'un 50% del temps inicial,
            // ho mostrarem en segons i en color verd.
            if((segonsRestants*100/segonsInicials) > 50) {
                txtTempsRestant.setTextColor(Color.rgb(0,133,119));
                txtTempsRestant.setText("Temps restant: "+ segonsRestants + "s");
            }
            // Si els segons que queden són més d'un 50% del temps inicial,
            // ho mostrarem en segons i en color verd.
            else if ((segonsRestants*100/segonsInicials) <= 50  &&(segonsRestants*100/segonsInicials) >= 25) {
                txtTempsRestant.setText("Temps restant: "+ (segonsRestants*10) + "ds");
                txtTempsRestant.setTextColor(Color.rgb(192,123,65));
            }
            // Si els segons que queden són menys d'un 25% del temps inicial,
            // ho mostrarem en centèsimes de segon i en color vermell.
            else {
                txtTempsRestant.setText("Temps restant: "+ (segonsRestants*100) + "cs");
                txtTempsRestant.setTextColor(Color.RED);
            }
        }

        @Override
        protected void onPostExecute(Long aLong) {
            /*
             Si s'ha acabat el temps ho indicarem al usuari,
             deshabilitarem el edittext i el botó d'endivinar i tornarem
             a habilitar el NumberPicker per tornar a jugar.
             Això si, per endivinar un número secret nou.
             */
            txtFeedbackUsuari.setText("S'ha acabat el temps! Torna a jugar.");
            txtFeedbackUsuari.setTextColor(Color.RED);
            numeroSecret = ns.nextInt(1000);
            Log.e("NÚMERO SECRET",""+ numeroSecret);
            btnEstablirTemps.setEnabled(true);
            npTemps.setEnabled(true);
            btnJugar.setEnabled(false);
            edNumUsuari.setEnabled(false);
        }
    }
}
