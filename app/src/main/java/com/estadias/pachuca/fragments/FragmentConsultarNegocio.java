package com.estadias.pachuca.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.estadias.pachuca.R;
import com.estadias.pachuca.models.ModelNegocios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentConsultarNegocio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentConsultarNegocio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentConsultarNegocio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Variable final
    public static final String ID_NEGOCIO_FIN = "1";

    //########### Insercion de Variables ###########

    TextView tv_nombre_consulta_neg;
    TextView tv_descripcion_consulta_neg;
    TextView tv_telefono_consulta_neg;
    TextView tv_ubicacion__consulta_neg;
    TextView tv_correo_consulta_neg;
    TextView tv_sitio_web_consulta_neg;
    TextView tv_dir_completa_consulta_neg;
    ImageView imv_logo_consulta_neg;

    //Boton de llamada y mensaje y mapa
    Button btn_llamar;
    Button btn_mensaje;
    Button btn_mapa;

    //Boton de promociones
    Button btn_promociones_consulta_neg;

    ProgressDialog progreso; //Para generar una ventana de carga mientras se ejecutan las peticiones

    //Permiten establecer la conexion con el webservice
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    public FragmentConsultarNegocio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentConsultarNegocio.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentConsultarNegocio newInstance(String param1, String param2) {
        FragmentConsultarNegocio fragment = new FragmentConsultarNegocio();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_consultar_negocio, container, false);

        initComponents(view);

        request = Volley.newRequestQueue(getContext());

        //Envio de parametro del otro fragment ListaCategorias
        Bundle bundle = this.getArguments();
        final String id_negocio = bundle.get(FragmentListaNegocios.ID_NEGOCIO).toString();

        tv_ubicacion__consulta_neg.setVisibility(View.GONE);

        conexionWebService(id_negocio);

        btn_llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarOnClick(view);
            }
        });

        btn_mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mensajeOnClick(view);
            }
        });

        btn_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vermapaOnClick(view);
            }
        });

        btn_promociones_consulta_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irapromocionesOnClick(view, id_negocio);
            }
        });

        return view;
    }

    private void irapromocionesOnClick(View view, String id_negocio) {
        //Toast.makeText(getContext(), "Presiono promociones y el id del negocio es: "+ id_negocio, Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle(); //Paquete que guarda la variable String final
        bundle.putString(ID_NEGOCIO_FIN, id_negocio);

        //Enviar de un fragment a otro
        Fragment consultarPromociones = new FragmentConsultarPromociones(); //Objeto que llama al fragment que se le enviaran los datos
        consultarPromociones.setArguments(bundle); //Se envia como parametro el valor guardado*/

        //Toast.makeText(getContext(), "ID: " + bundle.toString(), Toast.LENGTH_SHORT).show();

        FragmentTransaction ft = getFragmentManager().beginTransaction(); //Objeto creado para remplazar el fragment
        ft.replace(R.id.content_main, consultarPromociones); //se mapea el id del lugar donde de remplazara

        /* No cierra el fragment anterior*/
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);

        ft.commit();
    }

    private void mensajeOnClick(View view) {
        /*Uri mensaje = Uri.parse("smsto:"+ tv_telefono_consulta_neg.getText().toString());
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, mensaje);
        smsIntent.putExtra("sms_body", "");

        startActivity(smsIntent);*/
        try{
            String telefono_consulta = tv_telefono_consulta_neg.getText().toString();
            String numero = "+521" + telefono_consulta;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String uri = "whatsapp://send?phone=" + numero;
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }catch (Exception ex) {
            Toast.makeText(getContext(), "Ocurrió un problema con WhatsApp", Toast.LENGTH_SHORT).show();
            Log.e("\n\nERROR: " , ex.getMessage());
        }
    }

    private void llamarOnClick(View view) {
        Uri numero = Uri.parse("tel:"+tv_telefono_consulta_neg.getText().toString());
        Intent callIntent = new Intent(Intent.ACTION_DIAL, numero);

        startActivity(callIntent);
    }

    private void vermapaOnClick(View view){
        try {
            String ubicacion = tv_ubicacion__consulta_neg.getText().toString();
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(ubicacion));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        }catch (Exception ex){
            Toast.makeText(getContext(), "Ocurrió un problema con Google Maps", Toast.LENGTH_SHORT).show();
            Log.e("\n\nERROR: " , ex.getMessage());
        }
    }

    private void initComponents(View view) {
        tv_nombre_consulta_neg = view.findViewById(R.id.tv_nombre_consulta_neg);
        tv_descripcion_consulta_neg = view.findViewById(R.id.tv_descripcion_consulta_neg);
        tv_telefono_consulta_neg = view.findViewById(R.id.tv_telefono1_consulta_neg);
        tv_ubicacion__consulta_neg = view.findViewById(R.id.tv_ubicacion_consulta_neg);
        tv_correo_consulta_neg = view.findViewById(R.id.tv_correo_consulta_neg);
        tv_sitio_web_consulta_neg = view.findViewById(R.id.tv_sitio_web_consulta_neg);
        imv_logo_consulta_neg = view.findViewById(R.id.imv_logo_consulta_neg);

        tv_dir_completa_consulta_neg = view.findViewById(R.id.tv_dir_completa_consulta_neg);

        //Boton de Llamada y mensaje y mapa
        btn_llamar = view.findViewById(R.id.btn_llamar_consulta);
        btn_mensaje = view.findViewById(R.id.btn_mensaje_consulta);
        btn_mapa = view.findViewById(R.id.btn_mapa_consulta);

        //Boton de Promociones
        btn_promociones_consulta_neg = view.findViewById(R.id.btn_promociones_consulta_neg);
    }


    private void conexionWebService(String id_negocio) {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando");
        progreso.show();

        String URL = "https://pachuca.com.mx/webservice/api_usuarios/wsSelectOneUsuario.php?id="+id_negocio;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();

                //Mapear lo que devuelve el Webservice

                ModelNegocios negocios = new ModelNegocios();

                JSONArray json = response.optJSONArray("usuario"); //Separa el array que muestra el json -> "[usuario:"

                JSONObject jsonObject = null; //Se encarga de llenar cada objeto dependiendo de lo que tenga la consulta de json

                try{
                    jsonObject = json.getJSONObject(0);

                    negocios.setId(jsonObject.optInt("id"));
                    negocios.setNombre(jsonObject.optString("nombre"));
                    negocios.setDescripcion(jsonObject.optString("descripcion"));
                    negocios.setCalle(jsonObject.optString("calle"));
                    negocios.setColonia(jsonObject.optString("colonia"));
                    negocios.setNumero(jsonObject.optString("numero"));
                    negocios.setMunicipio(jsonObject.optString("municipio"));
                    negocios.setTelefono1(jsonObject.optString("telefono"));
                    negocios.setMaps_url(jsonObject.optString("maps_url"));
                    negocios.setCorreo(jsonObject.optString("correo"));
                    negocios.setSitio_web(jsonObject.optString("sitio_web"));

                    negocios.setLogo(jsonObject.optString("logo")); //Se da soporte para traer la imagen

                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "No se establecio la conexion: " +e.toString(), Toast.LENGTH_SHORT).show();
                    progreso.hide();
                }
                String direccion = "Calle "+ negocios.getCalle()+ " No. "+ negocios.getNumero() + " Colonia " + negocios.getColonia() + ", " + negocios.getMunicipio();

                tv_nombre_consulta_neg.setText(negocios.getNombre());
                tv_descripcion_consulta_neg.setText(negocios.getDescripcion());
                tv_telefono_consulta_neg.setText(negocios.getTelefono1());
                tv_ubicacion__consulta_neg.setText(negocios.getMaps_url());
                tv_correo_consulta_neg.setText(negocios.getCorreo());
                tv_sitio_web_consulta_neg.setText(negocios.getSitio_web());
                tv_dir_completa_consulta_neg.setText(direccion);

                /*
                 * Logica para cargar imagen desde WS por URL
                 */
                String logo_url = "https://pachuca.com.mx/" + negocios.getLogo();

                conexionWebServiceLogoUrl(logo_url);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No existe conexion con el Servidor", Toast.LENGTH_SHORT).show();
                System.out.println();
                progreso.hide();
                Log.i("ERROR: ", error.toString());
            }
        });

        request.add(jsonObjectRequest);
    }

    private void conexionWebServiceLogoUrl(String logo_url) {
        logo_url = logo_url.replace(" ", "%20"); //En caso de que exista un espacio en el nombre de la imagen

        ImageRequest imageRequest = new ImageRequest(logo_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imv_logo_consulta_neg.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Esperando imagen...", Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
