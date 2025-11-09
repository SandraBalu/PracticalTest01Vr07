package com.example.practicaltest01vr07

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PracticalTest01Vr07SecondaryActivity  : AppCompatActivity() {

    private lateinit var input1 : TextView
    private lateinit var input2 : TextView
    private lateinit var input3 : TextView
    private lateinit var input4 : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary_practical_test01_vr07)

        // luam valorile transpise prin intent din activitatea principala
        // NUMELE TREBUIE SA FIE IDENTIC CU CEL SCRIS IN MAIN ACTIVITY
        val in1 = intent.getIntExtra("INPUT1", 0)
        val in2 = intent.getIntExtra("INPUT2", 0)
        val in3 = intent.getIntExtra("INPUT3", 0)
        val in4 = intent.getIntExtra("INPUT4", 0)

        // casetele text din interfata (asociem cu id-urile din xml)
        input1 = findViewById(R.id.text1)
        input2 = findViewById(R.id.text2)
        input3 = findViewById(R.id.text3)
        input4 = findViewById(R.id.text4)

        // punem textul primit prin intent in casetele text din ui
        input1.text = in1.toString()
        input2.text = in2.toString()
        input3.text = in3.toString()
        input4.text = in4.toString()

        val sum = findViewById<Button>(R.id.sum)
        sum.setOnClickListener {
            val result = in1 + in2 +in3 + in4
            val intent = Intent()
            intent.putExtra("MESSAGE", "Sum = $result")
            setResult(RESULT_OK, intent)
            finish()
        }

        val prod = findViewById<Button>(R.id.prod)
        prod.setOnClickListener {
            val result = in1 * in2 * in3 *in4
            val intent = Intent()
            intent.putExtra("MESSAGE", "Prod = $result")
            setResult(RESULT_OK, intent)
            finish()
        }




    }
}