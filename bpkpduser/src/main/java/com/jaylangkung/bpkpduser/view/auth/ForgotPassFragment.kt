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
import com.jaylangkung.bpkpduser.databinding.FragmentForgotPassBinding
import com.jaylangkung.bpkpduser.utils.Transition
import com.jaylangkung.bpkpduser.utils.Utils
import com.jaylangkung.bpkpduser.viewmodel.AuthViewModel
import com.jaylangkung.bpkpduser.viewmodel.ViewModelFactory
import es.dmoral.toasty.Toasty

class ForgotPassFragment : Fragment() {

    private lateinit var _binding: FragmentForgotPassBinding
    private val binding get() = _binding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPassBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory)[AuthViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            if (key == viewModel.forgot) {
                when (it) {
                    "Reset Code Sent" -> {
                        Toasty.success(requireContext(), "Reset code sent", Toasty.LENGTH_SHORT).show()
                        val otpFragment = OtpFragment().apply {
                            arguments = Bundle().apply {
                                putString(OtpFragment.TAG, "forgotOtp")
                            }
                        }
                        Utils.loadFragment(requireActivity().supportFragmentManager, otpFragment, R.id.auth_fragment_container, Transition.OPEN)
                    }

                    "Bad Request" -> {
                        binding.btnSendResetCode.hideProgress(R.string.send_reset_code)
                        Toasty.error(requireContext(), "Bad request", Toasty.LENGTH_SHORT).show()
                    }

                    "Internal Server Error" -> {
                        binding.btnSendResetCode.hideProgress(R.string.send_reset_code)
                        Toasty.error(requireContext(), "Internal server error", Toasty.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.apply {
            bindProgressButton(btnSendResetCode)
            btnSendResetCode.setOnClickListener {
                btnSendResetCode.showProgress {
                    progressColor = Color.WHITE
                    buttonText = "Loading"
                }
                val email = tvValueEmailForgot.text.toString()
                if (email.isNotEmpty()) {
                    viewModel.userEmail = email
                    viewModel.forgotPassword(email)
                } else {
                    Toasty.error(requireContext(), "Email tidak boleh kosong", Toasty.LENGTH_SHORT).show()
                    btnSendResetCode.hideProgress(R.string.send_reset_code)
                }
            }
        }
    }
}