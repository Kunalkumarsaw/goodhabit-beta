package com.codinghub.goodhabitbeta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.codinghub.goodhabitbeta.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        var viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        setContentView(binding.root)
        binding.text.text= viewModel.name

    }
}