package org.p2pnexus.cliente.p2p;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;

import java.io.File;

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

        //Creamos el json donde vamos a tener toda la info del directorio con la
        JsonObject json = new JsonObject();
        json.addProperty("directorio", directorio.getAbsolutePath());

        JsonArray archivosArray = new JsonArray();


        File[] archivos = directorio.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    JsonObject jsonArchivo = new JsonObject();
                    jsonArchivo.addProperty("nombre", archivo.getName());
                    jsonArchivo.addProperty("tamaño", archivo.length());
                    jsonArchivo.addProperty("ruta", archivo.getAbsolutePath());
                    jsonArchivo.addProperty("extension", obtenerExtension(archivo));
                    archivosArray.add(jsonArchivo);
                } else {
                    System.out.println("El archivo no es un archivo: " + archivo.getAbsolutePath());
                }
            }
        }

        json.add("archivos", archivosArray);

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
