package iot.unisalento.it.appsmarttavolino;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.logging.Handler;

public class Preferenze extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferenze);
        textView=(TextView)findViewById(R.id.pref_text);
       // ClientHttp clientHttp=new ClientHttp(this.getApplicationContext());
        SharedPreferences pref=this.getSharedPreferences("appmuseo",MODE_PRIVATE);
        String token=pref.getString("token", null);
        String testo="Carico Le opere!";
        textView.setText(testo);
        updateUi(token);
    }
    void updateUi(String token){
        String testo="";
        try {
            testo=new ClientHttp(getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"GET","Utente",token).get();
            int idUtente=Integer.parseInt(testo.substring(testo.indexOf(" ")+1,testo.indexOf("\n")));
            testo=new ClientHttp(getApplicationContext()).execute("GET", "Preferenze", Integer.toString(idUtente)).get();
        } catch (Exception e) {
            testo="Problemi di rete";
            Log.e("Preferenze",e.getMessage());
        }
        textView.setText(testo);
    }
}
