package org.p2pnexus.cliente.p2p.observers;

import dev.onvoid.webrtc.RTCSdpType;
import dev.onvoid.webrtc.SetSessionDescriptionObserver;

public class ReceptorDeRespuestaObserver implements SetSessionDescriptionObserver {

    @Override
    public void onSuccess() {
        System.out.println("Descripción remota establecida correctamente.");

    }

    @Override
    public void onFailure(String s) {
        System.err.println("Error al establecer la descripción remota (respuesta): " + s);
    }
}
