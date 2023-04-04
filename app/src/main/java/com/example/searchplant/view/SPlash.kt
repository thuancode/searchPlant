package com.example.searchplant.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.searchplant.R

class SPlash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
    }
}
