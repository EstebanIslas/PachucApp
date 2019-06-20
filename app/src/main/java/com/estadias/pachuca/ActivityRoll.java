package com.estadias.pachuca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ActivityRoll extends AppCompatActivity {

    Button btn_roll_usuario;
    Button btn_roll_cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);

        ititComponents();

        btn_roll_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent roll_cliente = new Intent(getApplicationContext(), ActivityRegistroUsuario.class);//Ayuda a crear fucniones para pasar de una pantalla a otra
                //reg_empresa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(roll_cliente);
            }
        });

        btn_roll_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Negocios", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ititComponents() {
        btn_roll_usuario = findViewById(R.id.btn_roll_usuario);
        btn_roll_cliente = findViewById(R.id.btn_roll_cliente);
    }


}
