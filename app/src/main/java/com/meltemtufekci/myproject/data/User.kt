package com.meltemtufekci.myproject.data

import java.io.Serializable

data class User(
    val avatar: String,
    val email: String,
    val id: String,
    val name: String
) : Serializable