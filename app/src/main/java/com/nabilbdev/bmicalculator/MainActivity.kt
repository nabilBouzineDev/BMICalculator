package com.nabilbdev.bmicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val weightText = findViewById<EditText>(R.id.etWeight)
        val heightText = findViewById<EditText>(R.id.etHeight)
        val calculateButton = findViewById<Button>(R.id.btnCalculate)

        calculateButton.setOnClickListener {
            val weight = weightText.text.toString()
            val height = heightText.text.toString()

            if (validateInput(weight, height)) {
                val bmi = weight.toFloat() / ((height.toFloat() / 100) * (height.toFloat() / 100))
                // we want to get only 2 digits after decimal point.
                val bmi2Digit = String.format("%.2f", bmi).toFloat()
                displayResult(bmi2Digit)
            }
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
        var bodyLevel = ""
        var color = 0

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

