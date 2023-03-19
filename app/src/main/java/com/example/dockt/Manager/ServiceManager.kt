package com.example.dockt.Manager

import android.net.Uri
import com.example.dockt.DAO.ServiceDAO
import com.example.dockt.DTO.UserDataDTO

class ServiceManager {

    suspend fun autentificacion(userName: String?, password: String?): Int {
        val service = ServiceDAO()
        return service.autentificacion(userName, password)
    }

    suspend fun consultarIdUsuario(userName: String?) : String{
        val service = ServiceDAO()
        return service.obteneridUsuario(userName)
    }

    suspend fun obtenerDatosUsuario(idUser: String): UserDataDTO{
        val service = ServiceDAO()
        return service.DatosUsuario(idUser)
    }

    suspend fun actualizarDatosUsuario(idUser: String, name: String, correo: String, telefono: String){
        val service = ServiceDAO()
        service.actualizarDatosUsuario(idUser, name, correo, telefono)
    }

    suspend fun obtenerUrlFile(path : String) : Uri {
        val service = ServiceDAO()
        return service.obtenerUrlFile(path);
    }

    suspend fun eliminarFile(path : String) {
        val service = ServiceDAO()
        service.eliminarFile(path)
    }
}