package com.example.app5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private val db = FirebaseDatabase.getInstance().getReference("User")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.loginBTNSignin).setOnClickListener{
            var login = findViewById<EditText>(R.id.loginETLogin)
            var password = findViewById<EditText>(R.id.loginETPassword)
            db.child(login.text.toString()).get().addOnSuccessListener{
                if(it.exists()){
                    if(password.text.toString() == it.child("pwd").value){
                        val i = Intent(this@MainActivity, MainActivity3::class.java)
                        i.putExtra("user", login.text.toString())
                        startActivity(i)
                        Toast.makeText(this, "Добро пожаловать!", Toast.LENGTH_SHORT).show()
                    }else Toast.makeText(this, "Пароль неверный", Toast.LENGTH_SHORT).show()
                }else Toast.makeText(this, "Логин неверный", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<Button>(R.id.loginBTNSignup).setOnClickListener{
            val i = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(i)
        }
    }
}