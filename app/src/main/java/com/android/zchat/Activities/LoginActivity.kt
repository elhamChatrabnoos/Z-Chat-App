package com.android.zchat.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.zchat.MainActivity
import com.android.zchat.R
import com.android.zchat.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // initialize authentication
        mAuth = Firebase.auth

        binding.loginBtn.setOnClickListener{
            val email = binding.emailEdt.text.toString()
            val password = binding.passEdt.text.toString()
            login(email, password)
        }

        binding.signUpBtn.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                    Toast.makeText(this@LoginActivity, "login success", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("1212", "fail = " + task.exception)
                    Toast.makeText(this@LoginActivity, "fail = ", Toast.LENGTH_SHORT).show()
                }
            }
        binding.emailEdt.setText("")
        binding.passEdt.setText("")
    }
}