package com.martinz.mydownloader.presentation.detail_activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martinz.mydownloader.Const.FILE_NAME
import com.martinz.mydownloader.Const.FILE_STATUS
import com.martinz.mydownloader.Const.SUCCESS
import com.martinz.mydownloader.R
import com.martinz.mydownloader.databinding.ActivityDetailBinding
import com.martinz.mydownloader.presentation.main_activity.MainActivity


class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)

        receiveIntent()

        binding.contentDetail.btnOkay.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }



    private fun receiveIntent(){
        intent?.extras?.let {
            binding.contentDetail.apply {
                val color = applicationContext.getColor(R.color.colorPrimaryDark)
                tvFileName.text = intent.getStringExtra(FILE_NAME) ?: ""
                tvFileStatus.text = intent.getStringExtra(FILE_STATUS) ?: ""
                with(tvFileName) { setTextColor(color) }
                if (intent.getStringExtra(FILE_STATUS).equals(SUCCESS)){
                    tvFileStatus.setTextColor(color)
                    return
                }
                tvFileStatus.setTextColor(color)
            }

        }
    }
}
