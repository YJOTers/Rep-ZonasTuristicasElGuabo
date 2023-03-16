package com.example.login.layouts_models

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.content.Intent
import com.example.login.client_api.UserLM
import com.example.login.databinding.FragmentRecoveryBinding
import com.example.login.models.UserModel
import kotlin.random.Random

class RecoveryFragment : Fragment() {

    private lateinit var binding: FragmentRecoveryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSeePassword.setOnClickListener {
            if(binding.inputPassword.inputType == 129){
                binding.inputPassword.inputType = 145
            }else{
                binding.inputPassword.inputType = 129
            }
        }
        binding.btnCode.setOnClickListener{
            sendCodeByEmail()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRecoveryBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun sendCodeByEmail(){
        val email = binding.inputEmail.text.toString()
        if(email.isNotEmpty()){
            UserLM().findUserByEmail(email){
                if(it?.id != null){
                    val randomCode = Random.nextInt(9999 - 1000) + 1000
                    val intentMail = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                        putExtra(Intent.EXTRA_SUBJECT, "Cambiar clave")
                        putExtra(Intent.EXTRA_TEXT, "Su codigo de verificacion es: $randomCode")
                        type = "message/rfc822"
                    }
                    startActivity(Intent.createChooser(intentMail, "Elije un cliente de correo"))
                    binding.btnRecovery.setOnClickListener{_ ->
                        val code = binding.inputCode.text.toString()
                        val password = binding.inputPassword.text.toString()
                        val user = UserModel(it.id, it.nombre, it.correo, password)
                        if(code.isNotEmpty() && password.isNotEmpty() && code.toInt() == randomCode){
                            UserLM().updateUser(it.id!!, user){it3 ->
                                if(it3){
                                    requireActivity().runOnUiThread{
                                        Toast.makeText(context, "CLAVE CAMBIADA CON EXITO", Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    requireActivity().runOnUiThread{
                                        Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }else{
                            requireActivity().runOnUiThread{
                                Toast.makeText(context, "CAMPOS VACIOS O CODIGO INCORRECTO", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }else{
                    requireActivity().runOnUiThread{
                        Toast.makeText(context, "NINGUNA CUENTA POSEE ESTE CORREO", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            Toast.makeText(context, "CAMPOS VACIOS", Toast.LENGTH_SHORT).show()
        }
    }
}