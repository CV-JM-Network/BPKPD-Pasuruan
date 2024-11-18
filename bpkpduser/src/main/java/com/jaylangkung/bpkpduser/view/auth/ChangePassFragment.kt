package com.jaylangkung.bpkpduser.view.auth

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
import com.jaylangkung.bpkpduser.databinding.FragmentChangePassBinding
import com.jaylangkung.bpkpduser.utils.Transition
import com.jaylangkung.bpkpduser.utils.Utils.loadFragment
import com.jaylangkung.bpkpduser.viewmodel.AuthViewModel
import com.jaylangkung.bpkpduser.viewmodel.ViewModelFactory
import es.dmoral.toasty.Toasty

class ChangePassFragment : Fragment() {

    private lateinit var _binding: FragmentChangePassBinding
    private val binding get() = _binding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePassBinding.inflate(inflater, container, false)
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

        viewModel.startActivityEvent.observe(viewLifecycleOwner) { (key, it) ->
            if (key == viewModel.change) {
                when (it) {
                    "Password Changed" -> {
                        Toasty.success(requireContext(), "Password changed", Toasty.LENGTH_SHORT).show()
                        loadFragment(requireActivity().supportFragmentManager, LoginFragment(), R.id.auth_fragment_container, Transition.OPEN)
                    }

                    "Bad Request" -> {
                        binding.btnChangePass.hideProgress(R.string.change_password_title)
                        Toasty.error(requireContext(), "Bad request", Toasty.LENGTH_SHORT).show()
                    }

                    "Internal Server Error" -> {
                        binding.btnChangePass.hideProgress(R.string.change_password_title)
                        Toasty.error(requireContext(), "Internal server error", Toasty.LENGTH_SHORT).show()
                    }

                    else -> {
                        binding.btnChangePass.hideProgress(R.string.change_password_title)
                        Toasty.error(requireContext(), "Unknown error", Toasty.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.apply {
            bindProgressButton(btnChangePass)
            btnChangePass.setOnClickListener {
                btnChangePass.showProgress {
                    progressColor = Color.WHITE
                    buttonText = "Loading"
                }

                val email = viewModel.userEmail
                val pass = tvValueChangePass.text.toString()
                val passConfirm = tvValueChangePassConfirm.text.toString()
                if (pass.isEmpty() || passConfirm.isEmpty()) {
                    Toasty.error(requireContext(), "Password tidak boleh kosong").show()
                } else if (pass != passConfirm) {
                    Toasty.error(requireContext(), "Password tidak sama").show()
                } else {
                    viewModel.changePassword(email, pass)
                }
                btnChangePass.hideProgress(R.string.change_password_title)
            }
        }
    }

}