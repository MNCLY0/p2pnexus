package org.p2pnexus.cliente.ventanas;

import atlantafx.base.controls.Notification;
import javafx.scene.Node;

public class NotificacionConSize extends Notification {

    int size = 0;

    public NotificacionConSize(int size) {
        this.size = size;
    }

    public NotificacionConSize(String message, int size) {
        super(message);
        this.size = size;
    }

    public NotificacionConSize(String message, Node graphic, int size) {
        super(message, graphic);
        this.size = size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

}
