package iot.unisalento.it.appsmarttavolino;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Registrazione extends AppCompatActivity implements View.OnClickListener{

    private Button confirm;
    private EditText nome;
    private EditText cognome;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);
        nome=(EditText) findViewById(R.id.r_nome);
        cognome=(EditText) findViewById(R.id.r_cognome);
        email=(EditText) findViewById(R.id.r_email);
        password=(EditText) findViewById(R.id.r_pass);
        confirm=(Button) findViewById(R.id.r_button);
        confirm.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registrazione, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String nome = this.nome.getText().toString();
        String cognome= this.cognome.getText().toString();
        String email= this.email.getText().toString();
        String password= this.password.getText().toString();
        String token=md5(nome + cognome + email + password);
        ClientHttp c=new ClientHttp(this);
        c.execute("POST","Utente",nome,cognome,email,password,token);
        Intent intent=new Intent(Registrazione.this,MenuGenerale.class);
        startActivity(intent);
        finish();
    }

    private String md5(String arg){
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(arg.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
