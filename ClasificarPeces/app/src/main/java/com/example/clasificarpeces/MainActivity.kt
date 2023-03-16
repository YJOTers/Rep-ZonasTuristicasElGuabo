package com.example.clasificarpeces

import android.Manifest
import android.content.Context
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.example.clasificarpeces.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity: AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.include.myToolbar)
        setTitle(R.string.activity1_name)

        // Permisos requeridos de camara
        if(requirePermission()){
            //Obtener foto de galeria
            viewBinding.btnGalery.setOnClickListener { selectPhoto() }
            //Obtener foto de camara
            viewBinding.btnCapture.setOnClickListener { takePhoto() }
            cameraExecutor = Executors.newSingleThreadExecutor()
        }else{
            requirePermission()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!hasPermissions(this)) {
            requirePermission()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun sendPhoto(uriString: String){
        val nextActivity = Intent(this, CheckActivity::class.java).apply {
            putExtra("imagen", uriString)
        }
        startActivity(nextActivity)
    }

    private fun selectPhoto() {
        //Crear intent de abrir galeria
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    private fun takePhoto() {
        //Inicializar capturador de camara
        val imageCapture = imageCapture ?: return
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Captura de imagen fallida: ${exc.message}", exc)
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Captura de imagen satisfactoria: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                    sendPhoto(output.savedUri.toString())
                }
            }
        )
    }

    private fun requirePermission(): Boolean{
        if (PackageManager.PERMISSION_GRANTED ==
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)){
            Toast.makeText(this, "PERMISO DE CAMARA YA OTORGADO", Toast.LENGTH_SHORT).show()
        }else{
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        if (PackageManager.PERMISSION_GRANTED ==
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(this, "PERMISO DE LECTURA EN DISCO YA OTORGADO", Toast.LENGTH_SHORT).show()
        }else{
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            if (PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this, "PERMISO DE ESCRITURA EN DISCO YA OTORGADO", Toast.LENGTH_SHORT).show()
            }else{
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
        return true
    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data?.data
            sendPhoto(data.toString())
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "PERMISO REQUERIDO OTORGADO", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "PERMISO REQUERIDO DENEGADO", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)
        //Verifica si todos los permisos necesarios estan permitidos en esta app
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

}