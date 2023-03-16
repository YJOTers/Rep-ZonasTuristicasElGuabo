package com.example.login.layouts_models

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.login.client_api.UserLM
import com.example.login.databinding.FragmentRegisterBinding
import com.example.login.models.UserModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSeePassword.setOnClickListener {
            if(binding.inputPassword.inputType == 129){
                binding.inputPassword.inputType = 145
            }else{
                binding.inputPassword.inputType = 129
            }
        }
        binding.btnRegister.setOnClickListener{
            val name = binding.inputUser.text.toString()
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            val user = UserModel(null, name, email, password)
            if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                UserLM().findUser(user){
                    if(it?.id == null){
                        UserLM().insertUser(user){it2 ->
                            if(it2){
                                requireActivity().runOnUiThread{
                                    Toast.makeText(context, "Registro exitosa", Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                requireActivity().runOnUiThread{
                                    Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }else{
                        requireActivity().runOnUiThread{
                            Toast.makeText(context, "Usuario ya existente", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(context, "CAMPOS VACIOS", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }
}