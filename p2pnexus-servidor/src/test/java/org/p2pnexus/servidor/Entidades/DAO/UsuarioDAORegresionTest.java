package org.p2pnexus.servidor.Entidades.DAO;

import org.junit.jupiter.api.*;
import org.p2pnexus.servidor.ControladorHibernate;
import org.p2pnexus.servidor.Entidades.Usuario;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAORegresionTest {

    @BeforeAll
    static void abrirSesion() {
        ControladorHibernate.abrirSesion();
    }

    @Test
    void testGuardarBuscarActualizarEliminarUsuario() {
        UsuarioDAO dao = new UsuarioDAO();

        // Guardar usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("regresion");
        usuario.setContrasena("1234");
        dao.guardar(usuario);

        // Buscar usuario
        Usuario recuperado = dao.buscarPorNombre("regresion");
        assertNotNull(recuperado);
        assertEquals("regresion", recuperado.getNombre());

        // Actualizar usuario
        recuperado.setContrasena("nueva");
        dao.actualizar(recuperado);
        Usuario actualizado = dao.buscarPorNombre("regresion");
        assertEquals("nueva", actualizado.getContrasena());

        // Eliminar usuario
        dao.eliminar(actualizado);
        Usuario eliminado = dao.buscarPorNombre("regresion");
        assertNull(eliminado);
    }
}
