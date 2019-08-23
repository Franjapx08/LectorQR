package com.example.lectorqr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.example.lectorqr.Valores.JSON_SONG;
import static com.example.lectorqr.Valores.gson;

public class EventoVista extends AppCompatActivity {

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    TextView nombre, fecha, hora, ubicacion;
    Button elimar, invitar, invitados;
    InfEvento evento;
    String idEvent;




    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_vista);

        nombre = (TextView) findViewById(R.id.name);
        fecha = (TextView) findViewById(R.id.fecha_inf);
        hora = (TextView) findViewById(R.id.hora_ind);
        ubicacion = (TextView) findViewById(R.id.ubicacion_inf);

        elimar = (Button) findViewById(R.id.elimar);
        invitados = (Button) findViewById(R.id.invitados);

        String song = getIntent().getStringExtra(JSON_SONG);
        evento = gson.fromJson(song, InfEvento.class);
        nombre.setText(evento.getNombre());
        fecha.setText(evento.getFecha());
        hora.setText(evento.getHora());
        ubicacion.setText(evento.getUbicacion());
        final int id = evento.id;

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
      //  userID = user.getUid();


        Bundle extras = getIntent().getExtras();
        idEvent = extras.getString("id");


        elimar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = FirebaseDatabase.getInstance().getReference();
                Query query = myRef.child("eventos");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                            while (dataSnapshots.hasNext()) {
                                DataSnapshot dataSnapshotChild = dataSnapshots.next();
                                DatosEvento datos = dataSnapshotChild.getValue(DatosEvento.class);
                                int idd = datos.id_evento;
                                if (idd == id){
                                    String key = dataSnapshotChild.getKey();
                                    //System.out.println("key   " + key );
                                    HashMap<String, Object> result = new HashMap<>();
                                    result.put("disponible", 2);
                                    dataSnapshotChild.getRef().updateChildren(result);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getApplicationContext(), "Evento eliminado con éxito", Toast.LENGTH_LONG).show();
                Intent facil = new Intent(getApplicationContext(), InicioMenu.class);
                finish();
                startActivity(facil);//Inicia la actividad

            }
        });

        invitados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent invi = new Intent(getApplicationContext(), Invitados.class);
                Bundle extras = new Bundle();
                extras.putString("id" , idEvent);
                invi.putExtras(extras);
                startActivity(invi);//Inicia la actividad


            }
        });

    }

    public void launchSimpleActivity(View v) {
        launchActivity(LeerQR.class);

    }


    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            Bundle extras = new Bundle();
            extras.putString("id" , idEvent);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);

                    }
                } else {
                    Toast.makeText(this, "Por favor dar permiso para poder usar el escaner QR", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

}
