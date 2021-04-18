package com.example.goldgym

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_inicio.*

class Inicio : AppCompatActivity(), View.OnClickListener {



    private val REQ1 = 126
    private val REQ2 = 127
    private val REQ3 = 128
    private val REQ4 = 129
    private var correo = ""


    private var contador="inicializado"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        btnSeg0.setOnClickListener(this)
        btnAct0.setOnClickListener(this)
        btnMap0.setOnClickListener(this)
        btnChat0.setOnClickListener(this)

        btnfacebook.setOnClickListener(this)
        btninstagram.setOnClickListener(this)
        btntwitter.setOnClickListener(this)
        btnyoutube.setOnClickListener(this)
        btnrss.setOnClickListener(this)

        val extras = intent.extras
        correo =  extras?.getString("NOMBRE").toString().trim()?:"sin usuario"
        edpruebacorreo.setText(correo)

        //Obtenemos el PreferenceManager
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        //Cogemos de PreferenceManager el valor del contador, si no existe coge como defecto el 0

        var correo2 = pref.getString(contador, "emailerror")
        edpruebacorreo.setText(correo2)

    }

    override fun onClick(v: View?) {
        when(v){
            btnSeg0 -> {
                irSeguimiento()
            }
            btnAct0 -> {
                irActividades()
            }
            btnMap0 -> {
                irMapa()
            }
            btnChat0 -> {
                irChat()
            }
            btnfacebook -> {
                irfacebook()
            }
            btninstagram -> {
                irinstagram()
            }
            btntwitter -> {
                irtwitter()
            }
            btnyoutube -> {
                iryoutube()
            }
            btnrss -> {
                irrss()
            }
        }
    }

    private fun irrss() {
        val i = Intent(this, main_web::class.java)
        i.putExtra("DIRECCION", "https://blog.vivagym.es/")
        startActivity(i)
        startActivity(i)
    }

    private fun iryoutube() {
        val i = Intent(this, main_web::class.java)
        i.putExtra("DIRECCION", "https://www.youtube.com/user/vivagymclubs")
        startActivity(i)
        startActivity(i)
    }

    private fun irtwitter() {
        val i = Intent(this, main_web::class.java)
        i.putExtra("DIRECCION", "https://twitter.com/empleovivagym?lang=es")
        startActivity(i)
        startActivity(i)
    }

    private fun irinstagram() {
        val i = Intent(this, main_web::class.java)
        i.putExtra("DIRECCION", "https://www.instagram.com/vivagymclubs/")
        startActivity(i)
        startActivity(i)
    }

    private fun irfacebook() {
        val i = Intent(this, main_web::class.java)
        i.putExtra("DIRECCION", "https://www.facebook.com/VivaGym.es/")
        startActivity(i)
        startActivity(i)
    }

    private fun irChat() {
        val i = Intent(this, Chat::class.java)
        startActivityForResult(i, REQ1)
    }

    private fun irMapa() {
        val i = Intent(this, Mapa::class.java)
        startActivityForResult(i, REQ2)
    }

    private fun irActividades() {
        val i = Intent(this, Actividades::class.java)
        startActivityForResult(i, REQ3)
    }

    private fun irSeguimiento() {
        val ii = Intent(this, Seguimiento::class.java)
        ii.putExtra("NOMBRE2", correo)
        startActivity(ii)
    }
}