package org.p2pnexus.servidor.Entidades.DAO;

import org.hibernate.Session;
import org.p2pnexus.servidor.Entidades.Conversacion;
import org.p2pnexus.servidor.Entidades.Mensaje;
import org.p2pnexus.servidor.Entidades.Participante;
import org.p2pnexus.servidor.Entidades.Usuario;

import java.time.LocalDateTime;
import java.util.List;

public class ConversacionDAO extends DAO{


    public Conversacion obtenerConversacionEntreDos(int id_usuario1, int id_usuario2) {
            try (Session session = getSessionFactory().openSession()) {
                session.beginTransaction();
                List<Conversacion> conversacionList = session.createQuery(
                        """
                   SELECT c FROM Conversacion c
                   WHERE :id_usuario1 IN (
                   SELECT p.usuario.id_usuario FROM Participante p WHERE p.conversacion = c)
                   AND :id_usuario2 IN (SELECT p.usuario.id_usuario FROM Participante p WHERE p.conversacion = c)""", Conversacion.class)
                .setParameter("id_usuario1", id_usuario1)
                .setParameter("id_usuario2", id_usuario2)
                .getResultList();

        if (!conversacionList.isEmpty()) {
            session.getTransaction().commit();
            return conversacionList.get(0);
        }

        // Si no existe la creamos
        Conversacion nuevaConversacion = new Conversacion();
        nuevaConversacion.setFecha_creacion(LocalDateTime.now());
        session.persist(nuevaConversacion);

        Usuario usuario1 = session.get(Usuario.class, id_usuario1);
        Usuario usuario2 = session.get(Usuario.class, id_usuario2);

        Participante p1 = new Participante();
        p1.setUsuario(usuario1);
        p1.setConversacion(nuevaConversacion);
        session.persist(p1);

        Participante p2 = new Participante();
        p2.setUsuario(usuario2);
        p2.setConversacion(nuevaConversacion);
        session.persist(p2);

        session.getTransaction().commit();
        return nuevaConversacion;

    } catch (Exception e) {
        throw new RuntimeException("Error al obtener o crear conversación: " + e.getMessage(), e);
        }
    }

    public List<Mensaje> obtenerUltimosMensajesDeConversacion(int idConversacion) {
    try (Session session = getSessionFactory().openSession()) {
        return session.createQuery("""
                        FROM Mensaje m
                        WHERE m.conversacion.id_conversacion = :idConversacion
                        ORDER BY m.fecha_envio ASC
                    """, Mensaje.class)
                        .setParameter("idConversacion", idConversacion)
                        .setMaxResults(50)
                        .list();
            } catch (Exception e) {
                throw new RuntimeException("Error al obtener los últimos mensajes: " + e.getMessage(), e);
            }
        }

    }
