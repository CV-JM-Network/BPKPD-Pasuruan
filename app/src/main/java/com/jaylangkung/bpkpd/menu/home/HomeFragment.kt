package com.jaylangkung.bpkpd.menu.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jaylangkung.bpkpd.R
import com.jaylangkung.bpkpd.databinding.FragmentHomeBinding
import com.jaylangkung.bpkpd.viewModel.HomeViewModel
import com.jaylangkung.bpkpd.viewModel.ViewModelFactory
import es.dmoral.toasty.Toasty
import java.util.Calendar

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

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

        binding.apply {
            viewModel.userData.observe(viewLifecycleOwner) { userData ->
                Glide.with(requireContext())
                    .load(userData.img)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(imgPhoto)

                val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                tvGreetings.text = when (currentHour) {
                    in 4..11 -> getString(R.string.greetings, "Selamat Pagi", userData.nama)
                    in 12..14 -> getString(R.string.greetings, "Selamat Siang", userData.nama)
                    in 15..17 -> getString(R.string.greetings, "Selamat Sore", userData.nama)
                    else -> getString(R.string.greetings, "Selamat Malam", userData.nama)
                }
            }

            btnSalinan.setOnClickListener {
                Toasty.info(requireContext(), "Salinan", Toasty.LENGTH_SHORT).show()
            }

            btnPbb.setOnClickListener {
                Toasty.info(requireContext(), "PBB", Toasty.LENGTH_SHORT).show()
            }

            btnSkNjop.setOnClickListener {
                Toasty.info(requireContext(), "SK NJOP", Toasty.LENGTH_SHORT).show()
            }

            btnBphtb.setOnClickListener {
                Toasty.info(requireContext(), "BPHTB", Toasty.LENGTH_SHORT).show()
            }

            btnPenagihan.setOnClickListener {
                Toasty.info(requireContext(), "Penagihan", Toasty.LENGTH_SHORT).show()
            }

            btnBphtbKolektif.setOnClickListener {
                Toasty.info(requireContext(), "BPHTB Kolektif", Toasty.LENGTH_SHORT).show()
            }

            btnNpwpd.setOnClickListener {
                Toasty.info(requireContext(), "NPWPD", Toasty.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}