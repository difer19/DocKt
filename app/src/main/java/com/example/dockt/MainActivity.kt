package com.example.dockt

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.dockt.Manager.ServiceManager
import com.example.dockt.View.Activity.UserMenuActivity
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {

    private var idUserTV: TextView? = null
    private  var passwordTV:TextView? = null
    private var login: Button? = null
    private  var register: Button? = null
    private var manager: ServiceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        manager = ServiceManager()

        idUserTV = findViewById(R.id.user_name)
        passwordTV = findViewById<TextView>(R.id.password)
        login = findViewById(R.id.login_button)
        register = findViewById<Button>(R.id.register_button)

        login?.setOnClickListener(View.OnClickListener { v: View? ->
            if (!idUserTV?.getText().toString().isEmpty() && !passwordTV?.getText().toString().isEmpty()) {
                if (autentificacion(idUserTV?.getText().toString(), passwordTV?.getText().toString()) == 1) {
                    System.out.println("Usuario en Sesion")
                    val idUsuario = consultarId(idUserTV?.getText().toString())
                    val intent = Intent(this, UserMenuActivity::class.java)
                    intent.putExtra("usuario", idUsuario)
                    startActivity(intent)
                } else if (autentificacion(idUserTV?.getText().toString(), passwordTV?.getText().toString()) == 2) {
                    System.out.println("Contraseña incorrecta")
                    mostrarErrorPasswordIncorrecta()
                } else {
                    System.out.println("Usuario no existe")
                    mostrarErrorUsuarioNoExiste()
                }
            }
        })
        register?.setOnClickListener(View.OnClickListener { x: View? ->
            val uri = Uri.parse("https://candid-mousse-d87cec.netlify.app/")
            val intent2 = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent2)
        })
    }

    override fun onRestart() {
        super.onRestart()
        idUserTV?.text = ""
        passwordTV?.text = ""
    }

    fun autentificacion(idUser: String?, password: String?): Int? {
        verificarConexion()
        var status = 0;
        runBlocking {
            status = manager?.autentificacion(idUser, password)!!
        }
        return status
    }

    fun consultarId(username: String?): String? {
        verificarConexion()
        var idUs = ""
        runBlocking {
            idUs = manager?.consultarIdUsuario(username).toString()
        }
        return idUs
    }

    fun mostrarErrorPasswordIncorrecta() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Error")
            .setContentText("Contraseña incorrecta")
            .show()
    }

    fun mostrarErrorSinConexion(){
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Error")
            .setContentText("Sin Conexion a Internet")
            .show()
    }

    fun mostrarErrorUsuarioNoExiste() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Error")
            .setContentText("El usuario no existe")
            .show()
    }

    fun verificarConexion() {
        val cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
        println("Internet : $isConnected")
    }

}