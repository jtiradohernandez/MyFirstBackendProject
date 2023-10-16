package com.example.models

import kotlinx.serialization.Serializable

@Serializable
public class User (val userID: Int, var saldo: Int, val name: String) {}