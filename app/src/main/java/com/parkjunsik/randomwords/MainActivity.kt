package com.parkjunsik.randomwords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.parkjunsik.randomwords.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var dictionary:HashMap<String,String> = hashMapOf<String,String>()
    }
}