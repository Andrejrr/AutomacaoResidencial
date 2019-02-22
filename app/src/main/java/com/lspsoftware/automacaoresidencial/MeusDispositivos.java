package com.lspsoftware.automacaoresidencial;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.InflaterInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import core.adapters.RvListaDeDependenciasAdapter;
import core.nucleo.Dependencia;
import core.services.ConnectHelper;


public class MeusDispositivos extends Fragment {
    private String configCode;
    private List<Dependencia> dependencias;
    private File file;
    File xml;
    Fragment fragment;

    public MeusDispositivos() {
        // Required empty public constructor
    }

    public static MeusDispositivos newInstance(String configCode) {
        MeusDispositivos fragment = new MeusDispositivos();
        fragment.configCode = configCode;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentMeusDispositivos = inflater.inflate(R.layout.fragment_meus_dispositivos, container, false);
        fragment = this;
        final RecyclerView rvListaDeDependencias = fragmentMeusDispositivos.findViewById(R.id.rvListaDeDependencias);
        String storagePath = getContext().getExternalFilesDir(null).getPath();//+ "/configFiles/"+ configCode;
        file = new File(storagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        SharedPreferences preferences = getContext().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        String host = preferences.getString("ip",null);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://" + host + "/"+"dependencia.xml";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    xml = new File(file,"dependencias.xml");
                    FileOutputStream stream = new FileOutputStream(xml);
                    stream.write(response.getBytes());
                    stream.flush();
                    stream.close();
                }catch (Exception e){
                    Log.e("",e.getMessage());
                }
                dependencias = new ArrayList<>();
                boolean listaexiste = carregaLista(xml);
                if(listaexiste) {
                    RvListaDeDependenciasAdapter adapter = new RvListaDeDependenciasAdapter(getContext(), getDependencias(),fragment,configCode);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                    gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
                    rvListaDeDependencias.setLayoutManager(gridLayoutManager);
                    rvListaDeDependencias.setAdapter(adapter);
                }else{

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("",error.getMessage());
            }
        });
        queue.add(request);
        return fragmentMeusDispositivos;
    }

    private boolean carregaLista(File file) {
        boolean carregou = false;
        try {
                InputStream stream = new FileInputStream(file);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = factory.newDocumentBuilder();
                InputSource inputSource = new InputSource(stream);
                Document document = db.parse(inputSource);
                NodeList nodeList = document.getElementsByTagName("Dependencias");
                int nodeListLength = nodeList.getLength();
                for (int i = 0; i < nodeListLength; i++) {
                    Dependencia dependencia = new Dependencia();
                    Node nos = nodeList.item(i);
                    NodeList filhos = nos.getChildNodes();
                    int filhosLength = filhos.getLength();
                    for (int j = 0; j < filhosLength; j++) {
                        String tag = filhos.item(j).getNodeName();
                        if(tag.contains("Id")){
                            dependencia.setId(Integer.parseInt(filhos.item(j).getTextContent()));
                        }else if(tag.contains("Nome")){
                            dependencia.setNome(filhos.item(j).getTextContent());
                        }else if(tag.contains("Tipo")){
                            dependencia.setTipo(filhos.item(j).getTextContent());
                        }else if(tag.contains("NumeroDispositivos")){
                            dependencia.setNumeroDispositivos(Integer.parseInt(filhos.item(j).getTextContent()));
                        }
                    }
                    dependencias.add(dependencia);
                }
                return true;
        }catch (Exception e){
            Log.e("erro ao abrir arq:",e.getMessage());
        }
        return carregou;
    }


    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public List<Dependencia> getDependencias() {
        return dependencias;
    }

    public void setDependencias(List<Dependencia> dependencias) {
        this.dependencias = dependencias;
    }
}
