package com.jaylangkung.bpkpd.viewModel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import java.util.regex.Pattern

class ScanQrViewModel(application: Application) : ViewModel() {

    private val appContext: Application = application

    fun validateQRCode(result: String): Boolean {
        var id = ""
        if (result.contains("webapp")) {
            id = result
            vibrate(appContext)
        }

        val regex = Pattern.compile("(https:\\/\\/bkd\\.jaylangkung\\.co\\.id\\/get_info_surat\\.php\\?nomor_berkas=)(?:[DSKPN]{1,2}-\\d{2}\\.\\d{4}|\\d*)")
        val matcher = regex.matcher(result)
        if (matcher.find()) {
            id = result
            vibrate(appContext)
        }

        return id.isNotEmpty()
    }

    private fun vibrate(ctx: Context) {
        val vibrator = ContextCompat.getSystemService(ctx, Vibrator::class.java) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION") vibrator.vibrate(200)
        }
    }
}