package com.android.victor.congresoapp.Model;

public class ModelPrograma {
    //Declracion de variables para referencia a BD firebase....
    String fecha, horario, descripcion;

    public ModelPrograma(){
    }

    //geters and setters


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
