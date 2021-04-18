package com.example.goldgym

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MensajeAdapter(private val listaMensajes: ArrayList<Mensaje>): RecyclerView.Adapter<MensajeAdapter.miViewHolder>(){
    class miViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val tvMensjae : TextView = v.findViewById(R.id.tvMensaje)
        val tvFecha : TextView = v.findViewById(R.id.tvFecha)
        val imagen : ImageView = v.findViewById(R.id.imageView)
        val tvNombre: TextView = v.findViewById(R.id.tvNombre)
    }
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_layout)
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): miViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val v = inflate.inflate(R.layout.recycler_layout, null)
        return miViewHolder(v)
    }

    override fun onBindViewHolder(holder: miViewHolder, position: Int) {
        val item = listaMensajes[position]
        //Convertirmos el long de la fecha a un formato entendible
        val sdf = SimpleDateFormat("dd/MM-/yyyy  hh:mm:ss")
        val miFecha = Date(item.fecha!!)
        val miFecha2 = sdf.format(miFecha)
        holder.tvFecha.setText(miFecha2)
        holder.tvMensjae.setText(item.texto)
        holder.tvNombre.setText(item.nombre)
        holder.imagen.setImageResource(R.drawable.animal)
    }

    override fun getItemCount(): Int {
        return listaMensajes.size
    }
}