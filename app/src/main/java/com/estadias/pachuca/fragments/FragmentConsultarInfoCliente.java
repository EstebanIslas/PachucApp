package com.estadias.pachuca.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.estadias.pachuca.models.ModelClientes;
import com.estadias.pachuca.models.ModelCodigos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentConsultarInfoCliente.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentConsultarInfoCliente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentConsultarInfoCliente extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    /* ----- Variables ----- */
    public static String ID_CLIENTE = "0";
    public static String ID_CLIENTE_FINAL = "";

    TextView tv_nombre_consulta_info_cliente;
    TextView tv_correo_consulta_info_cliente;
    TextView tv_clave_ine_consulta_info_cliente;

    ProgressDialog progreso; //Para generar una ventana de carga mientras se ejecutan las peticiones

    //Permiten establecer la conexion con el webservice
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    public FragmentConsultarInfoCliente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentConsultarInfoCliente.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentConsultarInfoCliente newInstance(String param1, String param2) {
        FragmentConsultarInfoCliente fragment = new FragmentConsultarInfoCliente();
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
        View view = inflater.inflate(R.layout.fragment_consultar_info_cliente, container, false);

        request = Volley.newRequestQueue(getContext());

        //Documento Shared que trae el id del cliente
        SharedPreferences prefe = getActivity().getSharedPreferences("ID_CLIENTE", Context.MODE_PRIVATE);

        String id_cliente = prefe.getString(ID_CLIENTE, ID_CLIENTE);
        ID_CLIENTE_FINAL = id_cliente;

        //Toast.makeText(getContext(), "ID_CLIENTE: " + id_cliente, Toast.LENGTH_LONG).show();

        initComponents(view);

        conexionWebService(ID_CLIENTE_FINAL);

        return view;
    }


    private void initComponents(View view) {
        tv_nombre_consulta_info_cliente = view.findViewById(R.id.tv_nombre_consulta_info_cliente);
        tv_correo_consulta_info_cliente = view.findViewById(R.id.tv_correo_consulta_info_cliente);
        tv_clave_ine_consulta_info_cliente = view.findViewById(R.id.tv_clave_ine_consulta_info_cliente);
    }

    private void conexionWebService(String id_cliente) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando");
        progreso.show();

        String URL = "https://pachuca.com.mx/webservice/api_clientes/wsSelectOneCliente.php?id_cliente="+ id_cliente;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progreso.hide();

                //Se mapea los datos por medio del modelo
                ModelClientes clientes = new ModelClientes();

                JSONArray json = response.optJSONArray("cliente"); //Separa el array que muestra el json -> "[cliente:"

                JSONObject jsonObject = null; //Se encarga de llenar cada objeto dependiendo de lo que tenga la consulta de json

                try {

                    jsonObject = json.getJSONObject(0);

                    clientes.setId_cliente(jsonObject.optInt("id_cliente"));
                    clientes.setNombre(jsonObject.optString("nombre"));
                    clientes.setCorreo(jsonObject.optString("correo"));
                    clientes.setClave_ine(jsonObject.optString("clave_ine"));


                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "No se establecio la conexion: " +e.toString(), Toast.LENGTH_SHORT).show();
                    progreso.hide();
                }

                tv_nombre_consulta_info_cliente.setText(clientes.getNombre());
                tv_correo_consulta_info_cliente.setText(clientes.getCorreo());
                tv_clave_ine_consulta_info_cliente.setText(clientes.getClave_ine());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Lo sentimos ocurrió un problema, intentalo mas tarde!", Toast.LENGTH_SHORT).show();
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
