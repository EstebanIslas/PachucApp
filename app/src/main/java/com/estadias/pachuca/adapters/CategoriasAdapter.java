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
import com.estadias.pachuca.models.ModelCategorias;

import java.util.List;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.CategoriasHolder>
        implements View.OnClickListener{

    List<ModelCategorias> listaCategorias; // Lista que llama a la ModelCategorias para traer los campos

    /*
     * Se hace peticion a Volley para
     * traer la url de la imagen
     */

    RequestQueue request;
    Context context; //Se crea el contexto para darle soporte al request

    //Implementar Onclick
    private View.OnClickListener listener; //Escuchador


    public CategoriasAdapter(List<ModelCategorias> listaCategorias, Context context){
        this.listaCategorias = listaCategorias;
        this.context = context;
        request = Volley.newRequestQueue(context); //Permite la trasformacion de la imagen
    }


    @NonNull
    @Override
    public CategoriasAdapter.CategoriasHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.categorias_list_image,viewGroup,false); //Se agrega el archivo xml de diseño
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT); //Ajusta la imagen en el xml

        view.setLayoutParams(layoutParams);

        view.setOnClickListener(this); //Se pone a escuchar el evento Listener

        return new CategoriasHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriasAdapter.CategoriasHolder categoriasHolder, int i) {
        categoriasHolder.tv_nombre_categoria_list_img.setText(listaCategorias.get(i).getNombre());
        categoriasHolder.tv_descripcion_categoria_list_img.setText(listaCategorias.get(i).getDescripcion());

        //Enviar imagen por url
        if (listaCategorias.get(i).getLogo() != null){ //Validar si existe imagen en la base de datos
            conexionWSCategorias(listaCategorias.get(i).getLogo(), categoriasHolder);
        }else{//De lo contrario se envia el src de la imagen por defecto en el drawable
            //categoriasHolder.imv_logo_categoria_list_img.setImageResource(R.drawable.profile);
            categoriasHolder.imv_logo_categoria_list_img.setImageDrawable(null);
        }
    }

    private void conexionWSCategorias(String logo, final CategoriasHolder categoriasHolder) {
        String URL_imagen = "http://b9f9a392.ngrok.io/PachucaService/api_categorias/" + logo;
        URL_imagen = URL_imagen.replace(" ", "%20"); //En caso de que el nombre de la imagen tenga espacios

        ImageRequest imageRequest = new ImageRequest(URL_imagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                categoriasHolder.imv_logo_categoria_list_img.setImageBitmap(response); //Envia a la variable de tipo ImgView el contenido de nuestro modelo Categoria 'Imagen'
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
        return listaCategorias.size(); //Cuenta el tamaño de la lista
    }



    public class CategoriasHolder extends RecyclerView.ViewHolder{

        TextView tv_nombre_categoria_list_img;
        TextView tv_descripcion_categoria_list_img;

        ImageView imv_logo_categoria_list_img;

        public CategoriasHolder(@NonNull View itemView) {
            super(itemView);

            tv_nombre_categoria_list_img = itemView.findViewById(R.id.tv_nombre_categoria_list_img);
            tv_descripcion_categoria_list_img = itemView.findViewById(R.id.tv_descripcion_categoria_list_img);
            imv_logo_categoria_list_img = itemView.findViewById(R.id.imv_logo_categoria_list_img);
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
