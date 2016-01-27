package iot.unisalento.it.appsmarttavolino;

import android.view.View;
import android.widget.Toast;

public class DelPrefListener implements View.OnClickListener {
    private Preferenze p;
    public DelPrefListener(Preferenze p){
        this.p=p;
    }
    @Override
    public void onClick(View v) {
        Toast.makeText(p.getApplicationContext(), "Bottene Rimuovi premuto", Toast.LENGTH_LONG).show();
    }
}
