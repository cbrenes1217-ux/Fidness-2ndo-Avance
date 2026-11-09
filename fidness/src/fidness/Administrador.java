package fidness;

public class Administrador extends Usuario {

    private static final long serialVersionUID = 1L;

    private int nivelAcceso;

    public Administrador(int idUsuario, String nombre,
                         String nombreUsuario, String contrasena,
                         String correo, int nivelAcceso) {

        super(idUsuario, nombre, nombreUsuario, contrasena, correo);

        this.nivelAcceso = nivelAcceso;
    }

    public void registrarEjercicio(Ejercicio ejercicio,
                                   java.util.ArrayList<Ejercicio> ejercicios) {
        ejercicios.add(ejercicio);
    }

    public void modificarEjercicio(int indice, Ejercicio ejercicio,
                                    java.util.ArrayList<Ejercicio> ejercicios) {

        ejercicios.set(indice, ejercicio);
    }

    public void eliminarEjercicio(int indice,
                                  java.util.ArrayList<Ejercicio> ejercicios) {

        ejercicios.remove(indice);
    }

    public java.util.ArrayList<Ejercicio> consultarEjercicios(
            java.util.ArrayList<Ejercicio> ejercicios) {

        return ejercicios;
    }

    @Override
    public String obtenerTipoUsuario() {
        return "Administrador";
    }

    public int getNivelAcceso() {
        return nivelAcceso;
    }
}