package com.example.salonbookingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.salonbookingapp.data.User
import com.example.salonbookingapp.utils.DatabaseHelper
import com.example.salonbookingapp.utils.PrefManager
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private var dbHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // global handler to show crash reason (best-effort)
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
            setContentView(R.layout.activity_sign_up)
            prefManager = PrefManager(this)

            // Try to initialize DBHelper, but fail safely if not present
            try {
                dbHelper = DatabaseHelper(this)
            } catch (_: Throwable) {
                dbHelper = null
            }

            val etName = findViewById<TextInputEditText>(R.id.etName)
            val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
            val etPhone = findViewById<TextInputEditText>(R.id.etPhone)
            val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
            val etConfirmPassword = findViewById<TextInputEditText>(R.id.etConfirmPassword)
            val btnSignUp = findViewById<Button>(R.id.btnSignUp)
            val tvLogin = findViewById<TextView>(R.id.tvLogin)

            btnSignUp.setOnClickListener {
                try {
                    val name = etName.text.toString().trim()
                    val email = etEmail.text.toString().trim()
                    val phone = etPhone.text.toString().trim()
                    val password = etPassword.text.toString()
                    val confirmPassword = etConfirmPassword.text.toString()

                    if (!validateInput(name, email, phone, password, confirmPassword)) return@setOnClickListener

                    // Prefer DB if available (to get real id), else fallback to session-only
                    var user: User? = null
                    if (dbHelper != null) {
                        try {
                            if (dbHelper!!.isEmailExists(email)) {
                                showMessage("Email already registered. Please use a different email.")
                                return@setOnClickListener
                            }
                            val added = dbHelper!!.addUser(email, name, phone, password)
                            if (added) {
                                user = dbHelper!!.checkUser(email, password)
                            }
                        } catch (e: Exception) {
                            // DB failed; we will fall back to saving to PrefManager
                            showMessage("DB error — falling back to local session: ${e.message}")
                        }
                    }

                    if (user == null) {
                        // fallback user (no DB or DB failed)
                        user = User(id = "0", name = name, email = email, phone = phone)
                    }

                    prefManager.saveUser(user)
                    // setLoggedIn done inside saveUser

                    showMessage("🎉 Welcome to our salon family!")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    showErrorDialog("SignUp error", e)
                }
            }

            tvLogin.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        } catch (e: Exception) {
            showErrorDialog("SignUpActivity onCreate error", e)
        }
    }

    private fun validateInput(
        name: String, email: String, phone: String, password: String, confirmPassword: String
    ): Boolean {
        if (name.isEmpty() || name.length < 2) {
            showMessage("Please enter a valid full name")
            return false
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMessage("Please enter a valid email address")
            return false
        }
        if (phone.isEmpty() || phone.length < 10) {
            showMessage("Please enter a valid phone number")
            return false
        }
        if (password.isEmpty() || password.length < 6) {
            showMessage("Password must be at least 6 characters")
            return false
        }
        if (password != confirmPassword) {
            showMessage("Passwords do not match")
            return false
        }
        return true
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
