package com.capstone.aiskin.ui.intro

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.capstone.aiskin.R
import com.capstone.aiskin.databinding.ActivityIntroBinding
import com.capstone.aiskin.ui.PreviewActivity
import com.capstone.aiskin.ui.authentication.login.LoginActivity

class IntroActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }

    private lateinit var layouts: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        layouts = intArrayOf(
            R.layout.intro_layout_1,
            R.layout.intro_layout_2,
            R.layout.intro_layout_3
        )

        binding.viewPager.adapter = IntroAdapter(this, layouts)

        binding.tvSkip.setOnClickListener {
            goToPreviewActivity() // nanti ganti
        }

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            @SuppressLint("MissingSuperCall")
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> animateProgressBar(33)
                    1 -> animateProgressBar(66)
                    2 -> animateProgressBar(100)
                }

                binding.btnNext.text = getString(
                    if (position == layouts.size - 1) R.string.start else R.string.next
                )
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        binding.btnNext.setOnClickListener {
            when (binding.viewPager.currentItem) {
                layouts.size - 1 -> {
                    goToLoginActivity()
                }
                else -> {
                    binding.viewPager.currentItem += 1
                }
            }
        }
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    /**
     * Fungsi untuk dev prevActivity
     */
    private fun goToPreviewActivity() {
        startActivity(Intent(this, PreviewActivity::class.java))
        finish()
    }

    inner class IntroAdapter(private val context: Context, private val layouts: IntArray) :
        PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(layouts[position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int = layouts.size

        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    private fun animateProgressBar(toProgress: Int) {
        val animator = ObjectAnimator.ofInt(binding.progressBar, "progress", toProgress)
        animator.duration = 500
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }
}
