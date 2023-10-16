package com.jaylangkung.bpkpd.dataClass


import com.google.gson.annotations.SerializedName

data class TerimaBerkasResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: TerimaBerkasData,
    @SerializedName("nomer_surat") val nomerSurat: String
)

data class TerimaBerkasData(
    @SerializedName("message") val message: String,
    @SerializedName("nama_wp") val namaWp: String,
    @SerializedName("proses_berkas") val prosesBerkas: String,
    @SerializedName("tabel") val tabel: String,
    @SerializedName("idtabel") val idtabel: Int
)