package com.example.goldgym

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_mapa.*
import kotlin.collections.Map

class Mapa : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val REQ1 = 426
    private val REQ2 = 427
    private val REQ3 = 428
    private val REQ4 = 429

    private lateinit var mMap: GoogleMap
    private var REQ_PERMISO = 100
    //para nuestra última localización
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var ultimaLocalizacion : Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //inicializamos la variable fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnSeg3.setOnClickListener(this)
        btnAct3.setOnClickListener(this)
        btnChat3.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v){
            btnSeg3 -> {
                irSeguimiento()
            }
            btnAct3 -> {
                irActividades()
            }
            btnChat3 -> {
                irChat()
            }

        }
    }

    private fun irChat() {
        val i = Intent(this, Chat::class.java)
        startActivityForResult(i, REQ1)
    }

    private fun irMapa() {
        val i = Intent(this, Map::class.java)
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMarkerClickListener(this)
        mMap.uiSettings.isZoomControlsEnabled = true //activamos los controles del zoom

        setMapas()
    }

    private fun setMapas() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            //SI NO TENGO LOS PERMISOS LOS PIDO
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQ_PERMISO)
            return
        }
        mMap.isMyLocationEnabled = true //Activamos nuestra localización
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            //si la localizacion no es nula pasamos la longitud y la latitud
            if(location!=null){
                ultimaLocalizacion=location
                //var currentLatLng = LatLng(location.latitude, location.longitude)
                var gym1LatLng = LatLng(36.846825, -2.461227)
                var gym2LatLng = LatLng(36.844457, -2.457707)
                var gym3LatLng = LatLng(36.840242, -2.461895)
                var currentLatLng = gym3LatLng
                ponerMarcador(gym1LatLng,"Gold Gym Plaza de toros")
                ponerMarcador(gym2LatLng, "Gold Gym By Ana")
                ponerMarcador(gym3LatLng, "Gold Gym Marítimo Titán")
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)) //15f es el zoom con el que queremos verlo
            }
        }
    }

    private fun ponerMarcador(location: LatLng, title: String) {
        val markerOptions = MarkerOptions().position(location).title(title)
        mMap.addMarker(markerOptions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false;
    }
}

