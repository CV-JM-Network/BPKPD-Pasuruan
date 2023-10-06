package com.jaylangkung.bpkpd.dataClass

import com.google.gson.annotations.SerializedName

data class BerkasResponse(
    @SerializedName("data") var data: List<BerkasData>,
    @SerializedName("message") var message: String,
    @SerializedName("status") var status: String,
    @SerializedName("tabel") var tabel: String,
    @SerializedName("totalData") var totalData: Int
)

data class BerkasData(
    @SerializedName("alamat_obyek_pajak") var alamatObyekPajak: String = "",
    @SerializedName("alamat_subyek_pajak") var alamatSubyekPajak: String = "",
    @SerializedName("berkas_ditolak") var berkasDitolak: String = "",
    @SerializedName("bphtb") var bphtb: String = "",
    @SerializedName("contact_person") var contactPerson: String = "",
    @SerializedName("createddate") var createddate: String = "",
    @SerializedName("desa_kel") var desaKel: String = "",
    @SerializedName("dolar") var dolar: String = "",
    @SerializedName("harga_transaksi") var hargaTransaksi: String = "",
    @SerializedName("idagenda") var idagenda: Int = 0,
    @SerializedName("idbphtb") var idbphtb: Int = 0,
    @SerializedName("idbphtb_kolektif") var idbphtbKolektif: Int = 0,
    @SerializedName("idnpwpd") var idnpwpd: Int = 0,
    @SerializedName("idpenagihan") var idpenagihan: Int = 0,
    @SerializedName("idsalinan") var idsalinan: Int = 0,
    @SerializedName("idsk_njop") var idskNjop: Int = 0,
    @SerializedName("jenis_pengurangan") var jenisPengurangan: String = "",
    @SerializedName("jenis_permohonan") var jenisPermohonan: String = "",
    @SerializedName("jumlah") var jumlah: Int = 0,
    @SerializedName("jumlah_berkas") var jumlahBerkas: Int = 0,
    @SerializedName("kecamatan") var kecamatan: String = "",
    @SerializedName("keterangan") var keterangan: String = "",
    @SerializedName("lastupdate") var lastupdate: String = "",
    @SerializedName("nama_obyek_pajak") var namaObyekPajak: String = "",
    @SerializedName("nama_wp") var namaWp: String = "",
    @SerializedName("no_ply") var noPly: String = "",
    @SerializedName("notaris") var notaris: String = "",
    @SerializedName("pembeli") var pembeli: String = "",
    @SerializedName("pengajuan") var pengajuan: String = "",
    @SerializedName("pengurangan") var pengurangan: String = "",
    @SerializedName("penjual") var penjual: String = "",
    @SerializedName("permohonan") var permohonan: String = "",
    @SerializedName("proses_berkas") var prosesBerkas: String = "",
    @SerializedName("sspd") var sspd: String = "",
    @SerializedName("tahun") var tahun: String = "",
    @SerializedName("tanggal") var tanggal: String = "",
    @SerializedName("tanggal_selesai") var tanggalSelesai: String = ""
)