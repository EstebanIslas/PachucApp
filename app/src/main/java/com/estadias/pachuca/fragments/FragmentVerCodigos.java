package com.estadias.pachuca.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.estadias.pachuca.R;
import com.estadias.pachuca.adapters.CodigosGeneradosAdapter;
import com.estadias.pachuca.models.ModelCodigos;
import com.estadias.pachuca.models.ModelPromociones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentVerCodigos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentVerCodigos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentVerCodigos extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    //########## Variables implementadas ##########

    RecyclerView id_recycler_ver_codigos_canjeados; //Inicializa la variable de RV
    TextView tv_total_codigos_ver_codigos;
    ArrayList<ModelCodigos> listaCodigos; //Trae una lista de ModelNegocios

    ProgressDialog progreso; //Para generar una ventana de carga mientras se ejecutan las peticiones

    //Permiten establecer la conexion con el webservice
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;
    private static String ID_PROMO = "";

    // Variables finales que se envian a otro fragment
    public static final String CODIGO = "1";
    public static final String USUARIO = "1";
    public static final String ID_PROMOCION = "0";

    //Variables para eliminar las promociones.
    private static String RESULT_RV = " "; // Para saber si la consulta del recycler esta vacia
    Button btn_eliminar_promociones;



    public FragmentVerCodigos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentVerCodigos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentVerCodigos newInstance(String param1, String param2) {
        FragmentVerCodigos fragment = new FragmentVerCodigos();
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
        View view = inflater.inflate(R.layout.fragment_ver_codigos, container, false);

        tv_total_codigos_ver_codigos = view.findViewById(R.id.tv_total_codigos_ver_codigos);

        listaCodigos = new ArrayList<>(); // Asigna pre-instancia a la lista

        id_recycler_ver_codigos_canjeados= view.findViewById(R.id.id_recycler_ver_codigos_canjeados);
        id_recycler_ver_codigos_canjeados.setLayoutManager(new LinearLayoutManager(this.getContext()));
        id_recycler_ver_codigos_canjeados.setHasFixedSize(true); //Parametros para linear layout

        btn_eliminar_promociones = view.findViewById(R.id.btn_eliminar_promocion);

        request = Volley.newRequestQueue(getContext()); //Referenciar

        //Envio de parametro del otro fragment ListaCategorias
        Bundle bundle = this.getArguments();
        String id_promo = bundle.get(FragmentVerPromociones.ID_PROMO).toString();
        ID_PROMO = id_promo;

        totaldeCodigosWebService(id_promo);

        listaCodigosWebService(id_promo);

        btn_eliminar_promociones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RESULT_RV == "1"){ //Si el recycler esta vacio solo eliminara los codigos y la promocion porque no hay cliente_codigo
                    eliminarCodigosWebService(ID_PROMO);
                }else if (RESULT_RV == "0"){
                    eliminarClienteCodigoWebService(ID_PROMO);
                }
            }
        });

        return view;
    }

    private void eliminarClienteCodigoWebService(String id_promo) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Eliminando Códigos Generados por los clientes...");
        progreso.show();

        String URL = "https://pachuca.com.mx/webservice/api_promociones/wsEliminarClienteCodigo.php?id_promo="+id_promo;

        stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();

                if (response.trim().equalsIgnoreCase("elimina")){
                    Toast.makeText(getContext(), "Códigos Generados por los clientes eliminados!!!", Toast.LENGTH_SHORT).show();
                    eliminarCodigosWebService(ID_PROMO);
                }else {
                    Toast.makeText(getContext(), "Ocurrió un problema al eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Toast.makeText(getContext(), "Ocurrio un problema al eliminar los codigos generados, intentelo en otra ocasión" +error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("Error",error.toString());
            }
        });
        request.add(stringRequest);
    }

    private void eliminarCodigosWebService(String id_promo) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Eliminando Códigos...");
        progreso.show();

        String URL = "https://pachuca.com.mx/webservice/api_promociones/wsEliminarCodigos.php?id_promo="+id_promo;

        stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();

                if (response.trim().equalsIgnoreCase("elimina")){
                    Toast.makeText(getContext(), "Códigos Eliminados!!", Toast.LENGTH_SHORT).show();
                    eliminarPromocionWebService(ID_PROMO);
                } else {
                    Toast.makeText(getContext(), "Ocurrió un problema al eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Toast.makeText(getContext(), "Ocurrio un problema al eliminar los codigos, intentelo en otra ocasión" +error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("Error",error.toString());
            }
        });
        request.add(stringRequest);
    }

    private void eliminarPromocionWebService(String id_promo) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Eliminando Promoción...");
        progreso.show();

        String URL = "https://pachuca.com.mx/webservice/api_promociones/wsEliminarPromociones.php?id_promo="+id_promo;

        stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();

                if (response.trim().equalsIgnoreCase("elimina")){
                    Toast.makeText(getContext(), "Promoción Eliminada!!", Toast.LENGTH_SHORT).show();
                    totaldeCodigosWebService(ID_PROMO);
                    listaCodigosWebService(ID_PROMO);
                    btn_eliminar_promociones.setEnabled(false);
                } else {
                    Toast.makeText(getContext(), "Ocurrió un problema al eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Toast.makeText(getContext(), "Ocurrio al eliminar la promocion, intentelo en otra ocasión" +error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("Error",error.toString());
            }
        });
        request.add(stringRequest);
    }

    private void totaldeCodigosWebService(String id_promo) {

        String URL = "https://pachuca.com.mx/webservice/api_codigos/wsTotalCodigos.php?id_promo="+id_promo;

        //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Mapear lo que devuelve el Webservice
                String total = "";

                JSONArray json = response.optJSONArray("promocion"); //Separa el array que muestra el json -> "[usuario:"

                JSONObject jsonObject = null; //Se encarga de llenar cada objeto dependiendo de lo que tenga la consulta de json

                try {
                    jsonObject = json.getJSONObject(0);

                    total = jsonObject.optString("total_registros");

                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "No se establecio la conexion: " +e.toString(), Toast.LENGTH_SHORT).show();
                }
                tv_total_codigos_ver_codigos.setText(total);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No existe conexion con el Servidor", Toast.LENGTH_SHORT).show();
                System.out.println();
                Log.i("ERROR: ", error.toString());
            }
        });

        request.add(jsonObjectRequest);
    }

    private void listaCodigosWebService(String id_promo) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

        String URL = "https://pachuca.com.mx/webservice/api_codigos/wsSeleccionarCodigosUtilizados.php?id_promo="+id_promo;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null, this, this); //Procesa la informacion del Json

        request.add(jsonObjectRequest);
    }


    @Override
    public void onResponse(JSONObject response) {

        ModelCodigos codigos = null;

        JSONArray json = response.optJSONArray("promo_codigo");

        //Recorre la lista
        try{
            for (int i = 0; i<json.length(); i++){
                codigos = new ModelCodigos();
                JSONObject jsonObject = null;

                jsonObject = json.getJSONObject(i);

                codigos.setCodigo(jsonObject.optString("codigo"));
                codigos.setEstado(jsonObject.optString("estado"));
                codigos.setNombre(jsonObject.optString("nombre"));

                listaCodigos.add(codigos);
            }

            //Mostrar la informacion en el RecyclerView por medio del Adapter
            progreso.hide();
            CodigosGeneradosAdapter adapter = new CodigosGeneradosAdapter(listaCodigos);

            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //String datos_promocion = listaPromociones.get(id_recycler_ver_promociones.getChildAdapterPosition(view)).getId_promo().toString();
                    String codigo_save = listaCodigos.get(id_recycler_ver_codigos_canjeados.getChildAdapterPosition(view)).getCodigo();
                    String codigo = codigo_save;

                    String id_promo = ID_PROMO;

                    //Toast.makeText(getContext(), "Datos: " + codigo_save + " " + codigo, Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle(); //Paquete que guarda la variable String final
                    bundle.putString(ID_PROMOCION, id_promo);
                    bundle.putString(CODIGO, codigo);

                    //Toast.makeText(getContext(), "Datos: " + bundle.toString(), Toast.LENGTH_LONG).show();

                    //Enviar de un fragment a otro
                    Fragment enviarDatos = new FragmentCanjearCodigo(); //Objeto que llama al fragment que se le enviaran los datos
                    enviarDatos.setArguments(bundle); //Se envia como parametro el valor guardado

                    FragmentTransaction ft = getFragmentManager().beginTransaction(); //Objeto creado para remplazar el fragment
                    ft.replace(R.id.content_main_negocio, enviarDatos); //se mapea el id del lugar donde de remplazara

                    /* No cierra el fragment anterior*/
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);

                    ft.commit();

                }
            });
            RESULT_RV = "0";// Significa que el recycler esta vacio
            id_recycler_ver_codigos_canjeados.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se establecio la conexion: " +e.toString(), Toast.LENGTH_SHORT).show();
            progreso.hide();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Parece que no hay códigos generados por los clientes, intentalo en otra ocasión", Toast.LENGTH_SHORT).show();
        System.out.println();
        progreso.hide();
        Log.i("ERROR: ", error.toString());
        RESULT_RV = "1";// Significa que el recycler esta vacio
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
