package org.p2pnexus.cliente.configuracion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuracion {
    Properties configuracion = new Properties();
    String ruta = "configuracion/config.properties";

    public Configuracion() {
        configurar();
    }

    public void configurar()
    {
        try (FileInputStream input = new FileInputStream(ruta)) {
            configuracion.load(input);

        } catch (IOException e) {
            // Si no se encuentra el archivo se crea con valores por defecto y se vuelve a intentar cargar
            crearPorDefecto();
            configurar();
            System.out.println("No se ha encontrado el archivo de configuración, se ha creado uno por defecto.");
        }
    }

    public String getServidor() {
        return configuracion.getProperty("servidor");
    }

    public int getPuerto() {
        return Integer.parseInt(configuracion.getProperty("puerto"));
    }

    public void crearPorDefecto()
    {
        try {
            configuracion.setProperty("servidor", "sv.mncly.com");
            configuracion.setProperty("puerto", "17214"); // He elegido este puerto por defecto porque P -> 17 2 -> 2 N -> 14 asi tiene un poco de sentido la elección
            configuracion.store(new FileOutputStream(ruta), "Configuración por defecto de p2pnexus \nservidor = sv.mncly.com\npuerto : 17214");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
