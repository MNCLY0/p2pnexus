package org.p2pnexus.servidor.Entidades.DAO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SolicitudContactoDAOTest {

    SolicitudContactoDAO solicitudDAO = new SolicitudContactoDAO();

    @Test
    public void testPuedeMandarSolicitud_sinSolicitudesPrevias() {
        int idA = 1;
        int idB = 2;

        boolean resultado = solicitudDAO.puedeMandarSolicitud(idA, idB);
        assertTrue(resultado, "Debería poder mandar solicitud si no hay relación previa");
    }

    @Test
    public void testNoPuedeMandarSolicitud_siHayPendiente() {
        int idA = 1;
        int idB = 3;

        boolean resultado = solicitudDAO.puedeMandarSolicitud(idA, idB);
        assertFalse(resultado, "No debería poder mandar solicitud si ya hay una pendiente");
    }

    @Test
    public void testNoPuedeMandarSolicitud_siHayAceptada_ySonContactos() {
        int idA = 1;
        int idB = 4;

        boolean resultado = solicitudDAO.puedeMandarSolicitud(idA, idB);
        assertFalse(resultado, "No debería poder mandar si ya hay una aceptada y siguen siendo contactos");
    }

    @Test
    public void testPuedeMandarSolicitud_siAceptadaPeroEliminado() {
        int idA = 1;
        int idB = 5;

        boolean resultado = solicitudDAO.puedeMandarSolicitud(idA, idB);
        assertTrue(resultado, "Sí debería poder mandar si se eliminaron como contactos");
    }
}