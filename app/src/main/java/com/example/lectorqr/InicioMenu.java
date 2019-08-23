package com.example.lectorqr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class InicioMenu extends AppCompatActivity {

    Button evento, crear, cerrar, acercade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_menu);
        evento = (Button) findViewById(R.id.evento);
        crear = (Button) findViewById(R.id.crear);
        cerrar = (Button) findViewById(R.id.cerrar);

        evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent facil = new Intent(getApplicationContext(), Eventos.class);
                startActivity(facil);//Inicia la actividad
                finish();
            }
        });

                crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent facil = new Intent(getApplicationContext(), CrearEvento.class);
                startActivity(facil);//Inicia la actividad
                finish();
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent facil = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(facil);//Inicia la actividad
                finish();
                //getA().onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_op, menu);
        return true;



    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_op1:
                FirebaseAuth.getInstance().signOut();
                Intent acerca = new Intent(getApplicationContext(), acercaDe.class);
                startActivity(acerca);//Inicia la actividad
                finish();
                return true;

            case R.id.menu_op2:
                FirebaseAuth.getInstance().signOut();
                Intent cerrar = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(cerrar);//Inicia la actividad
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
