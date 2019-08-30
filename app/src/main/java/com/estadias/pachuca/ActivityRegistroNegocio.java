package com.estadias.pachuca;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ActivityRegistroNegocio extends AppCompatActivity {

    /*EditText edt_nombre_reg_negocio;
    EditText edt_correo_reg_negocio;
    EditText edt_calle_reg_negocio;
    EditText edt_colonia_reg_negocio;
    EditText edt_numero_reg_negocio;
    EditText edt_descripcion_reg_negocio;
    EditText edt_telefono1_reg_negocio;
    EditText edt_password_reg_negocio;
    //EditText edt_categoria_reg_negocio;
    EditText edt_municipio_reg_negocio;
    EditText edt_confirm_pass_reg_negocio;

    private static String CATEGORIA = "";

    //Spinner para categorias
    Spinner spin_categoria_reg_negocio;

    Button btn_registrar_negocio;

    //Agregar imagenes
    ImageView img_imagen_reg_negocio;
    Button btn_seleccionar_imagen_negocio;

    //Mostrar una ventana de descarga en caso de que se demore la peticion
    ProgressDialog progreso;

    //Conexion al Webservice
    RequestQueue request;

    StringRequest stringRequest;

    //Seleccionar Imagen
    Bitmap bitmap;
    private static final int COD_SELECCIONA = 10;*/

    Button btn_whats;
    Button btn_mapa;
    Button btn_llamar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_negocio);

        btn_llamar = findViewById(R.id.btn_llamar_registro);
        btn_whats = findViewById(R.id.btn_mensaje_registro);
        btn_mapa = findViewById(R.id.btn_mapa_registro);

        btn_whats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mensajeOnClick(view);
            }
        });

        btn_llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarOnClick(view);
            }
        });

        btn_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vermapaOnClick(view);
            }
        });

        /*initComponents();

        request = Volley.newRequestQueue(this);

        String[] categorias = {"Comercios", "Entretenimiento", "Escuelas", "Hoteles", "Profesionales", "Restaurantes", "Transportes", "Otros"};
        spin_categoria_reg_negocio.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_design_text, categorias)); //Se agrega el arreglo a la parte grafica
        spin_categoria_reg_negocio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String categoria = (String) adapterView.getItemAtPosition(i);
                //Toast.makeText(getApplicationContext(), (String) adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                CATEGORIA = categoria;
                //Toast.makeText(getApplicationContext(), "Categoria: " + CATEGORIA, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_registrar_negocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebservice(); //Metodo para iniciar el proceso de llamado al webservice
            }
        });


        //Cargar imagen desde la galeria
        btn_seleccionar_imagen_negocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogOpciones();
            }
        });*/
    }

    private void mensajeOnClick(View view) {
        /*Uri mensaje = Uri.parse("smsto:"+ tv_telefono_consulta_neg.getText().toString());
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, mensaje);
        smsIntent.putExtra("sms_body", "");

        startActivity(smsIntent);*/
        try{
            String telefono_consulta = "7711530636";
            String numero = "+521" + telefono_consulta;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String uri = "whatsapp://send?phone=" + numero;
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }catch (Exception ex) {
            Toast.makeText(this, "Ocurrió un problema con WhatsApp", Toast.LENGTH_SHORT).show();
            Log.e("\n\nERROR: " , ex.getMessage());
        }
    }

    private void llamarOnClick(View view) {
        Uri numero = Uri.parse("tel:"+ "7711530636");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, numero);

        startActivity(callIntent);
    }

    private void vermapaOnClick(View view){
        try {
            String ubicacion = "https://www.google.com/maps/place/Sargon+-+Distribuidor+Aspel+Soft+Restaurant+C%C3%A1maras+de+Seguridad+P%C3%A1ginas+Web+Pachuca+Hidalgo/@20.1227128,-98.747726,17z/data=!3m1!4b1!4m5!3m4!1s0x85d109f8546f0265:0x5be57bf68b9230d8!8m2!3d20.122866!4d-98.745469?hl=es";
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(ubicacion));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        }catch (Exception ex){
            Toast.makeText(this, "Ocurrió un problema con Google Maps", Toast.LENGTH_SHORT).show();
            Log.e("\n\nERROR: " , ex.getMessage());
        }
    }

    /*private void initComponents() {
        edt_nombre_reg_negocio = findViewById(R.id.edt_nombre_reg_negocio);
        edt_correo_reg_negocio = findViewById(R.id.edt_correo_reg_negocio);
        edt_calle_reg_negocio = findViewById(R.id.edt_calle_reg_negocio);
        edt_colonia_reg_negocio = findViewById(R.id.edt_colonia_reg_negocio);
        edt_numero_reg_negocio = findViewById(R.id.edt_numero_reg_negocio);
        edt_descripcion_reg_negocio = findViewById(R.id.edt_descripcion_reg_negocio);
        edt_telefono1_reg_negocio = findViewById(R.id.edt_telefono1_reg_negocio);
        edt_password_reg_negocio = findViewById(R.id.edt_password_reg_negocio);
        //edt_categoria_reg_negocio = findViewById(R.id.edt_categoria_reg_negocio);
        edt_municipio_reg_negocio = findViewById(R.id.edt_municipio_reg_negocio);
        edt_confirm_pass_reg_negocio = findViewById(R.id.edt_confirm_pass_reg_negocio);

        btn_registrar_negocio = findViewById(R.id.btn_registrar_negocio);



        img_imagen_reg_negocio = findViewById(R.id.img_imagen_reg_negocio);
        btn_seleccionar_imagen_negocio = findViewById(R.id.btn_seleccionar_imagen_negocio);

        //Spiner para categoria
        spin_categoria_reg_negocio = findViewById(R.id.spin_categoria_reg_negocio);
    }*/


    /*private void limpiarEditText() {

        edt_nombre_reg_negocio.setText("");
        edt_correo_reg_negocio.setText("");
        edt_calle_reg_negocio.setText("");
        edt_colonia_reg_negocio.setText("");
        edt_numero_reg_negocio.setText("");
        edt_descripcion_reg_negocio.setText("");
        edt_telefono1_reg_negocio.setText("");
        edt_password_reg_negocio.setText("");
        //edt_categoria_reg_negocio.setText("");
        edt_municipio_reg_negocio.setText("");
        edt_confirm_pass_reg_negocio.setText("");

    }*/


    /*private void cargarWebservice() {

        progreso = new ProgressDialog(this);
        progreso.setMessage("Conectando...");
        progreso.show();

        //Variables para igualar contraseñas de usuario
        String pass, save_password = "";
        boolean registro, valida, valida_email;;
        pass = edt_password_reg_negocio.getText().toString(); //Se envia lo que tiene password a la variable

        if (pass.equals(edt_confirm_pass_reg_negocio.getText().toString())){
            save_password = ActivityRegistroNegocio.md5(edt_confirm_pass_reg_negocio.getText().toString()); //Guarda la contraseña en formato md5
            registro = true;
        }else {
            registro = false;
            Toast.makeText(this, "Las contraseñas no coinciden!!", Toast.LENGTH_SHORT).show();
            progreso.hide();
        }
        // Validacion de campos vacios
        if (edt_nombre_reg_negocio.getText().toString().isEmpty() || edt_correo_reg_negocio.getText().toString().isEmpty()
                || edt_calle_reg_negocio.getText().toString().isEmpty() || edt_colonia_reg_negocio.getText().toString().isEmpty()
                || edt_colonia_reg_negocio.getText().toString().isEmpty() || edt_numero_reg_negocio.getText().toString().isEmpty()
                || edt_municipio_reg_negocio.getText().toString().isEmpty() || edt_descripcion_reg_negocio.getText().toString().isEmpty()
                || edt_telefono1_reg_negocio.getText().toString().isEmpty() || edt_password_reg_negocio.getText().toString().isEmpty()){

            valida = false;
            Toast.makeText(this, "Existen campos vacíos!", Toast.LENGTH_SHORT).show();

        }else {
            valida = true;
        }

        if (!validarEmail(edt_correo_reg_negocio.getText().toString())){
            edt_correo_reg_negocio.setError("Email no valido");
            valida_email = false;
        }else {
            valida_email = true;
        }

        if (registro == true && valida == true && valida_email == true) {


            //Url para metodo POST
            String URL = "https://pachuca.com.mx/webservice/api_usuarios/wsInsertarUsuarios.php";

            Toast.makeText(this, "Esto puede tardar unos minutos", Toast.LENGTH_SHORT).show();

            final String finalSave_password = save_password; //Guarda la variable en una final para su uso

            //Nueva estructura de conexion con volley, ahora no se implementa en la clase se hace directo en el metodo
            stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progreso.hide();

                    //Toast.makeText(getApplicationContext(), "Respuesta: " + response, Toast.LENGTH_SHORT).show();

                    if (response.trim().equalsIgnoreCase("registra")) {

                        limpiarEditText();

                        img_imagen_reg_negocio.setImageResource(R.drawable.profile);

                        Intent login = new Intent(getApplicationContext(), ActivityLogin.class);//Ayuda a crear fucniones para pasar de una pantalla a otra
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(login);

                        Toast.makeText(getApplicationContext(), "Se ha realizado el registro", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "No se realizo el registro", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progreso.hide();
                    Toast.makeText(getApplicationContext(), "Parece que hay problemas para registrar, revisa el llenado de campos o tu conexión a internet", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    String nombre = edt_nombre_reg_negocio.getText().toString();
                    String correo = edt_correo_reg_negocio.getText().toString();
                    String calle = edt_calle_reg_negocio.getText().toString();
                    String colonia = edt_colonia_reg_negocio.getText().toString();
                    String numero = edt_numero_reg_negocio.getText().toString();
                    String descripcion = edt_descripcion_reg_negocio.getText().toString();
                    String telefono1 = edt_telefono1_reg_negocio.getText().toString();
                    String password = edt_password_reg_negocio.getText().toString();
                    //String categoria = edt_categoria_reg_negocio.getText().toString();
                    String municipio = edt_municipio_reg_negocio.getText().toString();

                    String imagen = convertitImgString(bitmap);

                    //***** Enviar Strings a Webservice por metodo POST *****

                    Map<String, String> parametros = new HashMap<>();

                    parametros.put("nombre", nombre);
                    parametros.put("correo", correo);
                    parametros.put("calle", calle);
                    parametros.put("colonia", colonia);
                    parametros.put("numero", numero);
                    parametros.put("descripcion", descripcion);
                    parametros.put("telefono", telefono1);
                    parametros.put("password", finalSave_password);
                    parametros.put("categoria", CATEGORIA);
                    parametros.put("municipio", municipio);

                    parametros.put("imagen", imagen);


                    System.out.println("Resultado= " + nombre + " " + correo + " " + calle + " " + colonia +
                            " " + numero + " " + descripcion + " " + telefono1 + " " + finalSave_password + " " + CATEGORIA + " " + municipio);

                    return parametros;
                }
            };

            request.add(stringRequest);



        }else {
            Toast.makeText(this, "No se pudo registrar el negocio intentelo mas tarde", Toast.LENGTH_SHORT).show();
            progreso.hide();
        }
    }*/

    /*private String convertitImgString(Bitmap bitmap) {

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array); //Comprime el bitmap de el valor de la imagen seleccionada
        byte[] imageByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imageByte, Base64.DEFAULT); //Se retorna la imagen en base64

        return imagenString;
    }*/



    /*/Metodos para cargar la imagen desde la galeria

    private void mostrarDialogOpciones() {
        final CharSequence[] opciones = {"Elegir de Galeria", "Cancelar"}; //Se crea una secuencia de opciones
        final AlertDialog.Builder builder = new AlertDialog.Builder(this); //Para mostrar al usuario la secuencia en forma de ventana
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (opciones[i].equals("Elegir de Galeria")){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    intent.setType("image/");

                    startActivityForResult(intent.createChooser(intent, "Seleccione"), COD_SELECCIONA);
                } else {
                    dialogInterface.dismiss();
                }
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            switch (requestCode){
                case COD_SELECCIONA:
                    Uri miPath = data.getData();
                    img_imagen_reg_negocio.setImageURI(miPath);

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),miPath);
                    img_imagen_reg_negocio.setImageBitmap(bitmap);
                    break;
            }

        }catch (Exception err){
            Toast.makeText(this, "No ha seleccionado la imagen", Toast.LENGTH_SHORT).show();
        }
    }*/


    //########### Encriptar la contraseña mediante MD5 ###########

    /*private static String getHash(String txt, String hashType) {
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
    }*/

    /* Retorna un hash MD5 a partir de un texto
    private static String md5(String txt) {
        return getHash(txt, "MD5");
    }*/

    /* Retorna un hash SHA1 a partir de un texto
    private static String sha1(String txt) {
        return getHash(txt, "SHA1");
    }*/

    /* Valida el correo electronico
    private boolean validarEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }*/

}
