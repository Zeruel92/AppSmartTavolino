package iot.unisalento.it.appsmarttavolino;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
/*
 *Questa Classe fornisce il listener per i pulsanti di aggiunta preferenza
 * Quando un pulsante viene premuto viene inviata una richesta POST al server rest per aggiungere la specifica
 * preferenza associata al pulsante attraverso l'id del Genere Selezionato e aggiorna l'interfaccia delle preferenze
 */
public class AddPrefListener implements View.OnClickListener {
    private Preferenze p;
    public AddPrefListener(Preferenze p){
        this.p=p;
    }
    @Override
    public void onClick(View v) {
        String idGenere=Integer.toString(v.getId());
        try {
            new ClientHttp(p.getApplicationContext()).execute("POST", "Preferenze", Integer.toString(p.getIdUtente()), idGenere);
            SharedPreferences preferences=p.getSharedPreferences("appmuseo", Context.MODE_PRIVATE);
            String token=preferences.getString("token",null); //recupera il token dalla cache dello smartphone
            p.updateUi(token); //aggiorna l'interfaccia delle preferenze
        }catch(Exception e){
            Log.e("AddPrefListener",e.getMessage());
        }
    }
}
