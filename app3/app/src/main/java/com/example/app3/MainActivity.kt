package com.example.app3

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var isChecking = true
    private var checkedID = R.id.rbKilometers
    var currentMeasure:Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        insertNames()

        val input = findViewById<EditText>(R.id.input)
        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (input.length() == 0)
                    input.setText("0")
                insertNames()
            }
        })

        val rg1 = findViewById<RadioGroup>(R.id.radioGroup1)
        val rg2 = findViewById<RadioGroup>(R.id.radioGroup2)
        rg1.setOnCheckedChangeListener { _, checkedId ->
            insertNames()
            if (checkedId != -1 && isChecking) {
                currentMeasure = checkedId
                isChecking = false
                rg2.clearCheck()
                checkedID = checkedId
            }
            isChecking = true
        }
        rg2.setOnCheckedChangeListener { _, checkedId ->
            insertNames()
            if (checkedId != -1 && isChecking) {
                currentMeasure = checkedId
                isChecking = false
                rg1.clearCheck()
                checkedID = checkedId
            }
            isChecking = true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu!!.add("RU")
        menu!!.add("EN")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val locale = Locale(item.toString())
        Locale.setDefault(locale)
        val config: Configuration = baseContext.resources.configuration
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
        insertNames()
        return super.onOptionsItemSelected(item)

    }
    private fun insertNames(){

        this.setTitle(R.string.app_name)

        val a = getString(R.string.language)
        val b = getString(R.string.lang)
        findViewById<TextView>(R.id.twLang).text = "$a: $b"

        findViewById<RadioButton>(R.id.rbCentimeters).text = getString(R.string.centimeters)
        findViewById<RadioButton>(R.id.rbMeters).text = getString(R.string.meters)
        findViewById<RadioButton>(R.id.rbInches).text = getString(R.string.inches)
        findViewById<RadioButton>(R.id.rbMiles).text = getString(R.string.miles)
        findViewById<RadioButton>(R.id.rbFeet).text = getString(R.string.feet)
        findViewById<RadioButton>(R.id.rbKilometers).text = getString(R.string.kilometers)

        val df = DecimalFormat("#.####")
        df.roundingMode = RoundingMode.HALF_EVEN
        var value = findViewById<EditText>(R.id.input).text.toString().toDouble()
        var preRes = convert(currentMeasure, value, false)
        var res:Double

        res = convert(R.id.rbMeters, preRes, true)
        findViewById<TextView>(R.id.twMetres).text = "${df.format(res)}  ${getString(R.string.meters)}"

        res = convert(R.id.rbKilometers, preRes, true)
        findViewById<TextView>(R.id.twKilometers).text = "${df.format(res)}  ${getString(R.string.kilometers)}"

        res = convert(R.id.rbMiles, preRes, true)
        findViewById<TextView>(R.id.twMiles).text = "${df.format(res)}  ${getString(R.string.miles)}"

        res = convert(R.id.rbCentimeters, preRes, true)
        findViewById<TextView>(R.id.twCentimetres).text = "${df.format(res)}  ${getString(R.string.centimeters)}"

        res = convert(R.id.rbInches, preRes, true)
        findViewById<TextView>(R.id.twInches).text = "${df.format(res)}  ${getString(R.string.inches)}"

        res = convert(R.id.rbFeet, preRes, true)
        findViewById<TextView>(R.id.twFeet).text = "${df.format(res)}  ${getString(R.string.feet)}"
    }
    private fun convert(measure: Int, value: Double, fromIsFalseToIsTrue: Boolean): Double {
        val first = value.toString()
        var operator = "*"
        var second = 1.0
        when (measure) {
            R.id.rbKilometers -> {
                second = 1000.0
                operator = if (fromIsFalseToIsTrue) "/" else "*"
            }
            R.id.rbCentimeters -> {
                second = 100.0
                operator = if (fromIsFalseToIsTrue) "*" else "/"
            }
            R.id.rbFeet -> {
                second = 3.281
                operator = if (fromIsFalseToIsTrue) "*" else "/"
            }
            R.id.rbMiles -> {
                second = 1609.0
                operator = if (fromIsFalseToIsTrue) "/" else "*"
            }
            R.id.rbInches -> {
                second = 39.37
                operator = if (fromIsFalseToIsTrue) "*" else "/"
            }
        }
        val exp = "$first$operator$second"
        return ExpressionBuilder(exp).build().evaluate()
    }
}