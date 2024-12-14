package com.example.expensemanager

import android.content.DialogInterface
import android.content.Intent
import android.icu.number.IntegerWidth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.expensemanager.databinding.ActivityUpdateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.nio.file.Files.delete
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity : AppCompatActivity() {

    private var binding_:ActivityUpdateBinding?=null
    private val binding get()=binding_!!
    lateinit var database:FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    var transaction : com.example.expensemanager.Transaction?=null
    lateinit var transactionDao: TransactionDao
    var date_=SimpleDateFormat("dd-MM-yyyy").format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_=ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth=FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()


        val intent=intent
        transactionDao=TransactionDao(this)
        val id=intent.getStringExtra("id")!!




        var newType =""


        database.collection("Transaction").document(firebaseAuth.currentUser?.uid!!).collection(date_).document(id).get().addOnCompleteListener(){
            if(it.isSuccessful){
                transaction=it.result.toObject(com.example.expensemanager.Transaction::class.java)
//                Toast.makeText(this,it.result.id.toString(),Toast.LENGTH_SHORT).show()
                binding.etTitle.setText(transaction?.title)
                binding.etAmount.setText(transaction?.amount.toString())
                if(transaction?.type == "Income"){
                    binding.cbincome.isChecked=true
                }
                else{
                    binding.cbexpense.isChecked=true
                }
                newType=transaction?.type!!
            }
            else{
                Toast.makeText(this@UpdateActivity,it.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }


        binding.cbincome.setOnClickListener() {

            if (binding.cbincome.isChecked) {
                newType = "Income"

                binding.cbincome.isChecked = true
                binding.cbexpense.isChecked = false


            }
        }
        binding.cbexpense.setOnClickListener() {

            if (binding.cbexpense.isChecked) {
                newType = "Expense"

                binding.cbincome.isChecked = false
                binding.cbexpense.isChecked = true


            }
        }






        binding.btnUpdate.setOnClickListener(){
            val title=binding.etTitle.text.toString()
            val amount=Integer.parseInt(binding.etAmount.text.toString())
            val date_=transaction?.date.toString()
            val category=transaction?.category.toString()
            val newTransaction=Transaction(id,title,amount,newType,date_,category)
            transactionDao.updateTransaction(newTransaction)
            finish()
        }




        binding.btnDelete.setOnClickListener(){

            AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Are you sure you want to delele this transaction")
                .setIcon(R.drawable.ic_delete)
                .setTitle("Delete")
                .setPositiveButton("Delete",object:DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        database.collection("Transaction").document(firebaseAuth.currentUser?.uid!!).collection(date_).document(id).delete().addOnCompleteListener(){
                            if(it.isSuccessful){
                                Toast.makeText(this@UpdateActivity,"Deleted Successfully",Toast.LENGTH_SHORT).show()

                            }
                            else{
                                Toast.makeText(this@UpdateActivity,it.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                            }
                        }
                        val intent= Intent(this@UpdateActivity,MainActivity::class.java)
                        startActivity(intent)
                    }

                })
                .setNegativeButton("Cancel",object:DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog?.dismiss()
                    }
                }).show()


        }























    }

    override fun onStart() {
        super.onStart()
    }


}