package com.example.login.layouts_models

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.login.client_api.UserLM
import com.example.login.databinding.FragmentUserBinding
import com.example.login.models.UserModel

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recivir parametros de Fragmento
        val id = arguments?.getInt("id")
        var name = arguments?.getString("name")
        var email = arguments?.getString("email")
        var password = arguments?.getString("password")

        //Mostrar datos del usuario
        binding.inputUser.setText(name)
        binding.inputEmail.setText(email)
        binding.inputPassword.setText(password)

        binding.btnSeePassword.setOnClickListener {
            if(binding.inputPassword.inputType == 129){
                binding.inputPassword.inputType = 145
            }else{
                binding.inputPassword.inputType = 129
            }
        }
        binding.btnEditar.setOnClickListener{
            name = binding.inputUser.text.toString()
            email = binding.inputEmail.text.toString()
            password = binding.inputPassword.text.toString()
            val user = UserModel(id, name, email, password)
            if(name!!.isNotEmpty() && email!!.isNotEmpty() && password!!.isNotEmpty()){
                UserLM().updateUser(id!!, user){
                    if(it){
                        requireActivity().runOnUiThread{
                            Toast.makeText(context, "Actualizacion exitosa", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        requireActivity().runOnUiThread{
                            Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(context, "CAMPOS VACIOS", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnEliminar.setOnClickListener{
            UserLM().deleteUser(id!!){
                if(it){
                    requireActivity().runOnUiThread{
                        Toast.makeText(context, "Eliminacion exitosa", Toast.LENGTH_SHORT).show()
                    }
                    parentFragmentManager.popBackStack()
                }else{
                    requireActivity().runOnUiThread{
                        Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(layoutInflater)
        return binding.root
    }
}