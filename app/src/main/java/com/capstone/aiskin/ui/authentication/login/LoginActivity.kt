package com.capstone.aiskin.ui.authentication.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.aiskin.MainActivity
import com.capstone.aiskin.databinding.ActivityLoginBinding
import com.capstone.aiskin.ui.authentication.register.RegisterActivity
import com.capstone.aiskin.ui.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setupAction()

        binding.tvSignInAction.setOnClickListener {
            goToRegisterActivity()
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Email dan password tidak boleh kosong.")
                return@setOnClickListener
            }

            loginViewModel.login(email, password)

            loginViewModel.loginResult.observe(this) { user ->
                if (user != null) {
                    showToast("Login berhasil, selamat datang ${user.email}")
                    goToMainActivity()
                }
            }

            loginViewModel.errorMessage.observe(this) { error ->
                if (error != null) {
                    showToast("Login gagal: $error")
                    Log.e("LoginActivity", "Error: $error")
                }
            }
        }
    }

    private fun goToRegisterActivity() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
