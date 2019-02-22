package core.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import com.lspsoftware.automacaoresidencial.R;

import static android.provider.Settings.Global.getString;

public class Buscador {
    SharedPreferences sharedPreferences;
    private static final String SERVICE_TYPE = "_http._tcp.";
    private Context context;
    private String nomeServico = "central";
    private int porta;
    NsdManager nsdManager;
    NsdManager.DiscoveryListener discoveryListener;
    NsdManager.ResolveListener resolveListener;
    NsdServiceInfo serviceInfo;
    private String status;
    private boolean concluido;
    private boolean conectado;
    private boolean configurado;
    private String configCode;

    public Buscador(String nomeServico,Context context) {
        sharedPreferences = context.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        this.setNomeServico(nomeServico);
        this.setPorta(getPorta());
        this.context = context;
        nsdManager = (NsdManager) this.context.getSystemService(Context.NSD_SERVICE);
        criarResolverListener();
        criarDiscoveryListener();
        encontrarServico();
    }

    private void encontrarServico() {
        nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    private void criarResolverListener(){
        resolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int i) {

            }

            @Override
            public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {
                serviceInfo = nsdServiceInfo;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ip",serviceInfo.getHost().getHostName());
                editor.putInt("port",serviceInfo.getPort());
                editor.apply();
                try{
                    ConnectHelper connectHelper = new ConnectHelper(context);
                    while (!configurado){
                        configurado = connectHelper.conectar("configCode");
                        setConfigCode(connectHelper.getConfigString());
                    }
                    concluido = true;
                }catch (Exception e){
                    Log.e("erro ao conectar server",e.getMessage());
                }

            }
        };
    }

    private void criarDiscoveryListener() {
        discoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String s, int i) {
                Log.d("iniciou busca",s);
            }

            @Override
            public void onStopDiscoveryFailed(String s, int i) {

            }

            @Override
            public void onDiscoveryStarted(String s) {

            }

            @Override
            public void onDiscoveryStopped(String s) {

            }

            @Override
            public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
                serviceInfo = nsdServiceInfo;
                try {
                    if (nsdServiceInfo.getServiceType().equals(SERVICE_TYPE)) {
                        if (nsdServiceInfo.getServiceName().equals(getNomeServico())) {
                            nsdManager.resolveService(serviceInfo, resolveListener);
                            conectado = true;
                        }
                    }
                }catch (Exception e){
                    Log.e("erro ao buscar server",e.getMessage());
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo nsdServiceInfo) {

            }
        };
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public boolean isConfigurado() {
        return configurado;
    }

    public void setConfigurado(boolean configurado) {
        this.configurado = configurado;
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }
}
