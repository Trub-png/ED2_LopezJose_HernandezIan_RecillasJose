package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GravityActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mainLayout: LinearLayout
    private lateinit var tvX: TextView
    private lateinit var tvY: TextView
    private lateinit var tvZ: TextView
    private lateinit var tvMensaje: TextView
    private lateinit var botonRegresar: Button


    private lateinit var sensorManager: SensorManager
    private var sensorGravedad: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gravity)

        mainLayout = findViewById(R.id.main_layout)
        tvX = findViewById(R.id.tv_x)
        tvY = findViewById(R.id.tv_y)
        tvZ = findViewById(R.id.tv_z)
        tvMensaje = findViewById(R.id.tv_mensaje)
        botonRegresar = findViewById(R.id.botonRegresar)


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorGravedad = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

        botonRegresar.setOnClickListener {
            finish()
        }
    }


    override fun onResume() {
        super.onResume()
        sensorGravedad?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]


        tvX.text = "X: ${String.format("%.2f", x)}"
        tvY.text = "Y: ${String.format("%.2f", y)}"
        tvZ.text = "Z: ${String.format("%.2f", z)}"

        if (z > 7) {
            mainLayout.setBackgroundColor(Color.GREEN)
            tvMensaje.text = "Celular acostado o superficie plana"
            cambiarColorLetras(Color.BLACK)

        } else if (x > 7) {
            mainLayout.setBackgroundColor(Color.parseColor("#FFA500"))
            tvMensaje.text = "Celular inclinado hacia un lado"
            cambiarColorLetras(Color.BLACK)

        } else if (y > 7) {
            mainLayout.setBackgroundColor(Color.BLACK)
            tvMensaje.text = "Celular vertical o inclinado"
            cambiarColorLetras(Color.WHITE)

        } else {
            mainLayout.setBackgroundColor(Color.WHITE)
            tvMensaje.text = "Orientación indefinida"
            cambiarColorLetras(Color.BLACK)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun cambiarColorLetras(color: Int) {
        tvX.setTextColor(color)
        tvY.setTextColor(color)
        tvZ.setTextColor(color)
        tvMensaje.setTextColor(color)
    }
}