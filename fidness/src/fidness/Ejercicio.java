package fidness;

import java.io.Serializable;

public class Ejercicio implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idEjercicio;
    private String nombre;
    private String grupoMuscular;
    private String descripcion;
    private String instrucciones;
    private String imagenReferencia;

    public Ejercicio(int idEjercicio, String nombre,
                     String grupoMuscular, String descripcion,
                     String instrucciones, String imagenReferencia) {

        this.idEjercicio = idEjercicio;
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.descripcion = descripcion;
        this.instrucciones = instrucciones;
        this.imagenReferencia = imagenReferencia;
    }

    public String mostrarInformacion() {
        return "Ejercicio: " + nombre
                + "\nGrupo muscular: " + grupoMuscular
                + "\nDescripción: " + descripcion;
    }

    public String mostrarInstrucciones() {
        return instrucciones;
    }

    public void actualizarInformacion(String nombre,
                                       String grupoMuscular,
                                       String descripcion,
                                       String instrucciones,
                                       String imagenReferencia) {

        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.descripcion = descripcion;
        this.instrucciones = instrucciones;
        this.imagenReferencia = imagenReferencia;
    }

    public int getIdEjercicio() {
        return idEjercicio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public String getImagenReferencia() {
        return imagenReferencia;
    }

    @Override
    public String toString() {
        return nombre + " - " + grupoMuscular;
    }
}