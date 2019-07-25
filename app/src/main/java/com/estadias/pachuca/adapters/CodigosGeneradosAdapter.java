package com.estadias.pachuca.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.estadias.pachuca.R;
import com.estadias.pachuca.models.ModelCodigos;

import java.util.List;

public class CodigosGeneradosAdapter extends RecyclerView.Adapter<CodigosGeneradosAdapter.CodigosGeneradosHolder>
        implements View.OnClickListener {

    List<ModelCodigos> listaCodigosGenerados; // Lista que llama a ModelCodigos para traer los campos

    // Listener para entrar a los codigos generados de la promocion

    private View.OnClickListener listener;

    public CodigosGeneradosAdapter(List<ModelCodigos> listaCodigosGenerados) {
        this.listaCodigosGenerados = listaCodigosGenerados;
    }

    @NonNull
    @Override
    public CodigosGeneradosAdapter.CodigosGeneradosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.codigos_generados_list, viewGroup, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        view.setLayoutParams(layoutParams);

        view.setOnClickListener(this); //Se pone a escuchar

        return new CodigosGeneradosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CodigosGeneradosAdapter.CodigosGeneradosHolder codigosGeneradosHolder, int i) {
        codigosGeneradosHolder.tv_nombre_codigos_generados.setText(listaCodigosGenerados.get(i).getNombre());
        codigosGeneradosHolder.tv_codigo_codigos_generados.setText(listaCodigosGenerados.get(i).getCodigo());
        codigosGeneradosHolder.tv_estado_codigos_generados.setText(listaCodigosGenerados.get(i).getEstado());
    }

    @Override
    public int getItemCount() {
        return listaCodigosGenerados.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null){
            listener.onClick(view);
        }
    }

    public class CodigosGeneradosHolder extends RecyclerView.ViewHolder {

        TextView tv_nombre_codigos_generados, tv_codigo_codigos_generados, tv_estado_codigos_generados;

        public CodigosGeneradosHolder(@NonNull View itemView) {
            super(itemView);

            tv_nombre_codigos_generados = itemView.findViewById(R.id.tv_nombre_codigos_generados);
            tv_codigo_codigos_generados = itemView.findViewById(R.id.tv_codigo_codigos_generados);
            tv_estado_codigos_generados = itemView.findViewById(R.id.tv_estado_codigos_generados);
        }
    }
}