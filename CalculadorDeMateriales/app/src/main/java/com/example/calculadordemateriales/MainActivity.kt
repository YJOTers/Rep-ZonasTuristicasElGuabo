package com.example.calculadordemateriales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.calculadordemateriales.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Estado de guardado inicial
        colorThemeManager()
        //Fragmento inicial
        fragmentManager()

        //Accion de botones
        val saveState2 = getSharedPreferences("saveState2", MODE_PRIVATE)
        val editState2 = saveState2.edit()
        binding.btnPared.setOnClickListener{
            editState2.putInt("id", 1)
            editState2.apply()
            fragmentManager()
        }
        binding.btnPiso.setOnClickListener{
            editState2.putInt("id", 2)
            editState2.apply()
            fragmentManager()
        }
        binding.btnModo.setOnClickListener{
            val saveState = getSharedPreferences("saveState", MODE_PRIVATE)
            val editState = saveState.edit()
            if(delegate.localNightMode == 1){
                editState.putInt("theme", 2)
            }else{
                editState.putInt("theme", 1)
            }
            editState.apply() //Guarda los cambios del estado
            colorThemeManager() //Aplica los cambios del estado
        }
    }

    private fun fragmentManager(){
        val saveState2 = getSharedPreferences("saveState2", MODE_PRIVATE)
        val numFragment = saveState2.getInt("id", 1)
        val fm = supportFragmentManager.beginTransaction()
        when(numFragment){
            1 -> {
                fm.replace(R.id.adminFragment, ParedFragment())
                fm.addToBackStack(null)
                fm.commit()
            }
            2 -> {
                fm.replace(R.id.adminFragment, PisoFragment())
                fm.addToBackStack(null)
                fm.commit()
            }
        }
    }

    private fun colorThemeManager(){
        val saveState = getSharedPreferences("saveState", MODE_PRIVATE)
        val theme = saveState.getInt("theme", 1)
        if(theme == 2){
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            binding.btnModo.setText(R.string.theme_light)
        }else{
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            binding.btnModo.setText(R.string.theme_night)
        }
    }
}