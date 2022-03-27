package com.parkjunsik.randomwords

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parkjunsik.randomwords.databinding.ActivityExamBinding
import com.parkjunsik.randomwords.databinding.RecycleVocabularyBinding

class ExamActivity : AppCompatActivity() {
    val binding by lazy { ActivityExamBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val data = intent.getSerializableExtra("data") as ArrayList<Word>
        val VocabularyAdapter =VocabularyAdapter(data)
        binding.wordList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.wordList.adapter = VocabularyAdapter
    }
}

class VocabularyAdapter(val listData: ArrayList<Word>) : RecyclerView.Adapter<VocabularyAdapter.Holder>(){
    class Holder(val binding: RecycleVocabularyBinding): RecyclerView.ViewHolder(binding.root){
        fun setVocabulary(word: Word){
            binding.id.text = word.id.toString()
            binding.word.text = word.word
            binding.meaning.text = word.meaning
            binding.date.text = word.date.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecycleVocabularyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val word = listData[position]
        holder.setVocabulary(word)
    }

    override fun getItemCount() = listData.size
}