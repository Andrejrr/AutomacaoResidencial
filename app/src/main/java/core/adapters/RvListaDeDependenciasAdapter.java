package core.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lspsoftware.automacaoresidencial.Inicio;
import com.lspsoftware.automacaoresidencial.ListaDeDispositivos;
import com.lspsoftware.automacaoresidencial.R;

import java.util.List;

import core.nucleo.Dependencia;

public class RvListaDeDependenciasAdapter extends RecyclerView.Adapter {
    Context context;
    String configCode;
    List<Dependencia> dependencias;
    Fragment fragment;

    public RvListaDeDependenciasAdapter(Context context, List<Dependencia> dependencias,Fragment fragment,String configCode) {
        this.context = context;
        this.dependencias = dependencias;
        this.fragment = fragment;
        this.configCode = configCode;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.comodos_cardview,null);
        RvListaDeDependenciasHolder rvListaDeDispositivosHolder = new RvListaDeDependenciasHolder(itemView);
        return rvListaDeDispositivosHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final RvListaDeDependenciasHolder dependenciasHolder = (RvListaDeDependenciasHolder) holder;
        final Dependencia dependencia = dependencias.get(position);
        dependenciasHolder.tvNumeroDispositivo.setText(Integer.toString(dependencia.getNumeroDispositivos()));
        dependenciasHolder.tvNomeDependencia.setText(dependencia.getNome());
        dependenciasHolder.cvDependencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaDeDispositivos listaDeDispositivos = ListaDeDispositivos.newInstance(Integer.toString(dependencia.getId()),configCode);
                Inicio inicio = (Inicio) fragment.getActivity();
                inicio.openFrag(listaDeDispositivos);
            }
        });
        Resources resources =context.getResources();
        TypedArray typedArray = resources.obtainTypedArray(R.array.imagemListDependencias);
        Drawable drawable = typedArray.getDrawable(getImagem(dependencia.getTipo()));
        dependenciasHolder.imvDependencia.setImageDrawable(drawable);
    }

    private int getImagem(String tipo) {
        if(tipo.contains("Suite"))
            return 4;
        else if(tipo.contains("Quarto"))
            return 5;
        else if(tipo.contains("Banheiro"))
            return 0;
        else if(tipo.contains("Sala de estar"))
            return 2;
        else if(tipo.contains("Sala de Jantar"))
            return 3;
        else
            return 1;

    }

    @Override
    public int getItemCount() {
        return dependencias.size();
    }
}

class RvListaDeDependenciasHolder extends RecyclerView.ViewHolder{

    CardView cvDependencia;
    ImageView imvDependencia;
    TextView tvNomeDependencia;
    TextView tvNumeroDispositivo;
    public RvListaDeDependenciasHolder(View itemView) {
        super(itemView);
        cvDependencia = itemView.findViewById(R.id.cvDependencia);
        imvDependencia = itemView.findViewById(R.id.imvDependencia);
        tvNomeDependencia = itemView.findViewById(R.id.tvNomeArea);
        tvNumeroDispositivo = itemView.findViewById(R.id.tvNumeroDeDispositivos);
    }
}