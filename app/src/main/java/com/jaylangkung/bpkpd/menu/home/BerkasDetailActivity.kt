package com.jaylangkung.bpkpd.menu.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.bpkpd.MainActivity
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
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(
                    Intent(this@BerkasDetailActivity, MainActivity::class.java).putExtra(MainActivity.EXTRA_FRAGMENT, "scan")
                )
                finish()
            }
        })

        binding.apply {
            viewModel.apply {
                getDataBerkas(tabel, 10, page)
                berkasData.observe(this@BerkasDetailActivity) { berkas ->
                    if (berkas != null) {
                        adapter.setItem(berkas.data)
                        rvBerkas.apply {
                            layoutManager = LinearLayoutManager(this@BerkasDetailActivity)
                            setHasFixedSize(true)
                            adapter = this@BerkasDetailActivity.adapter
                            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                                    super.onScrolled(recyclerView, dx, dy)
                                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                                    if (lastPosition == this@BerkasDetailActivity.adapter.itemCount - 1) {
                                        var totalPage = berkas.totalData / 10
                                        totalPage += if (berkas.totalData % 10 > 0) 1 else 0
                                        if (page < totalPage) {
                                            page =+ 1
                                            getDataBerkas(tabel, 10, page)
                                        }

                                    }
                                }
                            })
                        }
                    } else {
                        adapter.setItem(emptyList())
                    }
                }

            }
        }
    }
}