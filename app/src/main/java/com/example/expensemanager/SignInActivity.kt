package com.example.expensemanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.expensemanager.databinding.ActivityLoginBinding
import com.example.expensemanager.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import java.lang.reflect.Array.get


class SignInActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    private var binding_: ActivitySignInBinding? = null
    private val binding get() = binding_!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_ = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.etEmail.requestFocus()

        binding.apply {
            btnsignin.setOnClickListener() {
                signIn()
                progressbar.visibility = View.VISIBLE
            }

        }

    }

    private fun signIn() {

        val email = binding.etEmail.text.toString()
        val UserName = binding.etUsername.text.toString()
        val Password = binding.etPassword.text.toString()

        if (email.isBlank() || UserName.isBlank() || Password.isBlank()) {
            Toast.makeText(this@SignInActivity,
                "Username/Email/Password cannot be blank",
                Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(this) {
            binding.progressbar.visibility = View.GONE
            if (it.isSuccessful) {
                firebaseAuth.currentUser?.updateProfile(UserProfileChangeRequest.Builder()
                    .setDisplayName(UserName).build())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this, it.exception?.localizedMessage.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}