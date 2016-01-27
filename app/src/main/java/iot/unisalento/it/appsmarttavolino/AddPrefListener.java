package iot.unisalento.it.appsmarttavolino;

import android.view.View;
import android.widget.Toast;

public class AddPrefListener implements View.OnClickListener {
    private Preferenze p;
    public AddPrefListener(Preferenze p){
        this.p=p;
    }
    @Override
    public void onClick(View v) {
        Toast.makeText(p.getApplicationContext(), Integer.toString(v.getId()), Toast.LENGTH_SHORT).show();
    }
}
