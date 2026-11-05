package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProximityActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var gestorSensores: SensorManager
    private var sensorProximidad: Sensor? = null

    private lateinit var disenoPrincipal: LinearLayout
    private lateinit var imagenEstado: ImageView
    private lateinit var textoEstado: TextView
    private lateinit var textoValorSensor: TextView
    private lateinit var botonMostrarContador: Button
    private lateinit var botonRegresar: Button

    private var contadorBloqueos = 0
    private var estaBloqueado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_proximity)

        disenoPrincipal = findViewById(R.id.disenoPrincipal)
        imagenEstado = findViewById(R.id.imagenEstado)
        textoEstado = findViewById(R.id.textoEstado)
        textoValorSensor = findViewById(R.id.textoValorSensor)
        botonMostrarContador = findViewById(R.id.botonMostrarContador)
        botonRegresar = findViewById(R.id.botonRegresar)

        ViewCompat.setOnApplyWindowInsetsListener(disenoPrincipal) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gestorSensores = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorProximidad = gestorSensores.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (sensorProximidad == null) {
            textoValorSensor.text = getString(R.string.sensor_not_available)
        }

        botonMostrarContador.setOnClickListener {
            Toast.makeText(this, getString(R.string.block_count_toast, contadorBloqueos), Toast.LENGTH_SHORT).show()
        }

        botonRegresar.setOnClickListener {
            finish() // Cierra esta actividad y regresa a la anterior (el menú)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorProximidad?.let {
            gestorSensores.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        gestorSensores.unregisterListener(this)
    }

    override fun onSensorChanged(evento: SensorEvent?) {
        if (evento?.sensor?.type == Sensor.TYPE_PROXIMITY) {
            val distancia = evento.values[0]

            textoValorSensor.text = getString(R.string.sensor_value, distancia.toString())

            val rangoMaximo = sensorProximidad?.maximumRange ?: 10f
            val estaCerca = distancia < rangoMaximo

            if (estaCerca) {
                if (!estaBloqueado) {
                    estaBloqueado = true
                    contadorBloqueos++
                    actualizarInterfaz(true)
                }
            } else {
                if (estaBloqueado) {
                    estaBloqueado = false
                    actualizarInterfaz(false)
                }
            }
        }
    }

    private fun actualizarInterfaz(bloqueado: Boolean) {
        if (bloqueado) {
            disenoPrincipal.setBackgroundColor(Color.BLACK)
            imagenEstado.setImageResource(R.drawable.ic_lock)
            imagenEstado.setColorFilter(Color.WHITE)
            textoEstado.text = getString(R.string.screen_locked)
            textoEstado.setTextColor(Color.WHITE)
            textoValorSensor.setTextColor(Color.WHITE)
        } else {
            disenoPrincipal.setBackgroundColor(Color.WHITE)
            imagenEstado.setImageResource(R.drawable.ic_lock_open)
            imagenEstado.setColorFilter(Color.BLACK)
            textoEstado.text = getString(R.string.screen_unlocked)
            textoEstado.setTextColor(Color.BLACK)
            textoValorSensor.setTextColor(Color.BLACK)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, precision: Int) {}
}