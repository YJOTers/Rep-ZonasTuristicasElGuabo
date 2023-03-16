package com.example.login.layouts_models

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.login.R
import com.example.login.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(requirePermission()){
            //Inicializar fragmento principal
            fragmentAdmin(1)
        }

        binding.btnLogin.setOnClickListener{
            fragmentAdmin(1)
        }
        binding.btnRegister.setOnClickListener{
            fragmentAdmin(2)
        }
        binding.btnRecovery.setOnClickListener{
            fragmentAdmin(3)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!hasPermissions(this)) {
            requirePermission()
        }
    }

    private fun fragmentAdmin(id: Int){
        val fm = supportFragmentManager.beginTransaction()
        when(id){
            1 -> {
                fm.replace(R.id.fragmentAdmin, LoginFragment())
            }
            2 -> {
                fm.replace(R.id.fragmentAdmin, RegisterFragment())
            }
            3 -> {
                fm.replace(R.id.fragmentAdmin, RecoveryFragment())
            }
        }
        fm.addToBackStack(null)
        fm.commit()
    }

    private fun requirePermission(): Boolean{
        if (PackageManager.PERMISSION_GRANTED ==
            ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)){
            Toast.makeText(this, "PERMISO DE INTERNET YA OTORGADO", Toast.LENGTH_SHORT).show()
        }else{
            requestPermissionLauncher.launch(Manifest.permission.INTERNET)
        }
        if (PackageManager.PERMISSION_GRANTED ==
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)){
            Toast.makeText(this, "PERMISO DE ESTADO DE RED YA OTORGADO", Toast.LENGTH_SHORT).show()
        }else{
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_NETWORK_STATE)
        }
        if (PackageManager.PERMISSION_GRANTED ==
            ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)){
            Toast.makeText(this, "PERMISO DE ENVIAR SMS YA OTORGADO", Toast.LENGTH_SHORT).show()
        }else{
            requestPermissionLauncher.launch(Manifest.permission.SEND_SMS)
        }
        return true
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

    companion object{
        private val PERMISSIONS_REQUIRED = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.SEND_SMS)
        //Verifica si todos los permisos necesarios estan permitidos en esta app
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}