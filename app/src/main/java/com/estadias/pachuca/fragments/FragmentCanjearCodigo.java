package com.estadias.pachuca.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.android.volley.toolbox.Volley;
import com.estadias.pachuca.R;
import com.estadias.pachuca.models.ModelCodigos;
import com.estadias.pachuca.models.ModelPromociones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCanjearCodigo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCanjearCodigo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCanjearCodigo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    //""""""""""""""""""" Variables """""""""""""""""""
    TextView tv_codigo_canjear_codigo;
    TextView tv_nombre_canjear_codigo;
    TextView tv_correo_canjear_codigo;
    TextView tv_estado_canjear_codigo;
    TextView tv_clave_ine_canjear_codigo;

    Button btn_canjear_codigo;

    ProgressDialog progreso; //Para generar una ventana de carga mientras se ejecutan las peticiones

    //Permiten establecer la conexion con el webservice
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private static String ID_PROMO = "";




    public FragmentCanjearCodigo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCanjearCodigo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCanjearCodigo newInstance(String param1, String param2) {
        FragmentCanjearCodigo fragment = new FragmentCanjearCodigo();
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
        View view = inflater.inflate(R.layout.fragment_canjear_codigo, container, false);

        initComponents(view);

        request = Volley.newRequestQueue(getContext()); //Se inicializa la variable request

        //Envio de variable ID del otro fragment ConsultarNegocio
        Bundle bundle = this.getArguments();
        final String codigo = bundle.get(FragmentVerCodigos.CODIGO).toString(); //Id de negocio para consulta de promociones
        final String id_promo = bundle.get(FragmentVerCodigos.ID_PROMOCION).toString();

        ID_PROMO = id_promo;
        //Toast.makeText(getContext(), "Datos: " + codigo + " " + id_promo, Toast.LENGTH_SHORT).show();

        conexionWebService(id_promo, codigo);

        return view;
    }


    private void initComponents(View view) {
        tv_nombre_canjear_codigo = view.findViewById(R.id.tv_nombre_canjear_codigo);
        tv_codigo_canjear_codigo = view.findViewById(R.id.tv_codigo_canjear_codigo);
        tv_correo_canjear_codigo = view.findViewById(R.id.tv_correo_canjear_codigo);
        tv_estado_canjear_codigo = view.findViewById(R.id.tv_estado_canjear_codigo);
        tv_clave_ine_canjear_codigo = view.findViewById(R.id.tv_clave_ine_canjear_codigo);

        btn_canjear_codigo = view.findViewById(R.id.btn_canjear_codigo);

    }


    private void conexionWebService(String id_promo,String codigo) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando");
        progreso.show();

        String URL = "https://pachuca.com.mx/webservice/api_codigos/wsSeleccionarClienteCodigoPromocion.php?"
                +"id_promo=" + id_promo + "&codigo=" + codigo;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progreso.hide();

                //Mapear lo que devuelve el Webservice
                ModelCodigos codigos = new ModelCodigos();


                JSONArray json = response.optJSONArray("promo_codigo"); //Separa el array que muestra el json -> "[promocion:"

                JSONObject jsonObject = null; //Se encarga de llenar cada objeto dependiendo de lo que tenga la consulta de json

                try{
                    jsonObject = json.getJSONObject(0);

                    codigos.setCodigo(jsonObject.optString("codigo"));
                    codigos.setNombre(jsonObject.optString("nombre"));
                    codigos.setCorreo(jsonObject.optString("correo"));
                    codigos.setClave_ine(jsonObject.optString("clave_ine"));
                    codigos.setEstado(jsonObject.optString("estado"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "No se establecio la conexion: " +e.toString(), Toast.LENGTH_SHORT).show();
                    progreso.hide();
                }

                String estado = codigos.getEstado();
                String codigo = codigos.getCodigo();

                final String codigofinal = codigo;

                tv_nombre_canjear_codigo.setText(codigos.getNombre());
                tv_codigo_canjear_codigo.setText(codigo);
                tv_correo_canjear_codigo.setText(codigos.getCorreo());
                tv_estado_canjear_codigo.setText(estado);
                tv_clave_ine_canjear_codigo.setText(codigos.getClave_ine());

                if (estado.equals("utilizable")){
                    btn_canjear_codigo.setVisibility(View.VISIBLE);
                    btn_canjear_codigo.setEnabled(true);
                    btn_canjear_codigo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            actualizarEstadoWebService(codigofinal, ID_PROMO);
                        }
                    });
                }else {
                    btn_canjear_codigo.setVisibility(View.GONE);
                    btn_canjear_codigo.setEnabled(false);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No existe conexion con el Servidor", Toast.LENGTH_SHORT).show();
                btn_canjear_codigo.setEnabled(false);
                btn_canjear_codigo.setVisibility(View.GONE);
                System.out.println();
                progreso.hide();
                Log.i("ERROR: ", error.toString());
            }
        });
        request.add(jsonObjectRequest);
    }

    private void actualizarEstadoWebService(String codigo, String id_promo) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Actualizando código...");
        progreso.show();


        String URL = "https://pachuca.com.mx/webservice/api_codigos/wsUpdateCanjeCodigo.php?codigo=" + codigo  + "&id_promo=" + id_promo;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();
                Toast.makeText(getContext(), "El código ha sido canjeado", Toast.LENGTH_SHORT).show();
                tv_estado_canjear_codigo.setText("canjeado");
                btn_canjear_codigo.setEnabled(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se pudo canjear el código, intente en otra ocasión", Toast.LENGTH_SHORT).show();
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
