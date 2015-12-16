package iot.unisalento.it.appsmarttavolino;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class Preferenze extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferenze);
        textView=(TextView)findViewById(R.id.pref_text);
        ClientHttp clientHttp=new ClientHttp(this);
        SharedPreferences pref=this.getSharedPreferences("appmuseo",MODE_PRIVATE);
        String token=pref.getString("token",null);
        clientHttp.execute("GET","Utente",token);
        String testo="Nessuna Opera trovata!";
        try {
            testo=clientHttp.get();
        } catch (Exception e) {
            Log.e("Preferenze","Problemi di rete");
        }
        textView.setText(testo);
    }
}
