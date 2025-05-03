package org.p2pnexus.servidor.Entidades.DAO;

import org.hibernate.Session;
import org.p2pnexus.servidor.Entidades.EspacioCompartido;

import java.util.List;

public class EspacioCompartidoDAO extends DAO{

    public EspacioCompartido crearEspacioCompartido(EspacioCompartido espacioCompartido) {
        try(Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(espacioCompartido);
            session.getTransaction().commit();
            return espacioCompartido;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el espacio compartido: " + e.getMessage(), e);
        }
    }

    public List<EspacioCompartido> espaciosCompartidosPorPropietario(int id) {

        try(Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            List<EspacioCompartido> espaciosCompartidos = session.createQuery("FROM EspacioCompartido WHERE propietario.id_usuario = :id", EspacioCompartido.class)
                    .setParameter("id", id)
                    .getResultList();
            session.getTransaction().commit();
            return espaciosCompartidos;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los espacios compartidos por propietario: " + e.getMessage(), e);
        }
    }

}
