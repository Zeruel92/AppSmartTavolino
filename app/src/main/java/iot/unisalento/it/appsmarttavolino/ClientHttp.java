package iot.unisalento.it.appsmarttavolino;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;

public class ClientHttp extends AsyncTask<String,Integer,String> {
    private Context context;
    private final String host="ec2-52-17-122-110.eu-west-1.compute.amazonaws.com";
    public ClientHttp (Context c){
        this.context=c;
    }
    @Override
    protected String doInBackground(String... params) {
        String result="";
        String request=params[0];
        String tabella=params[1];
        if(request.equals("POST")){
            if(tabella.equals("Utente")) {
                String nome = params[2];
                String cognome = params[3];
                String email = params[4];
                String password = params[5];
                String token = params[6];
                postProcess(tabella, nome, cognome, email, password, token);
            }
            else if(request.equals("GET")){
                String id;
                if(params[2]!=null)
                    id=params[2];
                else id="0";
                getProcess(tabella,id);
            }
        }
        return result;
    }
    private void postProcess(String tab,String nome,String cognome,String email,String password,String token){
        try {
            URL url = new URL("http://"+this.host+"/index.php/" + tab);

            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("idUtente","null");
            builder.appendQueryParameter("Nome", nome);
            builder.appendQueryParameter("Cognome", cognome);
            builder.appendQueryParameter("email", email);
            builder.appendQueryParameter("password", password);
            builder.appendQueryParameter("token", token);
            String query = builder.build().getEncodedQuery();

            OutputStream out = new BufferedOutputStream(client.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

            writer.write(query);
            writer.flush();

            InputStream in = new BufferedInputStream(client.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            StringBuffer sb = new StringBuffer();
            String inputLine = "";
            while ((inputLine = reader.readLine()) != null) {
                sb.append(inputLine);
            }
            String prova = sb.toString();
            client.disconnect();
            SharedPreferences preferences=context.getSharedPreferences("appmuseo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= preferences.edit();
            editor.putString("nome",nome);
            editor.putString("cognome",cognome);
            editor.putString("email",email);
            editor.putString("password",password);
            editor.putString("token",token);
            editor.commit();
        }catch(Exception e){
            //do nothing
            Toast.makeText(this.context, "Errore di rete Riprovare più tardi", Toast.LENGTH_SHORT).show();
        }
    }
    private String getProcess(String tabella,String id){
        String result="Problemi di connessione";
            try {
                String sUrl="http://"+this.host+"/index.php/" + tabella+"/"+id;

                Log.i("GET",sUrl);
            URL url = new URL(sUrl);
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(client.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String s = null;
            StringBuffer sb = new StringBuffer();
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
                String arrayjson=sb.toString();
                Log.i("GET",arrayjson);
            JSONArray array = new JSONArray(arrayjson);
            result="";
            for(int i=0;i<array.length();i++) {
                JSONObject json = array.getJSONObject(i);
                Iterator<String> iter = json.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    String value = json.getString(key);
                    result+=key+" "+value+"\n";
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
