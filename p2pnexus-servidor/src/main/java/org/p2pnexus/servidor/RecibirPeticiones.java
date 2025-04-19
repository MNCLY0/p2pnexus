package org.p2pnexus.servidor;

import com.google.gson.Gson;
import com.p2pnexus.comun.Peticion;
import com.p2pnexus.comun.TipoPeticion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeption;
import org.p2pnexus.servidor.acciones.manejadoresPeticiones.ManejadorPeticiones;
import org.p2pnexus.servidor.acciones.manejadoresPeticiones.ManejarLogin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RecibirPeticiones implements Runnable {

    Socket cliente;

    Map<TipoPeticion, ManejadorPeticiones> manejadoresPeticiones = new HashMap<>();

    public RecibirPeticiones(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        // Aquí se maneja la petición del cliente
        inicialidarManejadores();
        while (true) {
            try {
                // Aquí se recibiría la petición del cliente
                 BufferedReader input = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                 System.out.println("Esperando petición del cliente...");
                 String json = input.readLine();
                 System.out.println("Petición recibida: " + json);
                 Peticion peticion = new Gson().fromJson(json, Peticion.class);

                 manejarPeticion(peticion);


            } catch (Exception e) {
                System.err.println("Error al manejar la petición: " + e.getMessage());
            }
        }
    }

    void inicialidarManejadores()
    {
        manejadoresPeticiones.put(TipoPeticion.LOGIN, new ManejarLogin());


        // Test lo unico que hace es imprimir los datos de la peticion recibida así que simplemente creamos una clase anónima
        manejadoresPeticiones.put(TipoPeticion.TEST, (datos -> System.out.println("Petición de prueba recibida: " + datos)));
    }

    void manejarPeticion(Peticion peticion)
    {
        TipoPeticion tipoPeticion = peticion.getTipo();
        ManejadorPeticiones manejador = manejadoresPeticiones.get(tipoPeticion);
        try {
            manejador.manejarDatos(peticion.getData());
        }catch (ManejarPeticionesExeption e) {
            System.err.println("Error al manejar la petición: " + e.getMessage());
            //todo Enviar error al cliente (quiero implementar primero el sistema de notificaciones en el cliente)
        }

    }


}
