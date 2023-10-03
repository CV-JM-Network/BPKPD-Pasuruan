package com.jaylangkung.bpkpd.dataClass

import com.google.gson.annotations.SerializedName

data class BerkasResponse(
    @SerializedName("data") val data: List<BerkasData>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("tabel") val tabel: String,
    @SerializedName("totalData") val totalData: Int
)

data class BerkasData(
    @SerializedName("alamat_obyek_pajak") val alamatObyekPajak: String,
    @SerializedName("alamat_subyek_pajak") val alamatSubyekPajak: String,
    @SerializedName("berkas_ditolak") val berkasDitolak: String,
    @SerializedName("bphtb") val bphtb: String,
    @SerializedName("contact_person") val contactPerson: String,
    @SerializedName("createddate") val createddate: String,
    @SerializedName("desa_kel") val desaKel: String,
    @SerializedName("dolar") val dolar: String,
    @SerializedName("harga_transaksi") val hargaTransaksi: String,
    @SerializedName("idagenda") val idagenda: Int,
    @SerializedName("idbphtb") val idbphtb: Int,
    @SerializedName("idbphtb_kolektif") val idbphtbKolektif: Int,
    @SerializedName("idnpwpd") val idnpwpd: Int,
    @SerializedName("idpenagihan") val idpenagihan: Int,
    @SerializedName("idsalinan") val idsalinan: Int,
    @SerializedName("idsk_njop") val idskNjop: Int,
    @SerializedName("jenis_pengurangan") val jenisPengurangan: String,
    @SerializedName("jenis_permohonan") val jenisPermohonan: String,
    @SerializedName("jumlah") val jumlah: Int,
    @SerializedName("jumlah_berkas") val jumlahBerkas: Int,
    @SerializedName("kecamatan") val kecamatan: String,
    @SerializedName("keterangan") val keterangan: String,
    @SerializedName("lastupdate") val lastupdate: String,
    @SerializedName("nama_obyek_pajak") val namaObyekPajak: String,
    @SerializedName("nama_wp") val namaWp: String,
    @SerializedName("no_ply") val noPly: String,
    @SerializedName("notaris") val notaris: String,
    @SerializedName("pembeli") val pembeli: String,
    @SerializedName("pengajuan") val pengajuan: String,
    @SerializedName("pengurangan") val pengurangan: String,
    @SerializedName("penjual") val penjual: String,
    @SerializedName("permohonan") val permohonan: String,
    @SerializedName("proses_berkas") val prosesBerkas: String,
    @SerializedName("sspd") val sspd: String,
    @SerializedName("tahun") val tahun: String,
    @SerializedName("tanggal") val tanggal: String,
    @SerializedName("tanggal_selesai") val tanggalSelesai: String
)