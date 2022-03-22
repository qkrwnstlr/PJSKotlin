package com.parkjunsik.randomwords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.parkjunsik.randomwords.databinding.ActivityExamBinding

class ExamActivity : AppCompatActivity() {
    val binding by lazy { ActivityExamBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}