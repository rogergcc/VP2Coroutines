package com.hadi.vp2coroutines.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hadi.vp2coroutines.R
import com.hadi.vp2coroutines.databinding.ActivityHomeBinding
import com.hadi.vp2coroutines.ui.MainActivity
import com.hadi.vp2coroutines.ui.remote.UrlImagesActivity

class HomeActivity : AppCompatActivity(R.layout.activity_home) {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnGoDrawable.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))

        }
        binding.btnGoImageURL.setOnClickListener {
            startActivity(Intent(this, UrlImagesActivity::class.java))

        }
    }
}