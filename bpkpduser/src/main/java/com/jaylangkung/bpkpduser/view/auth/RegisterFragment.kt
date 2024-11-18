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
import com.jaylangkung.bpkpduser.databinding.FragmentRegisterBinding
import com.jaylangkung.bpkpduser.model.RegisterRequest
import com.jaylangkung.bpkpduser.utils.Utils
import com.jaylangkung.bpkpduser.utils.Utils.loadFragment
import com.jaylangkung.bpkpduser.viewmodel.AuthViewModel
import com.jaylangkung.bpkpduser.viewmodel.ViewModelFactory
import es.dmoral.toasty.Toasty

class RegisterFragment : Fragment() {

    private lateinit var _binding: FragmentRegisterBinding
    private val binding get() = _binding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
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
            if (key == viewModel.register) {
                when (it) {
                    "Registered" -> {
                        Toasty.success(requireContext(), "Registrasi berhasil", Toasty.LENGTH_SHORT).show()
                        loadFragment(requireActivity().supportFragmentManager, OtpFragment(), R.id.auth_fragment_container)
                    }

                    "Bad Request" -> {
                        binding.btnRegister.hideProgress("Registrasi")
                        Toasty.error(requireContext(), "Email sudah terdaftar", Toasty.LENGTH_SHORT).show()
                    }

                    else -> {
                        binding.btnLogin.hideProgress("Registrasi")
                        Toasty.error(requireContext(), it, Toasty.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.apply {
            bindProgressButton(btnRegister)
            btnRegister.setOnClickListener {
                btnRegister.showProgress {
                    progressColor = Color.WHITE
                    buttonText = "Proses Registrasi"
                }

                viewModel.setRegisterRequest(
                    RegisterRequest(
                        email = tvValueEmailRegister.text.toString(),
                        password = tvValuePasswordRegister.text.toString(),
                        nama = tvValueNameRegister.text.toString(),
                        alamat = tvValueAddressRegister.text.toString(),
                        telpon = tvValuePhoneRegister.text.toString()
                    )
                )
                val validate = viewModel.validate()
                if (validate.isEmpty()) {
                    viewModel.register()
                } else {
                    Toasty.error(requireContext(), validate, Toasty.LENGTH_SHORT).show()
                    btnRegister.hideProgress(R.string.register_button)
                }


            }

            btnLogin.setOnClickListener {
                Utils.loadFragment(requireActivity().supportFragmentManager, LoginFragment(), R.id.auth_fragment_container)
            }
        }
    }
}