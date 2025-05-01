package org.p2pnexus.servidor.Entidades.DAO;

import org.hibernate.Session;
import org.p2pnexus.servidor.Entidades.Contacto;
import org.p2pnexus.servidor.Entidades.ContactoId;
import org.p2pnexus.servidor.Entidades.Usuario;

import java.time.LocalDateTime;

public class ContactoDAO extends DAO {

    public boolean crearContacto(int id_usuario1, int id_usuario2)
    {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();

            Usuario usuario1 = session.get(Usuario.class, id_usuario1);
            Usuario usuario2 = session.get(Usuario.class, id_usuario2);

            ContactoId contactoId = new ContactoId();
            contactoId.setUsuario1(id_usuario1);
            contactoId.setUsuario2(id_usuario2);

            Contacto contacto = new Contacto();
            contacto.setId(contactoId);
            contacto.setUsuario1(usuario1);
            contacto.setUsuario2(usuario2);
            contacto.setFecha_agregado(LocalDateTime.now());

            session.persist(contacto);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el contacto: " + e.getMessage(), e);
        }
    }

}
