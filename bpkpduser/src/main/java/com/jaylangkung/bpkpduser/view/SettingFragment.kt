package com.jaylangkung.bpkpduser.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.jaylangkung.bpkpduser.R
import com.jaylangkung.bpkpduser.databinding.FragmentSettingBinding
import com.jaylangkung.bpkpduser.viewmodel.SettingViewModel
import com.jaylangkung.bpkpduser.viewmodel.ViewModelFactory

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

        // Set the windowSoftInputMode of the parent Activity to adjustPan
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding.apply {
            // bindProgressButton(btnSave)

            viewModel.userData.observe(viewLifecycleOwner) { userData ->
                 Glide.with(requireContext())
                     .load(userData.img)
                     .placeholder(R.drawable.ic_profile)
                     .error(R.drawable.ic_profile)
                     .into(binding.imgProfile)

                tvValueNameEdit.setText(userData.nama)
                tvValueAddressEdit.setText(userData.alamat)
                tvValuePhoneEdit.setText(userData.telpon)
            }
        }

        return binding.root
    }

//    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//        val resultCode = result.resultCode
//        val data = result.data
//        when (resultCode) {
//            Activity.RESULT_OK -> {
//                //Image Uri will not be null for RESULT_OK
//                val fileUri = data?.data!!
//                viewModel.photoUri = fileUri
//                binding.imgProfile.setImageURI(fileUri)
//            }
//
//            ImagePicker.RESULT_ERROR -> {
//                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//            }
//
//            else -> {
//                Log.d("Cancel image picking", "Task Cancelled")
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}