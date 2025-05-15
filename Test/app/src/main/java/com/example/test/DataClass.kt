package com.example.test

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id:Int?=null,
    val fname:String,
    val lname:String,
    val email:String,
    val password:String?=null
)
@Serializable
data class Login(
    val email: String,
    val password: String,
    val device_name:String
)