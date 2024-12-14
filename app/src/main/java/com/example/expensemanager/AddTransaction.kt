package com.example.expensemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.expensemanager.databinding.ActivityAddTransactionBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import java.lang.Long.parseLong
import java.text.SimpleDateFormat
import java.util.*

class AddTransaction : AppCompatActivity(){

    private var binding_:ActivityAddTransactionBinding?=null
    private val binding get()=binding_!!
    lateinit var transactionDao: TransactionDao
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var category:String

    override fun onResume() {
        super.onResume()
        val categories=resources.getStringArray(R.array.categories)
        val arrayAdapter=ArrayAdapter(this,R.layout.dropdown_item,categories)
        binding.autoCompletetextView.setAdapter(arrayAdapter)
        binding.autoCompletetextView.setOnItemClickListener { parent, view, position, id ->
            category=parent.getItemAtPosition(position).toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_ = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        transactionDao = TransactionDao(this)

        var sumIncome = 0
        var sumExpense = 0

        var type = ""

        binding.cbincome.setOnClickListener() {

            if (binding.cbincome.isChecked) {
                type = "Income"

                binding.cbincome.isChecked = true
                binding.cbexpense.isChecked = false


            }
        }
        binding.cbexpense.setOnClickListener() {

            if (binding.cbexpense.isChecked) {
                type = "Expense"

                binding.cbincome.isChecked = false
                binding.cbexpense.isChecked = true


            }
        }

        binding.btnAddTransaction.setOnClickListener() {
            val id = firebaseAuth.currentUser?.uid
            val amount = Integer.parseInt(binding.etAmount.text.toString())
            val title = binding.etTitle.text.toString()

            val dateFormatter=SimpleDateFormat("dd-MM-yyyy")
            val date=dateFormatter.format(Date())

//            if(type == "Income"){
//                sumIncome+=amount
//            }
//            else{
//                sumExpense+=amount
//            }
            val transaction = Transaction(id, title, amount, type, date,category)

            transactionDao.addTransaction(transaction)
            finish()

        }
    }

//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        category = parent?.selectedItem as String
//    }
//
//    override fun onNothingSelected(parent: AdapterView<*>?) {
//    }
}

