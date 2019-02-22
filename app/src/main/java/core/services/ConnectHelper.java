package core.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectHelper {
    private SharedPreferences preferences;
    private Context context;
    private NsdServiceInfo serviceInfo;
    private String host;
    private int porta;
    private Socket socket;
    private String configString;
    private String request;
    File file;
    boolean recebeuResposta;

    public ConnectHelper(Context context, NsdServiceInfo serviceInfo) {
        this.context = context;
        this.serviceInfo = serviceInfo;
        setHost(serviceInfo.getHost().getHostName());
        setPorta(serviceInfo.getPort());
        try {
            setSocket(new Socket(getHost(), getPorta()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConnectHelper(Context context) {
        try {
            this.context = context;
            preferences = context.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
            setHost(preferences.getString("ip",null));
            setPorta(preferences.getInt("port",80));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   /* public boolean conectar(){
        try {
            enviarMenssagem(criaRequestString("ConfigCode"));
            setConfigString(receberMenssagem());
            return true;
        }catch (Exception e){
            Log.e("socket error",e.getMessage());
            return false;
        }
    }
*/
   public boolean conectar(String diretorio){
       try {
           RequestQueue queue = Volley.newRequestQueue(this.context);
           String url = "http://" + host + "/"+diretorio;
           StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
               @Override
               public void onResponse(String response) {
                   setConfigString(response);
               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {

               }
           });
           queue.add(request);
           return true;

       }catch (Exception e){
           Log.e("error: ",e.getMessage());
           return false;
       }
   }

    public File conectar(String diretorio,String nome,String extensao){
        file = null;
        recebeuResposta = true;
        try {
            final RequestQueue queue = Volley.newRequestQueue(this.context);
            String url = "http://" + host + "/"+diretorio;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    file = new File(response);
                    recebeuResposta = false;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("",error.getMessage());
                }
            });
            queue.add(request);
            return file;

        }catch (Exception e){
            Log.e("error: ",e.getMessage());
            return file;
        }
    }

    public boolean enviarMenssagem(String menssagem,Socket socket){
        boolean enviado;
        try{
            OutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            ((DataOutputStream) out).writeUTF(menssagem);
            out.flush();
            enviado = true;
        }catch (Exception exption){
            Log.e("falha ao enviar: ",exption.getMessage());
            enviado = false;
        }
        return enviado;
    }

    public boolean enviarMenssagem(String menssagem){
        boolean enviado;
        try{
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            out.writeUTF(menssagem);
            out.flush();
            enviado = true;
        }catch (Exception exption){
            Log.e("falha ao enviar: ",exption.getMessage());
            enviado = false;
        }
        return enviado;
    }

    public String receberMenssagem(){
        String menssagem;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
            menssagem = in.readLine();
        }catch (Exception e){
            Log.e("falha ao receber:",e.getMessage());
            menssagem = "";
        }
        return menssagem;
    }

    public String receberMenssagem(Socket socket){
        String menssagem;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            menssagem = in.readLine();
        }catch (Exception e){
            Log.e("falha ao receber:",e.getMessage());
            menssagem = "";
        }
        return menssagem;
    }
    public String criaRequestString(String menssagem){
        return "GET /"+menssagem +" HTTP/1.1";
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public NsdServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(NsdServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public String getConfigString() {
        return configString;
    }

    public void setConfigString(String configString) {
        this.configString = configString;
    }
}
