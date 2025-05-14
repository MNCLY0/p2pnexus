package org.p2pnexus.servidor.Configuracion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Configuracion {
    Properties configuracion = new Properties();
    String ruta = "configuracion/server.properties";

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

    public String getModoTema() {return configuracion.getProperty("ui.modo-tema");}


    public void guardarConfiguracion()
    {
        try (FileOutputStream output = new FileOutputStream(ruta)) {
            configuracion.store(output, "Configuración de p2pNexus server");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getServidor() {
        return configuracion.getProperty("bbdd.server","");
    }

    public String getUsuario() {
        return configuracion.getProperty("bbdd.usuario","");
    }

    public String getPassword() {
        return configuracion.getProperty("bbdd.password","");
    }


    public String getPuerto() {
        return configuracion.getProperty("bbdd.puerto","");
    }

    public void crearPorDefecto()
    {
        try {
            // nos aseguramos de crear la carpeta de configuracion si no existe
            new File("configuracion").mkdirs();
            configuracion.setProperty("bbdd.server", "sv.mncly.com");
            configuracion.setProperty("bbdd.puerto", "3306");
            configuracion.setProperty("bbdd.usuario", "");
            configuracion.setProperty("bbdd.password", "");

            configuracion.store(new FileOutputStream(ruta), "Configuración por defecto de p2pNexus Server \n bbdd.server: sv.mncly.com \n bbdd.puerto: 3306 \n bbdd.usuario: \n bbdd.password: ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
