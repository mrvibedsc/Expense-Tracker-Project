package com.example.expensemanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.expensemanager.databinding.ActivityLoginBinding
import com.example.expensemanager.databinding.ActivityLoginIntroBinding
import com.google.firebase.auth.FirebaseAuth

class LoginIntro : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    private var binding_:ActivityLoginIntroBinding?=null
    private val binding get()=binding_!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_=ActivityLoginIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth=FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser!=null){
            redirect("MAIN")
            finish()
        }

        binding.btnStarted.setOnClickListener(){
            redirect("LOGIN")
        }



    }

    private fun redirect(s: String) {
        when(s){
            "LOGIN"->{
                val intent= Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            "MAIN"->{
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}