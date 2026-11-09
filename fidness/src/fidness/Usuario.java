package fidness;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int idUsuario;
    protected String nombre;
    protected String nombreUsuario;
    protected String contrasena;
    protected String correo;

    protected ArrayList<Rutina> rutinas;

    public Usuario(int idUsuario, String nombre, String nombreUsuario,
                   String contrasena, String correo) {

        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.correo = correo;
        this.rutinas = new ArrayList<>();
    }

    public boolean iniciarSesion(String usuario, String clave) {
        return nombreUsuario.equals(usuario)
                && contrasena.equals(clave);
    }

    public void cerrarSesion() {
        System.out.println("Sesión cerrada.");
    }

    public void actualizarDatos(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    public ArrayList<Rutina> consultarRutinas() {
        return rutinas;
    }

    public void agregarRutina(Rutina rutina) {
        rutinas.add(rutina);
    }

    public String obtenerTipoUsuario() {
        return "Usuario";
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    @Override
    public String toString() {
        return nombre + " (" + nombreUsuario + ")";
    }
}