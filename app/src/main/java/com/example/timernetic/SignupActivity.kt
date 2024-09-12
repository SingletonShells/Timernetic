package com.example.timernetic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var proceedbtn:ImageView
    private lateinit var emailtxt: EditText
    private lateinit var passwordtxt: EditText
    private lateinit var confirmPasswordtxt: EditText
    private lateinit var REG: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        proceedbtn = findViewById(R.id.proceedbtn)
        emailtxt = findViewById(R.id.emailtxt)
        passwordtxt = findViewById(R.id.passwordtxt)
        confirmPasswordtxt = findViewById(R.id.confirmPasswordtxt)
        REG= findViewById(R.id.REG)
        REG.setOnClickListener{
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        proceedbtn.setOnClickListener{
            val email = emailtxt.text.toString().trim()
            val pass = passwordtxt.text.toString().trim()
            val verifyPass = confirmPasswordtxt.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty()){
                if(pass == verifyPass){
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
                        OnCompleteListener {
                            if(it.isSuccessful){
                                Toast.makeText(applicationContext,"Registered Successfully", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@SignupActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(applicationContext, it.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}