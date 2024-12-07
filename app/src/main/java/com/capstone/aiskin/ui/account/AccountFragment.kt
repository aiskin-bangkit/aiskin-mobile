package com.capstone.aiskin.ui.account

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstone.aiskin.R
import com.capstone.aiskin.databinding.FragmentAccountBinding
import com.capstone.aiskin.ui.authentication.login.LoginActivity
import com.capstone.aiskin.core.di.Injection
import com.capstone.aiskin.ui.viewmodel.MainViewModel
import com.capstone.aiskin.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private var userToken: String? = null

    private lateinit var mainViewModel: MainViewModel
    private val accountViewModel: AccountViewModel by viewModels()

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
                Log.d("AccountFragment", "${userToken}")
                userToken?.let {
                    if (accountViewModel.userData.value == null) {
                        accountViewModel.fetchUserProfile(it)
                    }
                }
            }
        }

        observeViewModel()
        return view
    }

    private fun observeViewModel() {
        // Observe User Data
        accountViewModel.userData.observe(viewLifecycleOwner) { data ->
            data?.let {
                binding.tvName.text = it.name
                Glide.with(this)
                    .load(it.imageProfile ?: R.drawable.intro_image_3)
                    .into(binding.imgUserProfile)
            }
        }

        // Observe User Data Error
        accountViewModel.userDataError.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Log.d("AccountFragment", "Error fetching user profile: $it")
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
