package org.p2pnexus.servidor.Entidades.DAO;

import org.hibernate.Session;
import org.p2pnexus.servidor.ControladorHibernate;

public class DAO {
    protected Session getSession() {
        return ControladorHibernate.getSession();
    }
}
