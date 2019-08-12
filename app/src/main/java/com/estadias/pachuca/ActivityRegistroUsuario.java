package com.estadias.pachuca;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ActivityRegistroUsuario extends AppCompatActivity{

    //Se inicializan las variables de tipo ET y Botones
    EditText edt_nombre_registro_usuario;
    EditText edt_correo_registro_usuario;
    EditText edt_password_registro_usuario;
    EditText edt_conf_password_ru;
    EditText edt_clave_elector_registro_usuario;

    Button btn_registrar_usuario;

    ProgressDialog progreso; //Para generar una ventana de carga mienteas se ejecutan las peticiones

    /*Permiten establecer la conexion con el webservice*/
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        initComponents(); //Obtener los valores del xml a las variables de tipo ya creadas

        request = Volley.newRequestQueue(this);

        btn_registrar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conexionWebService();
            }
        });

    }

    private void conexionWebService() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Cargando");
        progreso.show();

        String pass, save_password = "";

        pass = edt_password_registro_usuario.getText().toString();
        boolean registro, valida, valida_email;

        if (pass.equals(edt_conf_password_ru.getText().toString())){
            save_password= ActivityRegistroUsuario.md5(edt_conf_password_ru.getText().toString());
            registro = true;
        }else {
            registro = false;
            Toast.makeText(this, "Las contraseñas no coinciden!!", Toast.LENGTH_SHORT).show();
            progreso.hide();
        }

        if (edt_nombre_registro_usuario.getText().toString().isEmpty() || edt_correo_registro_usuario.getText().toString().isEmpty()
                || edt_clave_elector_registro_usuario.getText().toString().isEmpty() || edt_password_registro_usuario.getText().toString().isEmpty()){
            valida = false;
            Toast.makeText(this, "Existen campos vacíos!", Toast.LENGTH_SHORT).show();
        }
        else {
            valida = true;
        }

        if (!validarEmail(edt_correo_registro_usuario.getText().toString())){
            edt_correo_registro_usuario.setError("Email no valido");
            valida_email = false;
        }else {
            valida_email = true;
        }

        if (registro == true && valida == true && valida_email == true) {
                /*String URL = "http://2428ffab.ngrok.io/Webservice/api_clientes/wsJSONInsertarCliente.php?nombre=" + edt_nombre_registro_usuario.getText().toString()
                        + "&correo=" + edt_correo_registro_usuario.getText().toString() + "&password=" + save_password;*/
            String URL= "http://192.168.1.69/PachucaService/api_clientes/wsClientesInsert.php";

            /*
             * Se implementa la clase volley directa para este metodo POST
             */

            final String finalSave_password = save_password;
            stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    progreso.hide();

                    /*El .trim() es en caso de que exista un espacio en el proceso y se llegue a implementar en la cadena*/
                    if (response.trim().equalsIgnoreCase("Registra")){//Si la respuesta del webservice es igual al registra que imprime el echo del script php
                        Toast.makeText(getApplicationContext(), "Se ha realizado el registro", Toast.LENGTH_SHORT).show();

                        Intent login = new Intent(getApplicationContext(), ActivityLogin.class);//Ayuda a crear fucniones para pasar de una pantalla a otra
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(login);

                        progreso.hide();
                        edt_nombre_registro_usuario.setText("");
                        edt_correo_registro_usuario.setText("");
                        edt_password_registro_usuario.setText("");
                        edt_conf_password_ru.setText("");
                        edt_clave_elector_registro_usuario.setText("");
                    }

                    }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progreso.hide();
                    Toast.makeText(getApplicationContext(), "Ocurrio un problema con la conexión del servidor " +error.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("Error",error.toString());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String nombre = edt_nombre_registro_usuario.getText().toString();
                    String correo = edt_correo_registro_usuario.getText().toString();
                    String clave_ine = edt_clave_elector_registro_usuario.getText().toString();

                    //Estructura para enviar al webservice por POST

                    Map<String, String> parametros = new HashMap<>();

                    parametros.put("nombre", nombre);
                    parametros.put("correo", correo);
                    parametros.put("clave_ine", clave_ine);
                    parametros.put("password", finalSave_password);

                    return parametros;
                }
            };

            request.add(stringRequest);

        }else {
            Toast.makeText(this, "No se pudo registrar!!", Toast.LENGTH_SHORT).show();
            progreso.hide();
        }
    }

    private void initComponents() {
        edt_nombre_registro_usuario = findViewById(R.id.edt_nombre_registro_usuario);
        edt_correo_registro_usuario = findViewById(R.id.edt_correo_registro_usuario);
        edt_password_registro_usuario = findViewById(R.id.edt_password_registro_usuario);
        edt_conf_password_ru = findViewById(R.id.edt_confir_password_ru);
        edt_clave_elector_registro_usuario = findViewById(R.id.edt_clave_elector_registro_usuario);

        btn_registrar_usuario = findViewById(R.id.btn_registrar_usuario);
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

    /* Valida el correo electronico  */
    private boolean validarEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

}
