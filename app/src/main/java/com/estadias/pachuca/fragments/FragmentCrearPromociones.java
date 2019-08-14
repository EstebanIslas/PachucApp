package com.estadias.pachuca.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCrearPromociones.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCrearPromociones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCrearPromociones extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;


    //""""""""""""""""""" Variables """""""""""""""""""
    EditText edt_titulo_crear_promociones;
    EditText edt_descripcion_crear_promociones;
    EditText edt_numero_codigos_crear_promociones;
    TextView tv_numero_codigos_crear_promociones;

    Button btn_insertar_promociones;
    Button btn_insertar_codigos;

    ProgressDialog progreso; //Para generar una ventana de carga mientras se ejecutan las peticiones

    //Permiten establecer la conexion con el webservice
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    //Variables finales para guardar datos sql
    public static String ID_USUARIO = "0";
    public static String ID_USUARIO_FINAL = "";


    public FragmentCrearPromociones() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCrearPromociones.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCrearPromociones newInstance(String param1, String param2) {
        FragmentCrearPromociones fragment = new FragmentCrearPromociones();
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
        View view = inflater.inflate(R.layout.fragment_crear_promociones, container, false);

        initComponents(view);

        //Deshabilita la vista de los edt y botones
        tv_numero_codigos_crear_promociones.setVisibility(View.GONE);
        edt_numero_codigos_crear_promociones.setVisibility(View.GONE);
        btn_insertar_codigos.setVisibility(View.GONE);

        request = Volley.newRequestQueue(getContext()); //Se inicializa la variable request

        SharedPreferences preferences = getActivity().getSharedPreferences("ID_USUARIO", Context.MODE_PRIVATE);

        String id_clientes = preferences.getString(ID_USUARIO, ID_USUARIO);
        ID_USUARIO_FINAL = id_clientes;

        //Toast.makeText(getContext(), "ID_USUARIO: " + ID_USUARIO_FINAL, Toast.LENGTH_SHORT).show();

        btn_insertar_promociones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarPromocionesWebService(ID_USUARIO_FINAL);
            }
        });


        return view;
    }



    private void initComponents(View view) {
        edt_titulo_crear_promociones = view.findViewById(R.id.edt_titulo_crear_promociones);
        edt_descripcion_crear_promociones = view.findViewById(R.id.edt_descripcion_crear_promociones);
        edt_numero_codigos_crear_promociones = view.findViewById(R.id.edt_numero_codigos_crear_promociones);
        tv_numero_codigos_crear_promociones = view.findViewById(R.id.tv_numero_codigos_crear_promociones);

        btn_insertar_promociones = view.findViewById(R.id.btn_insertar_promocion);
        btn_insertar_codigos = view.findViewById(R.id.btn_insertar_codigos);
    }


    private void insertarPromocionesWebService(String id_usuario) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Creando Promoción...");
        progreso.show();

        boolean valida;

        if (edt_titulo_crear_promociones.getText().toString().isEmpty() || edt_descripcion_crear_promociones.getText().toString().isEmpty()){
            valida = false;
            Toast.makeText(getContext(), "Existen campos vacíos!", Toast.LENGTH_SHORT).show();
        }else {
            valida = true;
        }

        if (valida == true) {

            String URL = "https://pachuca.com.mx/webservice/api_promociones/wsInsertarPromociones.php?titulo=" + edt_titulo_crear_promociones.getText()
                    + "&descripcion=" + edt_descripcion_crear_promociones.getText() + "&id=" + id_usuario;

            URL = URL.replace(" ", "%20");

            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progreso.hide();

                    //Mapear lo que devuelve el Webservice
                    ModelPromociones promociones = new ModelPromociones();

                    JSONArray json = response.optJSONArray("promocion"); //Separa el array que muestra el json -> "[promocion:" <-

                    JSONObject jsonObject = null; //Se encarga de llenar cada objeto dependiendo de lo que tenga la consulta de json

                    try {
                        jsonObject = json.getJSONObject(0);

                        promociones.setId_promo(jsonObject.optInt("id_promo"));
                        promociones.setTitulo(jsonObject.optString("titulo"));
                        promociones.setDescripcion(jsonObject.optString("descripcion"));
                        promociones.setId(jsonObject.optInt("id"));

                        final String id_promo, titulo, descripcion, id_usuario;//Variables donde se guardara el get del modelo

                        id_promo = String.valueOf(promociones.getId_promo());
                        titulo = String.valueOf(promociones.getTitulo());
                        descripcion = String.valueOf(promociones.getDescripcion());
                        id_usuario = String.valueOf(promociones.getId());

                        //Toast.makeText(getContext(), id_promo + " " + titulo + " " + descripcion + " " + id, Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
                        dialogo1.setTitle("Crear Códigos");
                        dialogo1.setMessage("¿Deseas crear codigos aleatorios para esta promoción?");
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                tv_numero_codigos_crear_promociones.setVisibility(View.VISIBLE);
                                edt_numero_codigos_crear_promociones.setVisibility(View.VISIBLE);
                                btn_insertar_codigos.setVisibility(View.VISIBLE);

                                edt_titulo_crear_promociones.setEnabled(false);
                                edt_descripcion_crear_promociones.setEnabled(false);
                                btn_insertar_promociones.setEnabled(false);

                                btn_insertar_codigos.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        generarCodigosWebService(id_promo);
                                    }
                                });
                            }
                        });
                        dialogo1.setNegativeButton("No crear", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                Toast.makeText(getContext(), "Promoción creada sin códigos!", Toast.LENGTH_SHORT).show();
                                edt_titulo_crear_promociones.setText("");
                                edt_descripcion_crear_promociones.setText("");
                                btn_insertar_promociones.setText("");
                            }
                        });
                        dialogo1.show();

                    } catch (JSONException error) {
                        Toast.makeText(getContext(), "No se obtuvo ninguna promocion creada", Toast.LENGTH_SHORT).show();
                        System.out.println();
                        progreso.hide();
                        Log.i("ERROR: ", error.toString());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "No existe conexion con el servidor", Toast.LENGTH_SHORT).show();
                    System.out.println();
                    progreso.hide();
                    Log.i("ERROR: ", error.toString());
                }
            });

            request.add(jsonObjectRequest);

        }else{
            Toast.makeText(getContext(), "No se puede crear promoción!!", Toast.LENGTH_SHORT).show();
            progreso.hide();
        }

    }

    private void generarCodigosWebService(String id_promo) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Generando Códigos...");
        progreso.show();
        progreso.setCancelable(false);

        boolean valida;

        if (edt_numero_codigos_crear_promociones.getText().toString().isEmpty()){
            valida = false;
            edt_numero_codigos_crear_promociones.setError("Campo vacío");
        }else {
            valida= true;
        }

        if (valida == true) {
            int total_numeros = Integer.parseInt(edt_numero_codigos_crear_promociones.getText().toString());

            for (int i = 1; i <= total_numeros; i++) {
                String random = UUID.randomUUID().toString().toUpperCase().substring(0, 6); //Genera cadena de 6 caracteres aleatorios
                String URL = "https://pachuca.com.mx/webservice/api_codigos/wsInsertarCodigos.php?codigo=" + random + "&id_promo=" + id_promo;

                //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progreso.hide();
                        progreso.setCancelable(true);

                        ModelCodigos codigos = new ModelCodigos();

                        JSONArray json = response.optJSONArray("codigo"); //Separa el array que muestra el json -> "[promocion:" <-

                        JSONObject jsonObject = null; //Se encarga de llenar cada objeto dependiendo de lo que tenga la consulta de json

                        try {
                            jsonObject = json.getJSONObject(0);

                            codigos.setId_codigo(jsonObject.optInt("id_codigo"));
                            codigos.setCodigo(jsonObject.optString("codigo"));
                            codigos.setEstado(jsonObject.optString("estado"));
                            codigos.setId_promo(jsonObject.optInt("id_promo"));

                            String codigo = String.valueOf(codigos.getCodigo());

                            Toast.makeText(getContext(), "Generando códigos " + codigo, Toast.LENGTH_SHORT).show();

                            tv_numero_codigos_crear_promociones.setVisibility(View.GONE);
                            edt_numero_codigos_crear_promociones.setVisibility(View.GONE);
                            btn_insertar_codigos.setVisibility(View.GONE);

                            edt_titulo_crear_promociones.setEnabled(true);
                            edt_descripcion_crear_promociones.setEnabled(true);
                            btn_insertar_promociones.setEnabled(true);

                            edt_titulo_crear_promociones.setText("");
                            edt_descripcion_crear_promociones.setText("");

                            tv_numero_codigos_crear_promociones.setText("Códigos generados");
                            tv_numero_codigos_crear_promociones.setVisibility(View.VISIBLE);

                        } catch (JSONException error) {
                            Toast.makeText(getContext(), "No se obtuvo ninguna promocion creada", Toast.LENGTH_SHORT).show();
                            System.out.println();
                            progreso.hide();
                            Log.i("ERROR: ", error.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "No se pudo generar los codigos", Toast.LENGTH_SHORT).show();
                        System.out.println();
                        progreso.hide();
                        progreso.setCancelable(true);
                        Log.i("ERROR: ", error.toString());
                    }
                });

                request.add(jsonObjectRequest);
            }
        }else {
            Toast.makeText(getContext(), "No se pueden generar los codigos", Toast.LENGTH_SHORT).show();
            progreso.hide();
        }

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
