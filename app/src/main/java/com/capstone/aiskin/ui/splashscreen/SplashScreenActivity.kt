package com.capstone.aiskin.ui.splashscreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.capstone.aiskin.databinding.ActivitySplashScreenBinding
import com.capstone.aiskin.ui.intro.IntroActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
//    private lateinit var splashScreenViewModel: SplashScreenViewModel

    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAnimation()
        setupAction()
        setupView()
    }


    private fun setupAnimation() {
        val translationAnimator = ObjectAnimator.ofFloat(binding.logoFruit, View.TRANSLATION_Y, 0f, -50f).apply {
            duration = 2000
        }

        val alphaAnimator = ObjectAnimator.ofFloat(binding.logoFruit, View.ALPHA,  1f).apply {
            duration = 2000
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationAnimator, alphaAnimator)
        animatorSet.start()
    }

    private fun setupAction() {
        lifecycleScope.launch(Dispatchers.IO) {
            delay(SPLASH_TIME_OUT)
            withContext(Dispatchers.Main){
                checkUserSession()
            }
        }
    }

    private fun checkUserSession() {
        val intent = Intent(this@SplashScreenActivity, IntroActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()

//        splashScreenViewModel.getUserSession().observe(this) {
//            it?.let { userData ->
//                if (userData.accessToken.isEmpty()) {
//                    val intent = Intent(this@SplashScreenActivity, IntroActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                    startActivity(intent)
//                    finish()
//                } else {
//                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}