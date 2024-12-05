package com.capstone.aiskin.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.capstone.aiskin.databinding.FragmentAccountBinding
import com.capstone.aiskin.ui.authentication.login.LoginActivity
import com.capstone.aiskin.core.di.Injection
import com.capstone.aiskin.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view: View = binding.root

        val authRepository = Injection.provideAuthRepository(requireContext())

        val factory = ViewModelFactory(authRepository)
        accountViewModel = ViewModelProvider(this, factory)[AccountViewModel::class.java]

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        accountViewModel.userSession.observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                navigateToLogin()
            }
        }

        return view
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
            accountViewModel.logout()
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
