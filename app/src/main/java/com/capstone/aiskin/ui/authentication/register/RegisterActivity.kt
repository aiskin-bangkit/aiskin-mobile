package com.capstone.aiskin.ui.authentication.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.aiskin.ui.authentication.login.LoginActivity
import com.capstone.aiskin.databinding.ActivityRegisterBinding
import com.capstone.aiskin.ui.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setupAction()

        binding.tvLoginAction.setOnClickListener{
            goToLoginActivity()
        }
    }


    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val retypePassword = binding.retypePassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || retypePassword.isEmpty()) {
                showToast("Semua kolom harus diisi")
                return@setOnClickListener
            }

            if (password != retypePassword) {
                showToast("Password dan Retype Password tidak sama")
                return@setOnClickListener
            }

            registerViewModel.register(name, email, password)
        }

        registerViewModel.onRegisterSuccess = { response ->
            showToast("Registrasi akun anda berhasil")
            goToLoginActivity()
        }

        registerViewModel.onRegisterError = { errorMessage ->
            showToast("Registrasi gagal: $errorMessage")
        }
    }




    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}