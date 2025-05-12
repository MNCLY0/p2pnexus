package org.p2pnexus.cliente.p2p.manejador.manejadores.descarga;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import org.p2pnexus.cliente.p2p.conexion.GestorP2P;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.ventanas.GestorVentanas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

public class ManejadorP2PDescargaSolicitud implements IManejadorMensaje {

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) {
        String ruta = mensaje.getData().get("ruta").getAsString();
        String nombre = mensaje.getData().get("nombre").getAsString();
        Usuario solicitante = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("solicitante").getAsJsonObject(), Usuario.class);

        GestorP2P gestor = GestorP2P.conexiones.get(solicitante.getId_usuario());

        if (gestor == null) {
            System.err.println("Se ha perdido la conexión con el usuario: " + solicitante.getNombre());
            return null;
        }

        File archivo = new File(ruta);
        if (!archivo.exists() || !archivo.isFile()) {
            System.err.println("Archivo no encontrado: " + ruta);
            return null;
        }

        try (FileInputStream in = new FileInputStream(archivo)) {
            byte[] buffer = new byte[4096];
            int bytesLeidos;

            while ((bytesLeidos = in.read(buffer)) != -1) {
                byte[] fragmento = Arrays.copyOf(buffer, bytesLeidos);
                String base64 = Base64.getEncoder().encodeToString(fragmento);

                JsonObject data = new JsonObject();
                data.addProperty("nombre", nombre);
                data.addProperty("fragmento", base64);
                data.addProperty("pesoTotal", archivo.length());

                Mensaje respuesta = new Mensaje(TipoMensaje.P2P_R_FRAGMENTO_ARCHIVO, data);
                gestor.manejador.enviarMensaje(respuesta);
            }

            JsonObject dataFin = new JsonObject();
            dataFin.addProperty("nombre", nombre);
            dataFin.addProperty("fin", true);

            Mensaje fin = new Mensaje(TipoMensaje.P2P_R_FIN_ARCHIVO, dataFin);
            gestor.manejador.enviarMensaje(fin);

        } catch (IOException e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }

        return null;
    }
}
