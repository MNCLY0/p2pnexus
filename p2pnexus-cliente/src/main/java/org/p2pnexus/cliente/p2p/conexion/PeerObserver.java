package org.p2pnexus.cliente.p2p.conexion;

import dev.onvoid.webrtc.*;


public class PeerObserver implements PeerConnectionObserver {
    @Override
    public void onSignalingChange(RTCSignalingState state) {
        PeerConnectionObserver.super.onSignalingChange(state);
    }

    @Override
    public void onConnectionChange(RTCPeerConnectionState state) {
        PeerConnectionObserver.super.onConnectionChange(state);
    }

    @Override
    public void onIceCandidate(RTCIceCandidate rtcIceCandidate) {

    }

    @Override
    public void onDataChannel(RTCDataChannel dataChannel) {
        PeerConnectionObserver.super.onDataChannel(dataChannel);
    }
}
