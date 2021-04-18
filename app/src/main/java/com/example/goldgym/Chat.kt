package com.example.goldgym

import android.content.Context
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_seguimiento.*
import java.util.*
import kotlin.collections.ArrayList

class Chat : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    lateinit var etMensaje: EditText
    lateinit var recView: RecyclerView
    lateinit var btnSend: ImageView

    private val REQ1 = 326
    private val REQ2 = 327
    private val REQ3 = 328
    private val REQ4 = 329

    //Objetos compartidos
    companion object DATOS {
        val MAIL = "correo"
    }

    private lateinit var mAuth: FirebaseAuth
    var firebaseUser: FirebaseUser? = null

    private lateinit var mail: String

    //Variable que se conecta con la bd Firebase
    private val db = FirebaseFirestore.getInstance()

    private var contador="inicializado"
    private var correo2 = " "
    private var nombre2 = " "

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //inicializamos
        mAuth = FirebaseAuth.getInstance()

        //Obtenemos el PreferenceManager
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        //Cogemos de PreferenceManager el valor del contador, si no existe coge como defecto el 0

        correo2 = pref.getString(contador, "emailerror").toString().trim()

        buscar()
        setUp()

        btnSend.setOnClickListener{
            enviarMensaje()
        }
        createDatabaseListener()

    }

    private fun buscar() {

        db.collection("Clientes").document(correo2).get().addOnSuccessListener {
            nombre2 = (it.get("Nombre") as String?).toString().trim()
        }
    }

    private fun createDatabaseListener() {
        val miListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listaMensajes = ArrayList<Mensaje>()
                for (data in snapshot.children){
                    val datoMensaje = data.getValue<Mensaje>(Mensaje::class.java)
                    val mensaje = datoMensaje?.let { it } ?:continue
                    // val nombre = intent.getStringExtra(MainActivity.DATOS.MAIL).toString()

                    listaMensajes.add(mensaje)
                    listaMensajes.sortBy {
                        it.fecha
                    }

                    setAdapter(listaMensajes)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //Toast.makeText(this,"Error con la Base de Datos", Toast.LENGTH_SHORT).show()
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
    private fun guardarFirebase(mensaje: String) {
        val miNodo = database.getReference("Mensajes")
        val fecha = Date().time
        val nombre = nombre2
        miNodo.child(fecha.toString()).setValue(Mensaje(mensaje, fecha, nombre))
        etMensaje.setText("")
    }

    private fun setUp() {
        etMensaje = findViewById(R.id.etMensaje)
        recView = findViewById(R.id.recView)
        btnSend = findViewById(R.id.ivSend)
    }

    private fun cerrarTecladoVirtual() {
        val v: View? = this.currentFocus
        if(v!=null){
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

}