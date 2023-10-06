package com.jaylangkung.bpkpd.menu.scan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.bpkpd.R
import com.jaylangkung.bpkpd.dataClass.BerkasRiwayatData
import com.jaylangkung.bpkpd.databinding.ItemBerkasRiwayatBinding
import java.text.SimpleDateFormat
import java.util.Locale

class BerkasRiwayatAdapter : RecyclerView.Adapter<BerkasRiwayatAdapter.ItemHolder>() {

    private var list = ArrayList<BerkasRiwayatData>()
    fun setItem(item: List<BerkasRiwayatData>?) {
        if (item == null) return
        this.list.clear()
        this.list.addAll(item)
        notifyItemRangeChanged(0, list.size)
    }

    class ItemHolder(private val binding: ItemBerkasRiwayatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BerkasRiwayatData) {
            binding.apply {
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = dateFormatter.parse(item.createddate)
                val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
                val dateStr = date?.let { formatter.format(it) }
                tvTglRiwayat.text = dateStr
                tvNamaStaff.text = itemView.context.getString(R.string.nama_staff, item.nama)
                tvAksiRiwayat.text = itemView.context.getString(R.string.aksi_riwayat, item.aksi)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemBerkasRiwayatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}