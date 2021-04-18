package com.example.goldgym

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_registro.*
import kotlinx.android.synthetic.main.activity_seguimiento.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val REQ1 = 123
    private val REQ2 = 124
    private val REQ3 = 125

    //Objetos compartidos
    val NOMBRE = "nombre"

    //Objetos compartidos
    companion object Nombre {
        val nom = "nombre"
    }


    private lateinit var mAuth: FirebaseAuth
    var firebaseUser: FirebaseUser? = null

    private lateinit var mail: String
    private lateinit var pass: String

    private var contador="inicializado"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //inicializamos
        mAuth = FirebaseAuth.getInstance()

        btnMainRegistrar.setOnClickListener(this)
        btnLogin.setOnClickListener(this)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.putString(contador, "mail")
        editor.apply()

        //irActivityMenu()

    }

    override fun onClick(v: View?) {
        when(v){
            btnMainRegistrar -> {
                irActivityRegistrar()
            }
            btnLogin -> {
                login()
            }

        }
    }
    //----------------------------------------------------------------------
    private fun login() {
        //Cogemos los datos
        if (!cogerDatos()) return
        mAuth.signInWithEmailAndPassword(mail, pass)
            .addOnCompleteListener(this) { task ->
                //Si me he registrado correctamente me voy al activity2
                if (task.isSuccessful) {
                    firebaseUser = mAuth.currentUser //podemos sacar el email

                    val pref = PreferenceManager.getDefaultSharedPreferences(this)
                    val editor = pref.edit()
                    editor.putString(contador, mail.toString().trim())
                    editor.apply()


                    irActivityMenu()
                } else {
                    Toast.makeText(
                        this,
                        "Autentificacion fallida: " + task.exception,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
    //----------------------------------------------------------------------
    private fun cogerDatos(): Boolean {
        mail = etCorreo.text.toString().trim()
        pass = etPass.text.toString().trim()
        if (mail.length == 0) {
            etCorreo.setError("Rellene el correo")
            return false
        }
        if (pass.length == 0) {
            etCorreo.setError("Rellene el contraseña")
            return false
        }
        return true

    }

    private fun irActivityMenu() {

        val i = Intent(this, Inicio::class.java)
        i.putExtra("NOMBRE",mail)
        startActivity(i)
    }

    //----------------------------------------------------------------------
    private fun irActivityRegistrar() {
        val i = Intent(this, MainRegistro::class.java)
        startActivity(i)
    }
}