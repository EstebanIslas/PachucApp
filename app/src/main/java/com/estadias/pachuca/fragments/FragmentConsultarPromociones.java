package com.estadias.pachuca.fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.estadias.pachuca.R;
import com.estadias.pachuca.models.ModelPromociones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentConsultarPromociones.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentConsultarPromociones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentConsultarPromociones extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;


    //""""""""""""""""""" Variables """""""""""""""""""

    TextView tv_titulo_promociones;
    TextView tv_descripcion;

    ProgressDialog progreso; //Para generar una ventana de carga mientras se ejecutan las peticiones

    //Permiten establecer la conexion con el webservice
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    public FragmentConsultarPromociones() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentConsultarPromociones.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentConsultarPromociones newInstance(String param1, String param2) {
        FragmentConsultarPromociones fragment = new FragmentConsultarPromociones();
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
        View view = inflater.inflate(R.layout.fragment_fragment_consultar_promociones, container, false);

        initComponents(view); //Metodo para mapear al xml y guarda a las variables de tipo TV

        request = Volley.newRequestQueue(getContext()); //Se inicializa la variable request

        //Envio de variable ID del otro fragment ConsultarNegocio
        Bundle bundle = this.getArguments();
        final String id_negocio = bundle.get(FragmentConsultarNegocio.ID_NEGOCIO_FIN).toString(); //Id de negocio para consulta de promociones

        conexionWebService(id_negocio);

        return view;
    }

    private void initComponents(View view) {
        tv_titulo_promociones = view.findViewById(R.id.tv_titulo_promociones);
        tv_descripcion = view.findViewById(R.id.tv_descripcion_promociones);
    }


    private void conexionWebService(String id_negocio) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando");
        progreso.show();

        String URL = "http://34a82a4f.ngrok.io/PachucaService/api_promociones/wsConsultarPromociones.php?id=" + id_negocio;

        //new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();

                //Mapear lo que devuelve el Webservice
                ModelPromociones promociones = new ModelPromociones();

                JSONArray json = response.optJSONArray("promocion"); //Separa el array que muestra el json -> "[promocion:"

                JSONObject jsonObject = null; //Se encarga de llenar cada objeto dependiendo de lo que tenga la consulta de json

                try{
                    jsonObject = json.getJSONObject(0);

                    promociones.setId_promo(jsonObject.optInt("id_promo"));
                    promociones.setTitulo(jsonObject.optString("titulo"));
                    promociones.setDescripcion(jsonObject.optString("descripcion"));
                    promociones.setId(jsonObject.optInt("id"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "No se establecio la conexion: " +e.toString(), Toast.LENGTH_SHORT).show();
                    progreso.hide();
                }

                tv_titulo_promociones.setText(promociones.getTitulo());
                tv_descripcion.setText(promociones.getDescripcion());

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
