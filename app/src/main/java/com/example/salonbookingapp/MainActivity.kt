package com.example.salonbookingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.salonbookingapp.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavigationListener {

    private lateinit var backButton: Button
    private lateinit var logoutButton: Button
    private lateinit var welcomeText: TextView
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        backButton = findViewById(R.id.backButton)
        logoutButton = findViewById(R.id.logoutButton)
        welcomeText = findViewById(R.id.welcomeText)

        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        welcomeText.text = "Welcome!"

        setupUI()
        showHomeFragment()
    }

    private fun setupUI() {
        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        hideBackButton()
    }

    fun showHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment.newInstance())
            .commit()
        hideBackButton()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment is HomeFragment) {
            super.onBackPressed()
        } else {
            showHomeFragment()
        }
    }

    // --- NavigationListener methods ---
    override fun setBackButtonVisible(visible: Boolean) {
        backButton.visibility = if (visible) Button.VISIBLE else Button.GONE
    }

    override fun goToHome() {
        showHomeFragment()
    }

    fun showBackButton() {
        backButton.visibility = Button.VISIBLE
    }

    fun hideBackButton() {
        backButton.visibility = Button.GONE
    }
}
