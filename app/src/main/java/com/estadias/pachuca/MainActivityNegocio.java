package com.estadias.pachuca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.estadias.pachuca.fragments.FragmentConsultarInfoNegocio;
import com.estadias.pachuca.fragments.FragmentCrearPromociones;
import com.estadias.pachuca.fragments.FragmentIndexCliente;
import com.estadias.pachuca.fragments.FragmentVerPromociones;
import com.estadias.pachuca.interfaces.IFragments;

public class MainActivityNegocio extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , IFragments {

    private String ID_GUARDADO = "";
    public static final String ID_USUARIO = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_negocio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Fragment fragment = new FragmentIndexCliente();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main_negocio,fragment).commit(); //Remplaza el fragment seleccionado


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Recibir variable de la clase login
        Intent intent = getIntent(); //Objet de tipo intent que recupera la variable
        String id_guardado = intent.getStringExtra(ActivityLogin.ID_USUARIO); ////Almacenar el id del cliente de la actividad Login
        ID_GUARDADO = id_guardado;
        //Toast.makeText(this, "ID_USUARIO = " + ID_GUARDADO, Toast.LENGTH_SHORT).show();

        //Nuevo metodo para enviar el id final a cualquier fragment o activity
        SharedPreferences prefe = getSharedPreferences("ID_USUARIO", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefe.edit();
        editor.putString(ID_USUARIO, ID_GUARDADO);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_negocio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Sargon Systems", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        Fragment miFragment = null;
        boolean fragmentseleccionado = false;

        if (id == R.id.nav_home) {
            miFragment = new FragmentIndexCliente();
            fragmentseleccionado = true;
        } else if (id == R.id.nav_ver_promos) {
            miFragment = new FragmentVerPromociones(); //Instanciamos el objeto de tipo fragmet a VerPromociones
            fragmentseleccionado = true;

        } else if (id == R.id.nav_crear_promos) {
            miFragment = new FragmentCrearPromociones(); //Instanciamos el objeto de tipo fragmet a CrearPromociones
            fragmentseleccionado = true;

        } /*else if (id == R.id.nav_tools) {

        } */else if (id == R.id.nav_profile) {
            miFragment = new FragmentConsultarInfoNegocio();
            fragmentseleccionado = true;
        }

        //Si el fragment seleccionado es verdadero reemplaza el contenido de content main por su id
        if (fragmentseleccionado == true){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main_negocio,miFragment).commit(); //Remplaza el fragment seleccionado
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
