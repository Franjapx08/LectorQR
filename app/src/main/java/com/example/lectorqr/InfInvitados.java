package com.example.lectorqr;

public class InfInvitados {

    public String  nombre, correo, estatus, ubicacion;
    public int id, idEvento;


    public InfInvitados(int id, int idEvento , String nombre, String correo , String estatus){
        this.id = id;
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.correo = correo;
        this.estatus = estatus;

    }

    public int getId(){
        return id;
    }

    public int getIdEvento(){
        return idEvento;
    }

    public String getNombre(){
        return nombre;
    }

    public String getcorreo(){
        return correo;
    }

    public String getestatus(){
        return estatus;
    }



}

