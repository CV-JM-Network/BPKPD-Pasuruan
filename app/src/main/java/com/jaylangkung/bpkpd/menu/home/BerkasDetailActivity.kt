package com.jaylangkung.bpkpd.menu.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.bpkpd.MainActivity
import com.jaylangkung.bpkpd.dataClass.BerkasData
import com.jaylangkung.bpkpd.databinding.ActivityBerkasDetailBinding
import com.jaylangkung.bpkpd.utils.MySharedPreferences
import com.jaylangkung.bpkpd.viewModel.HomeViewModel
import com.jaylangkung.bpkpd.viewModel.ViewModelFactory

class BerkasDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBerkasDetailBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var adapter: BerkasAdapter

    companion object {
        const val EXTRA_TABEL = "extra_tabel"
    }

    private var tempBerkasData = listOf<BerkasData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerkasDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this@BerkasDetailActivity, factory)[HomeViewModel::class.java]
        myPreferences = MySharedPreferences(this@BerkasDetailActivity)
        adapter = BerkasAdapter()

        val tabel = intent.getStringExtra(EXTRA_TABEL).toString()
        var page = 1
        val limit = 10

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(
                    Intent(this@BerkasDetailActivity, MainActivity::class.java).putExtra(MainActivity.EXTRA_FRAGMENT, "home")
                )
                finish()
            }
        })

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            tvTitle.text = when (tabel) {
                "salinan" -> "Berkas Salinan"
                "pbb" -> "Berkas PBB"
                "penagihan" -> "Berkas Penagihan"
                "sk_njop" -> "Berkas SK NJOP"
                "bphtb" -> "Berkas BPHTB"
                "bphtb_kolektif" -> "Berkas BPHTB Kolektif"
                "npwpd" -> "Berkas NPWPD"
                else -> "Berkas"
            }

            viewModel.apply {
                getDataBerkas(tabel, 10, 1)
                berkasData.observe(this@BerkasDetailActivity) { berkas ->
                    if (berkas != null) {
                        tempBerkasData = berkas
                        adapter.setItem(tempBerkasData, tabel)
                        progressBar.visibility = View.GONE
                    } else {
                        adapter.setItem(emptyList(), tabel)
                    }
                }

                rvBerkas.apply {
                    layoutManager = LinearLayoutManager(this@BerkasDetailActivity)
                    setHasFixedSize(false)
                    adapter = this@BerkasDetailActivity.adapter
                    addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                            val lastPosition = layoutManager.findLastVisibleItemPosition()
                            var totalPage = viewModel.totalData / limit
                            totalPage += if (viewModel.totalData % limit > 0) 1 else 0
                            if (lastPosition == this@BerkasDetailActivity.adapter.itemCount - 1) {
                                if (page < totalPage) {
                                    progressBar.visibility = View.VISIBLE
                                    page += 1
                                    viewModel.getDataBerkas(tabel, limit, page)
                                }
                            }
                        }
                    })
                }
            }
        }
    }

}