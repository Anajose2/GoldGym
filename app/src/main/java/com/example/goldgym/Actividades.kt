package com.example.goldgym

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_actividades.*

import kotlin.collections.Map

class Actividades : AppCompatActivity(), InterfazMenu, View.OnClickListener {

    var btn = 0
    val misFragmentos = arrayOf<Fragment>(Fragment_Fuerza(), Fragment_Cardio(), Fragment_Movimiento())

    private val REQ1 = 226
    private val REQ2 = 227
    private val REQ3 = 228
    private val REQ4 = 229

    companion object {
        val BTNPULSADO = "asdf"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividades)

        btnSeg2.setOnClickListener(this)
        btnMap2.setOnClickListener(this)
        btnChat2.setOnClickListener(this)

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val menuIluminado = Fragment_MenuAct()
        val datos = Bundle()
        datos.putInt(Actividades.BTNPULSADO,btn)
        menuIluminado.arguments = datos
        //cambio el menu de botones por el menu de botones iluminado
        transaction.replace(R.id.menuFragment1, menuIluminado)
        transaction.replace(R.id.layoutContenedor, Fragment_Fuerza())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun cogerDatos(){
        //Recuperamos el boton que hemos enviado desde el activity_menu
        btn = intent.getIntExtra(Actividades.BTNPULSADO,0)
    }

    override fun botonPulsado(btn: Int) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val menuIluminado = Fragment_MenuAct()
        val datos = Bundle()
        datos.putInt(Actividades.BTNPULSADO,btn)
        menuIluminado.arguments = datos
        //cambio el menu de botones por el menu de botones con un botonIlumnidado
        transaction.replace(R.id.menuFragment1, menuIluminado)
        transaction.replace(R.id.layoutContenedor, misFragmentos[btn])
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onClick(v: View?) {
        when(v){
            btnSeg2 -> {
                irSeguimiento()
            }
            btnMap2 -> {
                irMapa()
            }
            btnChat2 -> {
                irChat()
            }

        }
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
        val i = Intent(this, Seguimiento::class.java)
        startActivityForResult(i, REQ4)
    }

}