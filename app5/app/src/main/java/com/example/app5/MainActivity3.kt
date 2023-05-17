package com.example.app5

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.app5.databinding.ActivityMain4Binding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class MainActivity3 : AppCompatActivity() {
    private val db = FirebaseDatabase.getInstance().getReference("User")
    val storage = FirebaseStorage.getInstance()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain4Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_weather, R.id.nav_music
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val login: String? = intent.getStringExtra("user")

        db.child(login!!).get().addOnSuccessListener{
            if(it.exists()){
                findViewById<TextView>(R.id.navbarLOGIN).text = login
                findViewById<TextView>(R.id.navbarNAME).text = it.child("name").value as CharSequence?
                storage.reference.child("images/"+login).downloadUrl.addOnSuccessListener {
                    uri ->
                    Picasso.with(this).load(uri).into(findViewById<ImageView>(R.id.navbarIMG))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity3, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_logout){
            val i = Intent(this@MainActivity3, MainActivity::class.java)
            i.removeExtra("user")
            startActivity(i)
            Toast.makeText(this, "Вы вышли", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}