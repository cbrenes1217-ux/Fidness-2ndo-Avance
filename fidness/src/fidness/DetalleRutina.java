package fidness;

import java.io.Serializable;

public class DetalleRutina implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idDetalle;
    private Ejercicio ejercicio;
    private int orden;
    private int series;
    private int repeticiones;

    public DetalleRutina(int idDetalle, Ejercicio ejercicio,
                         int orden, int series, int repeticiones) {

        this.idDetalle = idDetalle;
        this.ejercicio = ejercicio;
        this.orden = orden;
        this.series = series;
        this.repeticiones = repeticiones;
    }

    public void actualizarSeries(int series) {
        this.series = series;
    }

    public void actualizarRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public String mostrarDetalle() {
        return orden + ". " + ejercicio.getNombre()
                + " | Series: " + series
                + " | Repeticiones: " + repeticiones;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public int getOrden() {
        return orden;
    }

    public int getSeries() {
        return series;
    }

    public int getRepeticiones() {
        return repeticiones;
    }
}