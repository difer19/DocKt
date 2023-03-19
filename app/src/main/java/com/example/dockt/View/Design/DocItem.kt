package com.example.dockt.View.Design

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.dockt.DTO.DocumentDTO
import com.example.dockt.Manager.ServiceManager
import com.example.dockt.R
import com.example.dockt.View.Activity.EditDocActivity
import kotlinx.coroutines.runBlocking

class DocItem {
    private var view: View? = null
    private var nomDoc : TextView? = null
    private var edit : Button? = null
    var delete : Button? = null
    var document : DocumentDTO? = null
    private val serviceManager : ServiceManager = ServiceManager()


    constructor(inflater: LayoutInflater, list: LinearLayout?, context: Context, document: DocumentDTO) {
        view = inflater.inflate(R.layout.item_doc_menu, list, false)
        nomDoc = view!!.findViewById<TextView>(R.id.nom_doc)
        edit = view!!.findViewById<Button>(R.id.button2)
        delete = view!!.findViewById<Button>(R.id.button3)
        nomDoc?.setText(document.documentName)

        this.document = document

        edit?.setOnClickListener(View.OnClickListener { f: View? ->
            val intent = Intent(context, EditDocActivity::class.java)
            context.startActivity(intent)
        })

        nomDoc?.setOnClickListener {
            var url : Uri
            runBlocking {
                url = document.documentURL?.let { it1 -> serviceManager.obtenerUrlFile(it1) }!!
            }

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = url
            context.startActivity(intent)
        }
    }

    fun getView(): View? {
        return view
    }
}