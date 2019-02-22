package com.lspsoftware.automacaoresidencial;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import core.adapters.RvListaDeDispositivosAdapter;
import core.nucleo.Dependencia;
import core.nucleo.Dispositivo;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaDeDispositivos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaDeDispositivos extends Fragment {
    private String dependenciaCode;
    private String configCode;
    private List<Dispositivo> dispositivos;
    private File file;
    File xml;

    public ListaDeDispositivos() {
        // Required empty public constructor
    }


    public static ListaDeDispositivos newInstance(String dependenciaCode,String configCode) {
        ListaDeDispositivos fragment = new ListaDeDispositivos();
        fragment.dependenciaCode = dependenciaCode;
        fragment.configCode = configCode;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispositivos = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View listaDeDispositivos = inflater.inflate(R.layout.fragment_lista_de_dispositivos, container, false);
        final RecyclerView rvListaDeDispositivos = listaDeDispositivos.findViewById(R.id.rvListaDeDispositivos);
        String storagePath = getContext().getExternalFilesDir(null).getPath();//+ "/configFiles/"+ configCode;
        file = new File(storagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        SharedPreferences preferences = getContext().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        String host = preferences.getString("ip",null);
        final RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://" + host + "/"+"dispositivos.xml";
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    xml = new File(file, "dispositivos.xml");
                    FileOutputStream stream = new FileOutputStream(xml);
                    stream.write(response.getBytes());
                    stream.flush();
                    stream.close();
                } catch (Exception e) {
                    Log.e("", e.getMessage());
                }
                carregaLista(xml);
                RvListaDeDispositivosAdapter adapter = new RvListaDeDispositivosAdapter(getContext(), dispositivos);
                rvListaDeDispositivos.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
                rvListaDeDispositivos.setLayoutManager(layoutManager);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("",error.getMessage());
            }
        });
        queue.add(request);
        return listaDeDispositivos;
    }

    private boolean carregaLista(File xml) {
        boolean carregou = false;
        try {
                InputStream is = new FileInputStream(xml);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = factory.newDocumentBuilder();
                InputSource inputSource = new InputSource(is);
                Document document = db.parse(inputSource);
                NodeList nodeList = document.getElementsByTagName("Dependencias");
                int nodeListLength = nodeList.getLength();
                for (int i = 0; i < nodeListLength; i++) {
                    Dispositivo dispositivo = new Dispositivo();
                    Node nos = nodeList.item(i);
                    NodeList filhos = nos.getChildNodes();
                    int filhosLength = filhos.getLength();
                    if(filhos.item(3).getTextContent().contains(dependenciaCode)) {
                        for (int j = 0; j < filhosLength; j++) {
                            String tag = filhos.item(j).getNodeName();
                            if (tag.contains("Id")) {
                                dispositivo.setId(Integer.parseInt(filhos.item(j).getTextContent()));
                            } else if (tag.contains("Nome")) {
                                dispositivo.setNome(filhos.item(j).getTextContent());
                            } else if (tag.contains("Tipo")) {
                                dispositivo.setTipo(filhos.item(j).getTextContent());
                            } else if (tag.contains("descricao")) {
                                dispositivo.setDescricao(filhos.item(j).getTextContent());
                            } else if (tag.contains("status")) {
                                switch (Integer.parseInt(filhos.item(j).getTextContent())) {
                                    case 0:
                                        dispositivo.setStatus(Dispositivo.DESLIGADO);
                                        break;
                                    case 1:
                                        dispositivo.setStatus(Dispositivo.LIGADO);
                                        break;
                                    default:
                                        dispositivo.setStatus(Dispositivo.DESCONHECIDO);
                                        break;
                                }
                            }
                        }
                    }
                    if(dispositivo.getNome()!=null)
                        dispositivos.add(dispositivo);
                }
                return true;
        }catch (Exception e){
            Log.e("erro ao abrir arq:",e.getMessage());
        }
        return carregou;
    }

}
