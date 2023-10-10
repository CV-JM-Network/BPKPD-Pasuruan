package com.jaylangkung.bpkpd.menu.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.jaylangkung.bpkpd.BuildConfig
import com.jaylangkung.bpkpd.MainActivity
import com.jaylangkung.bpkpd.R
import com.jaylangkung.bpkpd.auth.LoginActivity
import com.jaylangkung.bpkpd.databinding.FragmentSettingBinding
import com.jaylangkung.bpkpd.utils.FileUtils
import com.jaylangkung.bpkpd.viewModel.SettingViewModel
import com.jaylangkung.bpkpd.viewModel.ViewModelFactory
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import es.dmoral.toasty.Toasty
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[SettingViewModel::class.java]

        binding.apply {
            viewModel.userData.observe(viewLifecycleOwner) { userData ->
                Glide.with(requireContext())
                    .load(userData.img)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(imgProfile)

                tvValueNameEdit.setText(userData.nama)
                tvValueAddressEdit.setText(userData.alamat)
                tvValuePhoneEdit.setText(userData.telp)
            }

            appVersion.text = getString(R.string.app_ver, BuildConfig.VERSION_NAME)

            btnChangeImg.setOnClickListener {
                ImagePicker.with(requireActivity()).apply {
                    cropSquare()
                    compress(1024)
                    maxResultSize(1080, 1080)
                    galleryMimeTypes(arrayOf("image/png", "image/jpg", "image/jpeg"))

                    createIntent {
                        startForProfileImageResult.launch(it)
                    }
                }
            }

            btnSave.setOnClickListener {
                val idAdmin = viewModel.userData.value?.idadmin.toString()
                val nama = tvValueNameEdit.text.toString()
                val alamat = tvValueAddressEdit.text.toString()
                val telp = tvValuePhoneEdit.text.toString()
                var img: MultipartBody.Part? = null
                viewModel.photoUri?.let {
                    val file = FileUtils.getFile(requireContext(), it)
                    val requestBodyPhoto = file?.asRequestBody(requireActivity().contentResolver.getType(it).toString().toMediaTypeOrNull())
                    img = requestBodyPhoto?.let { it1 -> MultipartBody.Part.createFormData("img", file.name, it1) }
                }
                viewModel.updateProfile(
                    idAdmin.toRequestBody(MultipartBody.FORM),
                    nama.toRequestBody(MultipartBody.FORM),
                    alamat.toRequestBody(MultipartBody.FORM),
                    telp.toRequestBody(MultipartBody.FORM),
                    img
                ) { result ->
                    if (result) {
                        Toasty.success(requireContext(), "Berhasil Update Profile", Toasty.LENGTH_SHORT).show()
                    } else {
                        Toasty.error(requireContext(), "Gagal Update Profile", Toasty.LENGTH_SHORT).show()
                    }
                    btnSave.endAnimation()
                }
                btnSave.startAnimation()
            }

            btnLogout.setOnClickListener {
                val dialog = MaterialDialog.Builder(requireActivity())
                    .setTitle("Logout")
                    .setMessage(getString(R.string.confirm_logout))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.no), R.drawable.ic_close) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton(getString(R.string.yes), R.drawable.ic_logout) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        viewModel.logout()
                        Toasty.info(requireContext(), "Berhasil Logout", Toasty.LENGTH_SHORT).show()
                        activity?.let {
                            it.startActivity(Intent(it, LoginActivity::class.java))
                            it.finish()
                        }
                    }
                    .build()
                dialog.show()
            }
        }

        return binding.root
    }

    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                viewModel.photoUri = fileUri
                binding.imgProfile.setImageURI(fileUri)
            }

            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }

            else -> {
                Log.d("Cancel image picking", "Task Cancelled")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}