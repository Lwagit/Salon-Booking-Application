package com.example.salonbookingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.salonbookingapp.utils.DatabaseHelper
import com.example.salonbookingapp.utils.PrefManager
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private var dbHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // global handler
        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            try {
                runOnUiThread { showErrorDialog("Uncaught crash", throwable) }
                Thread.sleep(2500)
            } catch (_: Exception) {}
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(10)
        }

        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_login)
            prefManager = PrefManager(this)

            // If already logged in, go directly to main activity
            if (prefManager.isLoggedIn()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return
            }

            // Try to init DB helper if present
            try {
                dbHelper = DatabaseHelper(this)
            } catch (_: Throwable) {
                dbHelper = null
            }

            val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
            val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
            val btnLogin = findViewById<Button>(R.id.btnLogin)
            val tvSignUp = findViewById<TextView>(R.id.tvSignUp)

            btnLogin.setOnClickListener {
                try {
                    val email = etEmail.text.toString().trim()
                    val password = etPassword.text.toString()

                    if (email.isEmpty() || password.isEmpty()) {
                        showMessage("Please enter email and password")
                        return@setOnClickListener
                    }

                    var userFromDb = dbHelper?.checkUser(email, password)
                    if (userFromDb != null) {
                        // Save DB user to prefs/session
                        prefManager.saveUser(userFromDb)
                        showMessage("Welcome back, ${userFromDb.name}!")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        return@setOnClickListener
                    }

                    // Fallback: check pref-stored user (signup without DB)
                    val saved = prefManager.getUser()
                    if (saved != null && saved.email == email) {
                        prefManager.setLoggedIn(true)
                        showMessage("Welcome back, ${saved.name}!")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        showMessage("❌ Invalid email or password")
                    }
                } catch (e: Exception) {
                    showErrorDialog("Login error", e)
                }
            }

            tvSignUp.setOnClickListener {
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }
        } catch (e: Exception) {
            showErrorDialog("LoginActivity onCreate error", e)
        }
    }

    private fun showMessage(msg: String) {
        android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_LONG).show()
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
            showMessage("$title: ${t.message}")
        }
    }

    private fun buildMessageFromThrowable(t: Throwable): String {
        val stack = t.stackTraceToString()
        val header = "${t::class.java.simpleName}: ${t.message ?: "no message"}"
        val truncated = if (stack.length > 3000) stack.substring(0, 3000) + "\n\n…(truncated)" else stack
        return "$header\n\n$truncated"
    }
}
