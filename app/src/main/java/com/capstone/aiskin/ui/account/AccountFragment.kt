package com.capstone.aiskin.ui.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.aiskin.R
import com.capstone.aiskin.core.adapter.LikedArticleAdapter
import com.capstone.aiskin.databinding.FragmentAccountBinding
import com.capstone.aiskin.ui.authentication.login.LoginActivity
import com.capstone.aiskin.core.di.Injection
import com.capstone.aiskin.ui.detail.article.DetailArticleActivity
import com.capstone.aiskin.ui.viewmodel.MainViewModel
import com.capstone.aiskin.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private var userToken: String? = null

    private lateinit var mainViewModel: MainViewModel
    private val accountViewModel: AccountViewModel by viewModels()

    private val likedArticleViewModel: LikedArticleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view: View = binding.root

        val authRepository = Injection.provideAuthRepository(requireContext())

        val factory = ViewModelFactory(authRepository)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        mainViewModel.userSession.observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                navigateToLogin()
            } else{
                userToken = user.token
                userToken?.let {
                    if (accountViewModel.userData.value == null) {
                        accountViewModel.fetchUserProfile(it)
                    }
                }
            }
        }
        setupRecyclerView()
        observeLikedArticles()
        observeViewModel()
        return view
    }

    private fun observeViewModel() {
        accountViewModel.userData.observe(viewLifecycleOwner) { data ->
            data?.let {
                binding.tvName.text = it.name
                Glide.with(this)
                    .load(it.imageProfile ?: R.drawable.intro_image_3)
                    .into(binding.imgUserProfile)
            }
        }


        // Observe Loading State
        accountViewModel.isUserDataLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.imgUserProfile.visibility = View.GONE
                binding.layoutUserInformation.visibility = View.GONE
                binding.shimmerUserProfile.visibility = View.VISIBLE
            } else {
                binding.imgUserProfile.visibility = View.VISIBLE
                binding.layoutUserInformation.visibility = View.VISIBLE
                binding.shimmerUserProfile.visibility = View.GONE            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeLikedArticles() {
        likedArticleViewModel.likedArticles.observe(viewLifecycleOwner) { articles ->
            binding.textTotalLikedArticle.text = articles.size.toString()

            if (articles.isNotEmpty()) {
                binding.rvLikedArticle.visibility = View.VISIBLE
                binding.rvLikedArticle.adapter = LikedArticleAdapter(articles) { articleId ->
                    val intent = Intent(requireContext(), DetailArticleActivity::class.java).apply {
                        putExtra("ARTICLE_ID", articleId)
                    }
                    startActivity(intent)
                }
                binding.layoutNoLikedArticle.visibility = View.GONE
            } else {
                binding.rvLikedArticle.visibility = View.GONE
                binding.layoutNoLikedArticle.visibility = View.VISIBLE
            }
        }
    }



    private fun setupRecyclerView() {
        binding.rvLikedArticle.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLikedArticle.setHasFixedSize(true)
    }

    private fun showLogoutDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin logout?")
            .setPositiveButton("Ya") { _, _ ->
                logoutUser()
            }
            .setNegativeButton("Tidak", null)
            .create()
        dialog.show()
    }

    private fun logoutUser() {
        lifecycleScope.launch {
            mainViewModel.logout()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}