package com.example.salonbookingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.salonbookingapp.utils.PrefManager

class MainActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        // Global handler (best-effort) to show uncaught exceptions
        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            // Try to show dialog on UI thread before process dies
            try {
                runOnUiThread {
                    showErrorDialog("Uncaught crash", throwable)
                }
                // give user a moment to read
                Thread.sleep(3000)
            } catch (_: Exception) { /* ignore */ }
            // then kill process
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(10)
        }

        super.onCreate(savedInstanceState)

        try {
            setContentView(R.layout.activity_main)

            prefManager = PrefManager(this)

            // If not logged in, redirect to LoginActivity
            if (!prefManager.isLoggedIn()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return
            }

            setupUI()
            showHomeFragment()
        } catch (e: Exception) {
            showErrorDialog("MainActivity onCreate error", e)
        }
    }

    private fun setupUI() {
        try {
            val welcomeText = findViewById<TextView>(R.id.welcomeText)
            val logoutButton = findViewById<Button>(R.id.logoutButton)
            backButton = findViewById(R.id.backButton)

            // Show user name safely
            val user = prefManager.getUser()
            welcomeText.text = "Welcome ${user?.name ?: "User"}!"

            // Logout button
            logoutButton.setOnClickListener {
                try {
                    prefManager.logout()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    showErrorDialog("Logout error", e)
                }
            }

            // Back button behavior
            backButton.setOnClickListener { onBackPressed() }

            // Hide back button by default
            hideBackButton()
        } catch (e: Exception) {
            showErrorDialog("setupUI error", e)
        }
    }

    /** Public methods for fragments */
    fun showBackButton() {
        try {
            backButton.visibility = Button.VISIBLE
        } catch (e: Exception) {
            showErrorDialog("showBackButton error", e)
        }
    }

    fun hideBackButton() {
        try {
            backButton.visibility = Button.GONE
        } catch (e: Exception) {
            showErrorDialog("hideBackButton error", e)
        }
    }

    fun showHomeFragment() {
        try {
            val homeFragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, homeFragment)
                .commit()
            hideBackButton()
        } catch (e: Exception) {
            showErrorDialog("showHomeFragment error", e)
        }
    }

    override fun onBackPressed() {
        try {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            if (currentFragment is HomeFragment) {
                super.onBackPressed()
            } else {
                showHomeFragment()
            }
        } catch (e: Exception) {
            showErrorDialog("onBackPressed error", e)
        }
    }

    private fun showErrorDialog(title: String, t: Throwable) {
        try {
            val full = buildMessageFromThrowable(t)
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(full)
                .setPositiveButton("OK") { d, _ -> d.dismiss() }
                .show()
        } catch (ignored: Exception) {
            // fallback to toast if dialog fails
            android.widget.Toast.makeText(this, "$title: ${t.message}", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    private fun buildMessageFromThrowable(t: Throwable): String {
        val stack = t.stackTraceToString()
        val header = "${t::class.java.simpleName}: ${t.message ?: "no message"}"
        // truncate stack trace to avoid extremely long dialogs
        val truncated = if (stack.length > 4000) stack.substring(0, 4000) + "\n\n…(truncated)" else stack
        return "$header\n\n$truncated"
    }
}
