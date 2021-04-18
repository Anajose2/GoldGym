package com.example.goldgym

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_registro.*

class MainRegistro : AppCompatActivity(), View.OnClickListener {

    private val REQ1 = 123
    private val REQ2 = 124
    private val REQ3 = 125

    private lateinit var mAuth: FirebaseAuth
    var firebaseUser: FirebaseUser? = null

    private lateinit var mail: String
    private lateinit var pass: String

    //Variable que se conecta con la bd Firebase
    private val db = FirebaseFirestore.getInstance()
    //Creo las variables y las inicializo
    private var nombre: String =" "
    private var fecha_nacimiento : String = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_registro)

        //inicializamos
        mAuth = FirebaseAuth.getInstance()

        btnRegistroRegistrar.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            btnRegistroRegistrar -> {
                registrar()
            }
        }
    }

    //--------------------------------------------------------------------------------------------------
    private fun registrar() {
        //Si no coge el dato pasamos
        if (!cogerDatos()) return
        mAuth.createUserWithEmailAndPassword(mail, pass)
            .addOnCompleteListener(this) { task ->
                //Si la tarea ha tenido exito
                if (task.isSuccessful) {
                    Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_LONG).show()
                    guardarDatosUser()
                    irMainActivity()
                } else {
                    Toast.makeText(this, "Error: " + task.exception, Toast.LENGTH_LONG).show()
                    //limpiar()//Limpiamos todos los campos
                }

            }
    }

    private fun guardarDatosUser() {
        if(!comprobar()) return
        //Nombre de la Collection que creamos en Firebase Consola
        db.collection("Clientes").document(mail).set(
            //Lo guardamos como un mapa
            hashMapOf("Correo usuario" to mail, "Nombre" to nombre, "Fecha Nacimiento" to fecha_nacimiento)
        )
        //mensaje
        Toast.makeText(this, "Registro Guardado", Toast.LENGTH_LONG).show()
    }

    private fun comprobar(): Boolean {
        //Cogemos los valores del activity_main y los igualamos a los creados arriba del todo
        mail = edCorreo.text.toString().trim()
        nombre = edNombre.text.toString().trim()
        fecha_nacimiento = edFecha.text.toString().trim()

        if(mail.length==0){
            edCorreo.setError("Este campo es Obligatorio!!")
            return false
        }
        return true

    }

    private fun irMainActivity() {
        val i = Intent(this, MainActivity::class.java)
        startActivityForResult(i, REQ2)
    }

    private fun cogerDatos(): Boolean {
        mail = edCorreo.text.toString().trim()
        pass = edPass.text.toString().trim()
        if (mail.length == 0) {
            edCorreo.setError("Rellene el correo")
            return false
        }
        if (pass.length == 0) {
            edPass.setError("Rellene el contraseña")
            return false
        }
        return true

    }
}