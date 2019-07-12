package com.estadias.pachuca;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.estadias.pachuca.models.ModelClientes;
import com.estadias.pachuca.models.ModelLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ActivityLogin extends AppCompatActivity implements Response.Listener<JSONObject>
        , Response.ErrorListener{


    //Se inicializan las variables de tipo ET
    EditText edt_correo_login, edt_password_login;
    Button btn_ingresar, btn_registrar_usuario;

    ProgressDialog progreso; //Para generar una ventana de carga mienteas se ejecutan las peticiones

    //Permiten establecer la conexion con el webservice
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    //Guardar el id del cliente para usarlo en otro fragment

    public static final String ID_CLIENTE = "10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents(); //Obtener los valores del xml a las variables de tipo ya creadas

        request = Volley.newRequestQueue(this);

        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conexionWebService();
            }
        });

        btn_registrar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eleccionRollOnclick(view);
            }
        });
    }

    private void initComponents() {
        edt_correo_login = findViewById(R.id.edt_correo_login);
        edt_password_login = findViewById(R.id.edt_password_login);
        btn_ingresar = findViewById(R.id.btn_ingresar_login);
        btn_registrar_usuario = findViewById(R.id.btn_registrar_login);
    }

    private void conexionWebService() {
        progreso = new ProgressDialog(this); //Se crea nuevo objeto
        progreso.setMessage("Ingresando");
        progreso.show(); //muestra al usuario final el progress dialog

        String password = ActivityLogin.md5(edt_password_login.getText().toString());

        String URL = "http://376d089f.ngrok.io/PachucaService/api_login/wsLogin.php?correo=" + edt_correo_login.getText().toString() +
                "&password=" + password;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide(); //Oculta el progress si se ejecuta este metodo

        //Toast.makeText(this, "Mensaje: "+ response, Toast.LENGTH_SHORT).show();

        //Traemos la clase ModelCliente para guardar lo que retorna el Json
        ModelLogin login = new ModelLogin();

        //Variables para validar usuario

        String valid_correo, valid_password, nueva_password, rol;

        JSONArray json = response.optJSONArray("login"); // Busca el arreglo y como parametro el nombre del arreglo Json

        JSONObject jsonObject = null;

        try{

            jsonObject= json.getJSONObject(0);
            //Agrega el json a las variables
            login.setId(jsonObject.optInt("id"));
            login.setCorreo(jsonObject.optString("correo"));
            login.setPassword(jsonObject.optString("password"));
            login.setRol(jsonObject.optString("rol"));

        }catch(JSONException err){
            err.printStackTrace();
        }

        //Se agregan a las variables de validacion para hacer el proceso de logueo
        valid_correo = login.getCorreo();
        valid_password = login.getPassword();
        nueva_password = edt_password_login.getText().toString();
        rol = login.getRol();

        boolean comparar = passwordMD5(valid_password, nueva_password);

        //Toast.makeText(this, "Mensaje: "+ comparar + valid_password, Toast.LENGTH_SHORT).show();

        if (valid_correo.equals(edt_correo_login.getText().toString()) && comparar == true){ //Compara el metodo password md5 que recibe como parametros el password y un boolean

            if (rol.equals("cliente")) { //Enviar a su respectivo Drawer segun sea el rol

                Toast.makeText(this, "Bienvenido!!", Toast.LENGTH_SHORT).show();

                String id_client = Integer.toString(login.getId());//Envia el id para usarlo en otro fragment

                //Envia a la otra actividad
                Intent main_activity = new Intent(this, MainActivity.class);//Ayuda a crear funciones para pasar de una pantalla a otra

                main_activity.putExtra(ID_CLIENTE, id_client); //Enviar variable

                main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //Para que desaparezca la otra actividad anterior en este caso ActivityLogin
                startActivity(main_activity);
            } else if (rol.equals("user")){

                Toast.makeText(this, "Usted es negocio!!", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "El usuario o contraseña no coinciden!!", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(this, "No se puede conectar al servidor!!", Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
    }


    public void eleccionRollOnclick(View view){
        Intent roll_registro = new Intent(this, ActivityRoll.class);//Ayuda a crear fucniones para pasar de una pantalla a otra
        //reg_empresa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(roll_registro);
    }


    /**
     * Método para comparar el password de la base de datos con el que ingresa el cliente a la app
     * @param orig password encriptado
     * @param compare password que ingresa el usuario final
     * @return
     */
    private static boolean passwordMD5(String orig, String compare){
        String md5=null;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(compare.getBytes());
            byte[] digest = md.digest();
            md5 = new BigInteger(1, digest).toString(16);

            return md5.equals(orig);
        }catch(NoSuchAlgorithmException ex){
            return false;
        }
    }

    private static String getHash(String txt, String hashType) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /* Retorna un hash MD5 a partir de un texto */
    private static String md5(String txt) {
        return getHash(txt, "MD5");
    }

    /* Retorna un hash SHA1 a partir de un texto */
    private static String sha1(String txt) {
        return getHash(txt, "SHA1");
    }
}
