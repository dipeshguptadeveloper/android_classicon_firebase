package com.dkgtech.classicon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dkgtech.classicon.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    lateinit var auth: FirebaseAuth

    companion object {
        val TAG = "LoginInActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        with(binding) {

            btnSignIn.setOnClickListener {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                if (email != "" && password != "") {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            Log.d(TAG, "Logged-In Successfully : ${it.user!!.uid}")
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this@LoginActivity,
                                "SignUpError : ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(TAG, "SignUpError : ${it.message}")
                            it.printStackTrace()
                        }
                }

            }
        }

    }
}