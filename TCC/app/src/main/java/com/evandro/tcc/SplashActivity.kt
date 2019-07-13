package com.evandro.tcc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //Call function: change to login screen
        changeToLogin()
    }

    //Function change screen
    fun changeToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        Handler().postDelayed({intent.change()},2000)
    }

    //Function atribute screen to change
    fun Intent.change(){
        startActivity(this)
        finish()
    }
}
