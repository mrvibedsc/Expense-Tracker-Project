package com.example.expensemanager

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val intent=intent
        val sumincome=intent.getIntExtra("income",0)
        val sumexpense=intent.getIntExtra("expense",0)


        val firebaseAuth=FirebaseAuth.getInstance()
        val btn=findViewById<Button>(R.id.btnlogout)

        val email=findViewById<TextView>(R.id.email)
        email.text=firebaseAuth.currentUser?.displayName.toString()

        val income=findViewById<TextView>(R.id.income_)
        income.text= "Rs$sumincome"
        val expense=findViewById<TextView>(R.id.expense_)
        expense.text= "Rs$sumexpense"


        btn.setOnClickListener(){
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Are you sure you want to Logout?")
                .setTitle("Logout")
                .setIcon(R.drawable.ic_baseline_login_24)
                .setPositiveButton("Logout",object:DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                })
                .setNegativeButton("Cancel",object :DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog?.dismiss()
                    }

                })
                .show()

        }
    }
}