package com.example.login.layouts_models

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.login.R
import com.example.login.client_api.UserLM
import com.example.login.databinding.FragmentLoginBinding
import com.example.login.models.UserModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSeePassword.setOnClickListener {
            if(binding.inputPassword.inputType == 129){
                binding.inputPassword.inputType = 145
            }else{
                binding.inputPassword.inputType = 129
            }
        }
        binding.btnLogin.setOnClickListener{
            val name = binding.inputUser.text.toString()
            val password = binding.inputPassword.text.toString()
            val user = UserModel(null,name,null,password)
            if(name.isNotEmpty() && password.isNotEmpty()){
                UserLM().findUser(user){
                    if(it?.id != null){
                        //Enviar parametros a un fragmento
                        val bundle = Bundle().apply {
                            putInt("id", it.id!!)
                            putString("name", name)
                            putString("email", it.correo)
                            putString("password", password)
                        }
                        val fm = parentFragmentManager.beginTransaction()
                        fm.replace(R.id.fragmentAdmin, UserFragment().apply { arguments = bundle })
                        fm.addToBackStack(null)
                        fm.commit()
                    }else{
                        requireActivity().runOnUiThread{
                            Toast.makeText(context, "USUARIO NO ENCONTRADO", Toast.LENGTH_SHORT).show()
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
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }
}