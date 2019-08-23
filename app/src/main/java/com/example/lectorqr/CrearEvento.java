package com.example.lectorqr;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class CrearEvento extends AppCompatActivity implements View.OnClickListener {

    EditText nombre, etHora, etFecha, ubicacion;
    Button aceptar, ibObtenerFecha, ibObtenerHora;

    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    private ArrayList<DatosEvento> dat ;
    private int idGener;
    boolean idEncontrado = false;
    boolean encontrado = false;
    private String fechita;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        nombre = (EditText) findViewById(R.id.nombre_evento);
        etFecha = (EditText) findViewById(R.id.et_mostrar_fecha_picker);
        etHora = (EditText) findViewById(R.id.et_mostrar_hora_picker);
        ubicacion = (EditText) findViewById(R.id.ubicacion_evento);

        ibObtenerHora = (Button) findViewById(R.id.ib_obtener_hora);
        ibObtenerFecha = (Button) findViewById(R.id.ib_obtener_fecha);
        ibObtenerFecha.setOnClickListener(this);
        ibObtenerHora.setOnClickListener(this);

        aceptar =(Button) findViewById(R.id.aceptar_evento);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(nombre.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Faltan parametros",
                            Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etFecha.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Faltan parametros",
                            Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etHora.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Faltan parametros",
                            Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ubicacion.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Faltan parametros",
                            Toast.LENGTH_SHORT).show();
                }else {


                if (idEncontrado == false) {
                    // idGener = 2;
                    idGener = (int) (Math.random() * 999) + 1;
                }

                db = FirebaseDatabase.getInstance().getReference();
                Query query = db.child("eventos");
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
                            for (int j = 0; j < dat.size(); j++) {
                                DatosEvento datosas = dat.get(j);
                                int id = datosas.id_evento;
                                String nombre_evento = datosas.nombre_evento;
                                String hora = datosas.hora;
                                String fecha = datosas.fecha;
                                String ubi = datosas.ubicacion;
                                int dis = datosas.disponible;
                                String nombre_dom = nombre.getText().toString();
                                String fecha_dom = etFecha.getText().toString();
                                System.out.println("fecha MIA" + fecha_dom);
                                System.out.println("fechas enocntradas" + fecha);
                                if (id == idGener) {
                                    idEncontrado = false;
                                    System.out.println("Encontradooooooooooooo no insertes" + idGener);
                                    encontrado = true;
                                    if (encontrado) {
                                        break;
                                    }
                                }
                            }
                            if (!encontrado) {
                                System.out.println("inserta con el id" + idGener);
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference();
                                DatosEvento userInformation = new DatosEvento(idGener, 1, nombre.getText().toString(), etFecha.getText().toString(), etHora.getText().toString(), ubicacion.getText().toString());
                                myRef.child("eventos").push().setValue(userInformation);
                                Intent facil = new Intent(getApplicationContext(), InicioMenu.class);
                                startActivity(facil);//Inicia la actividad
                                finish();
                                Toast.makeText(getApplicationContext(),"Evento creado", Toast.LENGTH_LONG).show();
                            }
                        }else {

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_obtener_fecha:
                obtenerFecha();
                break;
            case R.id.ib_obtener_hora:
                obtenerHora();
                break;
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
               fechita = String.valueOf(etFecha);

            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }

    private void obtenerHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);

        recogerHora.show();
    }


}
