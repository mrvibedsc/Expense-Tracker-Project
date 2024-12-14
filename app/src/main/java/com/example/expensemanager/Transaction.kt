package com.example.expensemanager

data class Transaction(
    val id:String?="",
    val title:String="",
    val amount:Int=0,
    val type:String="",
    val date: String="",
    val category:String="Others"

)
