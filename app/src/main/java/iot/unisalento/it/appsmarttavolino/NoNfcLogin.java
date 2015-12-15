package iot.unisalento.it.appsmarttavolino;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NoNfcLogin extends AppCompatActivity {
    private SharedPreferences pref;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_nfc_login);
        pref=this.getSharedPreferences("appmuseo", Context.MODE_PRIVATE);
        String token=pref.getString("token",null);
        textView=(TextView)findViewById(R.id.nonfc_token);
        textView.setText(token);
    }
}
