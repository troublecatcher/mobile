package com.example.app4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app4.databinding.ActivityMainBinding
import db.DatabaseManager

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val DatabaseManager = DatabaseManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().add(R.id.fragmentsPlaceholder, NotesFragment.newInstance()).commit()
    }

    override fun onResume() {
        super.onResume()
        DatabaseManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        DatabaseManager.closeDb()
    }
}
