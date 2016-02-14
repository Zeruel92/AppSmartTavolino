package iot.unisalento.it.appsmarttavolino;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
 *Questa classe implementa l'interfaccia Generale dell'app dopo aver effettuato Login/Registrazione
 * Inoltre fornisce l'implementazione dell'NFC p2p
 */
public class MenuGenerale extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback, View.OnClickListener {

    private TextView textView;
    private SharedPreferences pref;
    private NfcAdapter nfcAdapter;
    private Button login;
    private Button modpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_generale);
        textView=(TextView) findViewById(R.id.mg_textView1);
        pref=this.getSharedPreferences("appmuseo", Context.MODE_PRIVATE);
        String nome= pref.getString("nome",null);
        String stringa="Benvenuto nello Smart Museum "+nome+": Per effettuare il login, avvicina lo smartphone al ricevitore NFC,";
        stringa+="oppure clicca su Login Senza NFC e segui le istruzioni.";
        textView.setText(stringa);

        login =(Button) findViewById(R.id.mg_nonfc);
        login.setOnClickListener(this);
        modpref=(Button) findViewById(R.id.mg_modpref);
        modpref.setOnClickListener(this);

        //Inizializzo l'nfc
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(!nfcAdapter.isEnabled()){
            Toast.makeText(this,"Nfc spento,si prega di attivarlo",Toast.LENGTH_LONG).show();
        }
        if(nfcAdapter==null){
            Toast.makeText(this,"Questo dispositivo non supporta la tecnologia NFC",Toast.LENGTH_LONG).show();
        }else{
            nfcAdapter.setNdefPushMessageCallback(this, this);//Setta la callback al listener (createNdefMessage())
            nfcAdapter.setOnNdefPushCompleteCallback(this, this);//setta la callback al listener( onNdefPushComplete())
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {//Crea il messaggio NDEF, viene invocato quando il telefono viene avvicinato al lettore
        String stringOut = pref.getString("token",null);
        String bytesOut = "Application/IoT";
        NdefRecord ndefRecordOut= NdefRecord.createMime(bytesOut,stringOut.getBytes());
        NdefMessage ndefMessageout = new NdefMessage(ndefRecordOut);
        return ndefMessageout;
    }
    @Override
    public void onNdefPushComplete(NfcEvent event) {//Mostra una notifica se lo scambio è avvenuto correttamente

        final String eventString = "onNdefPushComplete\n" + event.toString();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {Toast.makeText(getApplicationContext(), eventString,Toast.LENGTH_LONG).show();    }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.equals(findViewById(R.id.mg_nonfc))){
            Intent intent=new Intent(MenuGenerale.this,NoNfcLogin.class);
            startActivity(intent);
        }
        else{
            Intent intent=new Intent(MenuGenerale.this,Preferenze.class);
            startActivity(intent);
        }
    }
}
