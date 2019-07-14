package com.evandro.tcc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    //Forgot element declaration
    private var etEmail: EditText? = null
    private var btnSubmit: Button? = null

    //Database References
    private val TAG = "forgotPasswordActivity"
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        //Call function: initialise
        initialise()
    }

    private fun initialise(){

        //Find Values for variables
        etEmail = findViewById<View>(R.id.et_email) as EditText
        btnSubmit = findViewById<View>(R.id.btn_submit) as Button

        //Database Instances
        mAuth = FirebaseAuth.getInstance()

        //Action for button create account
        btnSubmit!!.setOnClickListener { sendPasswordEmail() }
    }

    private fun sendPasswordEmail(){

        //Convert variables to string
        val email = etEmail?.text.toString()

        //Verify if variables are empty
        if (!TextUtils.isEmpty(email)){
            mAuth!!
                .sendPasswordResetEmail(email)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        val message = "Email sent"
                        Log.d(TAG, message)
                        Toast.makeText(this, message,Toast.LENGTH_SHORT).show()

                        //Call function: update user interface
                        updateUI()
                    }else{
                        Log.w(TAG, task.exception!!.message)
                        Toast.makeText(this, "No user found with this email", Toast.LENGTH_SHORT).show()
                    }
                }
        }else {
            Toast.makeText(this, "Fill all the information", Toast.LENGTH_SHORT).show()
        }
    }

    //Directs user for login activity after send email
    private fun updateUI(){
        val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
