package com.jaylangkung.bpkpd.menu.scan

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaylangkung.bpkpd.MainActivity
import com.jaylangkung.bpkpd.R
import com.jaylangkung.bpkpd.dataClass.BerkasData
import com.jaylangkung.bpkpd.dataClass.BerkasRiwayatData
import com.jaylangkung.bpkpd.databinding.ActivityScanQrDetailBinding
import com.jaylangkung.bpkpd.databinding.BerkasBphtbBinding
import com.jaylangkung.bpkpd.databinding.BerkasBphtbKolektifBinding
import com.jaylangkung.bpkpd.databinding.BerkasNpwpdBinding
import com.jaylangkung.bpkpd.databinding.BerkasPbbBinding
import com.jaylangkung.bpkpd.databinding.BerkasPenagihanBinding
import com.jaylangkung.bpkpd.databinding.BerkasSalinanBinding
import com.jaylangkung.bpkpd.databinding.BerkasSknjopBinding
import com.jaylangkung.bpkpd.utils.Constants
import com.jaylangkung.bpkpd.utils.MySharedPreferences
import com.jaylangkung.bpkpd.viewModel.ScanQrViewModel
import com.jaylangkung.bpkpd.viewModel.ViewModelFactory

class ScanQrDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanQrDetailBinding
    private lateinit var viewModel: ScanQrViewModel
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var adapter: BerkasRiwayatAdapter

    private lateinit var berkas: BerkasData
    private lateinit var berkasRiwayat: List<BerkasRiwayatData>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanQrDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this@ScanQrDetailActivity, factory)[ScanQrViewModel::class.java]
        myPreferences = MySharedPreferences(this@ScanQrDetailActivity)
        adapter = BerkasRiwayatAdapter()
        berkas = BerkasData()
        berkasRiwayat = emptyList()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(
                    Intent(this@ScanQrDetailActivity, MainActivity::class.java).putExtra(MainActivity.EXTRA_FRAGMENT, "scan")
                )
                finish()
            }
        })

        binding.apply {
            val idAdmin = myPreferences.getValue(Constants.USER_IDADMIN).toString()
            val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
            val result = intent.getStringExtra("result").toString()

            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            viewModel.apply {
                getBerkas(idAdmin, result, tokenAuth)
                getRiwayatBerkas(idAdmin, result, tokenAuth)
                var tabel = ""
                var berkasIsLoaded = false
                var berkasRiwayatIsLoaded = false
                berkasData.observe(this@ScanQrDetailActivity) { berkas ->
                    if (berkas != null) {
                        tabel = berkas.tabel
                        this@ScanQrDetailActivity.berkas = berkas.data[0]
                        berkasIsLoaded = true

                        if (berkasIsLoaded && berkasRiwayatIsLoaded) {
                            showLayout(tabel)
                        }
                    }
                }
                berkasRiwayatData.observe(this@ScanQrDetailActivity) { berkasRiwayat ->
                    if (berkasRiwayat != null) {
                        berkasRiwayat.data.sortedByDescending { it.idriwayatBerkas }
                        this@ScanQrDetailActivity.berkasRiwayat = berkasRiwayat.data
                        adapter.setItem(berkasRiwayat.data)
                        adapter.notifyItemRangeChanged(0, berkasRiwayat.data.size)
                        berkasRiwayatIsLoaded = true

                        if (berkasIsLoaded && berkasRiwayatIsLoaded) {
                            showLayout(tabel)
                        }
                    }
                }


            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showLayout(tabel: String) {
        when (tabel) {
            "penagihan" -> salinanView(berkas)

            "pbb" -> pbbView(berkas)

            "salinan" -> penagihanView(berkas)

            "sk_njop" -> sknjopView(berkas)

            "bphtb" -> bphtbView(berkas)

            "bphtb_kolektif" -> bphtbKolektifView(berkas)

            "npwpd" -> npwpdView(berkas)

            else -> binding.tvTitle.text = getString(R.string.berkas_title, tabel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun salinanView(data: BerkasData) {
        binding.apply {
            tvTitle.text = getString(R.string.berkas_title, "Salinan")
            stubBerkas.layoutResource = R.layout.berkas_salinan
            val stubBinding = BerkasSalinanBinding.bind(stubBerkas.inflate())
            val tglDiterima = viewModel.convertDate(data.tanggal)
            val tglSelesai = viewModel.convertDate(data.tanggalSelesai)
            stubBinding.apply {
                tvNomorBerkas.text = getString(R.string.nomor_berkas, data.noPly, data.tahun)
                tvPermohonan.text = getString(R.string.permohonan, data.permohonan)
                tvTglDiterima.text = getString(R.string.tgl_diterima, tglDiterima)
                tvTglSelesai.text = getString(R.string.tgl_selesai, tglSelesai)
                when (data.prosesBerkas) {
                    "masuk" -> {
                        tvStatusBerkas.text = getString(R.string.status_berkas, "Masuk")
                        statusBerkasProgressBar.progress = 0
                        tvStatusBerkasPersen.text = getString(R.string.status_berkas_persen, 0)
                    }

                    "dalam proses" -> {
                        tvStatusBerkas.text = getString(R.string.status_berkas, "Dalam Proses")
                        statusBerkasProgressBar.progress = 50
                        tvStatusBerkasPersen.text = getString(R.string.status_berkas_persen, 50)
                    }

                    "kurang lengkap" -> {
                        tvStatusBerkas.text = getString(R.string.status_berkas, "Kurang Lengkap")
                        statusBerkasProgressBar.progress = 75
                        tvStatusBerkasPersen.text = getString(R.string.status_berkas_persen, 75)
                    }

                    "selesai" -> {
                        tvStatusBerkas.text = getString(R.string.status_berkas, "Selesai")
                        statusBerkasProgressBar.progress = 100
                        tvStatusBerkasPersen.text = getString(R.string.status_berkas_persen, 100)
                    }

                    "ditolak" -> {
                        tvStatusBerkas.text = getString(R.string.status_berkas, "Ditolak")
                        statusBerkasProgressBar.progress = 100
                        tvStatusBerkasPersen.text = getString(R.string.status_berkas_persen, 100)
                        tvDitolak.visibility = View.VISIBLE
                        tvDitolak.text = getString(R.string.ditolak, data.berkasDitolak)
                    }

                    else -> tvStatusBerkas.text = getString(R.string.status_berkas, "Masuk")
                }

                tvNama.text = getString(R.string.nama_pemohon, data.namaWp)
                tvAlamat.text = getString(R.string.alamat_pemohon, data.desaKel, data.kecamatan)
                tvKontak.text = getString(R.string.kontak_pemohon, data.contactPerson)
                tvJmlPermohonan.text = getString(R.string.jml_permohonan, data.jumlah)
                if (data.keterangan.isNotEmpty()) {
                    txtCatatan.visibility = View.VISIBLE
                    tvCatatan.visibility = View.VISIBLE
                    tvCatatan.text = data.keterangan
                }

                if (berkasRiwayat.isNotEmpty()) {
                    rvBerkasRiwayat.apply {
                        visibility = View.VISIBLE
                        layoutManager = LinearLayoutManager(this@ScanQrDetailActivity)
                        itemAnimator = DefaultItemAnimator()
                        setHasFixedSize(true)
                        adapter = this@ScanQrDetailActivity.adapter
                    }
                } else {
                    rvBerkasRiwayat.visibility = View.GONE
                    empty.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun pbbView(data: BerkasData) {
        binding.apply {
            tvTitle.text = getString(R.string.berkas_title, "PBB")
            stubBerkas.layoutResource = R.layout.berkas_pbb
            val stubBinding = BerkasPbbBinding.bind(stubBerkas.inflate())
            stubBinding.apply {
                tvNomorBerkas.text = getString(R.string.nomor_berkas, data.noPly, data.tahun)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun penagihanView(data: BerkasData) {
        binding.apply {
            tvTitle.text = getString(R.string.berkas_title, "Penagihan")
            stubBerkas.layoutResource = R.layout.berkas_penagihan
            val stubBinding = BerkasPenagihanBinding.bind(stubBerkas.inflate())
            val tglDiterima = viewModel.convertDate(data.tanggal)
            val tglSelesai = viewModel.convertDate(data.tanggalSelesai)
            stubBinding.apply {
                tvNomorBerkas.text = getString(R.string.nomor_berkas, data.noPly, data.tahun)
                tvTglDiterima.text = getString(R.string.tgl_diterima, tglDiterima)
                tvTglSelesai.text = getString(R.string.tgl_selesai, tglSelesai)
            }
        }
    }

    private fun sknjopView(data: BerkasData) {
        binding.apply {
            tvTitle.text = getString(R.string.berkas_title, "SK NJOP")
            stubBerkas.layoutResource = R.layout.berkas_sknjop
            val stubBinding = BerkasSknjopBinding.bind(stubBerkas.inflate())
            stubBinding.apply {
                tvNomorBerkas.text = getString(R.string.nomor_berkas, data.noPly, data.tahun)

            }
        }
    }

    private fun bphtbView(data: BerkasData) {
        binding.apply {
            tvTitle.text = getString(R.string.berkas_title, "BPTHB")
            stubBerkas.layoutResource = R.layout.berkas_bphtb
            val stubBinding = BerkasBphtbBinding.bind(stubBerkas.inflate())
            stubBinding.apply {
                tvNomorBerkas.text = getString(R.string.nomor_berkas, data.noPly, data.tahun)

            }
        }
    }

    private fun bphtbKolektifView(data: BerkasData) {
        binding.apply {
            tvTitle.text = getString(R.string.berkas_title, "BPTHB Kolektif")
            stubBerkas.layoutResource = R.layout.berkas_bphtb_kolektif
            val stubBinding = BerkasBphtbKolektifBinding.bind(stubBerkas.inflate())
            stubBinding.apply {
                tvNomorBerkas.text = getString(R.string.nomor_berkas, data.noPly, data.tahun)

            }
        }
    }

    private fun npwpdView(data: BerkasData) {
        binding.apply {
            tvTitle.text = getString(R.string.berkas_title, "NPWPD")
            stubBerkas.layoutResource = R.layout.berkas_npwpd
            val stubBinding = BerkasNpwpdBinding.bind(stubBerkas.inflate())
            stubBinding.apply {
                tvNomorBerkas.text = getString(R.string.nomor_berkas, data.noPly, data.tahun)

            }
        }
    }
}