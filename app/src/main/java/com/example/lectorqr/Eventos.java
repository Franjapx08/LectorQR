package com.example.lectorqr;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;
import com.google.firebase.auth.FirebaseAuth;

public class Eventos extends AppCompatActivity implements Adaptador.OnItemClickListener {

    private static final String TAG = "a";
    private RecyclerView mylist;
    private Adaptador mAdapter;
    private DatabaseReference db;

    private ArrayList<DatosEvento> dat ;
    private List<InfEvento> evento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        mylist = (RecyclerView) findViewById(R.id.mylista);
        LinearLayoutManager llm = new LinearLayoutManager(getApplication());
        mylist.setLayoutManager(llm);
        mylist.setHasFixedSize(true);
        initializeData();

    }

    private void initializeData(){
        db = FirebaseDatabase.getInstance().getReference();
        Query query = db.child("eventos");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getChildrenCount());
                if (dataSnapshot.exists()) {
                    Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                    dat = new ArrayList<>();
                    while (dataSnapshots.hasNext()) {
                        DataSnapshot dataSnapshotChild = dataSnapshots.next();
                        DatosEvento datos = dataSnapshotChild.getValue(DatosEvento.class);
                        dat.add(datos);
                    }
                    evento = new ArrayList<>();
                    for (int j = 0; j < dat.size(); j++) {
                        DatosEvento datosas = dat.get(j);
                        int id = datosas.id_evento;
                        String nombre = datosas.nombre_evento;
                        String hora = datosas.hora;
                        String fecha = datosas.fecha;
                        String ubi = datosas.ubicacion;
                        int dis = datosas.disponible;

                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, 1);
                        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

                        String formatted = format1.format(cal.getTime());
                        System.out.println("hora servidor" + formatted);
                        System.out.println("hora encontrada mia" + fecha);

                        if (dis == 1) {

                            if (fecha.compareTo(formatted) >= 0) {
                                evento.add(new InfEvento(id, nombre, hora, fecha, ubi));
                            }
                        }
                        Adaptador adapter = new Adaptador(Eventos.this, evento);
                        mylist.setAdapter(adapter);
                        adapter.setOnItemClickListener(Eventos.this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeDataOld (){
            db = FirebaseDatabase.getInstance().getReference();
             Query query = db.child("eventos").orderByChild("nombre_evento").startAt("A");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                        dat = new ArrayList<>();
                        while (dataSnapshots.hasNext()) {
                            DataSnapshot dataSnapshotChild = dataSnapshots.next();
                            DatosEvento datos = dataSnapshotChild.getValue(DatosEvento.class);
                            dat.add(datos);
                        }
                        evento = new ArrayList<>();
                        for (int j = 0; j < dat.size(); j++) {

                            DatosEvento datosas = dat.get(j);
                            int id = datosas.id_evento;
                            String nombre = datosas.nombre_evento;
                            String hora = datosas.hora;
                            String fecha = datosas.fecha;
                            String ubi = datosas.ubicacion;
                            int dis = datosas.disponible;
                            if (dis == 1) {

                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.DATE, 1);
                                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

                                String formatted = format1.format(cal.getTime());
                                if (fecha.compareTo(formatted) < 0) {
                                    evento.add(new InfEvento(id, nombre, hora, fecha, ubi));
                                }

                              //  evento.add(new InfEvento(id, nombre, hora, fecha, ubi));
                            }
                        }
                        Adaptador adapter = new Adaptador(Eventos.this, evento);
                        mylist.setAdapter(adapter);
                        adapter.setOnItemClickListener(Eventos.this);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_evento, menu);
        return true;



    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_crear:
                Intent crear = new Intent(getApplicationContext(), CrearEvento.class);
                startActivity(crear);//Inicia la actividad
                finish();
                return true;
            case R.id.menu_op3:
                initializeData();
                return true;
            case R.id.menu_op4:
                initializeDataOld();
                return true;
            case R.id.menu_op5:
                FirebaseAuth.getInstance().signOut();
                Intent cerrar = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(cerrar);//Inicia la actividad
                finish();
                return true;
            case R.id.menu_op6:
                FirebaseAuth.getInstance().signOut();
                Intent acerca = new Intent(getApplicationContext(), acercaDe.class);
                startActivity(acerca);//Inicia la actividad
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onItemClick(int position) {

    }


    @Override
    public void onBackPressed (){

    }

}
