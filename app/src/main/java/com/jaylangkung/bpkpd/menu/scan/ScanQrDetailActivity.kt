package com.jaylangkung.bpkpd.menu.scan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
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
import com.jaylangkung.bpkpd.databinding.BerkasDefaultBinding
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

                        if (berkas.data[0].prosesBerkas == "masuk") {
                            btnTerimaBerkas.visibility = View.VISIBLE
                            btnTerimaBerkas.setOnClickListener {
                                btnTerimaBerkas.startAnimation()
                                viewModel.terimaBerkas(idAdmin, result, tokenAuth) {
                                    btnTerimaBerkas.endAnimation()
                                    viewModel.berkasData.value?.data?.get(0)?.prosesBerkas = "dalam proses"
                                    btnTerimaBerkas.visibility = View.GONE
                                }
                            }
                        }

                        this@ScanQrDetailActivity.berkas = berkas.data[0]
                        berkasIsLoaded = true

                        if (berkasIsLoaded && berkasRiwayatIsLoaded) {
                            showLayout(tabel)
                        }
                    }
                }
                berkasRiwayatData.observe(this@ScanQrDetailActivity) { berkasRiwayat ->
                    if (berkasRiwayat != null) {
                        berkasRiwayat.data.sortedBy { it.idriwayatBerkas }
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

    private fun showLayout(tabel: String) {
        when (tabel) {
            "penagihan" -> defaultView(berkas, "Salinan")

            "agenda" -> defaultView(berkas, "PBB")

            "salinan" -> defaultView(berkas, "Penagihan")

            "sk_njop" -> defaultView(berkas, "SK NJOP")

            "bphtb" -> bphtbView(berkas)

            "bphtb_kolektif" -> bphtbKolektifView(berkas)

            "npwpd" -> defaultView(berkas, "NPWPD")

            else -> binding.tvTitle.text = getString(R.string.berkas_title, "Tidak Ditemukan")
        }
    }

    private fun defaultView(data: BerkasData, tabel: String) {
        binding.apply {
            tvTitle.text = getString(R.string.berkas_title, tabel)
            stubBerkas.layoutResource = R.layout.berkas_default
            val stubBinding = BerkasDefaultBinding.bind(stubBerkas.inflate())
            val tglDiterima = data.tanggal
            val tglSelesai = data.tanggalSelesai
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

                    else -> {
                        tvStatusBerkas.text = getString(R.string.status_berkas, "Tidak Diketahui")
                        statusBerkasProgressBar.progress = 0
                        tvStatusBerkasPersen.text = getString(R.string.status_berkas_persen, 0)
                    }
                }

                tvNama.text = getString(R.string.nama_pemohon, data.namaWp)
                tvAlamat.text = getString(R.string.alamat_pemohon, data.desaKel, data.kecamatan)
                tvKontak.text = getString(R.string.kontak_pemohon, data.contactPerson)
                if (data.contactPerson != "-" && data.prosesBerkas == "selesai") {
                    tvKontak.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_whatsapp, 0)
                    tvKontak.setOnClickListener {
                        val phone = data.contactPerson.substring(1)
                        val url =
                            "https://wa.me/62$phone?text=Halo%20${data.namaWp},%20Data%20Anda%20telah%20selesai%20diproses.%20Silahkan%20mengambil%20berkas%20di%20Kantor%20BPKPD%20Kabupaten%20Sidoarjo.%20Terima%20Kasih."
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)
                    }
                }
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

    private fun bphtbView(data: BerkasData) {
        binding.apply {
            tvTitle.text = getString(R.string.berkas_title, "BPTHB")
            stubBerkas.layoutResource = R.layout.berkas_bphtb
            val stubBinding = BerkasBphtbBinding.bind(stubBerkas.inflate())
            val tglDiterima = data.createddate
            val tglSelesai = data.tanggalSelesai
            stubBinding.apply {
                tvNomorSspd.text = getString(R.string.nomor_sspd, data.sspd, data.tahun)
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

                    else -> {
                        tvStatusBerkas.text = getString(R.string.status_berkas, "Tidak Diketahui")
                        statusBerkasProgressBar.progress = 0
                        tvStatusBerkasPersen.text = getString(R.string.status_berkas_persen, 0)
                    }
                }

                tvNotaris.text = getString(R.string.notaris, data.notaris)
                tvNamaPembeli.text = getString(R.string.pembeli, data.pembeli)
                tvNamaPenjual.text = getString(R.string.penjual, data.penjual)
                tvHarga.text = getString(R.string.harga, data.hargaTransaksi)
                tvBphtb.text = getString(R.string.nominal_bphtb, data.bphtb)
                tvPengurangan.text = getString(R.string.pengurangan, data.pengurangan)
                tvJenisPengurangan.text = getString(R.string.jenis_pengurangan, data.jenisPengurangan)
                tvKontak.text = getString(R.string.kontak_pemohon, data.contactPerson)
                tvJmlPermohonan.text = getString(R.string.jml_permohonan, data.jumlahBerkas)
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

    private fun bphtbKolektifView(data: BerkasData) {
        binding.apply {
            tvTitle.text = getString(R.string.berkas_title, "BPTHB Kolektif")
            stubBerkas.layoutResource = R.layout.berkas_bphtb_kolektif
            val stubBinding = BerkasBphtbKolektifBinding.bind(stubBerkas.inflate())
            val tglDiterima = data.createddate
            val tglSelesai = data.tanggalSelesai
            stubBinding.apply {
                val tahun = data.createddate.substring(6, 10)
                tvNomorSspd.text = getString(R.string.nomor_sspd, data.sspd, tahun)
                tvPengajuan.text = getString(R.string.pengajuan, data.pengajuan)
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

                    else -> {
                        tvStatusBerkas.text = getString(R.string.status_berkas, "Tidak Diketahui")
                        statusBerkasProgressBar.progress = 0
                        tvStatusBerkasPersen.text = getString(R.string.status_berkas_persen, 0)
                    }
                }

                tvNotaris.text = getString(R.string.notaris, data.notaris)
                tvKontak.text = getString(R.string.kontak_pemohon, data.contactPerson)
                tvJmlPermohonan.text = getString(R.string.jml_permohonan, data.jumlah)
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

}