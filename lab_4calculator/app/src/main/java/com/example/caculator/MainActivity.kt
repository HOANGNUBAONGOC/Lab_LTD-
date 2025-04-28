package com.example.caculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.caculator.R

public class MainActivity : AppCompatActivity() {

    private lateinit var tvInput: TextView
    private lateinit var tvOldInput: TextView

    private var oldValue: String = ""
    private var newValue: String = ""
    private var operator: String = ""
    private var isNewInput: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.tvInput)
        tvOldInput = findViewById(R.id.tvOldInput)

        val btnOne = findViewById<Button>(R.id.btnOne)
        val btnTwo = findViewById<Button>(R.id.btnTwo)
        val btnThree = findViewById<Button>(R.id.btnThree)
        val btnFour = findViewById<Button>(R.id.btnFour)
        val btnFive = findViewById<Button>(R.id.btnFive)
        val btnSix = findViewById<Button>(R.id.btnSix)
        val btnSeven = findViewById<Button>(R.id.btnSeven)
        val btnEight = findViewById<Button>(R.id.btnEight)
        val btnNine = findViewById<Button>(R.id.btnNine)
        val btnZero = findViewById<Button>(R.id.btnZero)
        val btnDot = findViewById<Button>(R.id.btnDot)
        val btnPlus = findViewById<Button>(R.id.btnPLus)
        val btnMinus = findViewById<Button>(R.id.btnMinus)
        val btnMultiply = findViewById<Button>(R.id.btnMultiply)
        val btnDivide = findViewById<Button>(R.id.btnDivide)
        val btnEqual = findViewById<Button>(R.id.btnEqual)
        val btnClear = findViewById<Button>(R.id.clear)
        val btnAllClear = findViewById<Button>(R.id.allClear)

        val numberButtons = listOf(
            btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix,
            btnSeven, btnEight, btnNine, btnZero
        )

        numberButtons.forEach { button ->
            button.setOnClickListener {
                if (isNewInput) {
                    tvInput.text = ""
                    isNewInput = false
                }
                tvInput.append((it as Button).text)
            }
        }

        btnDot.setOnClickListener {
            if (isNewInput) {
                tvInput.text = "0."
                isNewInput = false
            } else if (!tvInput.text.contains(".")) {
                tvInput.append(".")
            }
        }

        btnPlus.setOnClickListener { setOperator("+") }
        btnMinus.setOnClickListener { setOperator("-") }
        btnMultiply.setOnClickListener { setOperator("*") }
        btnDivide.setOnClickListener { setOperator("/") }

        btnClear.setOnClickListener {
            tvInput.text = ""
        }

        btnAllClear.setOnClickListener {
            tvInput.text = ""
            tvOldInput.text = ""
            oldValue = ""
            newValue = ""
            operator = ""
        }

        btnEqual.setOnClickListener {
            newValue = tvInput.text.toString()
            val result = calculate()
            tvInput.text = result.toString()
            tvOldInput.text = ""
            isNewInput = true
        }
    }

    private fun setOperator(op: String) {
        oldValue = tvInput.text.toString()
        operator = op
        tvOldInput.text = "$oldValue $operator"
        tvInput.text = ""
        isNewInput = true
    }

    private fun calculate(): Double {
        return when (operator) {
            "+" -> oldValue.toDouble() + newValue.toDouble()
            "-" -> oldValue.toDouble() - newValue.toDouble()
            "*" -> oldValue.toDouble() * newValue.toDouble()
            "/" -> oldValue.toDouble() / newValue.toDouble()
            else -> 0.0
        }
    }
}