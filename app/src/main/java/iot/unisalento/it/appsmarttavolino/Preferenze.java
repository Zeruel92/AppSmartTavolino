package iot.unisalento.it.appsmarttavolino;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Vector;
/*
 *Classe di gestione delle preferenze dell'utente
 */
public class Preferenze extends AppCompatActivity{

    private TextView textView;
    private TextView textView1;
    private LinearLayout ll;
    private int idUtente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_layout);
        textView=new TextView(getApplicationContext());
        textView.setTextColor(Color.BLACK);
        textView1=new TextView(getApplicationContext());
        textView.setTextSize(30);
        textView1.setTextColor(Color.BLACK);
        ll=(LinearLayout) findViewById(R.id.pref_layout);
        textView.setText("Le tue preferenze");
        SharedPreferences pref=this.getSharedPreferences("appmuseo", MODE_PRIVATE);
        String token=pref.getString("token", null);
        updateUi(token);
    }
    //Metodo che rigenera la schermata delle preferenze
    void updateUi(String token){
        ll.removeAllViews();
        ll.addView(textView);
        String testo="";
        String opere="";
        idUtente =-1;
        //richiesta GET per la lista delle preferenze dell'utente
        try {
            testo = new ClientHttp(getApplicationContext()).execute("GET", "Utente", token).get();
            String tmp1[]=testo.split("\n");
            for(int i =0;i<tmp1.length;i++) {
                if(tmp1[i].contains("idUtente"))
                     idUtente = Integer.parseInt(tmp1[i].substring(tmp1[i].indexOf(" ") + 1));
            }
            testo = new ClientHttp(getApplicationContext()).execute("GET", "Preferenze", Integer.toString(idUtente)).get();
            //Se il server risponde con una stringa vuota non ci sono preferenze e viene visualizzato il messaggio
            // Nessuna preferenza
            if(testo.equals("")) {
                TextView nopref = new TextView(getApplicationContext());
                ll.addView(nopref);
                nopref.setTextColor(Color.DKGRAY);
                nopref.setText("Non hai inserito nesssuna preferenza!");
            }
            else{
                //Genero i pulsanti con i nomi delle preferenze associando l'id del genere al pulsante
                //e imposto come listener il DelPrefLisnter per rimuove le prefenze già inserite
                String comodo[]=testo.split("\n");
                Vector<Integer> ids=new Vector<Integer>();
                Vector<String> names=new Vector<String>();
                for(int i=0;i<comodo.length;i++){
                    if(comodo[i].contains("idGenere")){
                        comodo[i]=comodo[i].substring(comodo[i].indexOf(" ")+1);
                        ids.addElement(Integer.parseInt(comodo[i]));
                    }
                    else if(comodo[i].contains("NomeGenere")){
                        comodo[i]=comodo[i].substring(comodo[i].indexOf(" ")+1);
                        names.addElement(comodo[i]);
                    }
                }
                for(int i=0;i<ids.size();i++){
                    Button buttone=new Button(getApplicationContext());
                    buttone.setOnClickListener(new DelPrefListener(this));
                    ll.addView(buttone);
                    buttone.setText(names.elementAt(i));
                    buttone.setId(ids.elementAt(i));
                }
            }
            ll.addView(textView1);
            textView1.setText("Opere presenti nel Museo");
            textView1.setTextSize(30);
            //Effettuo la richiesta GET per scaricare la lista di tutti i generi
            String tmp = new ClientHttp(getApplicationContext()).execute("GET", "Genere", "0").get();
            String[] comodo=tmp.split("\n");
            Vector<Integer> ids=new Vector<Integer>();
            Vector<String> names=new Vector<String>();
            //Genero i pulsanti con i nomi delle preferenze associando l'id del genere al pulsante
            //e imposto come listener l'AddPrefLisnter per aggiungere nuove preferenze
            for(int i=0;i<comodo.length;i++){
                if(comodo[i].contains("idGenere")){
                    comodo[i]=comodo[i].substring(comodo[i].indexOf(" ")+1);
                    ids.addElement(Integer.parseInt(comodo[i]));
                }
                else if(comodo[i].contains("Nome")){
                    comodo[i]=comodo[i].substring(comodo[i].indexOf(" ")+1);
                    names.addElement(comodo[i]);
                }
            }
            for(int i=0;i<ids.size();i++){
                Button buttone=new Button(getApplicationContext());
                buttone.setOnClickListener(new AddPrefListener(this));
                ll.addView(buttone);
                buttone.setText(names.elementAt(i));
                buttone.setId(ids.elementAt(i));
            }
        } catch (Exception e) {
            Log.e("Preferenze",e.getMessage());
        }
    }
    //Metodo usato dai listener per ottenere l'idUtente
    public int getIdUtente(){
        return idUtente;
    }
}
