package com.example.lectorqr;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Invitados extends AppCompatActivity implements AdaptadorIn.OnItemClickListener {

    private static final String TAG = "a";
    private RecyclerView mylist;
    private Adaptador mAdapter;
    private DatabaseReference db;

    private ArrayList<DatosInvitados> datoss;
    private List<InfInvitados> invitados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitados);


        mylist = (RecyclerView) findViewById(R.id.lista_invitados);
        LinearLayoutManager llm = new LinearLayoutManager(getApplication());
        mylist.setLayoutManager(llm);
        mylist.setHasFixedSize(true);

        initializeData();

    }


    private void initializeData(){
        db = FirebaseDatabase.getInstance().getReference();
        Query query = db.child("invitados");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                    datoss = new ArrayList<>();
                    while (dataSnapshots.hasNext()) {
                        DataSnapshot dataSnapshotChild = dataSnapshots.next();
                        DatosInvitados datos = dataSnapshotChild.getValue(DatosInvitados.class);
                        datoss.add(datos);
                    }
                    invitados = new ArrayList<>();
                    for (int j = 0; j < datoss.size(); j++) {

                        DatosInvitados datosas = datoss.get(j);
                        int id = datosas.id_invitado;
                        int idEvento = datosas.id_eventoIn;
                        String nombre = datosas.nombre;
                        String correo = datosas.correo;
                        String estatus = datosas.estatus;
                        Bundle extras = getIntent().getExtras();
                        String idd  = extras.getString("id");
                        int idEventos = Integer.parseInt(idd);
                        if (idEvento == idEventos ){
                            invitados.add(new InfInvitados(id, idEvento, nombre, correo , estatus));
                        }

                    }
                    System.out.println("se cumplo el for");
                    AdaptadorIn adapter = new AdaptadorIn(Invitados.this, invitados);
                    mylist.setAdapter(adapter);
                    adapter.setOnItemClickListener(Invitados.this);
                    //InfTeacher data = (InfTeacher) teachers2;
                    //names.adddata.getName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //db.addChildEventListener(childEventListener);

    }

    @Override
    public void onItemClick(int position) {

    }
}
