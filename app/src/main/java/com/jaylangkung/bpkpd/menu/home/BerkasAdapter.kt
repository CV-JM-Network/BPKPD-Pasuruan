package com.jaylangkung.bpkpd.menu.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.bpkpd.dataClass.BerkasData
import com.jaylangkung.bpkpd.databinding.ItemBerkasBinding

class BerkasAdapter : RecyclerView.Adapter<BerkasAdapter.ItemHolder>() {

    private var list = ArrayList<BerkasData>()
    fun setItem(item: List<BerkasData>?) {
        if (item == null) return
        val prevSize = list.size
        this.list.addAll(item)
        notifyItemRangeChanged(prevSize, item.size)
    }

    class ItemHolder(private val binding: ItemBerkasBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BerkasData) {
            binding.apply {
                tvNomorBerkas.text = item.noPly
//                val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//                val date = dateFormatter.parse(item.createddate)
//                val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
//                val dateStr = date?.let { formatter.format(it) }
//                tvTglRiwayat.text = dateStr
//                tvNamaStaff.text = itemView.context.getString(R.string.nama_staff, item.nama)
//                tvAksiRiwayat.text = itemView.context.getString(R.string.aksi_riwayat, item.aksi)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemBerkasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}