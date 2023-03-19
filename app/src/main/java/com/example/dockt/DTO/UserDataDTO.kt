package com.example.dockt.DTO

class UserDataDTO : java.io.Serializable{
    var id : String? = null
    var cedula: String? = null
    var nombre: String? = null
    var correo: String? = null
    var telefono: String? = null
    var username: String? = null
    var contraseña: String? = null
    var documentosPropios: ArrayList<DocumentDTO>? = null
}