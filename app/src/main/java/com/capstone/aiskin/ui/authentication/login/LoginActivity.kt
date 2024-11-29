package com.capstone.aiskin.ui.authentication.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.capstone.aiskin.MainActivity
import com.capstone.aiskin.databinding.ActivityLoginBinding
import com.capstone.aiskin.ui.authentication.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.tvSignInAction.setOnClickListener {
            goToRegisterActivity()
        }
        binding.btnLogin.setOnClickListener {
            goToMainActivity()
        }

    }

    private fun goToRegisterActivity() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
