package iot.unisalento.it.appsmarttavolino;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class ClientHttp extends AsyncTask<String,Integer,String> {
    private final String host="ec2-52-17-122-110.eu-west-1.compute.amazonaws.com";
    private Context context;
    public ClientHttp (Context c){
        this.context=c.getApplicationContext();
    }
    @Override
    protected String doInBackground(String... params) {
        String result="";
        if(haveInternetConnection(this.context)) {
            String request = params[0];
            String tabella = params[1];
            if (request.equals("POST")) {
                if (tabella.equals("Utente")) {
                    String nome = params[2];
                    String cognome = params[3];
                    String email = params[4];
                    String password = params[5];
                    String token = params[6];
                    postProcess(tabella, nome, cognome, email, password, token);
                } else {
                    String idUtente = params[2];
                    String idAutore = params[3];
                    postProcess(tabella, idUtente,idAutore);
                }
            } else if (request.equals("GET")) {
                String id = "0";
                if (!tabella.equals("Utente")) {
                    if (params.length > 2) {
                        id = params[2];
                    }
                    result = getProcess(tabella, id);
                } else {
                    String token = params[2];
                    result = getProcess(token);
                }
            }
            publishProgress(100);
        }
        else{
            result="Rete Assente!Riprova più tardi";
        }
        return result;
    }

    private void postProcess(String tabella, String idUtente,String idAutore) {
        try {
            URL url = new URL("http://" + this.host + "/index.php/" + tabella);
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("Utente_idUtente", idUtente);
            builder.appendQueryParameter("Autore_idAutore",idAutore);
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
           // String prova = sb.toString();
            reader.close();
            in.close();
            client.disconnect();
        }catch (Exception e){
            Log.e("POST", e.getMessage());
        }
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
          //  String prova = sb.toString();
            reader.close();
            in.close();
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
            Log.e("POST",e.getMessage());
        }
    }
    private String getProcess(String tabella,String id){
        String result="Problemi di connessione";
            try {
                String sUrl="http://"+this.host+"/index.php/" + tabella+"/"+id;
                URL url = new URL(sUrl);
                HttpURLConnection client = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(client.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String s = null;
                StringBuffer sb = new StringBuffer();
                while ((s = reader.readLine()) != null) {
                    sb.append(s);
                }

                String arrayjson = sb.toString();
                if (!arrayjson.equals("null")) {
                    JSONArray array = new JSONArray(arrayjson);
                    result = "";
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        Iterator<String> iter = json.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            String value = json.getString(key);
                            result += key + " " + value + "\n";
                        }
                    }
                } else {
                    result = "";
                }
                reader.close();
                in.close();
                client.disconnect();
        }catch(Exception e){
                result="Problema di Rete!";
        }
        return result;
    }
    private String getProcess(String token){
        String result="Problemi di connessione";
        try {
            String sUrl = "http://" + this.host + "/index.php/Utente/token/" + token;
                URL url = new URL(sUrl);
                HttpURLConnection client = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(client.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String s = null;
                StringBuffer sb = new StringBuffer();
                while ((s = reader.readLine()) != null) {
                    sb.append(s);
                }
                String arrayjson = sb.toString();
                if (!arrayjson.equals("null")) {
                    JSONArray array = new JSONArray(arrayjson);
                    result = "";
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        Iterator<String> iter = json.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            String value = json.getString(key);
                            result += key + " " + value + "\n";
                        }
                    }
                } else {
                    result = "";
                }
                reader.close();
                in.close();
                client.disconnect();
            }catch(Exception e){
                result="Problema di Rete!";
            }

        return result;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            if(s.equals("Rete Assente!Riprova più tardi"))
                Toast.makeText(this.context, "Rete Assente! Riprova più tardi", Toast.LENGTH_LONG).show();
            else if(s.equals("Problema di Rete!"))
                Toast.makeText(this.context, "Errore di rete Riprovare più tardi", Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            Log.e("RETE", e.getMessage());
        }
    }
    public boolean haveInternetConnection(Context contesto) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) contesto.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
        }catch (Exception e){
            Log.e("RETE", e.getMessage());
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
