package com.example.dockt.View.Activity

import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.dockt.DTO.DocumentDTO
import com.example.dockt.DTO.UserDataDTO
import com.example.dockt.Manager.ServiceManager
import com.example.dockt.R
import kotlinx.coroutines.runBlocking

class PerfilActivity : AppCompatActivity() {

    private var nombreEd: EditText? = null
    private var cedulaEd: EditText? = null
    private var correoEd: EditText? = null
    private var telefonoEd: EditText? = null
    private var actualizar: Button? = null
    private var foto : ImageView? = null

    private var idUser : String? = null
    private var name : String? = null
    private var cedula : String? = null
    private var correo : String? = null
    private var telefono : String? = null
    private var fileFoto : String? = null

    private var serviceManager : ServiceManager = ServiceManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil_layout)

        idUser = intent.extras!!.getString("idUser").toString()
        name = intent.extras!!.getString("name").toString()
        cedula = intent.extras!!.getString("cedula").toString()
        correo = intent.extras!!.getString("correo").toString()
        telefono = intent.extras!!.getString("telefono").toString()
        fileFoto = intent.extras!!.getString("foto")


        nombreEd = findViewById(R.id.name)
        cedulaEd = findViewById(R.id.cedula)
        correoEd = findViewById(R.id.correo)
        telefonoEd = findViewById(R.id.telefono)
        actualizar = findViewById(R.id.actualizar)
        foto = findViewById(R.id.foto_perfil)

        setData()
        setFoto()

        actualizar?.setOnClickListener {
            if (actualizar?.text?.equals("Actualizar") == true){
                nombreEd?.isEnabled = true
                correoEd?.isEnabled = true
                telefonoEd?.isEnabled = true
                actualizar?.setText("Guardar Cambios")
            }else{
                if (verificarConexion()){
                    runBlocking {
                         serviceManager.actualizarDatosUsuario(
                             idUser!!, nombreEd?.text.toString(),
                             correoEd?.text.toString(),telefonoEd?.text.toString()
                         )
                    }
                    mostrarMensaje()
                    actualizar?.setText("Actualizar")
                    nombreEd?.isEnabled = false
                    correoEd?.isEnabled = false
                    telefonoEd?.isEnabled = false

                }
            }
        }
    }

    fun setData(){
        nombreEd?.setText(name)
        cedulaEd?.setText(cedula)
        correoEd?.setText(correo)
        telefonoEd?.setText(telefono)
    }

    fun mostrarMensaje(){
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Great !")
            .setContentText("Datos Actualizados")
            .show()
    }

    fun verificarConexion() : Boolean{
        val cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
        println("Internet : $isConnected")
        return isConnected
    }

    fun setFoto(){
        if (fileFoto != ""){
            foto?.let { Glide.with(getApplicationContext()).load(fileFoto).into(it) };
        }else{
            foto?.setImageResource(R.drawable.avatar);
        }
    }
}