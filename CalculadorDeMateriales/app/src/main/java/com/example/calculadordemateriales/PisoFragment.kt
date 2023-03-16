package com.example.calculadordemateriales

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.calculadordemateriales.databinding.FragmentPisoBinding

class PisoFragment : Fragment() {

    private lateinit var binding: FragmentPisoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnCalcular.setOnClickListener{
            val m2DeCajaDeBaldosa = binding.editM2DeCajaBaldosas.text.toString()
            val m2DeLaHabitacion = binding.editM2DeLaHabitacion.text.toString()
            val baldosasPorCaja = binding.editBaldosasPorCaja.text.toString()
            if(m2DeCajaDeBaldosa.isNotEmpty() && m2DeLaHabitacion.isNotEmpty() && baldosasPorCaja.isNotEmpty()){
                calcular(m2DeCajaDeBaldosa.toFloat(), m2DeLaHabitacion.toFloat(), baldosasPorCaja.toFloat())
            }else{
                Toast.makeText(this.context, "EXISTEN CAMPOS VACIOS", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPisoBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun calcular(m2DeCajaDeBaldosa: Float, m2DeLaHabitacion: Float, baldosasPorCaja: Float){
        val totalCantidadDeCajasDeBaldosa = m2DeLaHabitacion / m2DeCajaDeBaldosa
        val totalCantidadDeBaldosas = (totalCantidadDeCajasDeBaldosa * baldosasPorCaja).inc()

        binding.result.text = resources.getString(R.string.result_dos, totalCantidadDeCajasDeBaldosa, totalCantidadDeBaldosas)
    }
}