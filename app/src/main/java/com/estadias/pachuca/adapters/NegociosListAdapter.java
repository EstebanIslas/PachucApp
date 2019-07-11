package com.estadias.pachuca.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.estadias.pachuca.R;
import com.estadias.pachuca.models.ModelNegocios;

import java.util.List;

public class NegociosListAdapter extends RecyclerView.Adapter<NegociosListAdapter.NegociosHolder>
        implements View.OnClickListener{

    List<ModelNegocios> listaNegocios; // Lista que llama a ModelNegociosList para traer los campos

    /*
     * Se hace la peticion Volley para
     * traer la url de las imagenes
     */

    RequestQueue request;
    Context context; //Se crea el contexto para darle soporte al request

    //Listener
    private View.OnClickListener listener; //Escuchador

    public NegociosListAdapter(List<ModelNegocios> listaNegocios, Context context){
        this.listaNegocios = listaNegocios;
        this.context = context;
        request = Volley.newRequestQueue(context); //Permite la trasformacion de la imagen
    }

    @NonNull
    @Override
    public NegociosListAdapter.NegociosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.negocios_list_image, viewGroup, false); //Se agrega el archivo xml de diseño
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT); //Ajusta la imagen en el xml

        view.setLayoutParams(layoutParams);

        view.setOnClickListener(this); //Se pone a escuchar el OnClick

        return new NegociosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NegociosListAdapter.NegociosHolder negociosHolder, int i) {
        negociosHolder.tv_nombre_negocio_lista_cat.setText(listaNegocios.get(i).getNombre());
        negociosHolder.tv_descripcion_negocio_lista_cat.setText(listaNegocios.get(i).getDescripcion());

        //Enviar imagen por url
        if (listaNegocios.get(i).getLogo() != null){ //Validar si existe imagen en la base de datos
            conexionWSCategorias(listaNegocios.get(i).getLogo(), negociosHolder);
        }else{//De lo contrario se envia el src de la imagen por defecto en el drawable
            //categoriasHolder.imv_logo_categoria_list_img.setImageResource(R.drawable.profile);
            negociosHolder.imv_logo_negocios_list.setImageDrawable(null);
        }
    }

    private void conexionWSCategorias(String logo, final NegociosHolder negociosHolder) {
        String URL_imagen = "http://34a82a4f.ngrok.io/PachucaService/api_usuarios/"+logo;
        URL_imagen = URL_imagen.replace(" ", "%20"); //En caso de que exista un espacio

        ImageRequest imageRequest = new ImageRequest(URL_imagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                negociosHolder.imv_logo_negocios_list.setImageBitmap(response); //Envia a la variable de tipo ImgView el contenido de nuestro modelo Negocios 'Imagen'

            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error al cargar la imagen desde el Adapter", Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return listaNegocios.size();
    }

    public class NegociosHolder extends RecyclerView.ViewHolder {

        TextView tv_nombre_negocio_lista_cat;
        TextView tv_descripcion_negocio_lista_cat;
        ImageView imv_logo_negocios_list;

        public NegociosHolder(@NonNull View itemView) {
            super(itemView);

            tv_nombre_negocio_lista_cat = itemView.findViewById(R.id.tv_nombre_negocio_lista_cat);
            tv_descripcion_negocio_lista_cat = itemView.findViewById(R.id.tv_descripcion_negocio_lista_cat);
            imv_logo_negocios_list = itemView.findViewById(R.id.imv_logo_negocios_list);

        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null){
            listener.onClick(view);
        }
    }
}
