package com.dkgtech.classicon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.dkgtech.classicon.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding

    lateinit var auth: FirebaseAuth

    companion object {
        val TAG = "SignUpActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        with(binding) {

            btnSignUp.setOnClickListener {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                if (email != "" && password != "") {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            Log.d(TAG, "Account Created : ${it.user!!.uid}")
                            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this@SignUpActivity,
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