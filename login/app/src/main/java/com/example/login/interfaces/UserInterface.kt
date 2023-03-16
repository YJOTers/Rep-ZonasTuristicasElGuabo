package com.example.login.interfaces

import com.example.login.models.UserModel
import retrofit2.Response
import retrofit2.http.*

interface UserInterface {
    @POST("find_user/")
    suspend fun findUser(@Body user:UserModel): Response<UserModel>

    @GET("find_user_by_email/{email}")
    suspend fun findUserByEmail(@Path("email") email:String): Response<UserModel>

    @POST("insert_user/")
    suspend fun insertUser(@Body user:UserModel): Response<Unit>

    @PUT("update_user/{id}")
    suspend fun updateUser(@Path("id") id:Int, @Body user:UserModel): Response<Unit>

    @DELETE("delete_user/{id}")
    suspend fun deleteUser(@Path("id") id:Int): Response<Unit>
}