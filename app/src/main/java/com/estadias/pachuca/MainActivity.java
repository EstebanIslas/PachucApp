package com.estadias.pachuca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.estadias.pachuca.fragments.FragmentConsultarNegocio;
import com.estadias.pachuca.fragments.FragmentConsultarPromociones;
import com.estadias.pachuca.fragments.FragmentListaCategorias;
import com.estadias.pachuca.interfaces.IFragments;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , IFragments {

    private String ID_GUARDADO = "";
    public static final String ID_CLIENTE = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Recibir variable de la clase login
        Intent intent = getIntent(); //Objet de tipo intent que recupera la variable
        String id_guardado = intent.getStringExtra(ActivityLogin.ID_CLIENTE); ////Almacenar el id del cliente de la actividad Login
        ID_GUARDADO = id_guardado;

        //Enviar el ID_GUARDADO a la clase de FragmentConsultarPromociones
        Fragment enviarIdPromociones = new FragmentConsultarPromociones(); //Objeto creado para remplazar el fragment

        Bundle bundle = new Bundle(); //variable de tipo paquete para guardar en la variable String final
        bundle.putString(ID_CLIENTE, ID_GUARDADO);

        //Nuevo metodo
        SharedPreferences prefe = getSharedPreferences("ID_CLIENTE", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefe.edit();
        editor.putString(ID_CLIENTE, ID_GUARDADO);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment miFragment = null;
        boolean fragmentseleccionado = false;

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_categorias) {

            miFragment = new FragmentListaCategorias(); //Instanciamos el objeto de tipo fragmet a ListaCategorias
            fragmentseleccionado = true;

        } /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } */else if (id == R.id.nav_profile) {

            Toast.makeText(this, "Selección Perfil", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, "Usted tiene de ID: " + ID_GUARDADO, Toast.LENGTH_SHORT).show();


        } /*else if (id == R.id.nav_send) {

        }*/

         //Si el fragment seleccionado es verdadero reemplaza el contenido de content main por su id
         if (fragmentseleccionado == true){
             getSupportFragmentManager().beginTransaction().replace(R.id.content_main,miFragment).commit(); //Remplaza el fragment seleccionado

         }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
