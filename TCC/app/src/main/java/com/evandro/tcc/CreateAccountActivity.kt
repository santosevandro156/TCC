package com.evandro.tcc

import android.app.ProgressDialog
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateAccountActivity : AppCompatActivity() {

    //Login element declaration
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnRegister:Button? = null
    private var mProgressBar: ProgressDialog? = null

    //Database References
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private val TAG = "CreateAccountActivity"

    //Global Variables
    private var firstName: String? = null
    private var lastName: String? = null
    private var email: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        //Call function: initialise
        initialise()
    }

    //Function Initialise
    private fun initialise(){

        //Find Values for variables
        etFirstName = findViewById<View>(R.id.et_First_Name) as EditText
        etLastName = findViewById<View>(R.id.et_Last_Name) as EditText
        etEmail = findViewById<View>(R.id.et_email) as EditText
        etPassword = findViewById<View>(R.id.et_password) as EditText
        btnRegister = findViewById<View>(R.id.btn_Register) as Button

        //Database Instances
        mDatabaseReference  = mDatabase!!.reference!!.child("Users")
        mDatabase           = FirebaseDatabase.getInstance()
        mAuth               = FirebaseAuth.getInstance()

        //Action for button create account
        btnRegister!!.setOnClickListener { createNewAccount() }
    }

    //Function create new account
    private fun createNewAccount(){

        //Convert variables to string
        firstName = etFirstName?.text.toString()
        lastName = etLastName?.text.toString()
        email = etEmail?.text.toString()
        password = etPassword?.text.toString()

        //Verify if variables are empty
        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

        }else{
            Toast.makeText(this,"Fill all the information", Toast.LENGTH_SHORT).show()
        }

        //Show progress
        mProgressBar!!.setMessage("Registering user")
        mProgressBar!!.show()

        //Input information in database
        mAuth!!
            .createUserWithEmailAndPassword(email!!,password!!)
            .addOnCompleteListener(this){ task ->
            mProgressBar!!.hide()
            if (task.isSuccessful){
                Log.d(TAG,"CreateUserWithEmail:Success")

                    val userId = mAuth!!.currentUser!!.uid

                //Call function: user email verification
                verifyEmail()

                val currentUserDb = mDatabaseReference!!.child(userId)
                currentUserDb.child("firstName").setValue(firstName)
                currentUserDb.child("lastName").setValue(lastName)

                //Call function: update information in database
                updateUserAndInfoId()
            }else{
                Log.w(TAG,"CreateUserWithEmail:Failure",task.exception)
                Toast.makeText(this@CreateAccountActivity,"Authentication Failure",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyEmail(){
        val mUser = mAuth!!.currentUser;
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    Toast.makeText(this@CreateAccountActivity,
                        "Verification email sent to" + mUser.getEmail(),
                        Toast.LENGTH_SHORT).show()
                } else {
                Log.e(TAG, "SendEmailVerification", task.exception)
                Toast.makeText(this@CreateAccountActivity,
                    "Failure to send verification email",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Directs user for main activity after insert information in database
    private fun updateUserAndInfoId(){
        val intent = Intent(this@CreateAccountActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }
}
