package com.example.goldgym

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


class Fragment_MenuAct : Fragment() {

    private var botonesMenu = arrayOf<Int>(R.id.ivFuerza, R.id.ivCardio, R.id.ivMovilidad)
    private var botonesIluminadosImagenes = arrayOf<Int>(R.drawable.ivfuerza2, R.drawable.ivcardio2, R.drawable.ivmovilidad2)
    private var botonesNoIluminadosImagenes = arrayOf<Int>(R.drawable.ivfuerza, R.drawable.ivcardio, R.drawable.ivmovilidad)
    private var btnIluminado = 200 //Inicializamos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment__menu_act, container, false)
        var boton : ImageView

        //Comprobamos los argumentos
        if(arguments != null){
            btnIluminado = arguments !!.getInt(Actividades.BTNPULSADO)
        }
        for(i in 0 until botonesMenu.size){
            boton=v.findViewById(botonesMenu[i])
            //Le ponemos un listener y le cambio informacion
            boton.setImageResource(botonesNoIluminadosImagenes[i])
            if(btnIluminado == i){
                boton.setImageResource(botonesIluminadosImagenes[i])
            }
            boton.setOnClickListener {

                val esteActivity = activity
                (esteActivity as InterfazMenu).botonPulsado(i)
            }
        }
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance() : Fragment_MenuAct{
            return Fragment_MenuAct()
        }

    }
}