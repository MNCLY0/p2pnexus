package org.p2pnexus.cliente.p2p;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HerramientasFicheros {

    public static JsonObject obtenerInformacionRuta(EspacioCompartido espacioCompartido) {
        // Obetenemos la ruta del espacio
        String ruta = espacioCompartido.getRutaPropiedadProperty().getValue();
        System.out.println("Ruta del directorio: " + ruta);
        File directorio = new File(ruta);

        if (!directorio.exists()) {
            // Si el directorio no existe significa que no se puede acceder a el
            return null;
        }

        if (!directorio.isDirectory()) {
            // Si no es un directorio pues no se puede acceder a el obviamente
            return null;
        }

        //Creamos el json donde vamos a tener toda la info del directorio con la lista de ficheros
        JsonObject json = new JsonObject();

        File[] ficheros = directorio.listFiles();
        List<Fichero> archivosDirectorio = new ArrayList<>();

        if (ficheros != null) {
            for (File archivo : ficheros) {

                long bytes = archivo.length();
                String lengthFormateado;
                // Si el tamaño es menor de de 1 MB se muestra en KB
                if (bytes < 1024 * 1024) {
                    lengthFormateado = String.format("%.2f KB", bytes / 1024.0);
                } else {
                    lengthFormateado = String.format("%.2f MB", bytes / (1024.0 * 1024.0));
                }

                if (archivo.isFile()) {
                    Fichero fichero = new Fichero(
                            archivo.getName(),
                            archivo.getAbsolutePath(),
                            lengthFormateado,
                            obtenerExtension(archivo)
                    );
                    archivosDirectorio.add(fichero);
                }
            }
        }

        json.add("ficheros", JsonHerramientas.empaquetarListaEnJsonObject(archivosDirectorio));

        return json;
    }

    public static String obtenerExtension(File file) {
        String nombre = file.getName();
        int i = nombre.lastIndexOf('.');
        if (i > 0 && i < nombre.length() - 1) {
            return nombre.substring(i + 1).toLowerCase();
        }
        return "";
    }
}
