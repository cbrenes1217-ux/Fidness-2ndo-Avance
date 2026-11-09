package fidness;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class FidnessApp extends JFrame {

    private ArrayList<Usuario> usuarios;
    private ArrayList<Ejercicio> ejercicios;

    private Usuario usuarioActual;

    private DefaultListModel<Ejercicio> modeloEjercicios;
    private JList<Ejercicio> listaEjercicios;

    private DefaultListModel<Rutina> modeloRutinas;
    private JList<Rutina> listaRutinas;

    private JTextArea areaInformacion;

    private int contadorEjercicios = 4;
    private int contadorRutinas = 1;
    private int contadorDetalles = 1;

    public FidnessApp() {

        usuarios = new ArrayList<>();
        ejercicios = new ArrayList<>();

        cargarDatosIniciales();

        setTitle("FIDNESS - Sistema de Rutinas");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mostrarLogin();
    }

    private void cargarDatosIniciales() {

        try {
            Object datosUsuarios = Persistencia.cargar("usuarios.dat");
            Object datosEjercicios = Persistencia.cargar("ejercicios.dat");

            if (datosUsuarios != null) {
                usuarios = (ArrayList<Usuario>) datosUsuarios;
            }

            if (datosEjercicios != null) {
                ejercicios = (ArrayList<Ejercicio>) datosEjercicios;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los datos iniciales.");
        }

        if (usuarios.isEmpty()) {

            usuarios.add(new Usuario(
                    1, "Usuario de prueba", "usuario",
                    "1234", "usuario@fidness.com"));

            usuarios.add(new Administrador(
                    2, "Administrador", "admin",
                    "1234", "admin@fidness.com", 1));
        }

        if (ejercicios.isEmpty()) {

            ejercicios.add(new Ejercicio(
                    1, "Sentadillas", "Piernas",
                    "Ejercicio para fortalecer las piernas.",
                    "Coloque los pies separados, flexione las rodillas "
                    + "y baje manteniendo la espalda recta.", ""));

            ejercicios.add(new Ejercicio(
                    2, "Flexiones", "Pecho",
                    "Ejercicio para fortalecer el pecho y brazos.",
                    "Coloque las manos en el suelo y flexione los brazos "
                    + "manteniendo el cuerpo recto.", ""));

            ejercicios.add(new Ejercicio(
                    3, "Remo", "Espalda",
                    "Ejercicio para fortalecer la espalda.",
                    "Incline ligeramente el torso y lleve los brazos "
                    + "hacia atrás.", ""));
        }

        actualizarContadores();
        guardarDatos();
    }

    private void actualizarContadores() {

        for (Ejercicio e : ejercicios) {
            contadorEjercicios = Math.max(
                    contadorEjercicios, e.getIdEjercicio() + 1);
        }

        for (Usuario u : usuarios) {
            for (Rutina r : u.consultarRutinas()) {
                contadorRutinas = Math.max(
                        contadorRutinas, r.getIdRutina() + 1);
            }
        }
    }

    private void guardarDatos() {

        try {
            Persistencia.guardar(usuarios, "usuarios.dat");
            Persistencia.guardar(ejercicios, "ejercicios.dat");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar los datos: " + e.getMessage());
        }
    }

    private void mostrarLogin() {

        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));

        JTextField campoUsuario = new JTextField();
        JPasswordField campoClave = new JPasswordField();

        panel.add(new JLabel("Nombre de usuario:"));
        panel.add(campoUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(campoClave);

        int resultado = JOptionPane.showConfirmDialog(
                this, panel, "FIDNESS - Iniciar sesión",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {

            String nombre = campoUsuario.getText();
            String clave = new String(campoClave.getPassword());

            usuarioActual = null;

            for (Usuario usuario : usuarios) {

                if (usuario.iniciarSesion(nombre, clave)) {
                    usuarioActual = usuario;
                    break;
                }
            }

            if (usuarioActual != null) {
                mostrarSistema();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Usuario o contraseña incorrectos.");
                mostrarLogin();
            }

        } else {
            System.exit(0);
        }
    }

    private void mostrarSistema() {

        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
                "FIDNESS - Bienvenido/a " + usuarioActual.getNombre(),
                SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        add(titulo, BorderLayout.NORTH);

        JTabbedPane pestañas = new JTabbedPane();

        pestañas.addTab("Catálogo de ejercicios",
                crearPanelCatalogo());

        pestañas.addTab("Mis rutinas",
                crearPanelRutinas());

        if (usuarioActual instanceof Administrador) {
            pestañas.addTab("Administrar ejercicios",
                    crearPanelAdministrador());
        }

        JButton cerrarSesion = new JButton("Cerrar sesión");

        cerrarSesion.addActionListener(e -> {
            guardarDatos();
            usuarioActual.cerrarSesion();
            usuarioActual = null;
            mostrarLogin();
        });

        JPanel inferior = new JPanel();
        inferior.add(cerrarSesion);

        add(pestañas, BorderLayout.CENTER);
        add(inferior, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel crearPanelCatalogo() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        modeloEjercicios = new DefaultListModel<>();
        listaEjercicios = new JList<>(modeloEjercicios);

        actualizarListaEjercicios();

        areaInformacion = new JTextArea();
        areaInformacion.setEditable(false);
        areaInformacion.setLineWrap(true);
        areaInformacion.setWrapStyleWord(true);

        JButton ver = new JButton("Ver información");
        JButton instrucciones = new JButton("Ver instrucciones");

        ver.addActionListener(e -> mostrarInformacionEjercicio());
        instrucciones.addActionListener(e -> mostrarInstrucciones());

        JPanel botones = new JPanel();
        botones.add(ver);
        botones.add(instrucciones);

        panel.add(new JLabel("Catálogo de ejercicios"),
                BorderLayout.NORTH);

        panel.add(new JScrollPane(listaEjercicios),
                BorderLayout.WEST);

        panel.add(new JScrollPane(areaInformacion),
                BorderLayout.CENTER);

        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    private void actualizarListaEjercicios() {

        if (modeloEjercicios == null) {
            return;
        }

        modeloEjercicios.clear();

        for (Ejercicio ejercicio : ejercicios) {
            modeloEjercicios.addElement(ejercicio);
        }
    }

    private Ejercicio obtenerEjercicioSeleccionado() {

        Ejercicio ejercicio = listaEjercicios.getSelectedValue();

        if (ejercicio == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un ejercicio primero.");
        }

        return ejercicio;
    }

    private void mostrarInformacionEjercicio() {

        Ejercicio ejercicio = obtenerEjercicioSeleccionado();

        if (ejercicio != null) {
            areaInformacion.setText(ejercicio.mostrarInformacion());
        }
    }

    private void mostrarInstrucciones() {

        Ejercicio ejercicio = obtenerEjercicioSeleccionado();

        if (ejercicio != null) {
            areaInformacion.setText(
                    ejercicio.mostrarInstrucciones());
        }
    }

    private JPanel crearPanelAdministrador() {

        JPanel panel = new JPanel(new BorderLayout());

        DefaultListModel<Ejercicio> modelo
                = new DefaultListModel<>();

        JList<Ejercicio> lista = new JList<>(modelo);

        actualizarModelo(modelo);

        JButton registrar = new JButton("Registrar ejercicio");
        JButton modificar = new JButton("Modificar ejercicio");
        JButton eliminar = new JButton("Eliminar ejercicio");

        registrar.addActionListener(e -> registrarEjercicio(modelo));

        modificar.addActionListener(e
                -> modificarEjercicio(lista, modelo));

        eliminar.addActionListener(e
                -> eliminarEjercicio(lista, modelo));

        JPanel botones = new JPanel();

        botones.add(registrar);
        botones.add(modificar);
        botones.add(eliminar);

        panel.add(new JScrollPane(lista), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    private void actualizarModelo(DefaultListModel<Ejercicio> modelo) {

        modelo.clear();

        for (Ejercicio ejercicio : ejercicios) {
            modelo.addElement(ejercicio);
        }
    }

    private void registrarEjercicio(DefaultListModel<Ejercicio> modelo) {

        JTextField nombre = new JTextField();
        JTextField grupo = new JTextField();
        JTextField descripcion = new JTextField();
        JTextField instrucciones = new JTextField();

        Object[] campos = {
            "Nombre:", nombre,
            "Grupo muscular:", grupo,
            "Descripción:", descripcion,
            "Instrucciones:", instrucciones
        };

        int opcion = JOptionPane.showConfirmDialog(
                this, campos, "Registrar ejercicio",
                JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {

            try {

                if (nombre.getText().trim().isEmpty()
                        || grupo.getText().trim().isEmpty()
                        || descripcion.getText().trim().isEmpty()
                        || instrucciones.getText().trim().isEmpty()) {

                    throw new ValidacionException(
                            "Todos los campos son obligatorios.");
                }

                Ejercicio nuevo = new Ejercicio(
                        contadorEjercicios++,
                        nombre.getText(),
                        grupo.getText(),
                        descripcion.getText(),
                        instrucciones.getText(), "");

                ((Administrador) usuarioActual)
                        .registrarEjercicio(nuevo, ejercicios);

                actualizarModelo(modelo);
                actualizarListaEjercicios();
                guardarDatos();

            } catch (ValidacionException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void modificarEjercicio(JList<Ejercicio> lista,
            DefaultListModel<Ejercicio> modelo) {

        Ejercicio seleccionado = lista.getSelectedValue();

        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un ejercicio.");
            return;
        }

        JTextField nombre = new JTextField(seleccionado.getNombre());
        JTextField grupo = new JTextField(
                seleccionado.getGrupoMuscular());

        JTextField descripcion = new JTextField(
                seleccionado.getDescripcion());

        JTextField instrucciones = new JTextField(
                seleccionado.getInstrucciones());

        Object[] campos = {
            "Nombre:", nombre,
            "Grupo muscular:", grupo,
            "Descripción:", descripcion,
            "Instrucciones:", instrucciones
        };

        int opcion = JOptionPane.showConfirmDialog(
                this, campos, "Modificar ejercicio",
                JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {

            try {

                if (nombre.getText().trim().isEmpty()
                        || grupo.getText().trim().isEmpty()
                        || descripcion.getText().trim().isEmpty()
                        || instrucciones.getText().trim().isEmpty()) {

                    throw new ValidacionException(
                            "Todos los campos son obligatorios.");
                }

                seleccionado.actualizarInformacion(
                        nombre.getText(),
                        grupo.getText(),
                        descripcion.getText(),
                        instrucciones.getText(), "");

                actualizarModelo(modelo);
                actualizarListaEjercicios();
                guardarDatos();

            } catch (ValidacionException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void eliminarEjercicio(JList<Ejercicio> lista,
            DefaultListModel<Ejercicio> modelo) {

        Ejercicio seleccionado = lista.getSelectedValue();

        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un ejercicio.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar " + seleccionado.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {

            int indice = ejercicios.indexOf(seleccionado);

            ((Administrador) usuarioActual)
                    .eliminarEjercicio(indice, ejercicios);

            actualizarModelo(modelo);
            actualizarListaEjercicios();
            guardarDatos();
        }
    }

    private JPanel crearPanelRutinas() {

        JPanel panel = new JPanel(new BorderLayout());

        modeloRutinas = new DefaultListModel<>();
        listaRutinas = new JList<>(modeloRutinas);

        actualizarListaRutinas();

        JButton crear = new JButton("Crear rutina");
        JButton agregar = new JButton("Agregar ejercicio");
        JButton ver = new JButton("Ver ejercicios");
        JButton eliminar = new JButton("Eliminar rutina");
        JButton exportar = new JButton("Exportar rutina");

        crear.addActionListener(e -> crearRutina());
        agregar.addActionListener(e -> agregarEjercicioRutina());
        ver.addActionListener(e -> verEjerciciosRutina());
        eliminar.addActionListener(e -> eliminarRutina());
        exportar.addActionListener(e -> exportarRutina());

        JPanel botones = new JPanel();

        botones.add(crear);
        botones.add(agregar);
        botones.add(ver);
        botones.add(eliminar);
        botones.add(exportar);

        panel.add(new JLabel("Mis rutinas"),
                BorderLayout.NORTH);

        panel.add(new JScrollPane(listaRutinas),
                BorderLayout.CENTER);

        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    private void actualizarListaRutinas() {

        modeloRutinas.clear();

        for (Rutina rutina : usuarioActual.consultarRutinas()) {
            modeloRutinas.addElement(rutina);
        }
    }

    private void crearRutina() {

        String nombre = JOptionPane.showInputDialog(
                this, "Ingrese el nombre de la rutina:");

        if (nombre == null) {
            return;
        }

        try {

            if (nombre.trim().isEmpty()) {
                throw new ValidacionException(
                        "El nombre de la rutina es obligatorio.");
            }

            Rutina nueva = new Rutina(
                    contadorRutinas++, nombre, usuarioActual);

            usuarioActual.agregarRutina(nueva);

            actualizarListaRutinas();
            guardarDatos();

        } catch (ValidacionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void agregarEjercicioRutina() {

        Rutina rutina = listaRutinas.getSelectedValue();

        if (rutina == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una rutina.");
            return;
        }

        Ejercicio ejercicio = (Ejercicio) JOptionPane.showInputDialog(
                this,
                "Seleccione un ejercicio:",
                "Ejercicios disponibles",
                JOptionPane.PLAIN_MESSAGE,
                null,
                ejercicios.toArray(),
                null);

        if (ejercicio == null) {
            return;
        }

        JTextField series = new JTextField("3");
        JTextField repeticiones = new JTextField("12");

        Object[] campos = {
            "Series:", series,
            "Repeticiones:", repeticiones
        };

        int opcion = JOptionPane.showConfirmDialog(
                this, campos, "Configurar ejercicio",
                JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {

            try {

                int cantidadSeries = Integer.parseInt(series.getText());
                int cantidadRepeticiones
                        = Integer.parseInt(repeticiones.getText());

                if (cantidadSeries <= 0 || cantidadRepeticiones <= 0) {
                    throw new ValidacionException(
                            "Las series y repeticiones deben ser mayores que cero.");
                }

                DetalleRutina detalle = new DetalleRutina(
                        contadorDetalles++,
                        ejercicio,
                        rutina.consultarRutina().size() + 1,
                        cantidadSeries,
                        cantidadRepeticiones);

                rutina.agregarEjercicio(detalle);

                guardarDatos();

                JOptionPane.showMessageDialog(this,
                        "Ejercicio agregado correctamente.");

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(this,
                        "Ingrese números válidos.");

            } catch (ValidacionException ex) {

                JOptionPane.showMessageDialog(this,
                        ex.getMessage());
            }
        }
    }

    private void verEjerciciosRutina() {

        Rutina rutina = listaRutinas.getSelectedValue();

        if (rutina == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una rutina primero.");
            return;
        }

        StringBuilder informacion = new StringBuilder();

        informacion.append("RUTINA: ")
                .append(rutina.getNombre())
                .append("\n\n");

        ArrayList<DetalleRutina> detalles
                = rutina.consultarRutina();

        if (detalles.isEmpty()) {

            informacion.append(
                    "Esta rutina todavía no tiene ejercicios.");

        } else {

            int numero = 1;

            for (DetalleRutina detalle : detalles) {

                informacion.append(numero++)
                        .append(". ")
                        .append(detalle.getEjercicio().getNombre())
                        .append("\n");

                informacion.append("   Series: ")
                        .append(detalle.getSeries())
                        .append("\n");

                informacion.append("   Repeticiones: ")
                        .append(detalle.getRepeticiones())
                        .append("\n\n");
            }
        }

        JTextArea area = new JTextArea(informacion.toString());

        area.setEditable(false);
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(area),
                "Ejercicios de la rutina",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarRutina() {

        Rutina rutina = listaRutinas.getSelectedValue();

        if (rutina == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una rutina para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar la rutina: "
                + rutina.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {

            usuarioActual.consultarRutinas().remove(rutina);

            actualizarListaRutinas();

            guardarDatos();

            JOptionPane.showMessageDialog(this,
                    "La rutina fue eliminada correctamente.");
        }
    }

    private void exportarRutina() {

        Rutina rutina = listaRutinas.getSelectedValue();

        if (rutina == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una rutina.");
            return;
        }

        JFileChooser selector = new JFileChooser();

        selector.setSelectedFile(
                new File(rutina.getNombre() + ".txt"));

        int resultado = selector.showSaveDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {

            File archivo = selector.getSelectedFile();

            try (FileWriter escritor = new FileWriter(archivo)) {

                escritor.write(rutina.exportarRutina());

                JOptionPane.showMessageDialog(this,
                        "Rutina exportada correctamente.");

            } catch (IOException ex) {

                JOptionPane.showMessageDialog(this,
                        "Error al exportar: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            FidnessApp aplicacion = new FidnessApp();

            aplicacion.setVisible(true);
        });
    }
}
