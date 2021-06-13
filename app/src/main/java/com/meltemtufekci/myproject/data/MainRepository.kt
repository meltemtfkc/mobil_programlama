package com.meltemtufekci.myproject.data

class MainRepository {
    suspend fun getUsers() = RetrofitBuilder.api.getUsers()
}