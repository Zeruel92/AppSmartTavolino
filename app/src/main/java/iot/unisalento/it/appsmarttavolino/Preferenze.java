package iot.unisalento.it.appsmarttavolino;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Preferenze extends AppCompatActivity {

    private TextView textView;
    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferenze);
        textView=(TextView)findViewById(R.id.pref_text);
        textView1=(TextView)findViewById(R.id.pref_opere);
        SharedPreferences pref=this.getSharedPreferences("appmuseo",MODE_PRIVATE);
        String token=pref.getString("token", null);
        String testo="Carico Le opere!";
        textView.setText(testo);
        updateUi(token);
    }
    void updateUi(String token){
        String testo="";
        String opere="";
        try {
            testo = new ClientHttp(getApplicationContext()).execute("GET", "Utente", token).get();
            int idUtente = Integer.parseInt(testo.substring(testo.indexOf(" ") + 1, testo.indexOf("\n")));
            testo = new ClientHttp(getApplicationContext()).execute("GET", "Preferenze", Integer.toString(idUtente)).get();
            if(testo.equals(""))
                testo="Non hai impostato alcuna preferenza!";
            else{
                //TODO Implementare gestione delle preferenze
            }
            String tmp = new ClientHttp(getApplicationContext()).execute("GET", "Opera", "0").get();
            String[] comodo=tmp.split("\n");
            for(int i=0;i<comodo.length;i++){
                if(comodo[i].contains("Nome")){
                    comodo[i]=comodo[i].substring(comodo[i].indexOf("Nome")+5);
                    opere+=comodo[i]+"\n";
                }
            }
        } catch (Exception e) {
            testo="Problemi di rete";
            Log.e("Preferenze",e.getMessage());
        }
        textView.setText(testo);
        textView1.setText(opere);
        //TODO Aggiungere controlli per aggiungere preferenze
    }
}
