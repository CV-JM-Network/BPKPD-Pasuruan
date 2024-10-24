package com.jaylangkung.bpkpduser.utils

import android.content.Context
import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.setCustomKeys
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty
import org.json.JSONObject
import java.util.Calendar
import java.util.Date

class CustomHandler {

    private val crashlytics = Firebase.crashlytics

    fun responseHandler(context: Context, func: String, message: String = "", code: Int = 200) {
        val now: Date = Calendar.getInstance().time
        val ctx = context.toString()
        when {
            message.contains("failed to connect to") -> {
                Toasty.error(context, "Terdapat permasalahan pada server", Toasty.LENGTH_LONG).show()
                crashlytics.recordException(Exception("Server connection failed"))
            }

            message.contains("Unable to resolve host") -> {
                Toasty.error(context, "Silahkan cek koneksi internet Anda", Toasty.LENGTH_LONG).show()
                crashlytics.log("Unable to resolve host in $func, context: $ctx")
                crashlytics.recordException(Exception("Unable to resolve host"))
            }

            message.contains("Forbidden") -> {
                Toasty.error(context, "File yang Anda upload tidak didukung", Toasty.LENGTH_LONG).show()
                crashlytics.log("Forbidden file upload in $func, context: $ctx")
            }

            message.contains("Unauthorized") -> {
                crashlytics.setCustomKeys {
                    key("context", ctx)
                    key("function", func)
                    key("message", message)
                }
            }

            code == 400 -> {
                Toasty.error(context, "Bad request", Toasty.LENGTH_LONG).show()
                crashlytics.recordException(Exception("Bad request, context: $ctx"))
            }

            code == 401 -> {
                Toasty.error(context, "Email atau password salah", Toasty.LENGTH_LONG).show()
                crashlytics.recordException(Exception("Unauthorized, context: $ctx"))
            }

            code == 404 -> {
                Toasty.error(context, "Data tidak ditemukan: $message", Toasty.LENGTH_LONG).show()
                crashlytics.recordException(Exception("Data not found: $message, context: $ctx"))
            }

            code == 500 -> {
                Toasty.error(context, "Internal server error", Toasty.LENGTH_LONG).show()
                crashlytics.recordException(Exception("Internal server error, context: $ctx"))
            }

            else -> {
//                Toasty.error(context, message, Toasty.LENGTH_LONG).show()
                Log.e("Logger", "context : $context, fun : $func, message : $message, time : $now")
                crashlytics.recordException(Exception("General error: $message, context: $ctx"))
            }
        }
    }

    fun parseError(message: String): Pair<String, String> {
        val errJson = JSONObject(message)
        val status = errJson.optString("status", "error")
        val msg = errJson.optString("message", "Error")
        return Pair(status, msg)
    }
}