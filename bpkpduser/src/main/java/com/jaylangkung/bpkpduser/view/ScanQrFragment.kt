package com.jaylangkung.bpkpduser.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.jaylangkung.bpkpduser.databinding.FragmentScanQrBinding
import com.jaylangkung.bpkpduser.utils.Constants
import com.jaylangkung.bpkpduser.viewmodel.AuthViewModel
import com.jaylangkung.bpkpduser.viewmodel.ViewModelFactory
import es.dmoral.toasty.Toasty

class ScanQrFragment : Fragment() {

    private var _binding: FragmentScanQrBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanQrBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory)[AuthViewModel::class.java]

        binding.apply {
            codeScanner = CodeScanner(requireContext(), scannerView).apply {
                camera = CodeScanner.CAMERA_BACK
                formats = CodeScanner.ALL_FORMATS
                autoFocusMode = AutoFocusMode.CONTINUOUS
                scanMode = ScanMode.SINGLE
                isAutoFocusEnabled = true
                isFlashEnabled = false
                startPreview()

                decodeCallback = DecodeCallback { qrString ->
                    requireActivity().runOnUiThread {
                        loadingAnim.visibility = View.VISIBLE

                        val validate = viewModel.validateQRCode(qrString.text)
                        vibrate(requireContext())
                        if (validate.isEmpty()) {
                            viewModel.loginWebApp()
                        } else {
                            Toasty.error(requireContext(), "QR Code tidak valid", Toasty.LENGTH_LONG).show()
                            loadingAnim.visibility = View.GONE
                            startPreview()
                        }

                    }
                }

                errorCallback = ErrorCallback {
                    requireActivity().runOnUiThread {
                        Toasty.error(requireContext(), "Camera initialization error: ${it.message}", Toasty.LENGTH_LONG).show()
                    }
                }
            }

            viewModel.startActivityEvent.observe(viewLifecycleOwner) { (key, it) ->
                if (key == viewModel.webapp) {
                    when (it) {
                        Constants.LOGIN -> {
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        }

                        "Internal Server Error" -> {
                            codeScanner.startPreview()
                            loadingAnim.visibility = View.GONE
                            Toasty.error(requireContext(), "Terjadi kesalahan pada server", Toasty.LENGTH_SHORT).show()
                        }

                        else -> {
                            codeScanner.startPreview()
                            loadingAnim.visibility = View.GONE
                            Toasty.error(requireContext(), it, Toasty.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun vibrate(ctx: Context) {
        val vibrator = ContextCompat.getSystemService(ctx, Vibrator::class.java) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION") vibrator.vibrate(200)
        }
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onDetach() {
        super.onDetach()
        codeScanner.releaseResources()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}