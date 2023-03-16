package com.example.calculadordemateriales

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.calculadordemateriales.databinding.FragmentParedBinding

class ParedFragment : Fragment() {

    private lateinit var binding: FragmentParedBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.rbLadrillo.isChecked = true
        binding.rbAparejoSoga.isChecked = true
        binding.btnCalcular.setOnClickListener{
            val alturaPared = binding.editAltura.text.toString()
            val largoPared = binding.editLargo.text.toString()
            val espesorJuntas = binding.editEspesorJuntas.text.toString()
            if(alturaPared.isNotEmpty() && largoPared.isNotEmpty() && espesorJuntas.isNotEmpty()){
                calcular(alturaPared.toFloat(), largoPared.toFloat(), espesorJuntas.toFloat())
            }else{
                Toast.makeText(this.context, "EXISTEN CAMPOS VACIOS", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParedBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun calcular(alturaPared: Float, largoPared: Float, espesorJuntas: Float){
        var alturaLadrillo: Float
        var largoLadrillo: Float
        var anchoLadrillo: Float
        var nombreObjeto = ""
        var totalCantidadDeLadrillos = 0f

        if(binding.rbLadrillo.isChecked){
            nombreObjeto = "LADRILLOS"
            if(binding.rbAparejoSoga.isChecked){
                alturaLadrillo = 0.25f
                largoLadrillo = 0.34f
                totalCantidadDeLadrillos = (1/((largoLadrillo+espesorJuntas)*(alturaLadrillo+espesorJuntas))) * (alturaPared*largoPared)
            }
            if(binding.rbAparejoEspanol.isChecked){
                alturaLadrillo = 0.25f
                anchoLadrillo = 0.10f
                totalCantidadDeLadrillos = (1/((anchoLadrillo+espesorJuntas)*(alturaLadrillo+espesorJuntas))) * (alturaPared*largoPared)
            }
        }
        if(binding.rbBloque.isChecked){
            nombreObjeto = "BLOQUES"
            if(binding.rbAparejoSoga.isChecked){
                alturaLadrillo = 0.20f
                largoLadrillo = 0.40f
                totalCantidadDeLadrillos = (1/((largoLadrillo+espesorJuntas)*(alturaLadrillo+espesorJuntas))) * (alturaPared*largoPared)
            }
            if(binding.rbAparejoEspanol.isChecked){
                alturaLadrillo = 0.20f
                anchoLadrillo = 0.07f
                totalCantidadDeLadrillos = (1/((anchoLadrillo+espesorJuntas)*(alturaLadrillo+espesorJuntas))) * (alturaPared*largoPared)
            }
        }

        binding.result.text = resources.getString(R.string.result_uno, totalCantidadDeLadrillos, nombreObjeto)
    }
}