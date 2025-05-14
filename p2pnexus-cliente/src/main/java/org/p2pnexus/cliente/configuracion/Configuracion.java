package org.p2pnexus.cliente.configuracion;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import javafx.application.Application;
import javafx.scene.image.Image;

import java.io.*;
import java.net.URL;
import java.util.Properties;

public class Configuracion {
    Properties configuracion = new Properties();
    String ruta = "configuracion/cliente.properties";

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

    public Image logoTema()
    {
        String modo = configuracion.getProperty("ui.modo-tema");
        URL url = null;
        if (modo.equals("nocturno")) {
            url = getClass().getResource("/org/p2pnexus/cliente/imagenes/logop2pnexus-nocturno.png");
        } else {
            url = getClass().getResource("/org/p2pnexus/cliente/imagenes/logop2pnexus-diurno.png");
        }
        if (url == null) {
            throw new RuntimeException("No se ha encontrado la imagen del logo");
        }
        return new Image(url.toString());
    }

    public void alternarModoTema()
    {
        System.out.printf("Cambiando tema, modo actual: %s\n", configuracion.getProperty("ui.modo-tema"));
        String modo = configuracion.getProperty("ui.modo-tema");
        if (modo.equals("nocturno")) {
            configuracion.setProperty("ui.modo-tema", "diurno");
        } else {
            configuracion.setProperty("ui.modo-tema", "nocturno");
        }

        guardarConfiguracion();

        aplicarModoTemaAcual();
    }

    public void aplicarModoTemaAcual()
    {
        String modo = configuracion.getProperty("ui.modo-tema");
        if (modo.equals("nocturno")) {
            Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
        } else {
            Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
        }
    }

    public void guardarConfiguracion()
    {
        try (FileOutputStream output = new FileOutputStream(ruta)) {
            configuracion.store(output, "Configuración de p2pnexus");
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            // nos aseguramos de crear la carpeta de configuracion si no existe
            new File("configuracion").mkdirs();
            configuracion.setProperty("servidor", "sv.mncly.com");
            configuracion.setProperty("puerto", "17214"); // He elegido este puerto por defecto porque P -> 17 2 -> 2 N -> 14 asi tiene un poco de sentido la elección
            configuracion.setProperty("ui.modo-tema", "nocturno");
            configuracion.store(new FileOutputStream(ruta), "Configuración por defecto de p2pnexus \nservidor = sv.mncly.com\npuerto : 17214");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
