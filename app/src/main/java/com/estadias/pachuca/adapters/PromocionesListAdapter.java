package com.estadias.pachuca.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.estadias.pachuca.R;
import com.estadias.pachuca.models.ModelPromociones;

import java.util.List;

public class PromocionesListAdapter extends RecyclerView.Adapter<PromocionesListAdapter.PromocionesHolder>
        implements View.OnClickListener {

    List<ModelPromociones> listaPromociones; // Lista que llama a ModelNegociosList para traer los campos

    // Listener para entrar a los codigos generados de la promocion

    private View.OnClickListener listener;

    public PromocionesListAdapter(List<ModelPromociones> listaPromociones) {
        this.listaPromociones = listaPromociones;
    }

    @NonNull
    @Override
    public PromocionesListAdapter.PromocionesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.promociones_list, viewGroup, false); //Se agrega el archivo xml de diseño
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT); //Ajusta el archiv xml al recyclerview

        view.setLayoutParams(layoutParams);

        view.setOnClickListener(this); //Se pone a escuchar el OnClick

        return new PromocionesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromocionesListAdapter.PromocionesHolder promocionesHolder, int i) {
        promocionesHolder.tv_titulo_ver_promociones_list.setText(listaPromociones.get(i).getTitulo());
        promocionesHolder.tv_descripcion_ver_promociones_list.setText(listaPromociones.get(i).getDescripcion());
    }

    @Override
    public int getItemCount() {
        return listaPromociones.size();
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

    public class PromocionesHolder extends RecyclerView.ViewHolder {

        TextView tv_titulo_ver_promociones_list, tv_descripcion_ver_promociones_list;

        public PromocionesHolder(@NonNull View itemView) {
            super(itemView);

            tv_titulo_ver_promociones_list = itemView.findViewById(R.id.tv_titulo_ver_promociones_list);
            tv_descripcion_ver_promociones_list = itemView.findViewById(R.id.tv_descripcion_ver_promociones_list);
        }
    }
}
