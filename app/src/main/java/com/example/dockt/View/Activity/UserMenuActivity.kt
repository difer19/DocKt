package com.example.dockt.View.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.dockt.DTO.DocumentDTO
import com.example.dockt.DTO.UserDataDTO
import com.example.dockt.Manager.ServiceManager
import com.example.dockt.R
import com.example.dockt.View.Design.DocItem
import kotlinx.coroutines.runBlocking

class UserMenuActivity : AppCompatActivity()  {
    private var cedula: TextView? = null
    private  var nombre:TextView? = null
    private var listDocs: LinearLayout? = null
    private var foto: ImageView? = null
    private var addDoc: Button? = null
    private  var sharedDoc:Button? = null
    private var manager: ServiceManager? = null
    private var user: UserDataDTO? = null
    private var docs : ArrayList<DocItem>? = java.util.ArrayList()
    private var fileFoto : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_menu_layout)

        manager = ServiceManager()
        runBlocking {
            user = manager?.obtenerDatosUsuario(intent.extras!!.getString("usuario").toString())
        }

        cedula = findViewById<TextView>(R.id.cedula)
        nombre = findViewById<TextView>(R.id.name)
        listDocs = findViewById<LinearLayout>(R.id.list_docs)
        addDoc = findViewById<Button>(R.id.add_doc)
        sharedDoc = findViewById<Button>(R.id.shared_doc)
        foto = findViewById<ImageView>(R.id.foto)

        setWidgets()
        setDocuments()
        setDeleteFileFun()
        setFoto()

        /*addDoc.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this, addDocsActivity::class.java)
            startActivity(intent)
        })*/

        sharedDoc?.setOnClickListener(View.OnClickListener { z: View? ->
            val intent = Intent(this, SharedDocsActivity::class.java)
            startActivity(intent)
        })

        foto?.setOnClickListener(View.OnClickListener { w: View? ->
            val intent = Intent(this, PerfilActivity::class.java)
            intent.putExtra("idUser", user?.id)
            intent.putExtra("name", user?.nombre)
            intent.putExtra("correo", user?.correo)
            intent.putExtra("telefono", user?.telefono)
            intent.putExtra("cedula", user?.cedula)
            intent.putExtra("foto", fileFoto)
            startActivity(intent)
        })
    }

    override fun onRestart() {
        super.onRestart()
        runBlocking {
            user = manager?.obtenerDatosUsuario(intent.extras!!.getString("usuario").toString())
        }
        listDocs?.removeAllViews();
        docs?.removeAll(docs!!)
        setWidgets()
        setDocuments()
        setDeleteFileFun()
        fileFoto = ""
        foto?.setImageResource(R.drawable.avatar);
        setFoto()
    }

    fun setWidgets() {
        nombre?.text = user?.nombre;
        cedula?.text = "C.C " + user?.cedula
    }

    fun setDocuments() {
        for (file in user?.documentosPropios!!){
            val doc = DocItem(layoutInflater, listDocs, this, file)
            docs?.add(doc);
            listDocs?.addView(doc.getView())
        }
    }

    fun setDeleteFileFun(){
        for (doc in docs!!){
            doc.delete?.setOnClickListener {
                runBlocking {
                    doc.document?.documentURL?.let { it1 -> manager?.eliminarFile(it1) }
                }
                onRestart()
            }
        }
    }

    fun setFoto(){
        var flag = 0
        var fotoI : DocumentDTO? = null
        for (file in user?.documentosPropios!!){
            if (file.documentName.equals("Foto.jpg") || file.documentName.equals("Foto.png")){
                flag = 1
                fotoI = file
            }
        }
        if (flag == 1){
            var url : Uri
            runBlocking {
                url = fotoI?.documentURL?.let { manager?.obtenerUrlFile(it) }!!
            }
            foto?.let { Glide.with(getApplicationContext()).load(url).into(it) };
            fileFoto = url.toString()
        }
    }


}