package com.example.dockt.DAO

import android.net.Uri
import com.example.dockt.DTO.DocumentDTO
import com.example.dockt.DTO.UserDataDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ServiceDAO {
    suspend fun autentificacion(username: String?, password: String?): Int {
        val db = FirebaseFirestore.getInstance()
        var result = 0
        for (user in db.collection("users").get().await()) {
            if (user.get("username") == username){
                if (user.get("password") == password){
                    result = 1;
                }else{
                    result = 2
                }
            }
        }
        return result;
    }

    suspend fun obteneridUsuario(username: String?) : String{
        val db = FirebaseFirestore.getInstance()
        var idUser = ""
        for (user in db.collection("users").get().await()) {
            if (user.get("username") == username){
                idUser = user.id
            }
        }
        return idUser;
    }

    suspend fun DatosUsuario(idUser: String ) : UserDataDTO{
        var user = UserDataDTO()
        val db = FirebaseFirestore.getInstance()
        var result = db.collection("users").document(idUser).get().await()

        user.id = result.id
        user.username = result.get("username") as String?
        user.contraseña = result.get("password") as String?
        user.nombre = result.get("nombre") as String?
        user.cedula = result.get("cedula") as String?
        user.correo = result.get("correo") as String?
        user.telefono = result.get("telefono") as String?
        user.documentosPropios = user.cedula?.let { obtenerDocumentosUsuario(it) }

        return user;
    }

    suspend fun actualizarDatosUsuario(idUser : String, name: String, correo: String, telefono: String){
        val db = FirebaseFirestore.getInstance()
        var data = db.collection("users").document(idUser)

        data.update("nombre", name).await()
        data.update("correo", correo).await()
        data.update("telefono", telefono).await()

    }

    suspend fun obtenerDocumentosUsuario(cedula : String) : ArrayList<DocumentDTO>{
        var files : ArrayList<DocumentDTO> = java.util.ArrayList()
        val storage = FirebaseStorage.getInstance().getReference(cedula)
        val documents = storage.listAll().await()
        for (file in documents.items){
            var doc : DocumentDTO = DocumentDTO()
            doc.documentName = file.name
            doc.documentURL = file.path
            files.add(doc)
        }
        return files
    }

    suspend fun obtenerUrlFile(path: String): Uri {
        val storage = FirebaseStorage.getInstance().reference
        return storage.child(path).downloadUrl.await();
    }

    suspend fun eliminarFile(path : String) {
        val storage = FirebaseStorage.getInstance().reference
        val file = storage.child(path)
        file.delete().await()
    }


}