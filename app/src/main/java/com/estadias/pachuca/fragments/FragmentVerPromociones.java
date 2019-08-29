package com.estadias.pachuca.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.estadias.pachuca.R;
import com.estadias.pachuca.adapters.PromocionesListAdapter;
import com.estadias.pachuca.models.ModelNegocios;
import com.estadias.pachuca.models.ModelPromociones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentVerPromociones.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentVerPromociones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentVerPromociones extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //########## Variables implementadas ##########

    RecyclerView id_recycler_ver_promociones; //Inicializa la variable de RV
    ArrayList<ModelPromociones> listaPromociones; //Trae una lista de ModelNegocios

    ProgressDialog progreso; //Para generar una ventana de carga mientras se ejecutan las peticiones

    //Permiten establecer la conexion con el webservice
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    //Variable para el onClickListener
    public static String ID_NEGOCIO = "0";
    public static String ID_NEGOCIO_FINAL = "";

    public static final String ID_PROMO = "1";


    public FragmentVerPromociones() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentVerPromociones.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentVerPromociones newInstance(String param1, String param2) {
        FragmentVerPromociones fragment = new FragmentVerPromociones();
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
        View view = inflater.inflate(R.layout.fragment_ver_promociones, container, false);

        listaPromociones = new ArrayList<>(); // Asigna pre-instancia a la lista

        id_recycler_ver_promociones = view.findViewById(R.id.id_recycler_ver_promociones);
        id_recycler_ver_promociones.setLayoutManager(new LinearLayoutManager(this.getContext()));
        id_recycler_ver_promociones.setHasFixedSize(true); //Parametros para linear layout

        request = Volley.newRequestQueue(getContext()); //Referenciar

        //Traer el id del negocio mediante el archivo sharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("ID_USUARIO", Context.MODE_PRIVATE);

        String id_usuario = preferences.getString(ID_NEGOCIO, ID_NEGOCIO);
        ID_NEGOCIO_FINAL = id_usuario;

        //Toast.makeText(getContext(), "ID: " + id_usuario, Toast.LENGTH_SHORT).show();

        conexionWebService(ID_NEGOCIO_FINAL);

        return view;
    }

    private void conexionWebService(String id_negocio) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Conectando...");
        progreso.show();

        String url = "https://pachuca.com.mx/webservice/api_promociones/wsConsultarPromociones.php?id=" + id_negocio;

        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,url,null,this,this);

        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        ModelPromociones promociones = null;

        JSONArray json =  response.optJSONArray("promocion");

        //Recorre la lista de json

        try{
            for (int i =0; i<json.length(); i++){
                promociones = new ModelPromociones();

                JSONObject jsonObject = null;

                jsonObject = json.getJSONObject(i);

                promociones.setId_promo(jsonObject.optInt("id_promo"));
                promociones.setTitulo(jsonObject.optString("titulo"));
                promociones.setDescripcion(jsonObject.optString("descripcion"));
                promociones.setId(jsonObject.optInt("id"));

                listaPromociones.add(promociones);

            }

            progreso.hide();
            PromocionesListAdapter adapter = new PromocionesListAdapter(listaPromociones);

            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String datos_promocion = listaPromociones.get(id_recycler_ver_promociones.getChildAdapterPosition(view)).getId_promo().toString();
                    String id_promocion = datos_promocion;

                    //Toast.makeText(getContext(), "El id es: " + id_promocion, Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle(); //Paquete que guarda la variable String final
                    bundle.putString(ID_PROMO, id_promocion);
                    //Toast.makeText(getContext(), bundle.toString()+ " " + NOMBRE_NEGOCIO, Toast.LENGTH_SHORT).show();

                    //Enviar de un fragment a otro
                    Fragment consultarCodigo = new FragmentVerCodigos(); //Objeto que llama al fragment que se le enviaran los datos
                    consultarCodigo.setArguments(bundle); //Se envia como parametro el valor guardado

                    FragmentTransaction ft = getFragmentManager().beginTransaction(); //Objeto creado para remplazar el fragment
                    ft.replace(R.id.content_main_negocio, consultarCodigo); //se mapea el id del lugar donde de remplazara

                    /* No cierra el fragment anterior*/
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);

                    ft.commit();
                }
            });

            id_recycler_ver_promociones.setAdapter(adapter);

        } catch (JSONException err){
            err.printStackTrace();
            Toast.makeText(getContext(), "No se establecio la conexion: " +err.toString(), Toast.LENGTH_SHORT).show();
            progreso.hide();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Parece que no hay promociones generadas, intentalo en otra ocasión", Toast.LENGTH_SHORT).show();
        System.out.println();
        progreso.hide();
        Log.i("ERROR: ", error.toString());
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
