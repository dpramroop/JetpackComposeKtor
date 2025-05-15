package com.example.test

import kotlinx.serialization.Serializable

sealed interface NavDestination

@Serializable
object Home:NavDestination

@Serializable
object Settings:NavDestination

@Serializable
data class AfterLogin(val username: String?=null) : NavDestination