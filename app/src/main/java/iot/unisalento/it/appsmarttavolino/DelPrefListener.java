package iot.unisalento.it.appsmarttavolino;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
/*
 *Questa Classe fornisce il listener per i pulsanti di rimozione preferenza
 * Quando un pulsante viene premuto viene inviata una richesta DELETE al server rest per rimuovere la specifica
 * preferenza associata al pulsante attraverso l'id del Genere Selezionato e aggiorna l'interfaccia delle preferenze
 */
public class DelPrefListener implements View.OnClickListener {
    private Preferenze p;
    public DelPrefListener(Preferenze p){
        this.p=p;
    }
    @Override
    public void onClick(View v) {
        String idGenere=Integer.toString(v.getId());
        try {
            new ClientHttp(p.getApplicationContext()).execute("DELETE", "Preferenze", Integer.toString(p.getIdUtente()), idGenere);
            SharedPreferences preferences=p.getSharedPreferences("appmuseo", Context.MODE_PRIVATE);
            String token=preferences.getString("token", null); //recupera il token dalla cache dello smartphone
            p.updateUi(token); //aggiorna l'interfaccia delle preferenze
        }catch(Exception e){
            Log.e("DelPrefListener",e.getMessage());
        }
    }
}
