package iot.unisalento.it.appsmarttavolino;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

public class AddPrefListener implements View.OnClickListener {
    private Preferenze p;
    public AddPrefListener(Preferenze p){
        this.p=p;
    }
    @Override
    public void onClick(View v) {
        //Toast.makeText(p.getApplicationContext(), Integer.toString(v.getId()), Toast.LENGTH_SHORT).show();
        String idOpera=Integer.toString(v.getId());
        try {
            String idAutore="";
            String tmp[] = new ClientHttp(p.getApplicationContext()).execute("GET", "Opera", idOpera).get().split("\n");
            for(int i=0;i<tmp.length;i++){
                if(tmp[i].contains("idAutore"))
                    idAutore=tmp[i].substring(tmp[i].indexOf(" ")+1);
            }
            new ClientHttp(p.getApplicationContext()).execute("POST", "Preferenze", Integer.toString(p.getIdUtente()), idAutore);
            SharedPreferences preferences=p.getSharedPreferences("appmuseo", Context.MODE_PRIVATE);
            String token=preferences.getString("token",null);
            p.updateUi(token);
        }catch(Exception e){
            Log.e("AddPrefListener",e.getMessage());
        }
    }
}
