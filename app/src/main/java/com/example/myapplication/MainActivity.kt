package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.botonProximidad).setOnClickListener {
            startActivity(Intent(this, ProximityActivity::class.java))
        }

        findViewById<Button>(R.id.botonGravedad).setOnClickListener {
            startActivity(Intent(this, GravityActivity::class.java))
        }
    }
}