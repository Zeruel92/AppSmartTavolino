package iot.unisalento.it.appsmarttavolino;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        String stringa="Benvenuto nello SmartMuseo "+nome+": Per effettuare il login sul tavolino, poggia il telefono vicino il ricevitore NFC,";
        stringa+="Oppure clicca su Login Senza NFC e segui le istruzioni. Oppure imposta le tue preferenze";
        textView.setText(stringa);

        login =(Button) findViewById(R.id.mg_nonfc);
        login.setOnClickListener(this);
        modpref=(Button) findViewById(R.id.mg_modpref);
        modpref.setOnClickListener(this);

        //Inizializzo l'nfc
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter==null){
            Toast.makeText(this,"nfcAdapter==null, no NFC adapter exists",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Set Callback(s)",Toast.LENGTH_LONG).show();
            nfcAdapter.setNdefPushMessageCallback(this, this);
            nfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String action = intent.getAction();
      /*  if(action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)){
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage inNdefMessage = (NdefMessage)parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord NdefRecord_0 = inNdefRecords[0];
            String inMsg = new String(NdefRecord_0.getPayload());
            textInfo.setText(inMsg);
        }*/
    }
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {

        String stringOut = pref.getString("token",null);
        byte[] bytesOut = new String("Application/IoT").getBytes();
        NdefRecord ndefRecordOut= NdefRecord.createMime(stringOut,bytesOut);
        NdefMessage ndefMessageout = new NdefMessage(ndefRecordOut);
        return ndefMessageout;
    }
    @Override
    public void onNdefPushComplete(NfcEvent event) {

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
