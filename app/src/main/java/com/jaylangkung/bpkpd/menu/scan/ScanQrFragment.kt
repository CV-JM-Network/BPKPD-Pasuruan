package com.jaylangkung.bpkpd.menu.scan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.jaylangkung.bpkpd.MainActivity
import com.jaylangkung.bpkpd.databinding.FragmentScanQrBinding
import com.jaylangkung.bpkpd.utils.ErrorHandler
import com.jaylangkung.bpkpd.viewModel.ScanQrViewModel
import com.jaylangkung.bpkpd.viewModel.ViewModelFactory
import es.dmoral.toasty.Toasty

class ScanQrFragment : Fragment() {
    private var _binding: FragmentScanQrBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ScanQrViewModel
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanQrBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[ScanQrViewModel::class.java]

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
                        viewModel.validateQRCode(qrString.text) { result ->
                            when (result) {
                                "webapp_success" -> {
                                    val intent = Intent(requireContext(), MainActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }

                                "webapp_failure" -> {
                                    loadingAnim.visibility = View.GONE
                                    startPreview()
                                }

                                "invalid_qr_code" -> {
                                    loadingAnim.visibility = View.GONE
                                    Toasty.error(requireContext(), "QR Code tidak valid", Toasty.LENGTH_LONG).show()
                                    startPreview()
                                }

                                else -> {
                                    startActivity(Intent(requireContext(), ScanQrDetailActivity::class.java))
                                    requireActivity().finish()
                                }
                            }
                        }
                    }
                }
                errorCallback = ErrorCallback {
                    requireActivity().runOnUiThread {
                        ErrorHandler().responseHandler(
                            requireContext(), "codeScanner | errorCallback", it.message.toString()
                        )
                        Toasty.error(requireContext(), it.message.toString(), Toasty.LENGTH_LONG).show()
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}