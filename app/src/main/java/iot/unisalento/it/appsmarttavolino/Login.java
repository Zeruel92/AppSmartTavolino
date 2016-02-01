package iot.unisalento.it.appsmarttavolino;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.l_email);
        email.setHint("E-Mail");
        password = (EditText) findViewById(R.id.l_password);
        password.setHint("Password");
        login = (Button) findViewById(R.id.l_button);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String s_email = email.getText().toString();
        String s_password = password.getText().toString();
        ClientHttp clientHttp = new ClientHttp(this);
        clientHttp.execute("GET", "Login", s_email, s_password);
        String tmp= "";
        try {
            tmp = clientHttp.get();
        } catch (Exception e) {
            Log.e("Login", e.getMessage());

        }
        if((!tmp.equals(""))&&(!tmp.equals("Problema di Rete!"))){
            String tmp_array[]=tmp.split("\n");
            String nome,cognome,token;
            nome="";
            cognome="";
            token="";
            for(int i=0;i<tmp_array.length;i++){
                if(tmp_array[i].contains("Nome")){
                    nome=tmp_array[i].substring(tmp_array[i].indexOf(" ")+1);
                }
                else if(tmp_array[i].contains("Cognome")){
                    cognome=tmp_array[i].substring(tmp_array[i].indexOf(" ")+1);
                }
                else if(tmp_array[i].contains("token")){
                    token=tmp_array[i].substring(tmp_array[i].indexOf(" ")+1);
                }
            }
            SharedPreferences preferences=this.getSharedPreferences("appmuseo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= preferences.edit();
            editor.putString("nome",nome);
            editor.putString("cognome",cognome);
            editor.putString("email",s_email);
            editor.putString("password",s_password);
            editor.putString("token", token);
            editor.commit();
            Intent intent=new Intent(Login.this,MenuGenerale.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this.getApplicationContext(),"I dati inseriti non sono corretti,riprovare!",Toast.LENGTH_LONG).show();
        }
    }

}
