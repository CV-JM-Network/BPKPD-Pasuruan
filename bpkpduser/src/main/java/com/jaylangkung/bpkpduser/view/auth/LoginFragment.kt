package com.jaylangkung.bpkpduser.view.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.jaylangkung.bpkpduser.R
import com.jaylangkung.bpkpduser.databinding.FragmentLoginBinding
import com.jaylangkung.bpkpduser.model.LoginRequest
import com.jaylangkung.bpkpduser.utils.Constants
import com.jaylangkung.bpkpduser.view.MainActivity
import com.jaylangkung.bpkpduser.viewmodel.AuthViewModel
import com.jaylangkung.bpkpduser.viewmodel.ViewModelFactory
import es.dmoral.toasty.Toasty

class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private val binding get() = _binding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory)[AuthViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    requireActivity().finish()
                }
            }
        })

        viewModel.startActivityEvent.observe(viewLifecycleOwner) {
            when (it) {
                Constants.LOGIN -> {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }

                "Unauthorized" -> {
                    binding.btnLogin.hideProgress("Login")
                    Toasty.error(requireContext(), "Email atau kata sandi salah", Toasty.LENGTH_SHORT).show()
                }

                "Internal Server Error" -> {
                    binding.btnLogin.hideProgress("Login")
                    Toasty.error(requireContext(), "Terjadi kesalahan pada server", Toasty.LENGTH_SHORT).show()
                }

                else -> {
                    binding.btnLogin.hideProgress("Login")
                    Toasty.error(requireContext(), it, Toasty.LENGTH_SHORT).show()
                }
            }
        }

        binding.apply {
            bindProgressButton(btnLogin)
            btnLogin.setOnClickListener {
                val email = tvValueEmailLogin.text.toString()
                val password = tvValuePasswordLogin.text.toString()
                viewModel.setLoginRequest(
                    LoginRequest(email, password)
                )
                val validate = viewModel.validate()
                if (validate.isEmpty()) {
                    viewModel.login()
                    btnLogin.showProgress {
                        progressColor = Color.WHITE
                        buttonText = "Proses Login"
                    }
                } else {
                    btnLogin.hideProgress(R.string.login_button)
                }
            }

            btnRegister.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction().setReorderingAllowed(true).replace(com.jaylangkung.bpkpduser.R.id.auth_fragment_container, RegisterFragment())
                    .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
            }
        }
    }
}