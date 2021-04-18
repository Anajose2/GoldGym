package com.example.goldgym

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_inicio.*
import kotlinx.android.synthetic.main.activity_main_registro.*
import kotlinx.android.synthetic.main.activity_seguimiento.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.Map

class Seguimiento : AppCompatActivity(), View.OnClickListener {

    private val REQ1 = 526
    private val REQ2 = 527
    private val REQ3 = 528
    private val REQ4 = 529


    private lateinit var mAuth: FirebaseAuth
    var firebaseUser: FirebaseUser? = null

    private lateinit var mail: String

    //Variable que se conecta con la bd Firebase
    private val db = FirebaseFirestore.getInstance()
    //Creo las variables y las inicializo
    private var nombre: String =" "
    private var fecha_nacimiento : String = " "
    private var altura : String = " "
    private var peso : String = " "

    private var contador="inicializado"


    private lateinit var boton: Button
    private lateinit var imagen: ImageView
    private val PERMISO_REQUEST_CAMERA=123
    private val CAMARA_REQUEST=100
    lateinit var  currentPhotoPath: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seguimiento)

        //inicializamos
        mAuth = FirebaseAuth.getInstance()

        //val extras2 = intent.extras
        //mail=  extras2?.getString("NOMBRE2").toString().trim()?:"sin usuario"

        //Obtenemos el PreferenceManager
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        //Cogemos de PreferenceManager el valor del contador, si no existe coge como defecto el 0

        mail = pref.getString(contador, "emailerror").toString().trim()

        buscar()

        btnAct1.setOnClickListener(this)
        btnMap1.setOnClickListener(this)
        btnChat1.setOnClickListener(this)

        btnActualizar.setOnClickListener(this)

        boton = findViewById(R.id.btnFoto)
        imagen = findViewById(R.id.ivPerfil)

        boton.setOnClickListener{
            if(pedirPermisos()) abrirCamara()
        }
    }
    //--------------------------------------
    private fun pedirPermisos(): Boolean{
        if(!isPermisoConcedido()){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this as Activity, android.Manifest.permission.CAMERA)){
                mostrarDialogoPermiso()
            }
            else{
                ActivityCompat.requestPermissions(this as Activity, arrayOf(android.Manifest.permission.CAMERA), PERMISO_REQUEST_CAMERA)
            }
        }
        return true
    }
    private fun mostrarDialogoPermiso() {
        val alerta= AlertDialog.Builder(this)
        alerta.setTitle("Permisos")
        alerta.setMessage("Es importate que esta aplicacion pueda acceder a la camara por muchos motivos")
        alerta.setPositiveButton("Abrir Settings"){_, _->
            val i = Intent()
            i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", "com.example.camara_19_01", null)
            i.data = uri
            startActivity(i)
        }
        alerta.setNegativeButton("Cancelar", null)
        alerta.show()
    }
    //-------------------------------------
    private fun abrirCamara(){
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also{
                startActivityForResult(takePictureIntent, PERMISO_REQUEST_CAMERA)
            }
        }
        //startActivityForResult(i, CAMARA_REQUEST)
    }
    //-------------------------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PERMISO_REQUEST_CAMERA && resultCode == RESULT_OK){
            //mostramos una miniatura de la foto en el imagenView
            val foto: Bitmap = data?.extras?.get("data") as Bitmap
            imagen.setImageBitmap(foto)

        }
        if(requestCode==CAMARA_REQUEST && resultCode== RESULT_CANCELED){
            Toast.makeText(this, "El usuario canceló sin hacer la foto", Toast.LENGTH_SHORT).show()
        }
    }
    //----------------------------------------------------------------------------------------------
    private fun añadirFoto(){
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    //----------------------------------------------------------------------------------------------
    val REQUEST_TAKE_PHOTO = 1
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Nos aseguramos de que haya una actividad de la camara para manejar el intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                //Crea el archivo donde debe ir la foto
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    private fun isPermisoConcedido(): Boolean{
        return ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISO_REQUEST_CAMERA->{
                if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    abrirCamara()
                }else{
                    pedirPermisos()
                }
                return
            }
            else->{}
        }
    }

    override fun onClick(v: View?) {
        when(v){

            btnAct1 -> {
                irActividades()
            }
            btnMap1 -> {
                irMapa()
            }
            btnChat1 -> {
                irChat()
            }
            btnActualizar ->{
                guardarDatosUser()
            }

        }
    }

    private fun guardarDatosUser() {
        if(!comprobar()) return
        //Nombre de la Collection que creamos en Firebase Consola
        db.collection("Clientes").document(mail).set(
            //Lo guardamos como un mapa
            hashMapOf("Correo usuario" to mail, "Nombre" to nombre, "Fecha Nacimiento" to fecha_nacimiento, "Altura" to altura, "Peso" to peso)
        )
        //mensaje
        Toast.makeText(this, "Registro Guardado", Toast.LENGTH_LONG).show()
    }

    private fun comprobar(): Boolean {
        //Cogemos los valores del activity_main y los igualamos a los creados arriba del todo
        nombre = edNombSeg.text.toString().trim()
        fecha_nacimiento = edFechaSeg.text.toString().trim()
        altura = edAlturaSeg.text.toString().trim()
        peso = edPesoSeg.text.toString().trim()

        if(mail.length==0){
            edNombSeg.setError("Este campo es Obligatorio!!")
            return false
        }
        return true

    }

    private fun buscar() {
        //
        if(!comprobar()) return
        db.collection("Clientes").document(mail).get().addOnSuccessListener {
            edNombSeg.setText(it.get("Nombre") as String?)
            edFechaSeg.setText(it.get("Fecha Nacimiento") as String?)
            edAlturaSeg.setText(it.get("Altura") as String?)
            edPesoSeg.setText(it.get("Peso") as String?)
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