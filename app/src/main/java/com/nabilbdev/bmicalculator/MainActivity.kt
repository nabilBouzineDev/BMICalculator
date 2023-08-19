package com.nabilbdev.bmicalculator

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var weightId: EditText
    private lateinit var heightId: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weightId = findViewById(R.id.etWeight)
        heightId = findViewById(R.id.etHeight)
        val calculateButton = findViewById<Button>(R.id.btnCalculate)

        calculateButton.setOnClickListener {
            val weight = weightId.text.toString()
            val height = heightId.text.toString()

            if (validateInput(weight, height)) {
                val bmi = weight.toFloat() / ((height.toFloat() / 100) * (height.toFloat() / 100))
                // we want to get only 2 digits after decimal point.
                val bmi2Digit = String.format("%.2f", bmi).toFloat()
                displayResult(bmi2Digit)
            }
        }
        sharedPref = getSharedPreferences("sf", MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    override fun onPause() {
        super.onPause()

        val weight = weightId.text.toString()
        val height = heightId.text.toString()

        editor.apply {
            putInt("weight_sf", weight.toInt())
            putInt("height_sf", height.toInt())
            commit()
        }
    }

    override fun onResume() {
        super.onResume()

        val weightValue = sharedPref.getInt("weight_sf", 0)
        val heightValue = sharedPref.getInt("height_sf", 0)

        if (weightValue != 0 && heightValue != 0) {
            weightId.setText(weightValue.toString())
            heightId.setText(heightValue.toString())
        }
    }

    private fun validateInput(weight: String?, height: String?) = when {
        weight.isNullOrEmpty() -> {
            Toast.makeText(this, "please enter a non empty weight", Toast.LENGTH_LONG).show()
            false
        }
        height.isNullOrEmpty() -> {
            Toast.makeText(this, "please enter a non empty height", Toast.LENGTH_LONG).show()
            false
        }
        else -> true
    }

    private fun displayResult(bmi: Float) {
        val indexResult = findViewById<TextView>(R.id.tvIndex)
        val descriptionResult = findViewById<TextView>(R.id.tvResult)

        indexResult.text = bmi.toString()

        // underweight, normal, overweight, and obese levels
        val bodyLevel: String
        val color: Int

        when {
            bmi < 18.50 -> { bodyLevel = "Underweight"; color = R.color.under_weight }
            bmi in 18.50..24.99 -> { bodyLevel = "Healthy"; color = R.color.normal }
            bmi in 25.00..29.90 -> { bodyLevel = "Overweight"; color = R.color.over_weight }
            else -> { bodyLevel = "Obese"; color = R.color.obese }
        }

        descriptionResult.setTextColor(ContextCompat.getColor(this, color))
        descriptionResult.text = bodyLevel
    }
}