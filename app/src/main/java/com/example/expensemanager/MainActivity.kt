package com.example.expensemanager

import android.accessibilityservice.GestureDescription
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.databinding.ActivityMainBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), onItemCliked {

    private var binding_:ActivityMainBinding?=null
    private val binding get()=binding_!!
    lateinit var transactionDao: TransactionDao
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var database:FirebaseFirestore
    lateinit var adapter: TransactionAdapter
    lateinit var prevTrans:Transaction

    var sumIncome=0
    var sumExpense=0

    var date_ = SimpleDateFormat("dd-MM-yyyy").format(Date())

    lateinit var databaseReference:DatabaseReference

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
        databaseReference=FirebaseDatabase.getInstance().getReference()
        setSupportActionBar(binding.bottomAppbar)

        binding.bottomAppbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.profile ->{
                    val intent=Intent(this,ProfileActivity::class.java)
                    intent.putExtra("income",sumIncome)
                    intent.putExtra("expense",sumExpense)
                    startActivity(intent)
                    finish()
                }

                R.id.about ->{
                    Toast.makeText(this,"About",Toast.LENGTH_SHORT).show()
                }
                R.id.graphs -> {
                    val intent = Intent(this, GraphActivity::class.java)
                    intent.putExtra("income", sumIncome)
                    intent.putExtra("expense", sumExpense)
                    intent.putExtra("balance", sumIncome - sumExpense)
                    startActivity(intent)
                }
                else->{
                    Toast.makeText(this,"Menu",Toast.LENGTH_SHORT).show()
                }


            }
            true
        }



        binding.btnAdd.setOnClickListener(){

            val intent= Intent(this,AddTransaction::class.java)
            startActivity(intent)

        }

        setUpViews()



    }



    private fun setUpViews() {

        val currentUserId=firebaseAuth.currentUser?.uid!!
        transactionDao=TransactionDao(this)



        val collection=database.collection("Transaction").document(firebaseAuth.currentUser?.uid!!).collection(date_)
        val query=collection.orderBy("amount",Query.Direction.DESCENDING)
        val options= FirestoreRecyclerOptions.Builder<Transaction>().setQuery(query,Transaction::class.java).build()

        adapter=TransactionAdapter(options,this)
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        binding.recyclerView.adapter=adapter
        binding.recyclerView.itemAnimator = null
        eventChangeListener()






    }





    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    private fun eventChangeListener() {



        val collectionReference=database.collection("Transaction").document(firebaseAuth.currentUser?.uid!!).collection(date_)
        collectionReference.addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(value == null){
                    Toast.makeText(this@MainActivity,"null",Toast.LENGTH_SHORT).show()
                }else{
                    value.documentChanges.forEach {
                        if(it.type == DocumentChange.Type.ADDED){
                            val trans=it.document.toObject(Transaction::class.java)
                            if(trans.type == "Income"){
                                sumIncome+=trans.amount
                            }
                            else{
                                sumExpense+=trans.amount
                            }
                            binding.income.text= "Rs$sumIncome"
                            binding.expense.text= "Rs$sumExpense"
                            binding.balance.text="Rs"+(sumIncome-sumExpense).toString()
                        }

                        else if(it.type == DocumentChange.Type.MODIFIED || it.type ==DocumentChange.Type.REMOVED){

                            val trans=it.document.toObject(Transaction::class.java)

                            if(prevTrans.type == "Income" && trans.type=="Income"){

                                sumIncome-=prevTrans.amount
                                sumIncome+=trans.amount
                            }
                            if(prevTrans.type=="Expense" && trans.type=="Expense"){

                                sumExpense-=prevTrans.amount
                                sumExpense+=trans.amount
                            }
                            if(prevTrans.type=="Income" && trans.type=="Expense"){

                                sumIncome-=prevTrans.amount
                                sumExpense+=trans.amount
                            }
                            if(prevTrans.type=="Expense" && trans.type=="Income"){

                                sumExpense-=prevTrans.amount
                                sumIncome+=trans.amount
                            }
//
                            binding.income.text= "Rs$sumIncome"
                            binding.expense.text= "Rs$sumExpense"
                            binding.balance.text="Rs"+(sumIncome-sumExpense).toString()
                        }

                    }
                }
            }

        })

    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onItemClicked(id: String) {

        GlobalScope.launch(Dispatchers.IO) {
            database.collection("Transaction").document(firebaseAuth.currentUser?.uid!!).collection(date_).document(id).get().addOnCompleteListener(){
                if(it.isSuccessful){
                    prevTrans=it.result.toObject(Transaction::class.java)!!
                }
            }
        }


        val intent=Intent(this,UpdateActivity::class.java)
        intent.putExtra("id",id)
        startActivity(intent)
    }


}