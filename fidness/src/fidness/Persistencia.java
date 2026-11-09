package fidness;

import java.io.*;

public class Persistencia {

    public static void guardar(Object objeto, String archivo)
            throws IOException {

        ObjectOutputStream salida =
                new ObjectOutputStream(new FileOutputStream(archivo));

        salida.writeObject(objeto);
        salida.close();
    }

    public static Object cargar(String archivo)
            throws IOException, ClassNotFoundException {

        File fichero = new File(archivo);

        if (!fichero.exists()) {
            return null;
        }

        ObjectInputStream entrada =
                new ObjectInputStream(new FileInputStream(archivo));

        Object objeto = entrada.readObject();
        entrada.close();

        return objeto;
    }
}