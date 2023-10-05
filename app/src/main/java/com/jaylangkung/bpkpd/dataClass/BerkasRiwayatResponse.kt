package com.jaylangkung.bpkpd.dataClass

import com.google.gson.annotations.SerializedName

data class BerkasRiwayatResponse(
    @SerializedName("data") val data: List<BerkasRiwayatData>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String
)

data class BerkasRiwayatData(
    @SerializedName("aksi") val aksi: String,
    @SerializedName("aksi_oleh_idadmin") val aksiOlehIdadmin: Int,
    @SerializedName("createddate") val createddate: String,
    @SerializedName("idriwayat_berkas") val idriwayatBerkas: Int,
    @SerializedName("idtabel") val idtabel: Int,
    @SerializedName("json_data") val jsonData: String,
    @SerializedName("lastupdate") val lastupdate: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("nomer_surat") val nomerSurat: String,
    @SerializedName("tabel") val tabel: String
)
