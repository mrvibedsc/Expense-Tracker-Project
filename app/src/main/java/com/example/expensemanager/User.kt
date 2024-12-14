package com.example.expensemanager

data class User(
    val title:String="",
    val transaction:MutableMap<String,Transaction> = mutableMapOf()
)