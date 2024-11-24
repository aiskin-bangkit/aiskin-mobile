package com.capstone.aiskin.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.aiskin.LoginActivity
import com.capstone.aiskin.R
import com.capstone.aiskin.databinding.ActivityLoginBinding
import com.capstone.aiskin.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.tvLoginAction.setOnClickListener{
            goToLoginActivity()
        }
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}