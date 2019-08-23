package com.example.lectorqr;

public class DatosInvitados {
    int id_invitado, id_eventoIn;
    String nombre , correo, estatus;

    public DatosInvitados(){

    }

    public DatosInvitados(int id_invitado, int id_eventoIn, String nombre, String correo, String estatus) {
        this.id_invitado = id_invitado;
        this.id_eventoIn = id_eventoIn;
        this.nombre = nombre;
        this.correo = correo;
        this.estatus = estatus;
    }
}
