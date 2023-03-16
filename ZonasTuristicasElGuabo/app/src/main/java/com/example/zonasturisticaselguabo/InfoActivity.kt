package com.example.zonasturisticaselguabo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        //Variables inicializadas
        val image = findViewById<ImageView>(R.id.imageView)
        val text = findViewById<TextView>(R.id.textView)

        //Argumentos importados
        val arg0 = intent?.extras?.getString("titulo").toString()
        val arg1 = intent?.extras?.getString("imagen").toString()
        val arg2 = intent?.extras?.getString("descripcion").toString()

        //Titulo de activity en la barra superior
        title = arg0

        //Mostrar datos
        Picasso.get().load(arg1).into(image)
        text.text = arg2
    }
}