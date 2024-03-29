package com.estadias.pachuca.fragments;

import android.app.DownloadManager;
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
import com.estadias.pachuca.MainActivity;
import com.estadias.pachuca.R;
import com.estadias.pachuca.models.ModelCodigos;
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
    //tv para mostrar codigo
    TextView tv_codigo_promociones;
    TextView tv_titulo_codigo_promociones;
    //tv para mostrar disponibilidad de codigo
    TextView tv_disponibilidad_promociones;
    TextView tv_titulo2_codigo_promociones;

    //Boton para generar codigo de promocion
    Button btn_generar_codigo_promociones;
    public static String ID_PROMO = "";
    public static String RESPUESTA = "";
    public static String ID_CODIGO = "";
    public static String CODIGO = "";
    public static String ID_CLIENTE = "0";
    public static String ID_CLIENTE_FINAL = "";


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

        SharedPreferences preferences = getActivity().getSharedPreferences("ID_CLIENTE", Context.MODE_PRIVATE);

        String id_clientes = preferences.getString(ID_CLIENTE, ID_CLIENTE);
        ID_CLIENTE_FINAL = id_clientes;

        //Toast.makeText(getContext(), "ID_CLIENTE: " + id_clientes, Toast.LENGTH_SHORT).show();

        conexionWebService(id_negocio);

        //Boton para generar codigo de promocion
        btn_generar_codigo_promociones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generarcodigoWebService(ID_PROMO);
            }
        });

        return view;
    }

    private void initComponents(View view) {
        tv_titulo_promociones = view.findViewById(R.id.tv_titulo_promociones);
        tv_descripcion = view.findViewById(R.id.tv_descripcion_promociones);
        //tv para mostrar codigo
        tv_codigo_promociones = view.findViewById(R.id.tv_codigo_promociones);
        tv_titulo_codigo_promociones = view.findViewById(R.id.tv_titulo_codigo_promociones);
        //tv para mostrar disponibilidad
        tv_titulo2_codigo_promociones = view.findViewById(R.id.tv_titulo2_codigo_promociones);
        tv_disponibilidad_promociones = view.findViewById(R.id.tv_disponibilidad_promociones);

        //Boton para generar codigo de promocion
        btn_generar_codigo_promociones = view.findViewById(R.id.btn_generar_codigo_promociones);
    }


    private void conexionWebService(String id_negocio) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando");
        progreso.show();

        String URL = "https://pachuca.com.mx/webservice/api_promociones/wsConsultarPromocionReciente.php?id=" + id_negocio;

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
                ID_PROMO = String.valueOf(promociones.getId_promo());
                btn_generar_codigo_promociones.setEnabled(true);
                consultaClienteCodigoWebService(ID_PROMO, ID_CLIENTE_FINAL);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No existe conexion con el Servidor", Toast.LENGTH_SHORT).show();
                ID_PROMO = "0";
                btn_generar_codigo_promociones.setEnabled(false);
                System.out.println();
                progreso.hide();
                Log.i("ERROR: ", error.toString());
            }
        });

        request.add(jsonObjectRequest);

    }

    private void consultaClienteCodigoWebService(String idPromo, String idCliente) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Buscando si tienes códigos generados...");
        progreso.show();

        String URL = "https://pachuca.com.mx/webservice/api_codigos/wsConsultarClienteCodigo.php?id_promo="+ idPromo + "&id_cliente=" + idCliente;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();

                //variables que guardan el retorno de la consulta json
                String codigo = "", estado  = "";

                JSONArray json = response.optJSONArray("cliente_codigo"); //Separa el array que muestra el json -> "[cliente_codigo:"

                JSONObject jsonObject = null; //Se encarga de llenar cada objeto dependiendo de lo que tenga la consulta de json

                try{
                    jsonObject = json.getJSONObject(0);
                    codigo = jsonObject.optString("codigo");
                    estado = jsonObject.optString("estado");

                }catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("ERROR: ", e.toString());
                    Toast.makeText(getContext(), "No existen codigos creados", Toast.LENGTH_SHORT).show();
                    progreso.hide();
                }

                tv_titulo_codigo_promociones.setText("Tu código es: ");
                tv_codigo_promociones.setText(codigo);
                tv_titulo2_codigo_promociones.setText("Disponibilidad: ");
                tv_disponibilidad_promociones.setText(estado);
                btn_generar_codigo_promociones.setEnabled(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btn_generar_codigo_promociones.setEnabled(true);
                System.out.println();
                progreso.hide();
                Log.i("ERROR: ", error.toString());
            }
        });

        request.add(jsonObjectRequest);
    }

    private void generarcodigoWebService(final String id_promo) {

        //Toast.makeText(getContext(), "El id_promo es = " + id_promo, Toast.LENGTH_SHORT).show();
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Buscando Códigos Disponibles...");
        progreso.show();

        //Toast.makeText(getContext(), "Id_promo = " + id_promo, Toast.LENGTH_SHORT).show();

        String URL = "https://pachuca.com.mx/webservice/api_codigos/wsSelectFirstCodigos.php?id_promo=" + id_promo;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();

                //Mapear lo que devuelve el Webservice
                ModelCodigos codigos = new ModelCodigos();

                JSONArray json = response.optJSONArray("codigo"); //Separa el array que muestra el json -> "[usuario:" <-

                JSONObject jsonObject = null; //Se encarga de llenar cada objeto dependiendo de lo que tenga la consulta de json

                try{
                    jsonObject = json.getJSONObject(0);

                    codigos.setId_codigo(jsonObject.optInt("id_codigo"));
                    codigos.setCodigo(jsonObject.optString("codigo"));
                    codigos.setId_promo(jsonObject.optInt("id_promo"));
                    codigos.setEstado(jsonObject.optString("estado"));

                    String  codigo, id_promos, estado;

                    ID_CODIGO = String.valueOf(codigos.getId_codigo());
                    CODIGO = String.valueOf(codigos.getCodigo());
                    id_promos = String.valueOf(codigos.getId_promo());
                    estado = String.valueOf(codigos.getEstado());


                    //Toast.makeText(getContext(), "Resultado: " + ID_CODIGO + " " + codigo + " "
                      //      + id_promos + " " + estado, Toast.LENGTH_SHORT).show();
                    RESPUESTA = "si";

                    if (RESPUESTA == "si"){
                        //Toast.makeText(getContext(), "Se encontraron resultados", Toast.LENGTH_SHORT).show();
                        insertarClienteCodigoWebService(ID_CODIGO);

                    }else{
                        Toast.makeText(getContext(), "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException error) {
                    Toast.makeText(getContext(), "No se obtuvo ningún código", Toast.LENGTH_SHORT).show();
                    System.out.println();
                    progreso.hide();
                    Log.i("ERROR: ", error.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se encontraron códigos disponibles intente en otra ocasión", Toast.LENGTH_SHORT).show();
                System.out.println();
                progreso.hide();
                Log.i("ERROR: ", error.toString());
                RESPUESTA = "no";
            }
        });

        request.add(jsonObjectRequest);

    }

    private void insertarClienteCodigoWebService(String id_codigo) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Asignando Código...");
        progreso.show();

        //Toast.makeText(getContext(), "Id_codigo: " + id_codigo, Toast.LENGTH_SHORT).show();

        String URL = "https://pachuca.com.mx/webservice/api_codigos/wsInsertarClienteCodigo.php?id_cliente="+ ID_CLIENTE_FINAL +"&id_codigo="+ id_codigo;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();
                //Toast.makeText(getContext(), "Asignando Código", Toast.LENGTH_SHORT).show();
                actualizarCodigoAsignadoWebService();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se pudo asignar el codigo", Toast.LENGTH_SHORT).show();
                System.out.println();
                progreso.hide();
                Log.i("ERROR: ", error.toString());
            }
        });
        request.add(jsonObjectRequest);
    }

    private void actualizarCodigoAsignadoWebService() {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Actualizando código...");
        progreso.show();

        Toast.makeText(getContext(), "ID_CODIGO: " + ID_CODIGO + " CODIGO: "+ CODIGO, Toast.LENGTH_SHORT).show();

        String URL = "https://pachuca.com.mx/webservice/api_codigos/wsUpdateCodigos.php?id_codigo=" + ID_CODIGO + "&codigo=" + CODIGO;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();
                Toast.makeText(getContext(), "Tu código ha sido generado", Toast.LENGTH_SHORT).show();

                tv_titulo_codigo_promociones.setText("Tu código es: ");
                tv_codigo_promociones.setText(CODIGO);
                tv_titulo2_codigo_promociones.setText("Disponibilidad: ");
                tv_disponibilidad_promociones.setText("utilizable");
                btn_generar_codigo_promociones.setEnabled(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se pudo finalizar tu código", Toast.LENGTH_SHORT).show();
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
