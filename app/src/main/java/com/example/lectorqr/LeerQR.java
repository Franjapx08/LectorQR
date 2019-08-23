package com.example.lectorqr;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.content.ContentValues.TAG;

public class LeerQR extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    private DatabaseReference mDatabase;// ...

    private DatabaseReference db;


    String idEvent;

    boolean encontro;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view


     //   System.out.println("aohdw98ahda8whdw " + idEvent );

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        /*
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
        */
        /*Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();*/

        final int idMaester = Integer.parseInt(rawResult.getText());

        db = FirebaseDatabase.getInstance().getReference();
        Query query = db.child("maestros");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getChildrenCount());
                if (dataSnapshot.exists()) {
                    Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();

                    List<DatosMaestros> dat = new ArrayList<>();
                    while (dataSnapshots.hasNext()) {
                        DataSnapshot dataSnapshotChild = dataSnapshots.next();
                        DatosMaestros datos = dataSnapshotChild.getValue(DatosMaestros.class);
                        dat.add(datos);
                    }
                   // List<DatosMaestros> maestro = new ArrayList<>();
                    for (int j = 0; j < dat.size(); j++) {
                        DatosMaestros datosas = dat.get(j);
                        final int id = datosas.id;
                        final String nombreMa = datosas.nombre;
                        final String correoMa = datosas.correo;
                        if (id == idMaester ){
                            Bundle extras = getIntent().getExtras();
                            String idd  = extras.getString("id");
                            final int idEvento = Integer.parseInt(idd);

                            System.out.println("usaurio registrado en la bd " + "nombre " + nombreMa);
                            System.out.println(idMaester);

                            db = FirebaseDatabase.getInstance().getReference();
                            Query query = db.child("invitados");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                                        List<DatosInvitados> date= new ArrayList<>();
                                        while (dataSnapshots.hasNext()) {
                                            DataSnapshot dataSnapshotChild = dataSnapshots.next();
                                            DatosInvitados datos = dataSnapshotChild.getValue(DatosInvitados.class);
                                            date.add(datos);
                                        }
                                        for (int j = 0; j < date.size(); j++) {
                                            DatosInvitados datosas = date.get(j);
                                            int idinvitadoD = datosas.id_invitado;
                                            int idEventoIn = datosas.id_eventoIn;
                                            if (idinvitadoD == idMaester) {
                                                if (idEvento == idEventoIn){
                                                    System.out.println("YA INVITADO EL" + idinvitadoD + " nombre: " + nombreMa + "" +
                                                            "al evento " + idEvento + " y " + idEventoIn);
                                                    Toast.makeText(getApplicationContext(), "Código Valido", Toast.LENGTH_LONG).show();
                                                    encontro = true;
                                                }
                                            }
                                        }

                                        if (encontro){
                                            onBackPressed();
                                            finish();
                                        }else{
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference myRef = database.getReference();
                                            DatosInvitados userInformation = new DatosInvitados(idMaester, idEvento, nombreMa, correoMa, "CORRECTO");
                                            myRef.child("invitados").push().setValue(userInformation);
                                            Toast.makeText(getApplicationContext(), "Código Valido" , Toast.LENGTH_LONG).show();
                                            onBackPressed();
                                            finish();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else if (id != idMaester){
                           Toast.makeText(getApplicationContext(), "Código invalido" , Toast.LENGTH_LONG).show();
                            //System.out.println("usuario no esta en los registros");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(LeerQR.this);
            }
        }, 2000);
    }
}