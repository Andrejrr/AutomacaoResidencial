package core.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lspsoftware.automacaoresidencial.R;

import java.util.List;

import core.auxiliares.DialogConstrutor;
import core.nucleo.Dispositivo;

public class RvListaDeDispositivosAdapter extends RecyclerView.Adapter {
    Context context;
    List<Dispositivo> dispositivos;

    public RvListaDeDispositivosAdapter(Context context, List<Dispositivo> dispositivos) {
        this.context = context;
        this.dispositivos = dispositivos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.dispositivo,null);
        RvListaDeDispositivosHolder rvListaDeDispositivosHolder = new RvListaDeDispositivosHolder(itemView);
        return rvListaDeDispositivosHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            RvListaDeDispositivosHolder dispositivosHolder = (RvListaDeDispositivosHolder) holder;
            final Dispositivo dispositivo = dispositivos.get(position);
            dispositivosHolder.tvStatusDispositivo.setText(dispositivo.getStatus());
            dispositivosHolder.tvNomeDispositivo.setText(dispositivo.getNome());
            dispositivosHolder.tvDescricaoDispositivo.setText(dispositivo.getDescricao());
            Resources resources = context.getResources();
            dispositivosHolder.imvDispositivo.setImageDrawable(getImagem(dispositivo.getTipo(),dispositivo.getStatus(),resources));
            dispositivosHolder.cvDispositivos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences preferences = context.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
                    String host = preferences.getString("ip",null);
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = "http://" + host + "/setStatusSaida="+dispositivo.getPosicao();
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            new DialogConstrutor(context,"Erro ao acionar servidor",error.getMessage(),"OK");
                        }
                    });
                    queue.add(request);
                }
            });

        }catch(Exception e){
            Log.e("erro:",e.getMessage());

        }
    }

    private Drawable getImagem(String tipo,String status,Resources resources) {
        if(tipo.contains("Lampada")||tipo.contains("LED")||tipo.contains("FitaLED")) {
            if(!(status == Dispositivo.LIGADO))
                return resources.getDrawable(R.drawable.led_off);
            else
                return resources.getDrawable(R.drawable.led_on);
        }
        else if(tipo.contains("Cortina")) {
            if (status==Dispositivo.LIGADO)
                return resources.getDrawable(R.drawable.cortina_aberta);
            else
                return resources.getDrawable(R.drawable.cortina_fechada);
        }
        else {
            if (!(status == Dispositivo.LIGADO))
                return resources.getDrawable(R.drawable.led_off);
            else
                return resources.getDrawable(R.drawable.led_on);
        }
    }

    @Override
    public int getItemCount() {
        return dispositivos.size();
    }
}

class RvListaDeDispositivosHolder extends RecyclerView.ViewHolder{

    CardView cvDispositivos;
    ImageView imvDispositivo;
    TextView tvNomeDispositivo;
    TextView tvDescricaoDispositivo;
    TextView tvStatusDispositivo;
    public RvListaDeDispositivosHolder(View itemView) {
        super(itemView);
        cvDispositivos = itemView.findViewById(R.id.cvDispositivos);
        imvDispositivo = itemView.findViewById(R.id.imvDispositivo);
        tvNomeDispositivo = itemView.findViewById(R.id.tvNomeDoDispositivo);
        tvStatusDispositivo = itemView.findViewById(R.id.tvStatusDispositivo);
        tvDescricaoDispositivo = itemView.findViewById(R.id.tvDescricaoDispositivo);
    }
}