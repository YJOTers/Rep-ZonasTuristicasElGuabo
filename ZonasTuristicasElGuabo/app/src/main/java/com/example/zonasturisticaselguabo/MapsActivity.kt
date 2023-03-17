package com.example.zonasturisticaselguabo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.zonasturisticaselguabo.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Titulo de activity en la barra superior
        setTitle(R.string.title_activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Posicion inicial de camara
        val posStart = LatLng(-3.2099019884381983, -79.81512207539423)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posStart, 10.5f))

        // Agregar marcas de locacion
        val pos1 = LatLng(-3.2041386029873475, -79.73564629295399)
        mMap.addMarker(MarkerOptions().position(pos1).title(getString(R.string.title_activity_info1)))
        val pos2 = LatLng(-3.105959818945006, -79.90009364814615)
        mMap.addMarker(MarkerOptions().position(pos2).title(getString(R.string.title_activity_info2)))
        val pos3 = LatLng(-3.2535029218247113, -79.81199675054458)
        mMap.addMarker(MarkerOptions().position(pos3).title(getString(R.string.title_activity_info3)))
        val pos4 = LatLng(-3.178280150740988, -79.75605126084874)
        mMap.addMarker(MarkerOptions().position(pos4).title(getString(R.string.title_activity_info4)))
        val pos5 = LatLng(-3.188914657907403, -79.74282166627961)
        mMap.addMarker(MarkerOptions().position(pos5).title(getString(R.string.title_activity_info5)))

        //Evento que muestra la info del marcador seleccionado
        mMap.setOnMarkerClickListener {
            it.showInfoWindow()
            true
        }

        //Evento que muestra una activity de la info seleccionada
        val intent = Intent(this, InfoActivity::class.java)
        mMap.setOnInfoWindowClickListener {
            when(it.position){
                pos1 -> {
                    intent.apply {
                        putExtra("titulo", getString(R.string.title_activity_info1))
                        putExtra("imagen", "https://lh5.googleusercontent.com/p/AF1QipNAQ69mtMhPMI_GGNMvnntmWWsHGBtl52UFdSn6=w426-h240-k-no")
                        putExtra("descripcion", "Cascadas de Manuel ofrece un amplio espacio de distracción en medio de la naturaleza en donde se puede disfrutar en familia y amigos.")
                    }
                }
                pos2 -> {
                    intent.apply {
                        putExtra("titulo", getString(R.string.title_activity_info2))
                        putExtra("imagen", "https://lh5.googleusercontent.com/p/AF1QipODLcEKlak0WRjM4hGCW7oBqhzhf5O5Yck18mHc=w426-h240-k-no")
                        putExtra("descripcion", "La playa es lugar muy concurrido actualmente, por lo que tiene ciertas virtudes como la facilidad de acceso a comida a precios accesibles y de calidad, la variedad de actividades que se pueden desarrollar en el mar y la confianza de un lugar seguro.")
                    }
                }
                pos3 -> {
                    intent.apply {
                        putExtra("titulo", getString(R.string.title_activity_info3))
                        putExtra("imagen", "https://lh5.googleusercontent.com/p/AF1QipNE9CMLM10AGRf6GfJa6xI1D5km3QK-KOgupmcP=w408-h306-k-no")
                        putExtra("descripcion", "Buenos paisajes, tiene 2 piscinas, una para adultos y otra para niños con tobogan, cerca hay una pista de moto cross.")
                    }
                }
                pos4 -> {
                    intent.apply {
                        putExtra("titulo", getString(R.string.title_activity_info4))
                        putExtra("imagen", "https://lh5.googleusercontent.com/p/AF1QipM4H0LTypOklEtzN1SeqJdtP5mRvXNrciVRISkM=w494-h240-k-no")
                        putExtra("descripcion", "Es muy bonito tiene cascadas y el agua es natural, ademas es un lugar para tu descanso, contamos con cabañas, piscina, río, restaurant, senderos ecológicos y más.")
                    }
                }
                pos5 -> {
                    intent.apply {
                        putExtra("titulo", getString(R.string.title_activity_info5))
                        putExtra("imagen", "https://lh5.googleusercontent.com/p/AF1QipN09ponHM_DgECvkVhbM38edCgLUF-cgFzgqQnj=w408-h306-k-no")
                        putExtra("descripcion", "Única piscina de agua 100% natural libre de químicos, proveniente de la vertiente de la montaña.")
                    }
                }
            }
            startActivity(intent)
        }
    }
}