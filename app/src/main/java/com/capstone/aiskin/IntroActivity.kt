package com.capstone.aiskin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.capstone.aiskin.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }

    private lateinit var layouts: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        layouts = intArrayOf(
            R.layout.intro_layout_1,
            R.layout.intro_layout_2,
            R.layout.intro_layout_3
        )

        binding.viewPager.adapter = IntroAdapter(this, layouts)

        binding.tvSkip.setOnClickListener {
            goToMainActivity()
        }

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            @SuppressLint("MissingSuperCall")
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                //
            }

            override fun onPageSelected(position: Int) {
                binding.btnNext.text = getString(
                    if (position == layouts.size - 1) R.string.start else R.string.next
                )
            }

            override fun onPageScrollStateChanged(state: Int) {
                //
            }
        })

        binding.btnNext.setOnClickListener {
            when (binding.viewPager.currentItem) {
                layouts.size - 1 -> {
                    goToMainActivity()
                }
                else -> {
                    binding.viewPager.currentItem += 1
                }
            }
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
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
}
