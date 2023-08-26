package com.example.wheather_app

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        window.statusBarColor= Color.TRANSPARENT


        Handler(Looper.getMainLooper()).postDelayed({
            val intent =Intent(this@MainActivity,Home::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}