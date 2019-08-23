package com.example.lectorqr;

public class InfEvento {

    public String  nombre, hora, fecha, ubicacion;
    public int id;


    public InfEvento(int id, String nombre, String hora , String fecha, String ubicacion){
        this.id = id;
        this.nombre = nombre;
        this.hora = hora;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
    }

    public int getId(){
        return id;
    }

    public String getNombre(){
        return nombre;
    }

    public String getHora(){
        return hora;
    }

    public String getFecha(){
        return fecha;
    }

    public String getUbicacion(){
        return ubicacion;
    }

}

