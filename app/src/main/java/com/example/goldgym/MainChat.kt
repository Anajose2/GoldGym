package com.example.goldgym

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class MainChat : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()

    lateinit var etMensaje: EditText
    lateinit var recView: RecyclerView
    lateinit var btnSend: ImageView

    private var contador="inicializado"
    private var correo2 = " "

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_chat)

        //Obtenemos el PreferenceManager
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        //Cogemos de PreferenceManager el valor del contador, si no existe coge como defecto el 0

        correo2 = pref.getString(contador, "emailerror").toString().trim()

        setUp()

        btnSend.setOnClickListener{
            enviarMensaje()
        }
        createDatabaseListener()
    }

    private fun createDatabaseListener() {
        val miListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val listaMensajes = ArrayList<Mensaje>()
                for(data in snapshot.children){
                    val datoMensaje = data.getValue<Mensaje>(Mensaje::class.java)
                    val mensaje = datoMensaje?.let { it } ?:continue

                    listaMensajes.add(mensaje)
                    listaMensajes.sortBy {
                        it.fecha
                    }
                    setAdapter(listaMensajes)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        database.getReference("Mensajes").addValueEventListener(miListener)
    }

    private fun setAdapter(listaMensajes: ArrayList<Mensaje>) {
        recView.setHasFixedSize(true)
        recView.layoutManager = LinearLayoutManager(this)
        val mensajeAdapter = MensajeAdapter(listaMensajes)
        recView.adapter = mensajeAdapter
        recView.scrollToPosition(listaMensajes.size-1)
    }

    private fun enviarMensaje() {
        cerrarTecladoVirtual()
        var mensaje = etMensaje.text.toString().trim()
        if(!mensaje.isEmpty()){
            guardarFirebase(mensaje)
        }else{
            Toast.makeText(this,"Escribe un mensaje!!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun cerrarTecladoVirtual() {
        val v: View? = this.currentFocus
        if(v!=null){
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
    private fun guardarFirebase(mensaje: String) {
        val miNodo = database.getReference("Mensajes")
        val fecha = Date().time
        val nombre = "lolo"
        miNodo.child(fecha.toString()).setValue(Mensaje(mensaje, fecha, nombre))
        etMensaje.setText("")
    }

    private fun setUp() {
        etMensaje = findViewById(R.id.etMensaje)
        recView = findViewById(R.id.recView)
        btnSend = findViewById(R.id.ivSend)
    }
}