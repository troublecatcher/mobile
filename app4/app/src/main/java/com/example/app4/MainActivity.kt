package com.example.app4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app4.databinding.ActivityMainBinding
import db.MyDbManager

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val myDbManager = MyDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().add(R.id.notesPlaceholder, notesFragment.newInstance()).commit()

    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }
}
