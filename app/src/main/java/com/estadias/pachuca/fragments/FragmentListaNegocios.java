package com.estadias.pachuca.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.estadias.pachuca.adapters.CategoriasAdapter;
import com.estadias.pachuca.adapters.NegociosListAdapter;
import com.estadias.pachuca.models.ModelCategorias;
import com.estadias.pachuca.models.ModelNegocios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentListaNegocios.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentListaNegocios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListaNegocios extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    //########## Variables implementadas ##########

    RecyclerView id_recycler_img_negocios; //Inicializa la variable de RV
    ArrayList<ModelNegocios> listaNegocios; //Trae una lista de ModelNegocios

    ProgressDialog progreso; //Para generar una ventana de carga mientras se ejecutan las peticiones

    //Permiten establecer la conexion con el webservice
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    public FragmentListaNegocios() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListaNegocios.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListaNegocios newInstance(String param1, String param2) {
        FragmentListaNegocios fragment = new FragmentListaNegocios();
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
        View view = inflater.inflate(R.layout.fragment_fragment_lista_negocios, container, false);

        listaNegocios = new ArrayList<>(); //Asigna pre-instancia a la lista

        id_recycler_img_negocios = view.findViewById(R.id.id_recycler_img_negocios);
        id_recycler_img_negocios.setLayoutManager(new LinearLayoutManager(this.getContext()));
        id_recycler_img_negocios.setHasFixedSize(true); //Parametros para linear layout

        request = Volley.newRequestQueue(getContext()); //Referenciar

        conexionWebService();

        return view;
    }

    private void conexionWebService() {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

        String URL = "http://7a940a31.ngrok.io/PachucaService/api_usuarios/wsConsultarUsuariosCategoria.php?categoria=Hoteles";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null, this, this); //Procesa la informacion del Json

        request.add(jsonObjectRequest);
    }


    @Override
    public void onResponse(JSONObject response) {

        ModelNegocios negocios = null;

        JSONArray json = response.optJSONArray("usuario");

        //Recorrer la lista
        try{
            for (int i=0; i<json.length(); i++) {//Recorre la lista de datos
                negocios = new ModelNegocios();
                JSONObject jsonObject = null;

                jsonObject = json.getJSONObject(i);

                negocios.setId(jsonObject.optInt("id"));
                negocios.setNombre(jsonObject.optString("nombre"));
                negocios.setDescripcion(jsonObject.optString("descripcion"));
                //imagen
                negocios.setLogo(jsonObject.optString("logo"));

                listaNegocios.add(negocios);

            }

            //Mostrar la informacion en el RecyclerView por medio del Adapter
            progreso.hide();
            NegociosListAdapter adapter = new NegociosListAdapter(listaNegocios, getContext());
            id_recycler_img_negocios.setAdapter(adapter);

        }  catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se establecio la conexion: " +e.toString(), Toast.LENGTH_SHORT).show();
            progreso.hide();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No existe conexion con el Servidor", Toast.LENGTH_SHORT).show();
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
