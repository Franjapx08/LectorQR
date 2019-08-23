package com.example.lectorqr;

public class DatosEvento {
    public String  nombre_evento, fecha, hora, ubicacion;
    public int id_evento, disponible;

    public DatosEvento(){

    }
    public DatosEvento(int id_evento, int disponible, String nombre_evento, String fecha, String hora, String ubicacion) {
        this.id_evento = id_evento;
        this.disponible = disponible;
        this.nombre_evento = nombre_evento;
        this.fecha = fecha;
        this.hora = hora;
        this.ubicacion = ubicacion;
    }

}
