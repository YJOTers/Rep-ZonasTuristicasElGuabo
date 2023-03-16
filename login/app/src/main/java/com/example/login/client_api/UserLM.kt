package com.example.login.client_api

import com.example.login.interfaces.UserInterface
import com.example.login.models.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class UserLM {

    private fun getRetrofit(): UserInterface = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/api/")
            .client(getClient())
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserInterface::class.java)

    private fun getClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()

    fun findUser(user: UserModel, getUser: (UserModel?) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val response = getRetrofit().findUser(user)
            if(response.isSuccessful){
                getUser(response.body())
            }
        }
    }

    fun findUserByEmail(email:String, getUser: (UserModel?) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val response = getRetrofit().findUserByEmail(email)
            if(response.isSuccessful){
                getUser(response.body())
            }
        }
    }

    fun insertUser(user: UserModel, getResult: (Boolean) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val response = getRetrofit().insertUser(user)
            if(response.isSuccessful){
                getResult(response.isSuccessful)
            }
        }
    }

    fun updateUser(id: Int, user: UserModel, getResult: (Boolean) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val response = getRetrofit().updateUser(id, user)
            if(response.isSuccessful){
                getResult(response.isSuccessful)
            }
        }
    }

    fun deleteUser(id: Int, getResult: (Boolean) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val response = getRetrofit().deleteUser(id)
            if(response.isSuccessful){
                getResult(response.isSuccessful)
            }
        }
    }
}