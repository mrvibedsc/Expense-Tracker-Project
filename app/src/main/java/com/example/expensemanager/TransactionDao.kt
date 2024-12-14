package com.example.expensemanager

import android.content.Context
import android.provider.Settings
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TransactionDao(val context: Context) {

    val firebaseAuth:FirebaseAuth= FirebaseAuth.getInstance()
    val database:FirebaseFirestore= FirebaseFirestore.getInstance()
    val collection=database.collection("Transaction")
    val currentUserId= firebaseAuth.currentUser?.uid!!
    var sumIncome=0
    var sumExpense=0

    var date_ =SimpleDateFormat("dd-MM-yyyy").format(Date())

    fun addTransaction(transaction: Transaction) {

        GlobalScope.launch(Dispatchers.IO) {


            collection.document(currentUserId).collection(date_)
                .document().set(transaction).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, it.exception?.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }

            }

        }
    }
    fun getTransaction(id:String) :Task<DocumentSnapshot>{

        return database.collection("Transaction").document(currentUserId).collection(date_).document(id).get()
    }

    fun updateTransaction(transaction: Transaction){

        database.collection("Transaction").document(currentUserId).collection(date_).document(transaction.id!!).update("amount",transaction.amount,"type",transaction.type,"title",transaction.title).addOnCompleteListener(){
            if(it.isSuccessful){
            Toast.makeText(context,"Updated Successfully",Toast.LENGTH_SHORT).show()

            }
        }
    }

    fun deleteTransaction(id: String){

            GlobalScope.launch(Dispatchers.IO) {

                database.collection("Transaction").document(currentUserId).collection(date_).document(
                    id).delete()


            }



    }








}