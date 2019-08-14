package com.estadias.pachuca.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * {@link FragmentConsultarInfoNegocio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentConsultarInfoNegocio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentConsultarInfoNegocio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /* ----- Variables ----- */
    public static String ID_USUARIO = "0";
    public static String ID_USUARIO_FINAL = "";

    TextView tv_nombre_consulta_info_neg;
    TextView tv_descripcion_consulta_info_neg;
    TextView tv_telefono_consulta_info_neg;
    TextView tv_ubicacion__consulta_info_neg;
    TextView tv_correo_consulta_info_neg;
    TextView tv_sitio_web_consulta_info_neg;
    TextView tv_dir_completa_consulta_info_neg;
    TextView tv_estado_consulta_info_neg;
    ImageView imv_logo_consulta_info_neg;

    TextView tv_visibilidad_texto;

    ProgressDialog progreso; //Para generar una ventana de carga mientras se ejecutan las peticiones

    //Permiten establecer la conexion con el webservice
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    public FragmentConsultarInfoNegocio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentConsultarInfoNegocio.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentConsultarInfoNegocio newInstance(String param1, String param2) {
        FragmentConsultarInfoNegocio fragment = new FragmentConsultarInfoNegocio();
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
        View view = inflater.inflate(R.layout.fragment_consultar_info_negocio, container, false);

        request = Volley.newRequestQueue(getContext());

        //Documento Shared que trae el id del usuario/empresa
        SharedPreferences preferences = getActivity().getSharedPreferences("ID_USUARIO", Context.MODE_PRIVATE);

        String id_usuario = preferences.getString(ID_USUARIO, ID_USUARIO);
        ID_USUARIO_FINAL = id_usuario;

        //Toast.makeText(getContext(), "ID_USUARIO: " + id_usuario, Toast.LENGTH_LONG).show();

        initComponents(view);

        tv_visibilidad_texto.setVisibility(View.GONE);

        conexionWebService(ID_USUARIO_FINAL);

        return view;
    }


    private void initComponents(View view) {
        tv_nombre_consulta_info_neg = view.findViewById(R.id.tv_nombre_consulta_info_neg);
        tv_descripcion_consulta_info_neg = view.findViewById(R.id.tv_descripcion_consulta_info_neg);
        tv_telefono_consulta_info_neg = view.findViewById(R.id.tv_telefono1_consulta_info_neg);
        tv_ubicacion__consulta_info_neg = view.findViewById(R.id.tv_ubicacion_consulta_info_neg);
        tv_correo_consulta_info_neg = view.findViewById(R.id.tv_correo_consulta_info_neg);
        tv_sitio_web_consulta_info_neg = view.findViewById(R.id.tv_sitio_web_consulta_info_neg);
        imv_logo_consulta_info_neg = view.findViewById(R.id.imv_logo_consulta_info_neg);
        tv_estado_consulta_info_neg = view.findViewById(R.id.tv_estado_consulta_info_neg);

        tv_dir_completa_consulta_info_neg = view.findViewById(R.id.tv_dir_completa_consulta_info_neg);

        tv_visibilidad_texto = view.findViewById(R.id.tv_visibilidad_texto);
    }


    private void conexionWebService(String id_usuario) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando");
        progreso.show();

        String URL = "https://pachuca.com.mx/webservice/api_usuarios/wsSelectOneUsuario.php?id="+ id_usuario;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progreso.hide();

                //Mapear lo que devuelve el Webservice

                ModelNegocios negocios = new ModelNegocios();

                JSONArray json = response.optJSONArray("usuario"); //Separa el array que muestra el json -> "[usuario:"

                JSONObject jsonObject = null; //Se encarga de llenar cada objeto dependiendo de lo que tenga la consulta de json

                String estado = "";
                try{
                    jsonObject = json.getJSONObject(0);

                    negocios.setId(jsonObject.optInt("id"));
                    negocios.setNombre(jsonObject.optString("nombre"));
                    negocios.setDescripcion(jsonObject.optString("descripcion"));
                    negocios.setCalle(jsonObject.optString("calle"));
                    negocios.setColonia(jsonObject.optString("colonia"));
                    negocios.setNumero(jsonObject.optString("numero"));
                    negocios.setMunicipio(jsonObject.optString("municipio"));
                    negocios.setTelefono1(jsonObject.optString("telefono1"));
                    negocios.setMaps_url(jsonObject.optString("maps_url"));
                    negocios.setCorreo(jsonObject.optString("correo"));
                    negocios.setSitio_web(jsonObject.optString("sitio_web"));
                    estado = jsonObject.optString("estado");

                    negocios.setLogo(jsonObject.optString("logo")); //Se da soporte para traer la imagen

                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "No se establecio la conexion: " +e.toString(), Toast.LENGTH_SHORT).show();
                    progreso.hide();
                }
                String direccion = "Calle "+ negocios.getCalle()+ " No. "+ negocios.getNumero() + " Colonia " + negocios.getColonia() + ", " + negocios.getMunicipio();
                String visibilidad = estado;



                tv_nombre_consulta_info_neg.setText(negocios.getNombre());
                tv_descripcion_consulta_info_neg.setText(negocios.getDescripcion());
                tv_telefono_consulta_info_neg.setText(negocios.getTelefono1());
                tv_ubicacion__consulta_info_neg.setText(negocios.getMaps_url());
                tv_correo_consulta_info_neg.setText(negocios.getCorreo());
                tv_sitio_web_consulta_info_neg.setText(negocios.getSitio_web());
                tv_dir_completa_consulta_info_neg.setText(direccion);
                tv_estado_consulta_info_neg.setText(estado);

                if (visibilidad.equals("0")){
                    tv_visibilidad_texto.setVisibility(View.VISIBLE);
                }

                /*
                 * Logica para cargar imagen desde WS por URL
                 */
                String logo_url = "http://192.168.1.69/PachucaService/api_usuarios/" + negocios.getLogo();

                conexionWebServiceLogoUrl(logo_url);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Lo sentimos ocurrió un problema, intentalo mas tarde!", Toast.LENGTH_SHORT).show();
                System.out.println();
                progreso.hide();
                tv_visibilidad_texto.setVisibility(View.GONE);
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
                imv_logo_consulta_info_neg.setImageBitmap(response);
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
