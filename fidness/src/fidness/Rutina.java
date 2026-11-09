package fidness;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Rutina implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idRutina;
    private String nombre;
    private LocalDate fechaCreacion;
    private Usuario usuario;

    private ArrayList<DetalleRutina> detalles;

    public Rutina(int idRutina, String nombre, Usuario usuario) {

        this.idRutina = idRutina;
        this.nombre = nombre;
        this.usuario = usuario;
        this.fechaCreacion = LocalDate.now();
        this.detalles = new ArrayList<>();
    }

    public void agregarEjercicio(DetalleRutina detalle) {
        detalles.add(detalle);
    }

    public void eliminarEjercicio(int indice) {
        detalles.remove(indice);
    }

    public ArrayList<DetalleRutina> consultarRutina() {
        return detalles;
    }

    public String exportarRutina() {

        String contenido = "FIDNESS - RUTINA PERSONALIZADA\n";
        contenido += "Nombre: " + nombre + "\n";
        contenido += "Fecha: " + fechaCreacion + "\n";
        contenido += "Usuario: " + usuario.getNombre() + "\n\n";

        for (DetalleRutina detalle : detalles) {
            contenido += detalle.mostrarDetalle() + "\n";
        }

        return contenido;
    }

    public int getIdRutina() {
        return idRutina;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public String toString() {
        return nombre + " - " + fechaCreacion;
    }
}