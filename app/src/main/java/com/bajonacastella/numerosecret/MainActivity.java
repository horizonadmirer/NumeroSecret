package com.bajonacastella.numerosecret;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int numeroSecret;
    private EditText edNumUsuari;
    private boolean numeroEndevinat = false;
    private Button btnJugar;
    private NumberPicker npTemps;
    private Button btnEstablirTemps;
    private TextView txtFeedbackUsuari, txtTempsRestant;
    private static String[] feedback = {"El número secret és més petit","El número secret és més gran", "Has ENCERTAT el número secret!"};
    private Random ns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edNumUsuari = findViewById(R.id.edNumUsuari);
        btnJugar = findViewById(R.id.btnJugar);
        npTemps = findViewById(R.id.npTemps);
        npTemps.setMinValue(10); npTemps.setMaxValue(60);
        btnEstablirTemps = findViewById(R.id.btnEstablirTemps);
        txtFeedbackUsuari = findViewById(R.id.txtFeedback);
        txtTempsRestant = findViewById(R.id.txtTempsR);

        //Generem un número aleatori entre 0 i 1000 amb el Random que ens dona Java.
        ns = new Random();
        numeroSecret = ns.nextInt(1000);

        btnJugar.setEnabled(false);
        edNumUsuari.setEnabled(false);


    }

    public void botoEndevinar(View v) {
        int numUsuari = Integer.parseInt(edNumUsuari.getText().toString());
        if(numUsuari < numeroSecret) {
            //Toast.makeText(this,"El número que has introduït és més petit", Toast.LENGTH_LONG).show();
            txtFeedbackUsuari.setText(feedback[1]);
            txtFeedbackUsuari.setTextColor(Color.RED);
        }
        else if (numUsuari > numeroSecret) {
            //Toast.makeText(this,"El número que has introduït és més gran", Toast.LENGTH_LONG).show();
            txtFeedbackUsuari.setText(feedback[0]);
            txtFeedbackUsuari.setTextColor(Color.RED);
        }
        else {
            //Toast.makeText(this,"El número que has introduït és el NÚMERO SECRET!", Toast.LENGTH_LONG).show();
            txtFeedbackUsuari.setText(feedback[2]);
            txtFeedbackUsuari.setTextColor(Color.RED);
            txtTempsRestant.setTextColor(Color.rgb(0,133,119));

            numeroEndevinat = true;
            btnJugar.setEnabled(false);
            edNumUsuari.setEnabled(false);
        }

    }

    public void establirTemps (View v) {
        TascaTemps tt = new TascaTemps();
        tt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,npTemps.getValue());
    }


    class TascaTemps extends AsyncTask<Integer, Integer, Long> {

        @Override
        protected void onPreExecute() {
            btnEstablirTemps.setEnabled(false);
            npTemps.setEnabled(false);
            btnJugar.setEnabled(true);
            edNumUsuari.setEnabled(true);

        }

        @Override
        protected Long doInBackground(Integer... integers) {
            int segons = integers[0];

            for(int i = segons; i >= 0; i--) {
                SystemClock.sleep(1000);
                publishProgress(i);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int segonsRestants = values[0];
            int segonsInicials = npTemps.getValue();
            if((segonsRestants*100/segonsInicials) > 50) {
                txtTempsRestant.setTextColor(Color.rgb(0,133,119));
                txtTempsRestant.setText("Temps restant: "+ segonsRestants + "s");
            } else if ((segonsRestants*100/segonsInicials) <= 50  &&(segonsRestants*100/segonsInicials) >= 25) {
                txtTempsRestant.setText("Temps restant: "+ (segonsRestants*10) + "ms");
                txtTempsRestant.setTextColor(Color.rgb(192,123,65));
            } else {
                txtTempsRestant.setText("Temps restant: "+ (segonsRestants*100) + "cs");
                txtTempsRestant.setTextColor(Color.RED);
            }

        }

        @Override
        protected void onPostExecute(Long aLong) {
            txtFeedbackUsuari.setText("S'ha acabat el temps! Torna a jugar.");
            txtFeedbackUsuari.setTextColor(Color.RED);
            numeroSecret = ns.nextInt(1000);
            btnEstablirTemps.setEnabled(true);
            npTemps.setEnabled(true);
            btnJugar.setEnabled(false);
            edNumUsuari.setEnabled(false);
        }
    }


}
