package org.p2pnexus.servidor.Entidades.DAO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.p2pnexus.servidor.ControladorHibernate;
import org.p2pnexus.servidor.Entidades.Usuario;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOIntegracionTest {

    @BeforeAll
    static void abrirSesion()
    {
        ControladorHibernate.abrirSesion();
    }

    @Test
    void testEliminarUsuario() {
        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = new Usuario();
        usuario.setNombre("eliminar");
        usuario.setContrasena("contrasena");
        dao.guardar(usuario);
        dao.eliminar(usuario);
        Usuario recuperado = dao.buscarPorNombre("eliminar");
        assertNull(recuperado);
    }
}