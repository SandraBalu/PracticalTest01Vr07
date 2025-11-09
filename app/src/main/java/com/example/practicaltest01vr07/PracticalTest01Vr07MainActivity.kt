
package com.example.practicaltest01vr07

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PracticalTest01Vr07MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE = 1
        private const val ACTION_PROCESSING = "com.example.practicaltest01vr07.PROCESSING_THREAD"
    }

    private lateinit var e1: EditText
    private lateinit var e2: EditText
    private lateinit var e3: EditText
    private lateinit var e4: EditText
    private lateinit var setButton: Button

    private lateinit var receiver: BroadcastReceiver
    private lateinit var filter: IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // asigură-te că ăsta e numele layout-ului tău principal
        setContentView(R.layout.activity_practical_test01_vr07)

        // legăm controalele din XML
        e1 = findViewById(R.id.editText1)
        e2 = findViewById(R.id.editText2)
        e3 = findViewById(R.id.editText3)
        e4 = findViewById(R.id.editText4)
        setButton = findViewById(R.id.setButton)

        // 🔹 PORNIM serviciul care generează numere (Task C.2)
        val serviceIntent = Intent(this, PracticalTest01Service::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }

        // 🔹 Receiver care ascultă numerele generate de ProcessingThread
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == ACTION_PROCESSING) {
                    val n1 = intent.getIntExtra("input1", 0)
                    val n2 = intent.getIntExtra("input2", 0)
                    val n3 = intent.getIntExtra("input3", 0)
                    val n4 = intent.getIntExtra("input4", 0)

                    // Actualizăm cele 4 câmpuri cu valorile primite
                    e1.setText(n1.toString())
                    e2.setText(n2.toString())
                    e3.setText(n3.toString())
                    e4.setText(n4.toString())
                }
            }
        }

        // 🔹 Filtrăm doar acțiunea noastră custom
        filter = IntentFilter(ACTION_PROCESSING)

        // 🔹 Butonul Set -> deschide SecondaryActivity cu valorile curente (Task C.1)
        setButton.setOnClickListener {
            val s1 = e1.text.toString()
            val s2 = e2.text.toString()
            val s3 = e3.text.toString()
            val s4 = e4.text.toString()

            // verificăm ca toate să fie numere
            if (s1.isBlank() || s2.isBlank() || s3.isBlank() || s4.isBlank()
                || s1.toIntOrNull() == null
                || s2.toIntOrNull() == null
                || s3.toIntOrNull() == null
                || s4.toIntOrNull() == null
            ) {
                // conform cerinței: dacă nu-s toate numere, ignorăm click-ul
                Toast.makeText(this, "Completează toate câmpurile cu numere!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, PracticalTest01Vr07SecondaryActivity::class.java).apply {
                putExtra("INPUT1", s1.toInt())
                putExtra("INPUT2", s2.toInt())
                putExtra("INPUT3", s3.toInt())
                putExtra("INPUT4", s4.toInt())
            }
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onResume() {
        super.onResume()
        // Receiver activ doar când activitatea e vizibilă
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(receiver, filter)
        }
    }

    override fun onPause() {
        // Dezactivăm receiver-ul când ieșim de pe ecran
        unregisterReceiver(receiver)
        super.onPause()
    }

    override fun onDestroy() {
        // Oprim serviciul când activitatea e distrusă complet
        stopService(Intent(this, PracticalTest01Service::class.java))
        super.onDestroy()
    }

    // 🔹 Primim rezultatul de la SecondaryActivity (Sum / Prod) și îl afișăm în Toast
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val message = data?.getStringExtra("MESSAGE")
            if (!message.isNullOrEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
