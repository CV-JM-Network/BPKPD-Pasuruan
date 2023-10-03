package com.jaylangkung.bpkpd.menu.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jaylangkung.bpkpd.databinding.FragmentHomeBinding
import com.jaylangkung.bpkpd.utils.Constants
import com.jaylangkung.bpkpd.utils.MySharedPreferences
import com.jaylangkung.bpkpd.viewModel.HomeViewModel
import com.jaylangkung.bpkpd.viewModel.ViewModelFactory

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        myPreferences = MySharedPreferences(requireContext())

        val idadmin = myPreferences.getValue(Constants.USER_IDADMIN).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

        binding.apply {

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}