package com.lspsoftware.automacaoresidencial;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import core.auxiliares.DialogConstrutor;
import core.services.Buscador;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuscarServicoLocal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuscarServicoLocal extends Fragment {
    List<List<String>> wifiList;
    RecyclerView rvWifiList;
    TextView tvProgresso;
    private boolean retornoSettings;

    public BuscarServicoLocal() {
        // Required empty public constructor
    }

    public static BuscarServicoLocal newInstance() {
        return new BuscarServicoLocal();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_servico_local, container, false);
        tvProgresso = view.findViewById(R.id.tvProgresso);
        wifiList = new ArrayList<>();
        confConn(tvProgresso);
        return view;
    }

    public void confConn(final TextView tvProgresso){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            tvProgresso.setText("O wifi está desligado. Ligue-o e tente novamente.");
            final DialogConstrutor dialogConstrutor = new DialogConstrutor(getContext());
            dialogConstrutor.setMenssagem("A rede wifi está desligada, ligue o seu wifi e  conecte-se ao roteador para ter acesso à central de automação de sua residencia");
            dialogConstrutor.setTitulo("Conecte-se!");
            dialogConstrutor.setPositiveButton("Conecte-se", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    retornoSettings = true;
                }
            });
            dialogConstrutor.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogConstrutor.fechar();
                }
            });
            dialogConstrutor.show();
        }else if(!wifi.isConnected()){
            tvProgresso.setText("O wifi está desligado. Ligue-o e tente novamente.");
            final DialogConstrutor dialogConstrutor = new DialogConstrutor(getContext());
            dialogConstrutor.setMenssagem("A rede wifi está desconectada, conecte-se ao roteador para ter acesso à central de automação de sua residencia");
            dialogConstrutor.setTitulo("Conecte-se!");
            dialogConstrutor.setPositiveButton("Conecte-se", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    retornoSettings = true;
                }
            });
            dialogConstrutor.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogConstrutor.fechar();
                }
            });
            dialogConstrutor.show();
        }else{
            TentarConectar conectar = new TentarConectar(getContext(),this);
            conectar.setTvProgresso(tvProgresso);
            conectar.execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(retornoSettings){
            retornoSettings = false;
            confConn(tvProgresso);
        }
    }
}
class TentarConectar extends AsyncTask<String,Integer,String>{
    private Context contexto;
    private TextView tvProgresso;
    Fragment fragment;
    Buscador buscador;

    public TentarConectar(Context contexto,Fragment fragment) {
        this.setContexto(contexto);
        this.fragment = fragment;
    }


    @Override
    protected String doInBackground(String... strings) {
        publishProgress(0);
        buscador = new Buscador("central", getContexto());
        while(!buscador.isConectado());
        publishProgress(50);
        while(!buscador.isConfigurado());
        publishProgress(75);
        while(!buscador.isConcluido());
        publishProgress(100);
        return "concluido";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Inicio inicio = (Inicio) fragment.getActivity();
        MeusDispositivos dispositivos = MeusDispositivos.newInstance(buscador.getConfigCode());
        inicio.openFrag(dispositivos);
        this.cancel(true);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        switch (values[0]){
            case 0:
                tvProgresso.setText("Procurando central de automação local");
                break;
            case 50:
                tvProgresso.setText("Conectando à central");
                break;
            case 75:
                tvProgresso.setText("coletando informações da configuração");
                break;
            case 100:
                tvProgresso.setText("concluido");
                break;
        }
    }

    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public TextView getTvProgresso() {
        return tvProgresso;
    }

    public void setTvProgresso(TextView tvProgresso) {
        this.tvProgresso = tvProgresso;
    }
}
