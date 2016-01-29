package iot.unisalento.it.appsmarttavolino;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

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
            String token=preferences.getString("token", null);
            p.updateUi(token);
        }catch(Exception e){
            Log.e("DelPrefListener",e.getMessage());
        }
    }
}
