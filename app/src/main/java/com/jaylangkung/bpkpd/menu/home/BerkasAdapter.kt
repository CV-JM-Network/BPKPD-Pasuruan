package com.jaylangkung.bpkpd.menu.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.bpkpd.R
import com.jaylangkung.bpkpd.dataClass.BerkasData
import com.jaylangkung.bpkpd.databinding.ItemBerkasBinding
import java.text.SimpleDateFormat
import java.util.Locale


class BerkasAdapter : RecyclerView.Adapter<BerkasAdapter.ItemHolder>() {

    private var list = ArrayList<BerkasData>()
    private var table = ""

    fun setItem(item: List<BerkasData>?, table: String) {
        if (item == null) return
        val prevSize = list.size
        this.list.addAll(item)
        this.table = table
        notifyItemRangeChanged(prevSize, item.size)
    }

    class ItemHolder(private val binding: ItemBerkasBinding, table: String) : RecyclerView.ViewHolder(binding.root) {
        private val table = table
        fun bind(item: BerkasData) {
            binding.apply {
                val tahun = if (item.tahun != "") item.tahun else item.createddate.substring(0, 4)
                val jumlah = if (item.jumlahBerkas != 0) item.jumlahBerkas else item.jumlah
                val tglDiterima = if (item.tanggal != "") item.tanggal else item.createddate

                tvNomorBerkas.text = itemView.context.getString(R.string.nomor_berkas, item.noPly, tahun)
                tvPermohonan.text = itemView.context.getString(R.string.permohonan, item.permohonan)
                tvTglDiterima.text = itemView.context.getString(R.string.tgl_diterima, tglDiterima)
                tvTglSelesai.text = itemView.context.getString(R.string.tgl_selesai, item.tanggalSelesai)
                tvStatusBerkas.text = itemView.context.getString(R.string.status_berkas, item.prosesBerkas)
                tvNama.text = itemView.context.getString(R.string.nama_pemohon, item.namaWp)
                tvAlamat.text = itemView.context.getString(R.string.alamat_pemohon, item.desaKel, item.kecamatan)
                tvNotaris.text = itemView.context.getString(R.string.notaris, item.notaris)
                tvNamaPembeli.text = itemView.context.getString(R.string.pembeli, item.pembeli)
                tvNamaPenjual.text = itemView.context.getString(R.string.penjual, item.penjual)
                tvHarga.text = itemView.context.getString(R.string.harga, item.hargaTransaksi)
                tvBphtb.text = itemView.context.getString(R.string.nominal_bphtb, item.bphtb)
                tvPengurangan.text = itemView.context.getString(R.string.pengurangan, item.pengurangan)
                tvJenisPengurangan.text = itemView.context.getString(R.string.jenis_pengurangan, item.jenisPengurangan)
                tvKontak.text = itemView.context.getString(R.string.kontak_pemohon, item.contactPerson)
                tvJmlPermohonan.text = itemView.context.getString(R.string.jml_permohonan, jumlah)
                if (table == "bphtb" || table == "bphtb_kolektif") {
                    layoutBpthb.visibility = View.VISIBLE
                    tvPermohonan.visibility = View.GONE
                    val constraintLayout: ConstraintLayout = tvKontak.parent as ConstraintLayout
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)
                    constraintSet.connect(tvKontak.id, ConstraintSet.TOP, layoutBpthb.id, ConstraintSet.BOTTOM, 6)
                    constraintSet.applyTo(constraintLayout)
                } else {
                    layoutDefault.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemBerkasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding, table)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}