package com.jaylangkung.bpkpduser.view.auth

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.jaylangkung.bpkpduser.R
import com.jaylangkung.bpkpduser.databinding.FragmentOtpBinding
import com.jaylangkung.bpkpduser.viewmodel.AuthViewModel
import com.jaylangkung.bpkpduser.viewmodel.ViewModelFactory
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OtpFragment : Fragment() {

    private lateinit var _binding: FragmentOtpBinding
    private val binding get() = _binding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
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

        binding.apply {
            bindProgressButton(btnSubmitOtp)
            val otpFields = listOf(otp1, otp2, otp3, otp4)
            val code = StringBuilder()

            otpFields.forEachIndexed { index, editText ->
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (s.toString().isNotEmpty() && index < otpFields.size - 1) {
                            otpFields[index + 1].requestFocus()
                        } else if (s.toString().isEmpty() && index > 0) {
                            otpFields[index - 1].requestFocus()
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        code.clear()
                        otpFields.forEach {
                            code.append(it.text.toString())
                        }

                        // performClick() when it's the last field
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(2000) // Delay for 2 seconds
                        }
                        if (s.toString().isNotEmpty() && index == otpFields.size - 1) {
                            btnSubmitOtp.performClick()
                        }

                    }
                })
            }

            viewModel.startActivityEvent.observe(viewLifecycleOwner) { (key, it) ->
                if (key == viewModel.confirmed) {
                    when (it) {
                        "Confirmed" -> {
                            Toasty.success(requireContext(), "Kode Konfirmasi Valid", Toasty.LENGTH_SHORT).show()
                            requireActivity().supportFragmentManager.beginTransaction().setReorderingAllowed(true).replace(R.id.auth_fragment_container, LoginFragment())
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                        }

                        "Bad Request" -> {
                            btnSubmitOtp.hideProgress("Konfirmasi")
                            Toasty.error(requireContext(), "Kode Konfirmasi Tidak Valid", Toasty.LENGTH_SHORT).show()
                        }

                        else -> {
                            btnSubmitOtp.hideProgress("Konfirmasi")
                            Toasty.error(requireContext(), it, Toasty.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            btnSubmitOtp.setOnClickListener {
                if (code.isEmpty()) {
                    Toasty.error(requireContext(), "Kode Konfirmasi Tidak Boleh Kosong", Toasty.LENGTH_SHORT).show()
                    btnSubmitOtp.hideProgress("Konfirmasi")
                    return@setOnClickListener
                }
                btnSubmitOtp.showProgress {
                    progressColor = Color.WHITE
                    buttonText = "Proses Konfirmasi"
                }
                viewModel.confirmRegister(code.toString())
            }
        }
    }
}