package com.p2pnexus.comun;

public enum TipoMensaje {

    NOTIFICACION,

    // Mensajes de consulta

    S_LOGIN, // Solicitud de login de cliente al servidor
    S_REGISTRO, // Solicitud de registro de cliente al servidor
    S_SOLICITAR_CLAVE_PUBLICA, // Solicitud de clave pública de cliente al servidor (no está en uso actualmente)
    S_CERRAR_SESION, // Solicitud de cierre de sesión de cliente al servidor
    S_BUSCAR_USUARIOS_POR_NOMBRE, // Solicitud de búsqueda de usuarios por nombre de cliente al servidor
    S_SOLICITUDES_POR_ID, // Solicitud de solicitudes de contacto por id de cliente al servidor
    S_CREAR_SOLICITUD, // Solicitud de creación de solicitud de contacto de cliente al servidor
    S_ACEPTAR_SOLICITUD, // Solicitud de aceptación de solicitud de contacto de cliente al servidor
    S_DENEGAR_SOLICITUD, // Solicitud de denegación de solicitud de contacto de cliente al servidor
    S_LISTA_CONTACTOS, // Solicitud de lista de contactos de cliente al servidor
    S_ACTUALIZAR_CHAT, // Solicitud de actualización de chat de cliente al servidor
    S_ENVIAR_MENSAJE_CHAT, // Solicitud de envío de mensaje de chat de cliente al servidor
    S_CONVERSACION_CON_USUARIO, // Solicitud de conversación con usuario de cliente al servidor
    S_CREAR_ESPACIO, // Solicitud de creación de espacio compartido de cliente al servidor
    S_ESPACIOS_POR_ID, // Solicitud de espacios compartidos por id de cliente al servidor
    S_ELIMINAR_ESPACIO, // Solicitud de eliminación de espacio compartido de cliente al servidor
    S_EDITAR_ESPACIO, // Solicitud de edición de espacio compartido de cliente al servidor
    S_COMPARTIR_ESPACIO, // Solicitud de compartir espacio compartido de cliente al servidor
    S_DEJAR_DE_COMPARTIR_ESPACIO, // Solicitud de dejar de compartir espacio compartido de cliente al servidor
    S_GUARDAR_IMAGEN_PERFIL, // Solicitud de guardar imagen de perfil de cliente al servidor

    // Mensajes de respuesta
    R_CERRAR_SESION_CLIENTE, // Respuesta de cierre de sesión de servidor al cliente
    R_LOGIN_OK, // Respuesta de login correcto de servidor al cliente
    R_BUSCAR_USUARIOS_POR_NOMBRE, // Respuesta de búsqueda de usuarios por nombre de servidor al cliente
    R_SOLICITUDES_POR_ID, // Respuesta de solicitudes de contacto por id de servidor al cliente
    R_LISTA_CONTACTOS, // Respuesta de lista de contactos de servidor al cliente
    R_ACTUALIZAR_CHAT, // Respuesta de actualización de chat de servidor al cliente
    R_NUEVO_MENSAJE_CHAT, // Respuesta de nuevo mensaje de chat de servidor al cliente
    R_CONVERSACION_CON_USUARIO, // Respuesta de conversación con usuario de servidor al cliente
    R_CREAR_ESPACIO_OK, // Respuesta de creación de espacio compartido correcto de servidor al cliente
    R_ESPACIOS_POR_ID, // Respuesta de espacios compartidos por id de servidor al cliente
    R_COMPARTIR_ESPACIO_OK, // Respuesta de compartir espacio compartido correcto de servidor al cliente
    R_NUEVO_ESPACIO_RECIBIDO, // Respuesta de nuevo espacio compartido recibido de servidor al cliente
    R_ACTUALIZAR_ESPACIO_RECIBIDO, // Respuesta de actualización de espacio compartido recibido de servidor al cliente
    R_ELIMINAR_ESPACIO_RECIBIDO, // Respuesta de eliminación de espacio compartido recibido de servidor al cliente
    R_ESTADO_SESION_CONTACTO, // Respuesta de estado de sesión de contacto de servidor al cliente


    // Mensajes de P2P Cliente - Servidor
    // (estos mensajes se utilizan para usar el servidor como intermediario en la conexión P2P, el servidor solo reenvía los mensajes)

    S_P2P_SDP_OFERTA, // Solicitud de oferta SDP de cliente a cliente (usado el servidor como intermediario)
    S_P2P_SDP_RESPUESTA, // Solicitud de respuesta SDP de cliente a cliente (usado el servidor como intermediario)
    S_P2P_ICE, // Solicitud de ICE de cliente a cliente (usado el servidor como intermediario)

    R_P2P_SDP_OFERTA, // Respuesta de oferta SDP de cliente a cliene (usado el servidor como intermediario)
    R_P2P_SDP_RESPUESTA, // Respuesta de respuesta SDP de cliente a cliente (usado el servidor como intermediario)
    R_P2P_ICE, // Respuesta de ICE de cliente a cliente (usado el servidor como intermediario)

    // Mensajes de P2P Cliente - Cliente (ya directamente entre clientes)

    P2P_DEBUG_MENSAJE, // Mensaje de debug de cliente a cliente (simplemente se utiliza para imprimir en consola el mensaje)
    P2P_S_INFO_RUTA, // Solicitud de información de ruta de cliente a cliente (se utiliza para saber la ruta del archivo que se va a descargar)
    P2P_R_INFO_RUTA, // Respuesta de información de ruta de cliente a cliente
    P2P_S_DESCARGAR_FICHERO, // Solicitud de descarga de archivo de cliente a cliente
    P2P_R_DESCARGAR_FICHERO, // Respuesta de descarga de archivo de cliente a cliente

}