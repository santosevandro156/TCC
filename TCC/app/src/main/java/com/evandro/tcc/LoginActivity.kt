package com.evandro.tcc


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import android.view.Window
import android.view.WindowManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    //Login element declaration
    private var tvForgotPassword: TextView? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnLogin: Button? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressDialog? = null

    //Database References
    private var mAuth: FirebaseAuth? = null
    private val TAG = "LoginActivity"

    //Global variables
    private var email: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.setStatusBarColorTo(R.color.colorPrimary)
        }

        //Call function: initialise
        initialise()
    }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun Window.setStatusBarColorTo(color: Int){
            this.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            this.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            this.statusBarColor = ContextCompat.getColor(baseContext, color)
        }

    private fun initialise(){
        //Find Values for variables
        tvForgotPassword = findViewById<View>(R.id.tv_forgot_password) as TextView
        etEmail = findViewById<View>(R.id.et_email) as EditText
        etPassword = findViewById<View>(R.id.et_password) as EditText
        btnLogin = findViewById<View>(R.id.btn_login) as Button
        btnCreateAccount = findViewById<View>(R.id.btn_register_account) as Button
        mProgressBar = ProgressDialog(this)

        //Database Instances
        mAuth   = FirebaseAuth.getInstance()

        //Action for forgot password
        tvForgotPassword!!
            .setOnClickListener{ startActivity(Intent(this@LoginActivity,
                ForgotPasswordActivity::class.java)) }

        //Action for create account
        btnCreateAccount!!
            .setOnClickListener{ startActivity(Intent(this@LoginActivity,
                CreateAccountActivity::class.java)) }

        //Action for login
        btnLogin!!.setOnClickListener { loginUser() }
    }

    private fun loginUser(){

        //Convert variables to string
        email = etEmail?.text.toString()
        password = etPassword?.text.toString()

        //Verify if variables are empty
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mProgressBar!!.setMessage("Checking User")
            mProgressBar!!.show()

            Log.d(TAG, "UserLogin")

            //Sign in database
            mAuth!!.signInWithEmailAndPassword(email!!,password!!)
                .addOnCompleteListener(this){ task ->

                mProgressBar!!.hide()

                //Authenticating user
                if(task.isSuccessful){
                    Log.d(TAG, "Login Success")
                    //Call function: update user information
                    updateUI()
                }else{
                    Log.d(TAG, "Login Failed", task.exception)
                    Toast.makeText(this@LoginActivity, "Authentication failure", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this, "Fill all the information",Toast.LENGTH_SHORT).show()
        }
    }

    //Directs user for main activity after insert information in database
    private fun updateUI(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
