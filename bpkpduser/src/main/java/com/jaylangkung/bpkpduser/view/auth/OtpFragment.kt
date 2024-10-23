package com.jaylangkung.bpkpduser.view.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jaylangkung.bpkpduser.databinding.FragmentOtpBinding
import com.jaylangkung.bpkpduser.viewmodel.AuthViewModel
import com.jaylangkung.bpkpduser.viewmodel.ViewModelFactory

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
            val otpFields = listOf(otp1, otp2, otp3, otp4)

            otpFields.forEachIndexed { index, editText ->
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (s.toString().isNotEmpty() && index < otpFields.size - 1) {
                            otpFields[index + 1].requestFocus()
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })
            }
        }
    }
}